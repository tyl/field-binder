package org.tylproject.vaadin.addon.fieldbinder.behavior;

import com.vaadin.data.Container;
import org.tylproject.vaadin.addon.fieldbinder.FieldBinder;

/**
 * Created by evacchi on 15/12/14.
 */
public class FieldBinderBehaviorFactory<U> implements BehaviorFactory<U> {

    final FieldBinder<U> fieldBinder;

    public FieldBinderBehaviorFactory(FieldBinder<U> fieldBinder) {
        this.fieldBinder = fieldBinder;
    }

    @Override
    public <T extends Behavior> T forContainer(Container container) {
        if (container != null) {

            switch (container.getClass().getCanonicalName()) {
                case "org.vaadin.maddon.ListContainer":
                case "org.vaadin.maddon.FilterableListContainer":
                    return (T) new ListContainerBehavior<U>(fieldBinder);
                case "org.tylproject.vaadin.addon.MongoContainer":
                    return (T) new MongoBehavior<U>(fieldBinder);
            }

        }

        throw new UnsupportedOperationException("Unknown container type: "+ container.getClass().getCanonicalName());
    }
}
