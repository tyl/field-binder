package org.tylproject.vaadin.addon.datanav.events;

import com.vaadin.util.ReflectTools;
import org.tylproject.vaadin.addon.datanav.DataNavigation;

import java.lang.reflect.Method;
import java.util.EventObject;

/**
 * Created by evacchi on 19/11/14.
 */
public class NavigationEnabled {

    public static class Event extends EventObject {
        private final boolean navigationEnabled;

        public Event(DataNavigation source, boolean navigationEnabled) {
            super(source);
            this.navigationEnabled = navigationEnabled;
        }

        public boolean isNavigationEnabled() {
            return navigationEnabled;
        }

        @Override
        public DataNavigation getSource() {
            return (DataNavigation) super.getSource();
        }

    }
    public static interface Listener {
        public static final Method METHOD =
                ReflectTools.findMethod(Listener.class, "navigationEnabled", Event.class);
        public void navigationEnabled(Event event);
    }

    public static interface Notifier {
        void addNavigationEnabledListener(Listener listener);
        void removeNavigationEnabledListener(Listener listener);
    }
}
