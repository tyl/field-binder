package org.tylproject.vaadin.addon.datanav.events;

import com.vaadin.util.ReflectTools;
import org.tylproject.vaadin.addon.datanav.DataNavigation;

import java.lang.reflect.Method;

/**
 * Created by evacchi on 19/11/14.
 */
public class BeforeCommit {
    public static class Event extends CrudEvent {
        public Event(DataNavigation source) {
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
