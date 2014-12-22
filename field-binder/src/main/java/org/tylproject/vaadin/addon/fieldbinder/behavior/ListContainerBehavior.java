package org.tylproject.vaadin.addon.fieldbinder.behavior;

import org.tylproject.vaadin.addon.datanav.events.ItemCreate;
import org.tylproject.vaadin.addon.fieldbinder.FieldBinder;
import org.vaadin.maddon.ListContainer;

/**
 * Created by evacchi on 15/12/14.
 */
public class ListContainerBehavior<T> extends AbstractBehavior<T> {
    public ListContainerBehavior(FieldBinder<T> fieldBinder) {
        super(fieldBinder);
    }

    @Override
    public void itemCreate(ItemCreate.Event event) {
        super.itemCreate(event);
        T bean = createBean();
        ListContainer<T> container = ((ListContainer<T>) event.getSource().getContainer());
        container.addItem(bean);
        event.getSource().setCurrentItemId(bean);
    }
}
