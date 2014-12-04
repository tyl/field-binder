package org.tylproject.vaadin.addon.crudnav.events;

import com.vaadin.util.ReflectTools;
import org.tylproject.vaadin.addon.crudnav.CrudNavigation;

import java.lang.reflect.Method;

/**
 * Created by evacchi on 19/11/14.
 */
public class OnClearToFind {
    public static class Event extends CrudEvent {
        public Event(CrudNavigation source) {
            super(source);
        }
    }

    public static interface Listener {
        public static final Method METHOD =
                ReflectTools.findMethod(Listener.class,
                        "onClearToFind", Event.class);
        public void onClearToFind(Event event);
    }

    public static interface Notifier {
        void addOnClearToFindListener(Listener listener);
        void removeOnClearToFindListener(Listener listener);
    }
}
