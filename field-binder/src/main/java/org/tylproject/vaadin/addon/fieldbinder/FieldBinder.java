package org.tylproject.vaadin.addon.fieldbinder;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItem;
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
 * An enhanced version of Vaadin's standard {@link com.vaadin.data.fieldgroup.FieldGroup}
 *
 * The FieldBinder mimicks the FieldGroup interface, but it supports more methods, and
 * it behaves in a slightly different way.
 *
 * It supports binding and unbinding elements, while still keeping track of which
 * elements are managed by the group. It also supports binding {@link com.vaadin.ui.Table} instances
 * to List values as their datasource, using {@link org.tylproject.vaadin.addon.fieldbinder.ListTable}
 * as their implementation.
 *
 * Example:
 *
 * <code>
 *     public class Person {
 *         String name;
 *         List<Address> addressList;
 *         // getters, setters
 *     }
 *     public class Address {
 *         String street, zipCode, city, state;
 *     }
 *     // ...
 *     FieldBinder<Person> fieldBinder = new FieldBinder<Person>(Person.class);
 *     Field<String> name = fieldBinder.build("name");
 *     ListTable<Address> fieldBinder.build("addressList");
 *     fieldBinder.setItemDataSource(...);
 *     fieldBinder.bindAll();
 * </code>
 *
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

    public void setItemDataSource(BeanItem<T> itemDataSource) {
        super.setItemDataSource(itemDataSource);
    }

    public void setBeanDataSource(T dataSource) {
        this.setItemDataSource(new BeanItem<T>(dataSource));
    }

    @Override
    public BeanItem<T> getItemDataSource() {
        return (BeanItem<T>) super.getItemDataSource();
    }

    public T getBeanDataSource() {
        return this.getItemDataSource().getBean();
    }

    /**
     * Generates a ListTable.
     *
     * This method automatically connects the generated ListTable to this FieldBinder.
     * The navigation of the FieldBinder and the navigation of the ListTable will act in
     * coordination, enabling and disabling themselves when it is required.
     *
     * When the DataNavigation of the FieldBinder enters editing mode
     * ({@link org.tylproject.vaadin.addon.datanav.DataNavigation#enterEditingMode()})
     * then the navigation on the ListTable is disabled; when the navigation of the
     * ListTable enters editing mode, then the navigation of the FieldBinder is disabled.
     *
     *
     * @param containedBeanClass
     * @param propertyId
     * @param <U>
     * @return
     */
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

    /**
     * Generates a default button bar implementation binding to this FieldBinder's built-in
     * {@link org.tylproject.vaadin.addon.datanav.DataNavigation} instance
     *
     */
    public ButtonBar buildDefaultButtonBar(Container.Ordered container) {
        navigation.setContainer(container);
        return new ButtonBar(navigation);
    }

    /**
     * builds all the fields for all the properties of the Class {@link #getType()}
     *
     */
    public Collection<Field<?>> buildAll() {
        for (DynaProperty prop : dynaClass.getDynaProperties()) {
            build(prop.getName());
        }
        bindAll();
        return getFields();
    }

    /**
     * shorthand for invoking {@link #build(Object)} multiple times
     *
     * <code>build(propertyId1, propertyId2, propertyId3, ...)</code>
     */
    public Collection<Field<?>> buildAll(Object propertyId, Object... propertyIds) {
        build(propertyId);
        for (Object pid: propertyIds) {
            build(pid);
        }
        bindAll();
        return getFields();
    }

    /**
     * shorthand for invoking {@link #build(Object)} multiple times,
     * for the given collection of propertyIds
     */
    public Collection<Field<?>> buildAll(Collection<?> propertyIds) {
        for (Object pid: propertyIds) {
            build(pid);
        }
        bindAll();
        return getFields();
    }

    public Class<T> getType() {
        return beanClass;
    }

    @Override
    protected Class<?> getPropertyType(Object propertyId) {
        return dynaClass.getDynaProperty(propertyId.toString()).getType();
    }

    public BasicDataNavigation getNavigation() {
        return navigation;
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
