package org.tylproject.vaadin.addon.datanav.events;

import com.vaadin.util.ReflectTools;
import org.tylproject.vaadin.addon.datanav.DataNavigation;

import java.lang.reflect.Method;

public class NextItem {
    public static class Event extends ItemNavigationEvent {
        public Event(DataNavigation source, Object newItemId, Object oldItemId) {
            super(source, newItemId, oldItemId);
        }
    }

    public static interface Listener {
        public static final Method METHOD =
                ReflectTools.findMethod(Listener.class,
                        "nextItem", Event.class);
        public void nextItem(Event event);
    }

    public static interface Notifier {
        void addNextItemListener(Listener listener);
        void removeNextItemListener(Listener listener);
    }
}