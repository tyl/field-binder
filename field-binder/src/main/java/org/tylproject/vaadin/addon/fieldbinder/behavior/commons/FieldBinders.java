package org.tylproject.vaadin.addon.fieldbinder.behavior.commons;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import org.tylproject.vaadin.addon.datanav.events.*;
import org.tylproject.vaadin.addon.fieldbinder.FieldBinder;
import org.tylproject.vaadin.addon.fieldbinder.behavior.CrudListeners;
import org.tylproject.vaadin.addon.fieldbinder.behavior.FindListeners;
import org.tylproject.vaadin.addon.fields.search.SearchFieldManager;
import org.tylproject.vaadin.addon.fields.search.SearchPattern;

/**
 * Base behavior for FieldBinders
 */
public class FieldBinders {

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


        @Override
        public void onCommit(OnCommit.Event event) {
            fieldBinder.commit();
            fieldBinder.setReadOnly(true);
        }

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

        @Override
        public void itemEdit(ItemEdit.Event event) {
            fieldBinder.setReadOnly(false);
            fieldBinder.focus();
        }

        @Override
        public void itemCreate(ItemCreate.Event event) {
            fieldBinder.setReadOnly(false);
            event.getSource().setCurrentItemId(null);
            T bean = createBean();
            fieldBinder.setBeanDataSource(bean);
        }


        protected T createBean() {
            try {
                return beanClass.newInstance();
            } catch (Exception ex) {
                throw new UnsupportedOperationException(ex);
            }
        }

    }

    public static class Find<T> implements FindListeners {
        boolean clearToFindMode = false;
        final FieldBinder<T> binder;
        final SearchFieldManager searchFieldManager;
        public Find(FieldBinder<T> binder) {
            this.binder = binder;
            this.searchFieldManager = new SearchFieldManager(binder);
        }


        @Override
        public void clearToFind(ClearToFind.Event event) {
            if (clearToFindMode) {
                searchFieldManager.clear();
                return;
            } else {
                clearToFindMode = true;
            }

            ((Container.Filterable)event.getSource().getContainer()).removeAllContainerFilters();

            searchFieldManager.replaceFields(binder);

        }

        @Override
        public void onFind(OnFind.Event event) {

            clearToFindMode = false;

            for (SearchPattern sp : searchFieldManager.getPatternsFromValues().values()) {
                ((Container.Filterable)event.getSource().getContainer())
                    .addContainerFilter(sp.getFilter());
            }

            searchFieldManager.restoreFields(binder);
            binder.getNavigation().first();

        }
    }
}
