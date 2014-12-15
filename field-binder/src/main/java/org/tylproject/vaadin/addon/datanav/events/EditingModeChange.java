package org.tylproject.vaadin.addon.datanav.events;

import com.vaadin.util.ReflectTools;
import org.tylproject.vaadin.addon.datanav.DataNavigation;

import java.lang.reflect.Method;
import java.util.EventObject;

/**
 * Created by evacchi on 19/11/14.
 */
public class EditingModeChange {

    public enum Status {
        Entering, Leaving
    }

    public static class Event extends EventObject {
        private final Status status;

        public Event(DataNavigation source, Status status) {
            super(source);
            this.status = status;
        }

        public boolean isEnteringEditingMode() {
            return status == Status.Entering;
        }
        public boolean isLeavingEditingMode() { return status == Status.Leaving; }

        @Override
        public DataNavigation getSource() {
            return (DataNavigation) super.getSource();
        }

    }
    public static interface Listener {
        public static final Method METHOD =
                ReflectTools.findMethod(Listener.class, "editingModeChange", Event.class);
        public void editingModeChange(Event event);
    }

    public static interface Notifier {
        void addEditingModeChangeListener(Listener listener);
        void removeEditingModeChangeListener(Listener listener);
    }
}
