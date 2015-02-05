/*
 * Copyright (c) 2015 - Tyl Consulting s.a.s.
 *
 *   Authors: Edoardo Vacchi
 *   Contributors: Marco Pancotti, Daniele Zonca
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.tylproject.vaadin.addon.fieldbinder;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.DefaultFieldFactory;
import com.vaadin.ui.Field;
import com.vaadin.ui.TextField;
import org.apache.commons.beanutils.DynaProperty;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.beanutils.WrapDynaClass;
import org.tylproject.vaadin.addon.datanav.*;
import org.tylproject.vaadin.addon.datanav.events.CurrentItemChange;
import org.tylproject.vaadin.addon.datanav.events.EditingModeChange;
import org.tylproject.vaadin.addon.fieldbinder.behavior.DefaultBehaviorFactory;
import org.tylproject.vaadin.addon.fields.zoom.*;

import java.beans.PropertyDescriptor;
import java.util.*;

/**
 * An enhanced version of Vaadin's standard {@link com.vaadin.data.fieldgroup.FieldGroup}
 *
 * The FieldBinder mimics the FieldGroup interface, but it supports more methods, and
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
    private boolean gridSupportEnabled = false;

    public FieldBinder(Class<T> beanClass) {
        super(new FieldGroup());
        this.beanClass = beanClass;
        this.dynaClass = WrapDynaClass.createDynaClass(beanClass);
        this.navigation = null;
    }

    /**
     * Creates a FieldBinder that will use the given container as the backing
     * for the values of its fields.
     *
     * {@link #getNavigation()} returns a controller on top of that container
     *
     *
     * @param beanClass
     * @param container
     */
    public FieldBinder(Class<T> beanClass, Container.Ordered container) {
        super(new FieldGroup());
        this.beanClass = beanClass;
        this.dynaClass = WrapDynaClass.createDynaClass(beanClass);

        BasicDataNavigation nav = new BasicDataNavigation(container);
        nav.setBehaviorFactory(new DefaultBehaviorFactory(this));

        this.navigation = nav;

    }

    public FieldBinder<T> withGridSupport() {
        this.gridSupportEnabled = true;
        return this;
    }

    public void setItemDataSource(BeanItem<T> itemDataSource) {
        super.setItemDataSource(itemDataSource);
    }

    public void setBeanDataSource(T dataSource) {
        this.setItemDataSource(new BeanItem<T>(dataSource));
    }

    @Override
    public Item getItemDataSource() {
        return super.getItemDataSource();
    }

    public T getBeanDataSource() {
        Item item = getItemDataSource();
        if (item instanceof BeanItem) {
            return ((BeanItem<T>)this.getItemDataSource()).getBean();
        } else {
            BeanItem<T> beanItem = new BeanItem<T>(createBean());
            for (Object propId: item.getItemPropertyIds()) {
                Object value = item.getItemProperty(propId).getValue();
                beanItem.getItemProperty(propId).setValue(value);
            }
            return beanItem.getBean();
        }
    }


    protected T createBean() {
        try {
            return beanClass.newInstance();
        } catch (Exception ex) {
            throw new UnsupportedOperationException(ex);
        }
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
    public <U,C extends Collection<U>> CollectionTable<U,C> buildCollectionOf(Class<U> containedBeanClass, Object propertyId) {
        final Class<C> dataType = (Class<C>) getPropertyType(propertyId);
        final CollectionTable<U,C> collectionTable = getFieldFactory().createDetailField(dataType, containedBeanClass);

        bind(collectionTable, propertyId);

        this.getNavigation().addEditingModeChangeListener(
            new EditingModeSwitcher(collectionTable.getNavigation()));

        collectionTable.getNavigation().disableCrud();


        return collectionTable;
    }

    public <U> ListTable<U> buildListOf(Class<U> containedBeanClass, Object propertyId) {
        return (ListTable<U>) this.<U,List<U>>buildCollectionOf(containedBeanClass, propertyId);
    }


    public TextZoomField buildZoomField(Object bindingPropertyId, Object containerPropertyId, Container.Indexed zoomCollection) {

        String caption = DefaultFieldFactory
                .createCaptionByPropertyId(bindingPropertyId);

//        TextField textField = super.build(null, bindingPropertyId, TextField.class);
        TextZoomField field = new TextZoomField();
        field.setCaption(caption);
        field.withZoomDialog(makeDefaultZoomDialog(containerPropertyId, zoomCollection));

        bind(field, bindingPropertyId);

        return field;
    }

    public TextZoomField buildDrillDownField(Object propertyId,  Object containerPropertyId, Container.Indexed zoomCollection) {
        return this.buildZoomField(propertyId, containerPropertyId, zoomCollection).drillDownOnly();
    }

    protected ZoomDialog makeDefaultZoomDialog(Object propertyId, Container.Indexed zoomCollection) {
        if (gridSupportEnabled) {
            return new GridZoomDialog(propertyId, zoomCollection);
        } else {
            return new TableZoomDialog(propertyId, zoomCollection);
        }
    }



//    public <U> ZoomField<U> buildZoomField(Object propertyId, BeanTable<?> beanTable) {
//        return this.<U>buildZoomField(propertyId).withZoomOnTable(beanTable);
//    }
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
        getNavigation().setContainer(container);
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


    /**
     * Retrieves the type of the property with the given name of the given
     * Class.
     *
     * Supports nested properties following bean naming convention.
     * e.g., "foo.bar.name"
     *
     * @see PropertyUtils#getPropertyDescriptors(Class)
     * @return Null if no property exists.
     */
    public Class<?> getPropertyType(Object propertyId) {
        if (propertyId == null)
            throw new IllegalArgumentException("PropertyName must not be null.");

        final String propertyName = propertyId.toString();

        final String[] path = propertyName.split("\\.");


        Class<?> propClass = beanClass;

        for (int i = 0; i < path.length; i++) {
            String propertyFragment = path[i];
            final PropertyDescriptor[] propDescs =
                PropertyUtils.getPropertyDescriptors(propClass);

            for (final PropertyDescriptor propDesc : propDescs)
                if (propDesc.getName().equals(propertyFragment)) {
                    propClass = propDesc.getPropertyType();
                    if (i == path.length - 1)
                        return propClass;
                }
        }

        return null;
    }



//    @Override
//    protected Class<?> getPropertyType(Object propertyId) {
//        DynaProperty dynaProperty = dynaClass.getDynaProperty(propertyId.toString());
//        if (dynaProperty == null) {
//            Container container = getNavigation().getContainer();
//            if (container != null) {
//                Class<?> type = container.getType(propertyId);
//                if (type != null) return type;
//            }
//
//            throw new IllegalArgumentException("Unknown property "+propertyId);
//        }
//        return dynaProperty.getType();
//    }

    public BasicDataNavigation getNavigation() {
        if (navigation == null)
            throw new IllegalStateException("Cannot return Navigation: no Container.Ordered instance was given at construction time");

        return navigation;
    }

    static class EditingModeSwitcher implements EditingModeChange.Listener {
        final DataNavigation otherNavigation;
        EditingModeSwitcher(DataNavigation other) {
            this.otherNavigation = other;
        }
        public void editingModeChange(EditingModeChange.Event event) {
            if (event.isEnteringEditingMode()) {
                otherNavigation.enableCrud();
            } else {
                otherNavigation.disableCrud();
            }
        }
    }

    private final CurrentItemChange.Listener defaultItemChangeListener = new CurrentItemChange.Listener() {
        @Override
        public void currentItemChange(CurrentItemChange.Event event) {
            FieldBinder.this.setItemDataSource(event.getNewItem());
        }
    };
    public CurrentItemChange.Listener defaultItemChangeListener() {
        return this.defaultItemChangeListener;
    }

}
