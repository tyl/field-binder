package org.tylproject.vaadin.addon.datanav.events;

import com.vaadin.util.ReflectTools;
import org.tylproject.vaadin.addon.datanav.CrudNavigation;

import java.lang.reflect.Method;

/**
 * Created by evacchi on 19/11/14.
 */
public class BeforeFind {
    public static class Event extends CrudEvent {
        public Event(CrudNavigation source) {
            super(source);
        }
    }

    public static interface Listener {
        public static final Method METHOD =
                ReflectTools.findMethod(Listener.class,
                        "beforeFind", Event.class);
        public void beforeFind(Event event);
    }

    public static interface Notifier {
        void addBeforeFindListener(Listener listener);
        void removeBeforeFindListener(Listener listener);
    }
}
