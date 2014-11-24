package org.tylproject.vaadin.addon.masterdetail;

import com.vaadin.data.Item;
import com.vaadin.ui.Component;
import com.vaadin.util.ReflectTools;

import java.lang.reflect.Method;
import java.util.EventObject;

/**
 * Created by evacchi on 24/11/14.
 */
public abstract class MasterItemChange {
    public static class Event extends EventObject {
        private final Item oldItem;
        private final Item newItem;
        public Event(MasterDetail source, Item oldItem, Item newItem) {
            super(source);
            this.oldItem = oldItem;
            this.newItem = newItem;
        }
        @Override
        public MasterDetail getSource() {
            return (MasterDetail) super.getSource();
        }

        public Item getOldItem() {
            return oldItem;
        }

        public Item getNewItem() {
            return newItem;
        }
    }


    public static interface Listener {
        public static final Method method = ReflectTools.findMethod(Listener.class, "masterItemChange", Event.class);

        public void masterItemChange(MasterItemChange.Event event);
    }

    public static interface Notifier {
        public void addMasterItemChangeListener(Listener listener);
        public void removeMasterItemChangeListener(Listener listener);
    }

}
