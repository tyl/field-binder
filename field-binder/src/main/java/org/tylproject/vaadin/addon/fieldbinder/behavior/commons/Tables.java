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
import com.vaadin.data.fieldgroup.FieldGroupFieldFactory;
import com.vaadin.ui.*;
import org.tylproject.vaadin.addon.datanav.events.*;
import org.tylproject.vaadin.addon.fieldbinder.FieldBinderFieldFactory;
import org.tylproject.vaadin.addon.fieldbinder.TabularViewAdaptor;
import org.tylproject.vaadin.addon.fieldbinder.behavior.CrudListeners;
import org.tylproject.vaadin.addon.fieldbinder.behavior.FindListeners;

import java.util.ArrayList;
import java.util.List;

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
        final protected TabularViewAdaptor<?> table;
        final protected Class<T> beanClass;
        final protected FieldManager fieldManager;

        protected T newEntity = null;

        public BaseCrud(final Class<T> beanClass, final TabularViewAdaptor<?> table) {
            this.beanClass = beanClass;
            this.table = table;

            this.fieldManager = new FieldManager(table);

//            table.setTableFieldFactory(fieldManager);
        }


        @Override
        public void itemEdit(ItemEdit.Event event) {
            table.setEditable(true);
            table.setSelectable(false);
            table.focus();
        }


        @Override
        public void itemCreate(ItemCreate.Event event) {
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
            this.table.getContainerDataSource().removeItem(event.getSource().getCurrentItemId());
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

    /**
     * Mimics a (lighter-weight) FieldBinder for Table inline-editing
     */
    public static class FieldManager implements TableFieldFactory {
        final FieldGroupFieldFactory fieldFactory ;
        final List<Field<?>> fields = new ArrayList<>();
        final TabularViewAdaptor<?> table;


        public FieldManager(TabularViewAdaptor<?> table) {
            this(table, new FieldBinderFieldFactory());
        }

        public FieldManager(TabularViewAdaptor<?> table, FieldGroupFieldFactory fieldFactory) {
            this.fieldFactory = fieldFactory;
            this.table = table;
        }

        @Override
        public Field<?> createField(Container container, Object itemId, Object propertyId, Component uiContext) {
            if (itemId == null || !itemId.equals(table.getSelectedItemId())) return null;

            Class<?> dataType = container.getType(propertyId);
            Field<?> f = fieldFactory.createField(dataType, Field.class);
            if (f instanceof AbstractTextField) {
                ((AbstractTextField) f).setNullRepresentation("");
                ((AbstractTextField) f).setImmediate(true);
            }
            f.setBuffered(true);
            fields.add(f);
            return f;
        }


        public void commitFields() {
            for (Field<?> f: fields) {
                f.commit();
            }
            fields.clear();
        }
        public void discardFields() {
            for (Field<?> f: fields) {
                f.discard();
            }
            fields.clear();
        }

    }
}
