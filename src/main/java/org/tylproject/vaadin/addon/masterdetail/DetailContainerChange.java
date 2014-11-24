package org.tylproject.vaadin.addon.masterdetail;

import com.vaadin.data.Container;
import com.vaadin.util.ReflectTools;

import java.lang.reflect.Method;
import java.util.EventObject;

/**
 * Created by evacchi on 24/11/14.
 */
public class DetailContainerChange {
    public static class Event extends EventObject {
        private final Container oldContainer;
        private final Container newContainer;

        public Event(MasterDetail source, Container oldContainer, Container newContainer) {
            super(source);
            this.oldContainer = oldContainer;
            this.newContainer = newContainer;
        }

        public Container getOldContainer() {
            return oldContainer;
        }

        public Container getNewContainer() {
            return newContainer;
        }
    }


    public static interface Listener {
        public static final Method method = ReflectTools.findMethod(Listener.class,
                "detailContainerChange", Event.class);

        public void detailContainerChange(Event event);
    }

    public static interface Notifier {
        public void addDetailContainerChangeListener(Listener listener);
        public void removeDetailContainerChangeListener(Listener listener);
    }
}
