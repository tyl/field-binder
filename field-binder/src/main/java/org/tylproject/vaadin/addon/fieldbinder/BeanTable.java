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

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Validator;
import com.vaadin.ui.*;
import org.tylproject.vaadin.addon.datanav.BasicDataNavigation;
import org.tylproject.vaadin.addon.datanav.CrudButtonBar;
import org.tylproject.vaadin.addon.fieldbinder.behavior.legacy.TableBehaviorFactory;

/**
 * Created by evacchi on 19/12/14.
 */
public class BeanTable<T> extends CustomField<T> {


    protected final VerticalLayout compositionRoot = new VerticalLayout();
    protected final Table table;
    protected final Class<T> beanClass;
    private Object[] visibleColumns;
    private final BasicDataNavigation navigation;


    /**
     * Constructs a BeanTable for the given class, and the given container.
     * Instantiates the underlying {@link com.vaadin.ui.Table} automatically
     *
     */
    public BeanTable(final Class<T> beanClass, final Container.Ordered container) {

        this(beanClass, container, new Table());

        table.setBuffered(true);
        table.setWidth("100%");
        table.setHeight("300px");


        table.setSelectable(true);
        table.setMultiSelect(false);
        table.setContainerDataSource(container);

    }

    /**
     * Constructs a BeanTable  for the given class, the given container,
     * and the given table instance
     *
     * @param beanClass
     * @param container
     * @param table
     */
    public BeanTable(final Class<T> beanClass, final Container.Ordered container, final Table table) {

        this.beanClass = beanClass;
        this.table = table;

        this.navigation = new BasicDataNavigation(container);
        this.navigation.setBehaviorFactory(new TableBehaviorFactory(beanClass, table));

        this.compositionRoot.addComponent(table);


        // when the value of this wrapper (the list of values!)
        // changes, restore the table state:
        // (selectable = true, select id = null)
        this.addValueChangeListener(new ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                Object itemId = event.getProperty().getValue();
                navigation.setCurrentItemId(itemId);
                table.select(itemId);
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

    public void setContainerDataSource(Container container) {
        this.table.setContainerDataSource(container);
        this.select(null);
    }

    public Container getContainerDataSource() {
        return this.table.getContainerDataSource();
    }

    public void select(Object itemId) {
        table.select(itemId);
        navigation.setCurrentItemId(itemId);
    }

    public Object getSelectedItemId() {
        return this.getNavigation().getCurrentItemId();
    }

    public Item getSelectedItem() {
        return this.getNavigation().getCurrentItem();
    }

    /**
     * Mimicks {@link com.vaadin.ui.Table} but automatically infer column names like a
     * {@link com.vaadin.data.fieldgroup.FieldGroup} or a {@link org.tylproject.vaadin.addon.fieldbinder.FieldBinder}
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
        return beanClass;
    }

    @Override
    public void focus() {
        table.focus();
    }

    @Override
    protected void setInternalValue(T value) {
        table.setValue(value);
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
    public BeanTable<T> withDefaultEditorBar() {
        CrudButtonBar buttonBar = buildDefaultCrudBar();
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
    public CrudButtonBar buildDefaultCrudBar() {
        return new CrudButtonBar(getNavigation().withDefaultBehavior());
    }

    /**
     * @return the DataNavigation instance bound to this component
     */
    public BasicDataNavigation getNavigation() {
        return navigation;
    }
}
