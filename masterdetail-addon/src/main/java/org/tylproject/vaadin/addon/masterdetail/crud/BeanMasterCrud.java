package org.tylproject.vaadin.addon.masterdetail.crud;

import com.vaadin.data.util.BeanItem;
import org.tylproject.vaadin.addon.crudnav.events.CurrentItemChange;
import org.tylproject.vaadin.addon.crudnav.events.ItemCreate;

/**
 * Created by evacchi on 26/11/14.
 */
public class BeanMasterCrud<T> extends DefaultMasterCrud
        implements ItemCreate.Listener {
    private final Class<T> beanClass;
    public BeanMasterCrud(Class<T> beanClass) {
        this.beanClass = beanClass;
    }


    @Override
    public void itemCreate(ItemCreate.Event event) {
        T bean = createBean();
        super.fieldGroup.setReadOnly(false);
        super.navigation.getContainer().addItem(bean);
        super.navigation.setCurrentItemId(bean);

    }

    @Override
    public void currentItemChange(CurrentItemChange.Event event) {
        if (event.getNewItemId() == null) {
            fieldGroup.setItemDataSource(new BeanItem<T>(createBean()));
        }
    }

    protected T createBean() {
        try {
            return beanClass.newInstance();
        } catch (Exception ex) {
            throw new UnsupportedOperationException(ex);
        }
    }
}
