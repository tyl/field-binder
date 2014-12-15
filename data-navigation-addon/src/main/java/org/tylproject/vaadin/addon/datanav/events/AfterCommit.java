package org.tylproject.vaadin.addon.datanav.events;

import com.vaadin.util.ReflectTools;
import org.tylproject.vaadin.addon.datanav.CrudNavigation;

import java.lang.reflect.Method;

/**
 * Created by evacchi on 19/11/14.
 */
public class AfterCommit {
    public static class Event extends CrudEvent {
        public Event(CrudNavigation source) {
            super(source);
        }
    }

    public static interface Listener {
        public static final Method METHOD =
                ReflectTools.findMethod(Listener.class,
                        "afterCommit", Event.class);
        public void afterCommit(Event event);
    }

    public static interface Notifier {
        void addAfterCommitListener(Listener listener);
        void removeAfterCommitListener(Listener listener);
    }
}
