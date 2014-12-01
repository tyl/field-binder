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



    private static BasicCrudStrategy instance = new BasicCrudStrategy();
    public static BasicCrudStrategy get() { return instance; }

    protected BasicCrudStrategy() {}

    @Override
    public void itemRemove(ItemRemove.Event event) {
        event.getSource().getContainer().removeItem(event.getSource().getCurrentItemId());
    }

    @Override
    public void itemCreate(ItemCreate.Event event) {
        Object id = event.getSource().getContainer().addItem();
        event.getSource().setCurrentItemId(id);
    }

    @Override
    public void onCommit(OnCommit.Event event) {
        Notification.show("committed");
    }
}
