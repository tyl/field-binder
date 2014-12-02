package org.tylproject.vaadin.addon.masterdetail;

import org.tylproject.vaadin.addon.crudnav.CrudNavigation;
import org.tylproject.vaadin.addon.crudnav.events.CurrentItemChange;
import org.tylproject.vaadin.addon.fieldbinder.FieldBinder;

/**
 * Created by evacchi on 26/11/14.
 */
public class NavigableFieldBinder<T>
        implements Navigable<FieldBinder<T>>, CurrentItemChange.Listener{
    private final FieldBinder<T> fieldBinder;
    private final CrudNavigation navigation;

    public NavigableFieldBinder(FieldBinder<T> fieldBinder, CrudNavigation navigation) {
        this.fieldBinder = fieldBinder;
        this.navigation = navigation;
        navigation.addCurrentItemChangeListener(this);
    }

    @Override
    public FieldBinder<T> getNavigable() {
        return this.getFieldBinder();
    }
    @Override
    public CrudNavigation getNavigation() {
        return this.navigation;
    }
    public FieldBinder<T> getFieldBinder() {
        return this.fieldBinder;
    }
    @Override
    public void currentItemChange(CurrentItemChange.Event event) {
        fieldBinder.setItemDataSource(event.getNewItem());
    }

}
