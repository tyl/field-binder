package org.tylproject.vaadin.addon.masterdetail.crud;

import com.vaadin.data.util.BeanItem;
import org.tylproject.vaadin.addon.crudnav.events.CurrentItemChange;
import org.tylproject.vaadin.addon.MongoContainer;
import org.tylproject.vaadin.addon.crudnav.events.ItemCreate;
import org.tylproject.vaadin.addon.crudnav.events.OnCommit;
import org.tylproject.vaadin.addon.fieldbinder.FieldBinder;

/**
 * Created by evacchi on 27/11/14.
 */
public class MongoNavigationStrategy<M> extends DefaultNavigationStrategy<M> {

    public MongoNavigationStrategy(Class<M> masterClass, FieldBinder<M> fieldBinder) {
        super(masterClass, fieldBinder);
    }




    @Override
    public void itemCreate(ItemCreate.Event event) {
        M bean = createBean();
        super.fieldBinder.setReadOnly(false);
//        final MongoContainer<M> container = (MongoContainer<M>) super.navigation.getContainer();
//        super.navigation.setCurrentItemId(container.addEntity(bean));
        event.getSource().setCurrentItemId(null);
        super.fieldBinder.setItemDataSource(new BeanItem<M>(bean));
    }

    @Override
    public void currentItemChange(CurrentItemChange.Event event) {
        super.fieldBinder.setItemDataSource(event.getNewItem());
    }

    protected M createBean() {
        try {
            return getBeanClass().newInstance();
        } catch (Exception ex) {
            throw new UnsupportedOperationException(ex);
        }
    }


    @Override
    public void onCommit(OnCommit.Event event) {
        super.onCommit(event);
        final MongoContainer<M> container = (MongoContainer<M>) event.getSource().getContainer();
        BeanItem<M> beanItem = (BeanItem<M>) fieldBinder.getItemDataSource();
        event.getSource().setCurrentItemId(container.addEntity(beanItem.getBean()));

    }
}
