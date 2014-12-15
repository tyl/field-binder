package org.tylproject.vaadin.addon.datanav;

import org.tylproject.vaadin.addon.fieldbinder.FieldBinder;
import org.tylproject.vaadin.addon.fieldbinder.strategies.DataNavigationStrategy;
import org.tylproject.vaadin.addon.fieldbinder.strategies.ListDataNavigationStrategy;
import org.tylproject.vaadin.addon.fieldbinder.strategies.MongoDataNavigationStrategy;

/**
 * Created by evacchi on 15/12/14.
 */
public class DefaultDataNavigationStrategyFactory<U> implements DataNavigationStrategyFactory<U> {

    final FieldBinder<U> fieldBinder;

    public DefaultDataNavigationStrategyFactory(FieldBinder<U> fieldBinder) {
        this.fieldBinder = fieldBinder;
    }

    @Override
    public <T extends DataNavigationStrategy> T forContainer(Class<?> containerType) {
        switch (containerType.getCanonicalName()) {
            case "org.vaadin.maddon.ListContainer":
            case "org.vaadin.maddon.FilterableListContainer":
                return (T) new ListDataNavigationStrategy<U>(fieldBinder);
            case "org.tylproject.vaadin.addon.MongoContainer" :
                return (T) new MongoDataNavigationStrategy<U>(fieldBinder);
        }

        throw new UnsupportedOperationException("Unknown container type: "+containerType.getCanonicalName());
    }
}
