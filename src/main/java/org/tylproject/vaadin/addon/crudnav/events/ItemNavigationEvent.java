package org.tylproject.vaadin.addon.crudnav.events;

import com.vaadin.data.Item;
import org.tylproject.vaadin.addon.crudnav.CrudNavigation;

import java.util.EventObject;

/**
 * Created by evacchi on 19/11/14.
 */
public class ItemNavigationEvent extends EventObject {
    private final Object newItemId;
    private final Object oldItemId;

    public ItemNavigationEvent(CrudNavigation source, Object newItemId, Object oldItemId)  {
        super(source);
        this.newItemId = newItemId;
        this.oldItemId = oldItemId;
    }

    public Object getNewItemId() {
        return newItemId;
    }
    public Item getNewItem() {
        return getSource().getContainer().getItem(newItemId);
    }

    public Object getOldItemId() {
        return oldItemId;
    }
    public Item getOldItem() {
        return getSource().getContainer().getItem(oldItemId);
    }

    @Override
    public CrudNavigation getSource() {
        return (CrudNavigation) super.getSource();
    }
}