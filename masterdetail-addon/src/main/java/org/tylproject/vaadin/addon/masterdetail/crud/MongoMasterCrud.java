package org.tylproject.vaadin.addon.masterdetail.crud;

import com.vaadin.data.util.BeanItem;
import org.tylproject.vaadin.addon.crudnav.CrudNavigation;
import org.tylproject.vaadin.addon.crudnav.events.CurrentItemChange;
import org.tylproject.vaadin.addon.MongoContainer;
import org.tylproject.vaadin.addon.crudnav.events.ItemCreate;
import org.tylproject.vaadin.addon.crudnav.events.OnCommit;
import org.tylproject.vaadin.addon.fieldbinder.FieldBinder;

/**
 * Created by evacchi on 27/11/14.
 */
public class MongoMasterCrud<M> extends DefaultMasterCrud {
    private final Class<M> masterClass;

    public MongoMasterCrud(Class<M> masterClass) {
        this.masterClass = masterClass;
    }

    public MongoMasterCrud(Class<M> masterClass, FieldBinder<M> fieldBinder, CrudNavigation nav) {
        this.masterClass = masterClass;
        this.fieldBinder = fieldBinder;
        this.navigation = nav;
    }




    @Override
    public void itemCreate(ItemCreate.Event event) {
        M bean = createBean();
        super.fieldBinder.setReadOnly(false);
//        final MongoContainer<M> container = (MongoContainer<M>) super.navigation.getContainer();
//        super.navigation.setCurrentItemId(container.addEntity(bean));
        super.navigation.setCurrentItemId(null);
        super.fieldBinder.setItemDataSource(new BeanItem<M>(bean));
    }

    @Override
    public void currentItemChange(CurrentItemChange.Event event) {
        if (event.getNewItemId() == null) {
            fieldBinder.setItemDataSource(new BeanItem<M>(createBean()));
        }
    }

    protected M createBean() {
        try {
            return masterClass.newInstance();
        } catch (Exception ex) {
            throw new UnsupportedOperationException(ex);
        }
    }


    @Override
    public void onCommit(OnCommit.Event event) {
        super.onCommit(event);
        final MongoContainer<M> container = (MongoContainer<M>) super.navigation.getContainer();
        BeanItem<M> beanItem = (BeanItem<M>) fieldBinder.getItemDataSource();
        super.navigation.setCurrentItemId(container.addEntity(beanItem.getBean()));

    }
}
