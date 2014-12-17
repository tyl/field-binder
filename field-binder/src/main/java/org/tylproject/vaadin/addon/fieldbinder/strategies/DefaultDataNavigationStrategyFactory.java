package org.tylproject.vaadin.addon.fieldbinder.strategies;

import com.vaadin.data.Container;
import org.tylproject.vaadin.addon.fieldbinder.FieldBinder;

/**
 * Created by evacchi on 15/12/14.
 */
public class DefaultDataNavigationStrategyFactory<U> implements DataNavigationStrategyFactory<U> {

    final FieldBinder<U> fieldBinder;

    public DefaultDataNavigationStrategyFactory(FieldBinder<U> fieldBinder) {
        this.fieldBinder = fieldBinder;
    }

    @Override
    public <T extends DataNavigationStrategy> T forContainer(Container container) {
        if (container != null) {

            switch (container.getClass().getCanonicalName()) {
                case "org.vaadin.maddon.ListContainer":
                case "org.vaadin.maddon.FilterableListContainer":
                    return (T) new ListDataNavigationStrategy<U>(fieldBinder);
                case "org.tylproject.vaadin.addon.MongoContainer":
                    return (T) new MongoDataNavigationStrategy<U>(fieldBinder);
            }

        }

        throw new UnsupportedOperationException("Unknown container type: "+ container.getClass().getCanonicalName());
    }
}
