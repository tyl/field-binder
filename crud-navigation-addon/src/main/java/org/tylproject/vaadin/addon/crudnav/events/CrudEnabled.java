package org.tylproject.vaadin.addon.crudnav.events;

import com.vaadin.util.ReflectTools;
import org.tylproject.vaadin.addon.crudnav.CrudNavigation;

import java.lang.reflect.Method;
import java.util.EventObject;

/**
 * Created by evacchi on 19/11/14.
 */
public class CrudEnabled {

    public static class Event extends EventObject {
        private final boolean crudEnabled;

        public Event(CrudNavigation source, boolean crudEnabled) {
            super(source);
            this.crudEnabled = crudEnabled;
        }

        public boolean isCrudEnabled() {
            return crudEnabled;
        }

        @Override
        public CrudNavigation getSource() {
            return (CrudNavigation) super.getSource();
        }

    }
    public static interface Listener {
        public static final Method METHOD =
                ReflectTools.findMethod(Listener.class, "crudEnabled", Event.class);
        public void crudEnabled(Event event);
    }

    public static interface Notifier {
        void addCrudEnabledListener(Listener listener);
        void removeCrudEnabledListener(Listener listener);
    }
}
