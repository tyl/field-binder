/*
 * Copyright (c) 2015 - Tyl Consulting s.a.s.
 *
 *   Authors: Edoardo Vacchi
 *   Contributors: Marco Pancotti, Daniele Zonca
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.tylproject.vaadin.addon.fields.collectiontables;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Validator;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.shared.util.SharedUtil;
import com.vaadin.ui.*;
import org.tylproject.vaadin.addon.datanav.BasicDataNavigation;
import org.tylproject.vaadin.addon.datanav.CrudButtonBar;
import org.tylproject.vaadin.addon.fieldbinder.FieldBinder;
import org.tylproject.vaadin.addon.fields.collectiontables.adaptors.TableAdaptor;
import org.tylproject.vaadin.addon.fields.collectiontables.adaptors.TabularViewAdaptor;
import org.tylproject.vaadin.addon.utils.CachingContainerProxy;
import org.vaadin.viritin.FilterableListContainer;

import java.util.ArrayList;
import java.util.Collection;

/**
 * A table wrapper where the value is a {@link java.util.List}
 *
 * Generally used together with {@link org.tylproject.vaadin.addon.fieldbinder.FieldBinder}
 */
public class CollectionTabularView<T,U extends Collection<T>> extends CustomField<U> {

    protected final VerticalLayout compositionRoot = new VerticalLayout();
    protected TabularViewAdaptor<T,?> adaptor;
    protected final Class<T> containedBeanClass;
    protected final Class<U> collectionType;


    private final BasicDataNavigation navigation;

    final private FilterableListContainer<T> listContainer;

    public CollectionTabularView(Class<T> containedBeanClass, Class<U> collectionType) {
        this.containedBeanClass = containedBeanClass;
        this.collectionType = collectionType;
        this.listContainer = new FilterableListContainer<T>(containedBeanClass);
        this.navigation = new BasicDataNavigation();
        getNavigation().setContainer(listContainer);
    }

    protected void setAdaptor(final TabularViewAdaptor<T,?> adaptor) {

        if (this.adaptor != null) throw new IllegalStateException("cannot setAdaptor() twice");

        this.adaptor = adaptor;

        // makes current selected item "stick" between container.getItem(currentId) invocations
        final CachingContainerProxy<?> proxy = new CachingContainerProxy<>(navigation);
        adaptor.setContainerDataSource(proxy);

        compositionRoot.addComponent(adaptor.getComponent());


        // when the value of this wrapper (the list of values!)
        // changes, restore the tableAdaptor state:
        // (selectable = true, select id = null)
        this.addValueChangeListener(new ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                navigation.setCurrentItemId(null);
                adaptor.select(null);
                adaptor.setSelectable(true);
            }
        });


        // when someone selects an item on the actual tableAdaptor widget,
        // then update the navigator accordingly
        adaptor.attachNavigation(navigation);
    }


    // this may throw an exception if the adaptor is a GRID adaptor
    // grids do not include a FieldBinder, but a FieldGroup
    // it may be worth to "adapt" this in some way
    public FieldBinder<T> getFieldBinder() {
        return getAdaptor().getFieldBinder();
    }


    public void select(Object itemId) {
        adaptor.select(itemId);
    }


    public Object getSelectedItemId() {
        return this.getNavigation().getCurrentItemId();
    }

    public Item getSelectedItem() {
        return this.getNavigation().getCurrentItem();
    }

    public TabularViewAdaptor<T,?> getAdaptor() {
        return adaptor;
    }


    /**
     * Mimicks {@link com.vaadin.ui.Table} but automatically infer column names like a
     * {@link com.vaadin.data.fieldgroup.FieldGroup} or a {@link org.tylproject.vaadin.addon.fieldbinder.FieldBinder}
     */
    public void setVisibleColumns(Object ... visibleColumns) {
        adaptor.setVisibleColumns(visibleColumns);
        setAllHeadersFromColumns(visibleColumns);
    }

    /**
     * Infers the column names from the column ids.
     *
     * Internally relies upon {@link com.vaadin.ui.DefaultFieldFactory}
     */
    private void setAllHeadersFromColumns(Object[] columns) {
        String[] headers = new String[columns.length];
        for (int i = 0; i < columns.length; i++) {
            Object propertyId = columns[i];
            headers[i] = createCaptionByPropertyId(propertyId);
        }
        adaptor.setColumnHeaders(headers);
    }

    /**
     * Create caption from propertyId
     *
     * Caveat: internally uses the FieldBinder from the Table;
     * but it requires the adaptor to be a TableAdaptor!
     *
     * TODO: needs work for Grid
     *
     */
    protected String createCaptionByPropertyId(Object propertyId) {
        if (getAdaptor() instanceof TableAdaptor) {
            return getAdaptor().getFieldBinder().getFieldFactory().createCaptionByPropertyId(propertyId);
        }
        return SharedUtil.propertyIdToHumanFriendly(propertyId);
    }
    @Override
    protected Component initContent() {
        return compositionRoot;
    }

    @Override
    public Class getType() {
        return collectionType;
    }

    /**
     * return the type parameter for the List that this tableAdaptor contains
     */
    /**
     * @return the data type contained by the list
     */
    public Class<T> getListType() { return containedBeanClass; }

    @Override
    public void focus() {
        adaptor.focus();
    }

    public void setCollection(U collection) {
        setInternalValue(collection);
    }

    @Override
    protected void setInternalValue(U collection) {
        // reset the navigation status
        navigation.setCurrentItemId(null);

        super.setInternalValue(collection);

        clearContainerState();
        if (collection != null) {
            listContainer.setCollection(collection);
        }

        setupColumns();
    }

    protected void clearContainerState() {
        listContainer.removeAllContainerFilters();
        listContainer.setCollection(new ArrayList<T>());
        adaptor.select(null);
        // FIXME reset sort?
    }

    protected FilterableListContainer<T> getListContainer() {
        return listContainer;
    }


    public Container.Ordered getContainerDataSource() {
        return getListContainer();
    }

    @Override
    public U getValue() {
        // the super implementation invokes getInternalValue() (via getFieldValue())
        // but only if (dataSource == null || isBuffered() || isModified())
        // otherwise it calls convertFromModel(getDataSourceValue())
        // which is a *private* pass-through for propertyDataSource.getValue()

        if (getPropertyDataSource() == null || isBuffered() || isModified()) {
            // return the buffered value
            return getInternalValue();
        }

        // not buffered, nor modified, just return the underlying value
        // no conversion is needed
        return (U) getPropertyDataSource().getValue();
    }

//    @Override
//    public void setConverter(Converter<U, ?> converter) {
//        throw new UnsupportedOperationException();
//    }
//
//    @Override
//    public void setConverter(Class<?> datamodelType) {
//        throw new UnsupportedOperationException();
//    }

    /*
     * return the actual value, pulling it from the container
     */
    @Override
    protected U getInternalValue() {

        FilterableListContainer<T> container = getListContainer();

        // the value must be pulled from the internal collection
        // we know this is a ListContainer, so we just get all the Ids == all the values
        Collection<T> allItems = container.getItemIds(); // ListContainer returns the list of all the values
        U internalValue = super.getInternalValue();

        // if they are the same instance, no need to copy the values over
        // i.e.: if the collection returned by container.getItemIds()
        //       is the same as the internal value, then there is no
        //       need to copy over the values; we can just return the collection itself.

        if (internalValue == allItems) {
            return internalValue;
        }

        // this is only needed during initialization/binding
        // there is a moment during which the internal state IS NOT consistent
        // (caused by setPropertyDataSource())
        // the internalValue is null, but the listContainer contains an empty collection
        // we'll just ignore the inconsistency, and return null
        if (internalValue == null) {
            return null;
        }

        internalValue.clear();
        internalValue.addAll(allItems);

        return internalValue;

    }

    public void setPropertyDataSource(Property newDataSource) {
        super.setPropertyDataSource(newDataSource);

        setupColumns();
    }

    private void setupColumns() {
        setAllHeadersFromColumns(adaptor.getVisibleColumns());
    }


    public Object getConvertedValue() {
        // no need to convert
        return getValue();
    }


    @Override
    public void commit() throws SourceException, Validator.InvalidValueException {
        super.commit();
        adaptor.commit();
    }

    @Override
    public void discard() {
        adaptor.discard();
    }

    /**
     * @return adds a default button bar to the bottom right of this component
     */
    public CollectionTabularView<T,U> withDefaultEditorBar() {
        CrudButtonBar buttonBar = buildDefaultEditorBar();
        compositionRoot.setSizeFull();

        HorizontalLayout inner = new HorizontalLayout(buttonBar);
        inner.setSizeFull();
        inner.setComponentAlignment(buttonBar, Alignment.BOTTOM_RIGHT);

        compositionRoot.addComponent(inner);
        return this;
    }


    /**
     * build and returns a default button bar for this component
     * @return
     */
    public CrudButtonBar buildDefaultEditorBar() {
        return new CrudButtonBar(getNavigation().withDefaultBehavior());
    }

    /**
     * @return the DataNavigation instance bound to this component
     */
    public BasicDataNavigation getNavigation() {
        return navigation;
    }

}
