package org.tylproject.vaadin.addon.crudnav.events;

import com.vaadin.ui.Component;
import com.vaadin.util.ReflectTools;
import org.tylproject.vaadin.addon.crudnav.CrudNavigation;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.EventObject;

/**
 * Created by evacchi on 19/11/14.
 */
public class CurrentItemChange {

    public static class Event extends ItemNavigationEvent {
        public Event(CrudNavigation source, Object newItemId, Object oldItemId) {
            super(source, newItemId, oldItemId);
        }
    }

    public static interface Listener {
        public static final Method METHOD =
                ReflectTools.findMethod(Listener.class, "currentItemChange", Event.class);
        public void currentItemChange(Event event);
    }

    public static interface Notifier {
        void addCurrentItemChangeListener(Listener listener);
        void removeCurrentItemChangeListener(Listener listener);
    }
}
