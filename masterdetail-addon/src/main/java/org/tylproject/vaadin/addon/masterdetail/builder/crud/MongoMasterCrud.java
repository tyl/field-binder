package org.tylproject.vaadin.addon.masterdetail.builder.crud;

import com.vaadin.data.util.BeanItem;
import org.tylproject.vaadin.addon.masterdetail.builder.crud.BeanMasterCrud;
import org.tylproject.vaadin.addon.masterdetail.builder.crud.DefaultMasterCrud;
import org.tylproject.vaadin.addon.MongoContainer;
import org.tylproject.vaadin.addon.crudnav.events.ItemCreate;
import org.tylproject.vaadin.addon.crudnav.events.OnCommit;

/**
 * Created by evacchi on 27/11/14.
 */
public class MongoMasterCrud<M> extends DefaultMasterCrud {
    private final Class<M> masterClass;

    public MongoMasterCrud(Class<M> masterClass) {
        this.masterClass = masterClass;
    }



    @Override
    public void itemCreate(ItemCreate.Event event) {
        try {
            M bean = masterClass.newInstance();
            super.fieldGroup.setReadOnly(true);
            final MongoContainer<M> container = (MongoContainer<M>) super.navigation.getContainer();
            super.navigation.setCurrentItemId(container.addEntity(bean));
        } catch (Exception ex) {
            throw new UnsupportedOperationException(ex);
        }
    }

    @Override
    public void onCommit(OnCommit.Event event) {
        super.onCommit(event);
        final MongoContainer<M> container = (MongoContainer<M>) super.navigation.getContainer();
        BeanItem<M> beanItem = (BeanItem<M>) fieldGroup.getItemDataSource();
        container.addEntity(beanItem.getBean());
    }
}
