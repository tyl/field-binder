package org.tylproject.vaadin.addon.fieldbinder.behavior;

import com.vaadin.data.Container;
import com.vaadin.ui.Table;

/**
 * Created by evacchi on 15/12/14.
 */
public class TableBehaviorFactory<U> implements BehaviorFactory<U> {

    final Class<U> beanClass;
    final Table table;

    public TableBehaviorFactory(Class<U> beanClass, Table table) {
        this.beanClass = beanClass;
        this.table = table;
    }


    @Override
    public <T extends Behavior> T forContainer(Container container) {
        if (container != null) {

            switch (container.getClass().getCanonicalName()) {
                case "org.vaadin.maddon.ListContainer":
                case "org.vaadin.maddon.FilterableListContainer":
                    return (T) new ListContainerTableBehavior<U>(beanClass, table);
                case "org.tylproject.vaadin.addon.BufferedMongoContainer":
                    return (T) new BufferedMongoContainerTableBehavior<U>(beanClass, table);
            }

        }

        throw new UnsupportedOperationException("Unknown container type: "+ container.getClass().getCanonicalName());
    }

}
