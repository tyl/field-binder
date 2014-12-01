package org.tylproject.vaadin.addon.masterdetail.builder.crud;

import org.tylproject.vaadin.addon.crudnav.events.ItemCreate;

/**
 * Created by evacchi on 26/11/14.
 */
public class BeanMasterCrud<T> extends DefaultMasterCrud implements ItemCreate.Listener {
    private final Class<T> beanClass;
    public BeanMasterCrud(Class<T> beanClass) {
        this.beanClass = beanClass;
    }


    @Override
    public void itemCreate(ItemCreate.Event event) {
        try {
            T bean = beanClass.newInstance();
            super.fieldGroup.setReadOnly(true);
            super.navigation.getContainer().addItem(bean);
            super.navigation.setCurrentItemId(bean);
        } catch (Exception ex) {
            throw new UnsupportedOperationException(ex);
        }
    }
}
