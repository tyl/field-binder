package org.tylproject.vaadin.addon.masterdetail;

import com.vaadin.data.fieldgroup.FieldGroup;
import org.tylproject.vaadin.addon.crudnav.CrudNavigation;
import org.tylproject.vaadin.addon.crudnav.events.CurrentItemChange;

/**
 * Created by evacchi on 26/11/14.
 */
public class NavigableFieldGroup<T extends FieldGroup>
        implements Navigable<T>, CurrentItemChange.Listener{
    private final T fieldGroup;
    private final CrudNavigation navigation;

    public NavigableFieldGroup(T fieldGroup, CrudNavigation navigation) {
        this.fieldGroup = fieldGroup;
        this.navigation = navigation;
        navigation.addCurrentItemChangeListener(this);
    }

    @Override
    public T getNavigable() {
        return this.getFieldGroup();
    }
    @Override
    public CrudNavigation getNavigation() {
        return this.navigation;
    }
    public T getFieldGroup() {
        return this.fieldGroup;
    }
    @Override
    public void currentItemChange(CurrentItemChange.Event event) {
        fieldGroup.setItemDataSource(event.getNewItem());
    }

}
