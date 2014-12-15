package org.tylproject.vaadin.addon.datanav.events;

import com.vaadin.util.ReflectTools;
import org.tylproject.vaadin.addon.datanav.CrudNavigation;

import java.lang.reflect.Method;

public class LastItem {
    public static class Event extends ItemNavigationEvent {
        public Event(CrudNavigation source, Object newItemId, Object oldItemId) {
            super(source, newItemId, oldItemId);
        }
    }

    public static interface Listener {
        public static final Method METHOD =
                ReflectTools.findMethod(Listener.class,
                        "lastItem", Event.class);
        public void lastItem(Event event);
    }

    public static interface Notifier {
        void addLastItemListener(Listener listener);
        void removeLastItemListener(Listener listener);
    }
}
