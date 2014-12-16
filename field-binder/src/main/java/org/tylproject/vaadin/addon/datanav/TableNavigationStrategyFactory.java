package org.tylproject.vaadin.addon.datanav;

import com.vaadin.data.Container;
import org.tylproject.vaadin.addon.fieldbinder.ListTable;
import org.tylproject.vaadin.addon.fieldbinder.strategies.DataNavigationStrategy;
import org.tylproject.vaadin.addon.fieldbinder.strategies.DefaultTableStrategy;

/**
 * Created by evacchi on 15/12/14.
 */
public class TableNavigationStrategyFactory<U> implements DataNavigationStrategyFactory<U> {

    final ListTable<U> tableWrapper;

    public TableNavigationStrategyFactory(ListTable<U> tableWrapper) {
        this.tableWrapper = tableWrapper;
    }

    @Override
    public <T extends DataNavigationStrategy> T forContainer(Container container) {
        return (T) new DefaultTableStrategy<U>(tableWrapper.getListType(), tableWrapper.getTable());
    }
}
