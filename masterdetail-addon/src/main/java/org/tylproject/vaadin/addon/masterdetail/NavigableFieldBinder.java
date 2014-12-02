package org.tylproject.vaadin.addon.masterdetail;

import com.vaadin.data.fieldgroup.FieldGroup;
import org.springframework.context.annotation.Bean;
import org.tylproject.vaadin.addon.crudnav.CrudNavigation;
import org.tylproject.vaadin.addon.crudnav.events.CurrentItemChange;
import org.tylproject.vaadin.addon.fieldbinder.BeanFieldBinder;

/**
 * Created by evacchi on 26/11/14.
 */
public class NavigableFieldBinder<T>
        implements Navigable<BeanFieldBinder<T>>, CurrentItemChange.Listener{
    private final BeanFieldBinder fieldGroup;
    private final CrudNavigation navigation;

    public NavigableFieldBinder(BeanFieldBinder<T> fieldGroup, CrudNavigation navigation) {
        this.fieldGroup = fieldGroup;
        this.navigation = navigation;
        navigation.addCurrentItemChangeListener(this);
    }

    @Override
    public BeanFieldBinder<T> getNavigable() {
        return this.getFieldGroup();
    }
    @Override
    public CrudNavigation getNavigation() {
        return this.navigation;
    }
    public BeanFieldBinder<T> getFieldGroup() {
        return this.fieldGroup;
    }
    @Override
    public void currentItemChange(CurrentItemChange.Event event) {
        fieldGroup.setItemDataSource(event.getNewItem());
    }

}
