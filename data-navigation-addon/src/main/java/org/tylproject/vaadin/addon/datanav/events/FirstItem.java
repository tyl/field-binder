package org.tylproject.vaadin.addon.datanav.events;

import com.vaadin.util.ReflectTools;
import org.tylproject.vaadin.addon.datanav.DataNavigation;

import java.lang.reflect.Method;

public class FirstItem {
    public static class Event extends ItemNavigationEvent {
        public Event(DataNavigation source, Object newItemId, Object oldItemId) {
            super(source, newItemId, oldItemId);
        }
    }

    public static interface Listener {
        public static final Method METHOD =
                ReflectTools.findMethod(Listener.class,
                        "firstItem", Event.class);
        public void firstItem(Event event);
    }

    public static interface Notifier {
        void addFirstItemListener(Listener listener);
        void removeFirstItemListener(Listener listener);
    }
}
