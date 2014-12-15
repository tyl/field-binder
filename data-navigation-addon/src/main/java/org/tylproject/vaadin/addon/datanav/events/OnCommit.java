package org.tylproject.vaadin.addon.datanav.events;

import com.vaadin.util.ReflectTools;
import org.tylproject.vaadin.addon.datanav.CrudNavigation;

import java.lang.reflect.Method;

/**
 * Created by evacchi on 19/11/14.
 */
public class OnCommit {
    public static class Event extends CrudEvent {
        public Event(CrudNavigation source) {
            super(source);
        }
    }

    public static interface Listener {
        public static final Method METHOD =
                ReflectTools.findMethod(Listener.class,
                        "onCommit", Event.class);
        public void onCommit(Event event);
    }

    public static interface Notifier {
        void addOnCommitListener(Listener listener);
        void removeOnCommitListener(Listener listener);
    }
}
