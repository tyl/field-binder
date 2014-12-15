package org.tylproject.vaadin.addon.fieldbinder.strategies;

import com.vaadin.data.Container;
import com.vaadin.ui.*;
import org.tylproject.vaadin.addon.datanav.events.*;

/**
 * Created by evacchi on 11/12/14.
 */
public class DefaultTableStrategy<T> implements CrudStrategy, CurrentItemChange.Listener {

    final Table table;
    final Class<T> beanClass;

    public DefaultTableStrategy(final Class<T> beanClass, final Table table) {
        this.beanClass = beanClass;
        this.table = table;
        table.setTableFieldFactory(new TableFieldFactory() {
            final DefaultFieldFactory fieldFactory = DefaultFieldFactory.get();
            @Override
            public Field<?> createField(Container container, Object itemId, Object propertyId, Component uiContext) {
                if (itemId == table.getValue())
                    return fieldFactory.createField(container, itemId, propertyId, uiContext);
                else return null;
            }
        });
    }

    @Override
    public void itemEdit(ItemEdit.Event event) {
        table.setEditable(!this.table.isEditable());
        table.setSelectable(false);
        table.focus();
    }


    @Override
    public void itemCreate(ItemCreate.Event event) {
        T bean = createBean();
        event.getSource().getContainer().addItem(bean);
        event.getSource().setCurrentItemId(bean);
        table.select(bean);
        table.setEditable(true);
        table.setSelectable(false);
        table.focus();


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
        this.table.setSelectable(true);
    }

    public void onCommit(OnCommit.Event event) {
        this.table.commit();
        this.table.setEditable(false);
        this.table.setSelectable(true);
    }

    public void itemRemove(ItemRemove.Event event) {
        this.table.removeItem(event.getSource().getCurrentItemId());
    }

    @Override
    public void currentItemChange(CurrentItemChange.Event event) {
        table.select(event.getNewItemId());
    }
}
