/*
 * Copyright (c) 2014 - Tyl Consulting s.a.s.
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

import com.vaadin.data.Property;
import com.vaadin.data.Validator;
import com.vaadin.ui.*;
import org.tylproject.vaadin.addon.datanav.BasicDataNavigation;
import org.tylproject.vaadin.addon.datanav.CrudButtonBar;
import org.tylproject.vaadin.addon.fieldbinder.behavior.TableBehaviorFactory;
import org.vaadin.viritin.FilterableListContainer;

import java.util.Collection;
import java.util.List;

/**
 * A table wrapper where the value is a {@link java.util.List}
 *
 * Generally used together with {@link FieldBinder}
 */
public class CollectionTable<T,U extends Collection<T>> extends CustomField<U> {

    protected final VerticalLayout compositionRoot = new VerticalLayout();
    protected final Table table;
    protected final Class<T> containedBeanClass;
    protected final Class<U> collectionType;
    private Object[] visibleColumns;
    private final BasicDataNavigation navigation;

    public CollectionTable(Class<T> containedBeanClass, Class<U> collectionType) {
        this.containedBeanClass = containedBeanClass;
        this.collectionType = collectionType;

        table = new Table();
        table.setBuffered(true);
        table.setSizeFull();
        table.setHeight("300px");
        table.setSelectable(true);
        table.setMultiSelect(false);

        navigation = new BasicDataNavigation();
        navigation.setBehaviorFactory(new TableBehaviorFactory<T>(containedBeanClass, table));
        navigation.restrictContainerType(FilterableListContainer.class);

        compositionRoot.addComponent(table);


        // when the value of this wrapper (the list of values!)
        // changes, restore the table state:
        // (selectable = true, select id = null)
        this.addValueChangeListener(new ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                navigation.setCurrentItemId(null);
                table.select(null);
                table.setSelectable(true);
            }
        });

        // when someone selects an item on the actual table widget,
        // then update the navigator accordingly
        table.addValueChangeListener(new ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                navigation.setCurrentItemId(event.getProperty().getValue());
            }
        });


    }

    public void select(Object itemId) {
        table.select(itemId);
    }


    /**
     * Mimicks {@link com.vaadin.ui.Table} but automatically infer column names like a
     * {@link com.vaadin.data.fieldgroup.FieldGroup} or a {@link FieldBinder}
     */
    public void setVisibleColumns(Object ... visibleColumns) {
    this.visibleColumns = visibleColumns;
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

    /**
     * Returns the actual {@link com.vaadin.ui.Table} instance
     * @return
     */
    public Table getTable() {
        return table;
    }

    @Override
    public Class getType() {
        return collectionType;
    }

    /**
     * return the type parameter for the List that this table contains
     */
    /**
     * @return the data type contained by the list
     */
    public Class<T> getListType() { return containedBeanClass; }

    @Override
    public void focus() {
        table.focus();
    }

    @Override
    protected void setInternalValue(U collection) {
        // reset the navigation status
        navigation.setCurrentItemId(null);

        super.setInternalValue(collection);

        if (collection == null) {
            // clears the table contents
            table.setContainerDataSource(null);
            table.select(null);
            navigation.setContainer(null);

        } else {
            FilterableListContainer<T> listContainer = new FilterableListContainer<T>(containedBeanClass);
            listContainer.setCollection(collection);

            table.setContainerDataSource(listContainer);
            // clear selection
            table.select(null);
            navigation.setContainer(listContainer);

            if (visibleColumns != null) {
                this.setVisibleColumns(visibleColumns);
            } else {
                setAllHeadersFromColumns(table.getVisibleColumns());
            }

        }
    }

    @Override
    public void commit() throws SourceException, Validator.InvalidValueException {
        table.commit();
    }

    @Override
    public void discard() {
        table.discard();
    }

    /**
     * @return adds a default button bar to the bottom right of this component
     */
    public CollectionTable<T,U> withDefaultEditorBar() {
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
