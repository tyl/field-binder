package org.tylproject.vaadin.addon.fieldbinder.behavior;

import com.vaadin.ui.Table;
import org.bson.types.ObjectId;
import org.tylproject.vaadin.addon.BufferedMongoContainer;
import org.tylproject.vaadin.addon.datanav.events.*;

/**
 * Created by evacchi on 19/12/14.
 */
public class BufferedMongoContainerTableBehavior<T> extends ListContainerTableBehavior<T> {
    public BufferedMongoContainerTableBehavior(Class<T> beanClass, Table table) {
        super(beanClass, table);
    }

    @Override
    public void itemEdit(ItemEdit.Event event) {
        BufferedMongoContainer<T> bmc = (BufferedMongoContainer<T>) event.getSource().getContainer();

        // notify the BufferedMongoContainer
        // that we want to edit an item
        bmc.updateItem((ObjectId) event.getSource().getCurrentItemId());
        super.itemEdit(event);

    }

    @Override
    public void itemCreate(ItemCreate.Event event) {
        T bean = createBean();

        BufferedMongoContainer<T> bmc = (BufferedMongoContainer<T>) event.getSource().getContainer();
        ObjectId itemId = bmc.addItem();

        event.getSource().setCurrentItemId(itemId);

        table.setEditable(true);
        table.setSelectable(false);
        table.focus();


    }

    @Override
    public void itemRemove(ItemRemove.Event event) {
        super.itemRemove(event);
        BufferedMongoContainer<T> bmc = (BufferedMongoContainer<T>) event.getSource().getContainer();
        bmc.commit();
    }

    @Override
    public void onCommit(OnCommit.Event event) {
        super.onCommit(event);
        BufferedMongoContainer<T> bmc = (BufferedMongoContainer<T>) event.getSource().getContainer();
        bmc.commit();
    }

    @Override
    public void onDiscard(OnDiscard.Event event) {
        super.onDiscard(event);
        BufferedMongoContainer<T> bmc = (BufferedMongoContainer<T>) event.getSource().getContainer();
        bmc.discard();
    }
}
