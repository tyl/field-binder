package org.tylproject.vaadin.addon.crudnav.events;

import com.vaadin.util.ReflectTools;
import org.tylproject.vaadin.addon.crudnav.CrudNavigation;

import java.lang.reflect.Method;
import java.util.EventObject;

/**
 * Created by evacchi on 19/11/14.
 */
public class OnDiscard {
    public static class Event extends CrudEvent {
        public Event(CrudNavigation source) {
            super(source);
        }
    }

    public static interface Listener {
        public static final Method METHOD =
                ReflectTools.findMethod(Listener.class,
                        "onDiscardListener", Event.class);
        public void onDiscardListener(Event event);
    }

    public static interface Notifier {
        void addOnDiscardListener(Listener listener);
        void removeOnDiscardListener(Listener listener);
    }
}
