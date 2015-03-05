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

package org.tylproject.vaadin.addon.fields.zoom;

import com.vaadin.data.Container;
import org.tylproject.vaadin.addon.fields.collectiontables.BeanTable;

/**
 * Displays a table, allows to select an Item in the table,
 * returns the itemId of the selection
 */
public class TableZoomDialog extends AbstractZoomDialog {

    private final BeanTable<?> beanTable;


    public TableZoomDialog(BeanTable<?> beanTable) {
        this.beanTable = beanTable;
        beanTable.setSizeFull();
        addComponent(beanTable);
    }

    public TableZoomDialog(BeanTable<?> beanTable, String caption) {
        this(beanTable);
        this.setCaption(caption);
    }

    public TableZoomDialog(Object propertyId, Container.Ordered zoomCollection) {
        this(new BeanTable<>(Object.class, zoomCollection));
        withContainerPropertyId(propertyId, zoomCollection.getType(propertyId));
    }

    public BeanTable<?> getTable() {
        return beanTable;
    }


    public TableZoomDialog withContainerPropertyId(Object propertyId, Class<?> propertyType) {
        super.withContainerPropertyId(propertyId, propertyType);
        return this;
    }

    @Override
    public Container getContainer() {
        return beanTable.getContainerDataSource();
    }

    @Override
    public void show(Object value) {
        beanTable.getTable().setNullSelectionAllowed(isNullSelectionAllowed());
        beanTable.getTable().setSelectable(!isReadOnly());
    }

    @Override
    public Object getSelectedItemId() {
        return beanTable.getSelectedItemId();
    }}
