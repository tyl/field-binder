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
import com.vaadin.data.Item;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.tylproject.vaadin.addon.datanav.resources.Strings;
import org.tylproject.vaadin.addon.fieldbinder.BeanTable;
import org.vaadin.viritin.FilterableListContainer;

import java.util.ResourceBundle;

/**
 * Displays a table, allows to select an Item in the table,
 * returns the itemId of the selection
 */
public class TableZoomDialog extends CustomComponent implements ZoomDialog {

    private final BeanTable<?> beanTable;
    private Object propertyId;


    public TableZoomDialog(BeanTable<?> beanTable) {
        this.beanTable = beanTable;
        beanTable.setSizeFull();
        setCompositionRoot(beanTable);
    }

    public TableZoomDialog(BeanTable<?> beanTable, String caption) {
        this(beanTable);
        this.setCaption(caption);
    }

    public TableZoomDialog(Object propertyId, Container.Ordered zoomCollection) {
        this(new BeanTable<>(Object.class, zoomCollection));
        withPropertyId(propertyId);
    }

    public BeanTable<?> getTable() {
        return beanTable;
    }


    public TableZoomDialog withPropertyId(Object propertyId) {
        this.propertyId = propertyId;
        return this;
    }

    @Override
    public void show(Object value) {

    }

    /**
     * Returns the current selected itemId
     */
    @Override
    public Object dismiss() {
        Object itemId = beanTable.getSelectedItemId();
        if (itemId == null) return null;
        Item item = beanTable.getContainerDataSource().getItem(itemId);
        return item.getItemProperty(propertyId).getValue();
    }}
