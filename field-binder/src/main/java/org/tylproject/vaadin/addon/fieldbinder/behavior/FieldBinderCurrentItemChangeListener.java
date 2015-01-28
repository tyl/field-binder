package org.tylproject.vaadin.addon.fieldbinder.behavior;

import org.tylproject.vaadin.addon.datanav.events.CurrentItemChange;
import org.tylproject.vaadin.addon.fieldbinder.FieldBinder;

/**
 * Created by evacchi on 28/01/15.
 */
public class FieldBinderCurrentItemChangeListener<T> implements CurrentItemChange.Listener {
    private final FieldBinder<T> fieldBinder;

    public FieldBinderCurrentItemChangeListener(FieldBinder<T> fieldBinder) {
        this.fieldBinder = fieldBinder;
    }

    @Override
    public void currentItemChange(CurrentItemChange.Event event) {
        fieldBinder.setItemDataSource(event.getNewItem());
    }
}
