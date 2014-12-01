package org.tylproject.vaadin.addon.crudnav.events;

import com.vaadin.util.ReflectTools;
import org.tylproject.vaadin.addon.crudnav.CrudNavigation;

import java.lang.reflect.Method;

public class PrevItem {
    public static class Event extends ItemNavigationEvent {
        public Event(CrudNavigation source, Object newItemId, Object oldItemId) {
            super(source, newItemId, oldItemId);
        }
    }

    public static interface Listener {
        public static final Method METHOD =
                ReflectTools.findMethod(Listener.class,
                        "prevItem", Event.class);
        public void prevItem(Event event);
    }

    public static interface Notifier {
        void addPrevItemListener(Listener listener);
        void removePrevItemListener(Listener listener);
    }
}
