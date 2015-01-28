package org.tylproject.vaadin.addon.fieldbinder.behavior.legacy;

import com.vaadin.addon.jpacontainer.JPAContainer;
import org.tylproject.vaadin.addon.datanav.DataNavigation;
import org.tylproject.vaadin.addon.datanav.events.OnCommit;
import org.tylproject.vaadin.addon.fieldbinder.FieldBinder;

public class JPAContainerBehavior<T> extends AbstractBehavior<T> {
    public JPAContainerBehavior(FieldBinder<T> fieldBinder) {
        super(fieldBinder);
    }

    @Override
    public void onCommit(OnCommit.Event event) {
        super.onCommit(event);
        DataNavigation nav = event.getSource();
        T bean = fieldBinder.getBeanDataSource();
        JPAContainer<T> container = (JPAContainer<T>) nav.getContainer();
        Object id = container.addEntity(bean);
        nav.setCurrentItemId(id);
    }

}
