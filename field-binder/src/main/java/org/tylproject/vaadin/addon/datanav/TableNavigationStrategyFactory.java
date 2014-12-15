package org.tylproject.vaadin.addon.datanav;

import com.vaadin.ui.Table;
import org.tylproject.vaadin.addon.fieldbinder.FieldBinder;
import org.tylproject.vaadin.addon.fieldbinder.ListTable;
import org.tylproject.vaadin.addon.fieldbinder.strategies.DataNavigationStrategy;
import org.tylproject.vaadin.addon.fieldbinder.strategies.DefaultTableStrategy;
import org.tylproject.vaadin.addon.fieldbinder.strategies.ListDataNavigationStrategy;
import org.tylproject.vaadin.addon.fieldbinder.strategies.MongoDataNavigationStrategy;

/**
 * Created by evacchi on 15/12/14.
 */
public class TableNavigationStrategyFactory<U> implements DataNavigationStrategyFactory<U> {

    final ListTable<U> table;

    public TableNavigationStrategyFactory(ListTable<U> table) {
        this.table = table;
    }

    @Override
    public <T extends DataNavigationStrategy> T forContainer(Class<?> containerType) {
        switch (containerType.getCanonicalName()) {
            case "org.vaadin.maddon.ListContainer":
            case "org.vaadin.maddon.FilterableListContainer":
                return (T) new DefaultTableStrategy<U>(table.getListType(),table.getTable());
        }

        throw new UnsupportedOperationException("Unknown container type: "+containerType.getCanonicalName());
    }
}
