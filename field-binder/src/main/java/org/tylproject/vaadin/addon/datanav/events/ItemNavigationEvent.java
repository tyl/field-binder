package org.tylproject.vaadin.addon.datanav.events;

import com.vaadin.data.Item;
import org.tylproject.vaadin.addon.datanav.DataNavigation;

import java.util.EventObject;

/**
 * Created by evacchi on 19/11/14.
 */
public class ItemNavigationEvent extends EventObject {
    private final Object newItemId;
    private final Object oldItemId;
    private Item newItem;
    private Item oldItem;

    public ItemNavigationEvent(DataNavigation source, Object newItemId, Object oldItemId)  {
        super(source);
        this.newItemId = newItemId;
        this.oldItemId = oldItemId;
    }

    public Object getNewItemId() {
        return newItemId;
    }
    public Item getNewItem() {
        if (newItemId == null) return null;
        if (newItem == null) {
            newItem = getSource().getContainer().getItem(newItemId);
        }
        return newItem;
    }

    public Object getOldItemId() {
        return oldItemId;
    }
    public Item getOldItem() {
        if (oldItemId == null) return null;
        if (oldItem == null) {
            oldItem = getSource().getContainer().getItem(oldItemId);
        }
        return oldItem;
    }

    @Override
    public DataNavigation getSource() {
        return (DataNavigation) super.getSource();
    }
}