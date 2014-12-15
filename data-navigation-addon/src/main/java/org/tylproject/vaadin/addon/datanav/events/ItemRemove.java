package org.tylproject.vaadin.addon.datanav.events;

import com.vaadin.util.ReflectTools;
import org.tylproject.vaadin.addon.datanav.DataNavigation;

import java.lang.reflect.Method;

public class ItemRemove {
    public static class Event extends CrudEvent {
        public Event(DataNavigation source) {
            super(source);
        }
    }

    public static interface Listener {
        public static final Method METHOD =
                ReflectTools.findMethod(Listener.class,
                        "itemRemove", Event.class);
        public void itemRemove(Event event);
    }

    public static interface Notifier {
        void addItemRemoveListener(Listener listener);
        void removeItemRemoveListener(Listener listener);
    }
}
