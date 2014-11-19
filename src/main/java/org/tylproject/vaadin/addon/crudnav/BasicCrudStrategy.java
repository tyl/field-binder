package org.tylproject.vaadin.addon.crudnav;

import com.vaadin.ui.Notification;
import org.tylproject.vaadin.addon.crudnav.events.ItemRemove;
import org.tylproject.vaadin.addon.crudnav.events.ItemCreate;
import org.tylproject.vaadin.addon.crudnav.events.OnCommit;

/**
 * Created by evacchi on 19/11/14.
 */
public class BasicCrudStrategy implements ItemRemove.Listener,
        ItemCreate.Listener, OnCommit.Listener {
    @Override
    public void itemRemoveListener(ItemRemove.Event event) {
        event.getSource().getContainer().removeItem(event.getSource().getCurrentItemId());
    }

    @Override
    public void itemCreateListener(ItemCreate.Event event) {
        Object id = event.getSource().getContainer().addItem();
        event.getSource().setCurrentItemId(id);
    }

    @Override
    public void onCommitListener(OnCommit.Event event) {
        Notification.show("committed");
    }
}
