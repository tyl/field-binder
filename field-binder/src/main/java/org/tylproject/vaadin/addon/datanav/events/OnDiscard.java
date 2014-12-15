package org.tylproject.vaadin.addon.datanav.events;

import com.vaadin.util.ReflectTools;
import org.tylproject.vaadin.addon.datanav.DataNavigation;

import java.lang.reflect.Method;

/**
 * Created by evacchi on 19/11/14.
 */
public class OnDiscard {
    public static class Event extends CrudEvent {
        public Event(DataNavigation source) {
            super(source);
        }
    }

    public static interface Listener {
        public static final Method METHOD =
                ReflectTools.findMethod(Listener.class,
                        "onDiscard", Event.class);
        public void onDiscard(Event event);
    }

    public static interface Notifier {
        void addOnDiscardListener(Listener listener);
        void removeOnDiscardListener(Listener listener);
    }
}
