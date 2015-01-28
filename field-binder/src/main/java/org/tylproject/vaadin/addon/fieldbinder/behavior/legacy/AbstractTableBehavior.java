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

package org.tylproject.vaadin.addon.fieldbinder.behavior.legacy;

import com.vaadin.data.Container;
import com.vaadin.ui.Table;
import org.tylproject.vaadin.addon.datanav.events.*;
import org.tylproject.vaadin.addon.fieldbinder.behavior.Behavior;
import org.tylproject.vaadin.addon.fieldbinder.behavior.FindListeners;
import org.tylproject.vaadin.addon.fieldbinder.behavior.commons.SearchWindowFindListeners;
import org.tylproject.vaadin.addon.fieldbinder.behavior.commons.Tables;
import org.tylproject.vaadin.addon.fields.search.SearchForm;
import org.tylproject.vaadin.addon.fields.search.SearchWindow;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by evacchi on 28/01/15.
 */
public abstract class AbstractTableBehavior<T> implements Behavior {
    final protected Table table;
    final protected Class<T> beanClass;
//    final protected TableFindListeners<T> findListeners;
    final protected SearchForm searchForm;
    final protected Tables.FieldManager fieldManager;

    protected T newEntity = null;
    protected FindListeners findListeners;

    public AbstractTableBehavior(final Class<T> beanClass, final Table table) {
        this.beanClass = beanClass;
        this.table = table;
//        this.findListeners = new TableFindListeners<>(beanClass);
        this.searchForm =
            new SearchForm(makePropertyIdToTypeMap(
                table.getContainerDataSource(),
                Arrays.asList(table.getVisibleColumns())));


        this.fieldManager = new Tables.FieldManager(table);

        table.setTableFieldFactory(fieldManager);
    }


    protected Map<Object, Class<?>>
            makePropertyIdToTypeMap(Container container, Collection<?> propertyIds) {

        LinkedHashMap<Object, Class<?>> map = new LinkedHashMap<>();
        for (Object propertyId: propertyIds) {
            map.put(propertyId, container.getType(propertyId));
        }

        return map;
    }



    @Override
    public void itemEdit(ItemEdit.Event event) {
        table.setEditable(true);
        table.setSelectable(false);
        table.focus();
    }


    @Override
    public void itemCreate(ItemCreate.Event event) {
//        event.getSource().getContainer().addItem(...);
//        event.getSource().setCurrentItemId(...);

        table.setEditable(true);
        table.setSelectable(false);
        table.focus();
    }


    public void onDiscard(OnDiscard.Event event) {
        this.table.discard();

        fieldManager.discardFields();
        this.table.setEditable(false);

        this.table.setSelectable(true);
        if (newEntity != null) {
            newEntity = null;
            event.getSource().remove();
        }
    }


    public void onCommit(OnCommit.Event event) {

        fieldManager.commitFields();
        this.table.commit();
        this.table.setEditable(false);

        this.table.setSelectable(true);

        newEntity = null;
    }

    public void itemRemove(ItemRemove.Event event) {
        this.table.removeItem(event.getSource().getCurrentItemId());
    }

    @Override
    public void currentItemChange(CurrentItemChange.Event event) {
        table.select(event.getNewItemId());
    }


    @Override
    public void clearToFind(ClearToFind.Event event) {
        // lazily initialize the SearchWindow
        if (this.findListeners == null) {
            final SearchWindow searchWindow =
                    new SearchWindow(searchForm).callFindOnClose(event.getSource());
            this.findListeners = new SearchWindowFindListeners(searchWindow);
        }
        this.findListeners.clearToFind(event);
    }


    @Override
    public void onFind(OnFind.Event event) {
        this.findListeners.onFind(event);
    }



    protected T createBean() {
        try {
            T bean = beanClass.newInstance();
            newEntity = bean;
            return bean;
        } catch (Exception ex) {
            throw new UnsupportedOperationException(ex);
        }
    }
}