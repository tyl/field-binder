package org.tylproject.vaadin.addon.crudnav.events;

import com.vaadin.util.ReflectTools;
import org.tylproject.vaadin.addon.crudnav.CrudNavigation;

import java.lang.reflect.Method;

public class FirstItem {
    public static class Event extends ItemNavigationEvent {
        public Event(CrudNavigation source, Object newItemId, Object oldItemId) {
            super(source, newItemId, oldItemId);
        }
    }

    public static interface Listener {
        public static final Method METHOD =
                ReflectTools.findMethod(Listener.class,
                        "firstItemListener", Event.class);
        public void firstItemListener(Event event);
    }

    public static interface Notifier {
        void addFirstItemListener(Listener listener);
        void removeFirstItemListener(Listener listener);
    }
}
