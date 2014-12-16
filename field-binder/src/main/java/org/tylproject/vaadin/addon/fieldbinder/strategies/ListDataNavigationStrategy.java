package org.tylproject.vaadin.addon.fieldbinder.strategies;

import org.tylproject.vaadin.addon.MongoContainer;
import org.tylproject.vaadin.addon.datanav.DataNavigation;
import org.tylproject.vaadin.addon.datanav.events.ItemCreate;
import org.tylproject.vaadin.addon.datanav.events.OnCommit;
import org.tylproject.vaadin.addon.fieldbinder.FieldBinder;

/**
 * Created by evacchi on 15/12/14.
 */
public class ListDataNavigationStrategy<T> extends AbstractDataNavigationStrategy<T> {
    public ListDataNavigationStrategy(FieldBinder<T> fieldBinder) {
        super(fieldBinder);
    }

    @Override
    public void itemCreate(ItemCreate.Event event) {
        super.itemCreate(event);
        T bean = createBean();
        super.fieldBinder.setBeanDataSource(bean);
    }


}