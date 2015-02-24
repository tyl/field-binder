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

package org.tylproject.vaadin.addon.fieldbinder;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Validator;
import com.vaadin.ui.*;
import org.tylproject.vaadin.addon.datanav.BasicDataNavigation;
import org.tylproject.vaadin.addon.datanav.CrudButtonBar;
import org.tylproject.vaadin.addon.fieldbinder.behavior.DefaultTableBehaviorFactory;
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
    protected final TabularViewAdaptor<?> table;
    protected final Class<T> containedBeanClass;
    protected final Class<U> collectionType;
//    private final CachingContainerProxy<FilterableListContainer<T>> cache;
    private Object[] visibleColumns;
    private final BasicDataNavigation navigation;
    final private FilterableListContainer<T> listContainer;
    private boolean delayedColumnInit = false;

    public CollectionTabularView(Class<T> containedBeanClass, Class<U> collectionType, TabularViewAdaptor<?> adaptor) {
        this.containedBeanClass = containedBeanClass;
        this.collectionType = collectionType;


        this.table = adaptor;

        navigation = new BasicDataNavigation();
        navigation.restrictContainerType(FilterableListContainer.class);

        this.listContainer = new FilterableListContainer<T>(containedBeanClass);
        table.setContainerDataSource(listContainer);
        navigation.setContainer(listContainer);

        compositionRoot.addComponent(adaptor.getComponent());


        // when the value of this wrapper (the list of values!)
        // changes, restore the tableAdaptor state:
        // (selectable = true, select id = null)
        this.addValueChangeListener(new ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                navigation.setCurrentItemId(null);
                table.select(null);
                table.setSelectable(true);
            }
        });

        // when someone selects an item on the actual tableAdaptor widget,
        // then update the navigator accordingly
        table.attachNavigation(navigation);


    }

    public void select(Object itemId) {
        table.select(itemId);
    }


    public Object getSelectedItemId() {
        return this.getNavigation().getCurrentItemId();
    }

    public Item getSelectedItem() {
        return this.getNavigation().getCurrentItem();
    }

    public TabularViewAdaptor<?> getAdaptor() {
        return table;
    }


    /**
     * Mimicks {@link com.vaadin.ui.Table} but automatically infer column names like a
     * {@link com.vaadin.data.fieldgroup.FieldGroup} or a {@link org.tylproject.vaadin.addon.fieldbinder.FieldBinder}
     */
    public void setVisibleColumns(Object ... visibleColumns) {
        this.visibleColumns = visibleColumns;

        // delay until a data source with more than 0 properties is available
        if (table.getContainerDataSource().getContainerPropertyIds().size() == 0) {
            delayedColumnInit = true;
            return;
        }
        delayedColumnInit = true;

        table.setVisibleColumns(visibleColumns);
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
            headers[i] = DefaultFieldFactory.createCaptionByPropertyId(propertyId);
        }
        table.setColumnHeaders(headers);
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
        table.focus();
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

        visibleColumns = getContainerDataSource().getContainerPropertyIds().toArray();

        setupColumns();
    }

    protected void clearContainerState() {
        listContainer.removeAllContainerFilters();
        listContainer.setCollection(new ArrayList<T>());
        table.select(null);
        // FIXME reset sort?
    }




    protected void setListContainer(FilterableListContainer<T> listContainer) {
//        this.listContainer = listContainer;
        if (listContainer == null) {
            // clears the table contents

            table.setContainerDataSource(null);
            table.select(null);
            navigation.setContainer(null);
        } else {

            table.setContainerDataSource(listContainer);
            // clear selection
            table.select(null);
            navigation.setContainer(listContainer);

            setupColumns();
        }
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


    /*
     * return the actual value, pulling it from the container
     */
    @Override
    protected U getInternalValue() {

        FilterableListContainer<T> container = getListContainer();
        if (container == null) return super.getInternalValue();

        // the value must be pulled from the internal collection
        // we know this is a ListContainer, so we just get all the Ids == all the values
        Collection<T> allItems = container.getItemIds();
        U propertyCollection = super.getInternalValue();

        // if they are the same instance, no need to copy the values over
        if (propertyCollection == allItems) {
            return propertyCollection;
        }

        if (propertyCollection == null) {
            return null;
        }

        propertyCollection.clear();
        propertyCollection.addAll(container.getItemIds());

        return propertyCollection;

    }

    public void setPropertyDataSource(Property newDataSource) {
        if (newDataSource == null) return;
        else super.setPropertyDataSource(newDataSource);

        setupColumns();
    }

    private void setupColumns() {
        if (delayedColumnInit) {
            this.setVisibleColumns(visibleColumns);
        } else {
            setAllHeadersFromColumns(table.getVisibleColumns());
        }
    }


    public Object getConvertedValue() {
        // no need to convert
        return getValue();
    }


    @Override
    public void commit() throws SourceException, Validator.InvalidValueException {
        super.commit();
        table.commit();
    }

    @Override
    public void discard() {
        table.discard();
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
