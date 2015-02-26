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

package org.tylproject.vaadin.addon.fieldbinder.behavior.commons;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.DefaultFieldGroupFieldFactory;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.FieldGroupFieldFactory;
import com.vaadin.ui.*;
import org.tylproject.vaadin.addon.datanav.DataNavigation;
import org.tylproject.vaadin.addon.datanav.events.*;
import org.tylproject.vaadin.addon.fieldbinder.*;
import org.tylproject.vaadin.addon.fieldbinder.behavior.CrudListeners;

import java.lang.reflect.Constructor;
import java.util.*;

/**
 * Base behavior for Tables
 */
public class Tables {


    /**
     * Link Table selection to the DataNavigation instance
     */
    public static class CurrentItemChangeListener implements CurrentItemChange.Listener {
        private final TabularViewAdaptor<?> table;

        public CurrentItemChangeListener(TabularViewAdaptor<?> table) {
            this.table = table;
        }

        @Override
        public void currentItemChange(CurrentItemChange.Event event) {
            table.select(event.getNewItemId());
        }

    }

    /**
     * Base CRUD behavior for tabular data.
     *
     * Implementations may extend this class. In most cases, only
     * {@link org.tylproject.vaadin.addon.fieldbinder.behavior.commons.Tables.BaseCrud#itemCreate(org.tylproject.vaadin.addon.datanav.events.ItemCreate.Event)}
     * must be overridden
     *
     */
    public static class BaseCrud<T> implements CrudListeners {
        final protected TabularViewAdaptor<?> tableAdaptor;
        final protected Class<T> beanClass;
        protected T newEntity = null;


        public BaseCrud(final Class<T> beanClass, final TabularViewAdaptor<?> tableAdaptor) {
            this.beanClass = beanClass;
            this.tableAdaptor = tableAdaptor;
        }
        @Override
        public void itemEdit(ItemEdit.Event event) {
            tableAdaptor.setEditable(true);
            tableAdaptor.setSelectable(false);
            tableAdaptor.focus();

            tableAdaptor.setEditorDataSource(event.getSource().getCurrentItem());


        }


        @Override
        public void itemCreate(ItemCreate.Event event) {
            tableAdaptor.setEditable(true);
            tableAdaptor.setSelectable(false);
            tableAdaptor.focus();
        }


        public void onDiscard(OnDiscard.Event event) {
            this.tableAdaptor.discard();

            this.tableAdaptor.setEditable(false);
            this.tableAdaptor.setSelectable(true);
            if (newEntity != null) {
                newEntity = null;
                event.getSource().remove();
            }
        }


        public void onCommit(OnCommit.Event event) {

            this.tableAdaptor.setEditable(false);
            this.tableAdaptor.commit();

//            this.tableAdaptor.getFieldBinder().commit();

            ((TableAdaptor<?>)this.tableAdaptor).getComponent().refreshRowCache();

            this.tableAdaptor.setSelectable(true);

            newEntity = null;
        }

        public void itemRemove(ItemRemove.Event event) {
            this.tableAdaptor.getContainerDataSource().removeItem(event.getSource().getCurrentItemId());
        }

        protected T createBean() {
            try {
                Constructor<T> ctor = beanClass.getConstructor();
                T bean = ctor.newInstance();
                newEntity = bean;
                return bean;
            } catch (Exception ex) {
                throw new UnsupportedOperationException(ex);
            }
        }
    }

    public static class SingleLineFieldFactory implements TableFieldFactory {
        private FieldBinder<?> fieldBinder;
        private boolean autoGenerateFields = true;

        public SingleLineFieldFactory(FieldBinder<?> fieldBinder) {
            this.fieldBinder = fieldBinder;
        }

        @Override
        public Field<?> createField(Container container, Object itemId, Object propertyId, Component uiContext) {
            if (itemId != fieldBinder.getNavigation().getCurrentItemId()) return null;

            Field<?> f = fieldBinder.getField(propertyId);
            if (f == null) {
                fieldBinder.build(propertyId);
            }


            return fieldBinder.getField(propertyId);
        }
    }

}
