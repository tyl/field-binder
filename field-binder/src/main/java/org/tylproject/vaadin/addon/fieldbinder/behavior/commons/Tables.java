package org.tylproject.vaadin.addon.fieldbinder.behavior.commons;

import com.vaadin.ui.Table;
import org.tylproject.vaadin.addon.datanav.events.*;
import org.tylproject.vaadin.addon.fieldbinder.TableFieldManager;
import org.tylproject.vaadin.addon.fieldbinder.behavior.CrudListeners;
import org.tylproject.vaadin.addon.fieldbinder.behavior.FindListeners;

/**
 * Base behavior for Tables
 */
public class Tables {

    public static class CurrentItemChangeListener implements CurrentItemChange.Listener {
        private final Table table;

        public CurrentItemChangeListener(Table table) {
            this.table = table;
        }

        @Override
        public void currentItemChange(CurrentItemChange.Event event) {
            table.select(event.getNewItemId());
        }

    }

    public static class BaseCrud<T> implements CrudListeners {
        final protected Table table;
        final protected Class<T> beanClass;
        final protected TableFieldManager fieldManager;

        protected T newEntity = null;
        protected FindListeners findListeners;

        public BaseCrud(final Class<T> beanClass, final Table table) {
            this.beanClass = beanClass;
            this.table = table;

            this.fieldManager = new TableFieldManager(table);

            table.setTableFieldFactory(fieldManager);
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
}
