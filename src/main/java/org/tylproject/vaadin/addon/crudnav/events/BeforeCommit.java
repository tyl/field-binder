package org.tylproject.vaadin.addon.crudnav.events;

import com.vaadin.util.ReflectTools;
import org.tylproject.vaadin.addon.crudnav.CrudNavigation;

import java.lang.reflect.Method;
import java.util.EventObject;

/**
 * Created by evacchi on 19/11/14.
 */
public class BeforeCommit {
    public static class Event extends CrudEvent {
        public Event(CrudNavigation source) {
            super(source);
        }
    }

    public static interface Listener {
        public static final Method METHOD =
                ReflectTools.findMethod(Listener.class,
                        "beforeCommit", Event.class);
        public void beforeCommit(Event event);
    }

    public static interface Notifier {
        void addBeforeCommitListener(Listener listener);
        void removeBeforeCommitListener(Listener listener);
    }
}
