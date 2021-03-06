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

import com.vaadin.ui.*;
import org.tylproject.vaadin.addon.datanav.BasicDataNavigation;
import org.tylproject.vaadin.addon.fieldbinder.*;
import org.tylproject.vaadin.addon.fields.collectiontables.adaptors.GridAdaptor;
import org.tylproject.vaadin.addon.fields.collectiontables.adaptors.TableAdaptor;
import org.tylproject.vaadin.addon.fields.collectiontables.adaptors.TabularViewAdaptor;

import java.util.Collection;

/**
 * A table wrapper where the value is a {@link java.util.List}
 *
 * Generally used together with {@link org.tylproject.vaadin.addon.fieldbinder.FieldBinder}
 */
public class CollectionTable<T,U extends Collection<T>> extends CollectionTabularView<T,U> {

    final TableAdaptor<?> tableAdaptor;
    final GridAdaptor<?> gridAdaptor;

    /**
     * constructs a collection table. Defaults to a Table widget
     */
    public CollectionTable(Class<T> containedBeanClass, Class<U> collectionType) {
        this(containedBeanClass, collectionType, GridSupport.UseTable);
    }

    public CollectionTable(Class<T> containedBeanClass, Class<U> collectionType, GridSupport gridSupport) {
        super(containedBeanClass, collectionType);

        final TabularViewAdaptor<T,?> adaptor = makeAdaptor(containedBeanClass, gridSupport);
        setAdaptor(adaptor);

        switch (gridSupport) {
            case UseTable:
                tableAdaptor = (TableAdaptor) super.getAdaptor();
                gridAdaptor = null;
                initTableAdaptor();
                break;
            case UseGrid:
                gridAdaptor = (GridAdaptor) super.getAdaptor();
                tableAdaptor = null;
                initGridAdaptor();
                break;
            default:
                throw new IllegalArgumentException(""+gridSupport);
        }
    }

    private TabularViewAdaptor<T,?> makeAdaptor(Class<T> containedBeanClass, GridSupport gridSupport) {
        switch (gridSupport) {
            case UseTable:
                // fieldBinder.setNavigation() does not exist
                return new TableAdaptor<T>(containedBeanClass, new FieldBinder<T>(containedBeanClass) {
                    @Override
                    public BasicDataNavigation getNavigation() {
                        return CollectionTable.this.getNavigation();
                    }
                });
            case UseGrid:
                return new GridAdaptor<T>(containedBeanClass);
            default:
                throw new IllegalArgumentException(""+gridSupport);
        }
    }

    public void initTableAdaptor() {
        Table tableComponent = tableAdaptor.getComponent();

        tableComponent.setBuffered(true);
        tableComponent.setSizeFull();
        tableComponent.setHeight("300px");
        tableComponent.setSelectable(true);
        tableComponent.setMultiSelect(false);
        tableComponent.setNullSelectionAllowed(false);

    }

    public void initGridAdaptor() {
        Grid gridComponent = gridAdaptor.getComponent();

        gridComponent.setSizeFull();
        gridComponent.setWidth("100%");
        gridComponent.setHeight("300px");
        gridComponent.setSelectionMode(Grid.SelectionMode.SINGLE);

    }




    /**
     * Returns the actual {@link com.vaadin.ui.Table} instance
     * @return
     */
    public Table getTable() {
        if (tableAdaptor == null) {
            throw new IllegalStateException("This CollectionTable does not use a com.vaadin.ui.Table");
        }

        return (Table) getAdaptor().getComponent();
    }

    public Grid getGrid() {
        if (gridAdaptor == null) {
            throw new IllegalStateException("This CollectionTable does not use a com.vaadin.ui.Grid");
        }

        return (Grid) getAdaptor().getComponent();
    }

}
