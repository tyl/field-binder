package org.tylproject.vaadin.addon.fieldbinder;

import com.vaadin.data.Container;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.ui.Field;
import org.apache.commons.beanutils.DynaProperty;
import org.apache.commons.beanutils.WrapDynaClass;
import org.tylproject.vaadin.addon.datanav.BasicDataNavigation;
import org.tylproject.vaadin.addon.datanav.ButtonBar;
import org.tylproject.vaadin.addon.datanav.DataNavigation;
import org.tylproject.vaadin.addon.datanav.events.EditingModeChange;
import org.tylproject.vaadin.addon.fieldbinder.strategies.DefaultNavigationStrategy;

import java.util.*;

/**
 * Created by evacchi on 02/12/14.
 */
public class FieldBinder<T> extends AbstractFieldBinder<FieldGroup> {

    private final WrapDynaClass dynaClass ;
    private final Class<T> beanClass;
    private final BasicDataNavigation navigation;

    public FieldBinder(Class<T> beanClass) {
        super(new FieldGroup());
        this.beanClass = beanClass;
        this.dynaClass = WrapDynaClass.createDynaClass(beanClass);


        DefaultNavigationStrategy<T> defaultNavigationStrategy = new DefaultNavigationStrategy<T>(beanClass, this);

        BasicDataNavigation nav = new BasicDataNavigation()
                .withCrudListenersFrom(defaultNavigationStrategy)
                .withFindListenersFrom(defaultNavigationStrategy);

        nav.addCurrentItemChangeListener(defaultNavigationStrategy);

        this.navigation = nav;


    }

    public <U> ListTable<U> buildListOf(Class<U> containedBeanClass, Object propertyId) {
        final Class<?> dataType = getPropertyType(propertyId);
        final ListTable<U> field = getFieldFactory().createDetailField(dataType, containedBeanClass);

        bind(field, propertyId);

        field.getNavigation().addEditingModeChangeListener(new EditingModeSwitcher(navigation));

        this.navigation.addEditingModeChangeListener(new EditingModeSwitcher(field.getNavigation()));


        return field;
    }

    /**
     * focus first component
     */
    public void focus() {
        if (getFields().isEmpty()) return;
        getFields().iterator().next().focus();
    }

    public ButtonBar buildDefaultButtonBar(Container.Ordered container) {
        navigation.setContainer(container);
        return new ButtonBar(navigation);
    }

    public Collection<Field<?>> buildAll() {
        for (DynaProperty prop : dynaClass.getDynaProperties()) {
            build(prop.getName());
        }
        bindAll();
        return getFields();
    }

    public Collection<Field<?>> buildAll(Object propertyId, Object... propertyIds) {
        build(propertyId);
        for (Object pid: propertyIds) {
            build(pid);
        }
        bindAll();
        return getFields();
    }

    public Collection<Field<?>> buildAll(Collection<?> propertyIds) {
        for (Object pid: propertyIds) {
            build(pid);
        }
        bindAll();
        return getFields();
    }

    @Override
    protected Class<?> getPropertyType(Object propertyId) {
        return dynaClass.getDynaProperty(propertyId.toString()).getType();
    }

    static class EditingModeSwitcher implements EditingModeChange.Listener {
        final DataNavigation otherNavigation;
        EditingModeSwitcher(DataNavigation other) {
            this.otherNavigation = other;
        }

        @Override
        public void editingModeChange(EditingModeChange.Event event) {
            if (event.isEnteringEditingMode()) {
                otherNavigation.disableNavigation();
                otherNavigation.disableCrud();
                otherNavigation.disableFind();
            } else {
                otherNavigation.enableNavigation();
                otherNavigation.enableCrud();
                otherNavigation.enableFind();
            }
        }
    }



}
