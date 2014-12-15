package org.tylproject.vaadin.addon.fieldbinder.strategies;

import org.tylproject.vaadin.addon.MongoContainer;
import org.tylproject.vaadin.addon.datanav.DataNavigation;
import org.tylproject.vaadin.addon.datanav.events.ItemCreate;
import org.tylproject.vaadin.addon.datanav.events.OnCommit;
import org.tylproject.vaadin.addon.fieldbinder.FieldBinder;

/**
 * Created by evacchi on 27/11/14.
 */
public class MongoDataNavigationStrategy<M> extends AbstractDataNavigationStrategy<M> {

    public MongoDataNavigationStrategy(FieldBinder<M> fieldBinder) {
        super(fieldBinder);
    }

    @Override
    public void onCommit(OnCommit.Event event) {
        super.onCommit(event);
        DataNavigation nav = event.getSource();
        M bean = fieldBinder.getBeanDataSource();
        MongoContainer<M> container = (MongoContainer<M>) nav.getContainer();
        Object id = container.addEntity(bean);
        nav.setCurrentItemId(id);
    }
}
