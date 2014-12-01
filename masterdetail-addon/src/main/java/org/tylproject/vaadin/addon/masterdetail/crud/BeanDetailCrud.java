package org.tylproject.vaadin.addon.masterdetail.crud;

import org.tylproject.vaadin.addon.crudnav.events.ItemCreate;

/**
 * Created by evacchi on 26/11/14.
 */
public class BeanDetailCrud<T> extends DefaultDetailCrud implements ItemCreate.Listener{
    private final Class<T> beanClass;
    public BeanDetailCrud(Class<T> beanClass) {
        this.beanClass = beanClass;
    }

    @Override
    public void itemCreate(ItemCreate.Event event) {
        try {
            T bean = beanClass.newInstance();
            super.table.addItem(bean);
            super.navigation.setCurrentItemId(bean);
        } catch (Exception ex) {
            throw new UnsupportedOperationException(ex);
        }
    }

}
