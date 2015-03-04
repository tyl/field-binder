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
import com.vaadin.ui.*;
import org.tylproject.vaadin.addon.datanav.events.*;
import org.tylproject.vaadin.addon.fieldbinder.*;
import org.tylproject.vaadin.addon.fieldbinder.behavior.CrudHandler;
import org.tylproject.vaadin.addon.fieldbinder.behavior.CrudListeners;
import org.tylproject.vaadin.addon.fields.collectiontables.adaptors.TableAdaptor;
import org.tylproject.vaadin.addon.fields.collectiontables.adaptors.TabularViewAdaptor;

import java.lang.reflect.Constructor;

/**
 * Base behavior for Tables
 */
public class Tables {


    /**
     * Link Table selection to the DataNavigation instance
     */
    public static class CurrentItemChangeListener implements CurrentItemChange.Listener {
        private final TabularViewAdaptor<?,?> table;

        public CurrentItemChangeListener(TabularViewAdaptor<?,?> table) {
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
    public abstract static class BaseCrud<T> implements CrudHandler {
        final protected TabularViewAdaptor<T,?> tableAdaptor;
        final protected Class<T> beanClass;
        protected T newEntity = null;

        public BaseCrud(final Class<T> beanClass, final TabularViewAdaptor<T,?> tableAdaptor) {
            this.beanClass = beanClass;
            this.tableAdaptor = tableAdaptor;
        }

        @Override
        public boolean matches(Class<?> clazz) {
            try {
                return verifyMatch(clazz);
            } catch (NoClassDefFoundError ex) { return false; }
        }

        protected abstract boolean verifyMatch(Class<?> clazz);

        @Override
        public void itemEdit(ItemEdit.Event event) {

            // setEditable(true) causes table to generate Fields and bind them to the propertyIds
            // Field creation is done through the TableFieldFactory.

            // However, this binding *does not* work the same as FieldGroup's binding
            // FieldGroup wraps properties inside TransactionalPropertyWrappers
            // on commit it checks whether properties are of this type and *throws an error*
            // if they aren't!

            // thus, we need to setEditable(true)
            // and THEN invoke fieldBinder.setItemDataSource()

            // IT IS NOT enough to attach to the currentItemChange event to do this binding
            // because Table will RESET the bindings each time we do setEditable(true)!

            tableAdaptor.setEditable(true);
            tableAdaptor.setSelectable(false);
            tableAdaptor.focus();

            // this line is equivalent to getting the table's fieldBinder and
            // then invoking fieldBinder.setItemDataSource(...)
            tableAdaptor.setEditorDataSource(event.getSource().getCurrentItem());

        }


        @Override
        public void itemCreate(ItemCreate.Event event) {
            tableAdaptor.setEditable(true);
            tableAdaptor.setSelectable(false);
            tableAdaptor.focus();

            // see itemEdit() to understand why this is necessary
            tableAdaptor.setEditorDataSource(event.getSource().getCurrentItem());
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

            if (this.tableAdaptor instanceof TableAdaptor) {
                ((TableAdaptor<?>) this.tableAdaptor).getComponent().refreshRowCache();
            } // else: grid does not support this right now.


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

    /**
     * TableFieldFactory for inline editing in a table. Only one line at a time is editable. Uses a FieldBinder instance
     */
    public static class SingleLineFieldFactory implements TableFieldFactory {
        private FieldBinder<?> fieldBinder;

        public SingleLineFieldFactory(FieldBinder<?> fieldBinder) {
            this.fieldBinder = fieldBinder;
        }

        @Override
        public Field<?> createField(Container container, Object itemId, Object propertyId, Component uiContext) {
            if (itemId != fieldBinder.getNavigation().getCurrentItemId()) return null;

            Field<?> f = fieldBinder.getField(propertyId);

            if (f == null) {

                // if the propertyId is unknown/no field exists, generate it

                // CAVEAT: this might *not* be what people want;
                // e.g., users may want to NOT show a field if it is not explicitly declared
                //       if users will request this feature, than this should be properly
                //       configurable

                return fieldBinder.build(propertyId);
            } else {
                return f;
            }
        }
    }

}
