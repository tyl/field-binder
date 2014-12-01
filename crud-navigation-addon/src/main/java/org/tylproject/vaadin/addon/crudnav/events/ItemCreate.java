package org.tylproject.vaadin.addon.crudnav.events;

import com.vaadin.util.ReflectTools;
import org.tylproject.vaadin.addon.crudnav.CrudNavigation;

import java.lang.reflect.Method;
import java.util.EventObject;

public class ItemCreate {
    public static class Event extends CrudEvent {
        public Event(CrudNavigation source) {
            super(source);
        }
    }

    public static interface Listener {
        public static final Method METHOD =
                ReflectTools.findMethod(Listener.class,
                        "itemCreate", Event.class);
        public void itemCreate(Event event);
    }

    public static interface Notifier {
        void addItemCreateListener(Listener listener);
        void removeItemCreateListener(Listener listener);
    }
}
