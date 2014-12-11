package org.tylproject.vaadin.addon.fieldbinder.strategies;

import com.vaadin.ui.Table;
import org.tylproject.vaadin.addon.crudnav.events.*;

/**
 * Created by evacchi on 11/12/14.
 */
public class DefaultTableStrategy<T> implements CrudStrategy {

    final Table table;
    final Class<T> beanClass;

    public DefaultTableStrategy(Class<T> beanClass, Table table) {
        this.beanClass = beanClass;
        this.table = table;
    }

    public void itemEdit(ItemEdit.Event event) {
        this.table.setEditable(!this.table.isEditable());
    }


    @Override
    public void itemCreate(ItemCreate.Event event) {
        T bean = createBean();
        event.getSource().getContainer().addItem(bean);
        event.getSource().setCurrentItemId(bean);

    }

    protected T createBean() {
        try {
            return beanClass.newInstance();
        } catch (Exception ex) {
            throw new UnsupportedOperationException(ex);
        }
    }

    public void onDiscard(OnDiscard.Event event) {
        this.table.discard();
        this.table.setEditable(false);
    }

    public void onCommit(OnCommit.Event event) {
        this.table.commit();
        this.table.setEditable(false);
    }

    public void itemRemove(ItemRemove.Event event) {
        this.table.removeItem(event.getSource().getCurrentItemId());
    }
}
