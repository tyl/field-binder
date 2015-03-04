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
import org.tylproject.vaadin.addon.datanav.events.*;
import org.tylproject.vaadin.addon.fieldbinder.FieldBinder;
import org.tylproject.vaadin.addon.fieldbinder.behavior.CrudListeners;
import org.tylproject.vaadin.addon.fieldbinder.behavior.FindListeners;
import org.tylproject.vaadin.addon.fields.search.FieldBinderSearchFieldManager;
import org.tylproject.vaadin.addon.fields.search.SearchPattern;

import java.lang.reflect.Constructor;

/**
 * Base behavior for FieldBinders
 */
public class FieldBinders {

    /**
     * Link field data sources to the DataNavigation instance
     */
    public static class CurrentItemChangeListener<T> implements CurrentItemChange.Listener {
        private final FieldBinder<T> fieldBinder;

        public CurrentItemChangeListener(FieldBinder<T> fieldBinder) {
            this.fieldBinder = fieldBinder;
        }

        @Override
        public void currentItemChange(CurrentItemChange.Event event) {
            fieldBinder.setItemDataSource(event.getNewItem());
        }
    }

    /**
     * Base behavior for all containers.
     *
     * Container-specific implementations should extend this class.
     * In most cases only {@link org.tylproject.vaadin.addon.fieldbinder.behavior.commons.FieldBinders.BaseCrud#itemCreate(org.tylproject.vaadin.addon.datanav.events.ItemCreate.Event)}
     * must be overridden.
     *
     * The default behavior is to set the FieldBinder to read-only
     * and set it to writable in edit mode (edit or create). State
     * is re-set to read-only when the edit mode is left
     * (onCommit or onDiscard).
     *
     */
    public static class BaseCrud<T> implements CrudListeners {

        protected final FieldBinder<T> fieldBinder;
        private final Class<T> beanClass;

        public BaseCrud(FieldBinder<T> fieldBinder) {
            this.fieldBinder = fieldBinder;
            this.beanClass = fieldBinder.getType();
            fieldBinder.setReadOnly(true);
        }

        public Class<T> getBeanClass() {
            return beanClass;
        }

        public FieldBinder<T> getFieldBinder() {
            return fieldBinder;
        }

        /**
         * commit the FieldBinder and set it to read-only
         */
        @Override
        public void onCommit(OnCommit.Event event) {
            fieldBinder.commit();
            fieldBinder.setReadOnly(true);
        }

        /**
         * discard and set read-only.
         * If currentItem was null, also go to first element.
         */
        @Override
        public void onDiscard(OnDiscard.Event event) {
            fieldBinder.discard();
            fieldBinder.setReadOnly(true);
            Item currentItem = event.getSource().getCurrentItem();
            if (currentItem == null) {
                event.getSource().first();
            } else {
                // restore item again (so that also the tables get updated)
                fieldBinder.setItemDataSource(currentItem);
            }
        }

        @Override
        public void itemRemove(ItemRemove.Event event) {
            event.getSource().getContainer().removeItem(event.getSource().getCurrentItemId());
        }

        /**
         * set writable; focus the first field
         * @param event
         */
        @Override
        public void itemEdit(ItemEdit.Event event) {
            fieldBinder.setReadOnly(false);
            fieldBinder.focus();
        }

        /**
         * create item, set current itemId to null
         */
        @Override
        public void itemCreate(ItemCreate.Event event) {
            fieldBinder.setReadOnly(false);
            event.getSource().setCurrentItemId(null);
            T bean = createBean();
            fieldBinder.setBeanDataSource(bean);
        }


        protected T createBean() {
            try {
                Constructor<T> ctor = beanClass.getConstructor();
                return ctor.newInstance();
            } catch (Exception ex) {
                throw new UnsupportedOperationException(ex);
            }
        }

    }

    /**
     * Default implementation for FindListeners. It replaces
     * the fields in the form with custom text inputs for search.
     *
     * @see org.tylproject.vaadin.addon.fields.search.SearchPatternField
     * @see org.tylproject.vaadin.addon.fields.search.SearchFieldManager
     * @see org.tylproject.vaadin.addon.fields.search.FieldBinderSearchFieldManager
     *
     */
    public static class Find<T> implements FindListeners {
        boolean clearToFindMode = false;
        final FieldBinder<T> binder;
        final FieldBinderSearchFieldManager searchFieldManager;
        public Find(FieldBinder<T> binder) {
            this.binder = binder;
            this.searchFieldManager = new FieldBinderSearchFieldManager(binder);
        }


        @Override
        public void clearToFind(ClearToFind.Event event) {
            event.getSource().setCurrentItemId(null);

            // click twice on ClearToFind button to clear the fields
            if (clearToFindMode) {
                searchFieldManager.clear();
                return;
            } else {
                clearToFindMode = true;
            }

            ((Container.Filterable)event.getSource().getContainer()).removeAllContainerFilters();

            searchFieldManager.replaceFields();

        }

        /**
         * pulls the generated Filters from the FieldBinderSearchFieldManager
         * and applies them to the underlying container. Restores back the regular fields
         *
         */
        @Override
        public void onFind(OnFind.Event event) {

            clearToFindMode = false;

            for (SearchPattern sp : searchFieldManager.getPatternsFromValues().values()) {
                ((Container.Filterable)event.getSource().getContainer())
                    .addContainerFilter(sp.getFilter());
            }

            searchFieldManager.restoreFields();
            binder.getNavigation().first();

        }
    }
}
