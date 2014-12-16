package org.tylproject.vaadin.addon.datanav.events;

import com.vaadin.util.ReflectTools;
import org.tylproject.vaadin.addon.datanav.DataNavigation;

import java.lang.reflect.Method;
import java.util.EventObject;

/**
 * Created by evacchi on 19/11/14.
 */
public class FindEnabled {

    public static class Event extends EventObject {
        private final boolean findEnabled;

        public Event(DataNavigation source, boolean findEnabled) {
            super(source);
            this.findEnabled = findEnabled;
        }

        public boolean isFindEnabled() {
            return findEnabled;
        }

        @Override
        public DataNavigation getSource() {
            return (DataNavigation) super.getSource();
        }

    }
    public static interface Listener {
        public static final Method METHOD =
                ReflectTools.findMethod(Listener.class, "findEnabled", Event.class);
        public void findEnabled(Event event);
    }

    public static interface Notifier {
        void addFindEnabledListener(Listener listener);
        void removeFindEnabledListener(Listener listener);
    }
}