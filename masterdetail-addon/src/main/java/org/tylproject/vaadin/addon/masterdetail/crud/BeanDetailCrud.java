package org.tylproject.vaadin.addon.masterdetail.crud;

import com.vaadin.ui.Table;
import org.tylproject.vaadin.addon.crudnav.CrudNavigation;
import org.tylproject.vaadin.addon.crudnav.events.ItemCreate;
import org.tylproject.vaadin.addon.fieldbinder.FieldBinder;

/**
 * Created by evacchi on 26/11/14.
 */
public class BeanDetailCrud<T> extends DefaultDetailCrud implements ItemCreate.Listener{
    private final Class<T> beanClass;
    public BeanDetailCrud(Class<T> beanClass) {
        this.beanClass = beanClass;
    }

    public BeanDetailCrud(Class<T> beanClass, Table table, CrudNavigation navigation) {
        this.beanClass = beanClass;
        this.table = table;
        this.navigation = navigation;
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
