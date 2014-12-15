package org.tylproject.vaadin.addon.datanav.events;

import com.vaadin.util.ReflectTools;
import org.tylproject.vaadin.addon.datanav.CrudNavigation;

import java.lang.reflect.Method;

public class ItemEdit {
    public static class Event extends CrudEvent {
        public Event(CrudNavigation source) {
            super(source);
        }
    }

    public static interface Listener {
        public static final Method METHOD =
                ReflectTools.findMethod(Listener.class,
                        "itemEdit", Event.class);
        public void itemEdit(Event event);
    }

    public static interface Notifier {
        void addItemEditListener(Listener listener);
        void removeItemEditListener(Listener listener);
    }
}
