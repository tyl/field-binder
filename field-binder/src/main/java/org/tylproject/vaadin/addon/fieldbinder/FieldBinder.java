package org.tylproject.vaadin.addon.fieldbinder;

import com.vaadin.data.Container;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.ui.Field;
import org.apache.commons.beanutils.DynaProperty;
import org.apache.commons.beanutils.WrapDynaClass;
import org.tylproject.vaadin.addon.crudnav.BasicCrudNavigation;
import org.tylproject.vaadin.addon.crudnav.ButtonBar;
import org.tylproject.vaadin.addon.fieldbinder.strategies.DefaultNavigationStrategy;

import java.util.*;

/**
 * Created by evacchi on 02/12/14.
 */
public class FieldBinder<T> extends AbstractFieldBinder<FieldGroup> {

    private final WrapDynaClass dynaClass ;
    private final Class<T> beanClass;

    public FieldBinder(Class<T> beanClass) {
        super(new FieldGroup());
        this.beanClass = beanClass;
        this.dynaClass = WrapDynaClass.createDynaClass(beanClass);
    }

    public <T> ListTable<T> buildListOf(Class<T> containedBeanClass, Object propertyId) {
        Class<?> dataType = getPropertyType(propertyId);
        ListTable<T> field = getFieldFactory().createDetailField(dataType, containedBeanClass);
        bind(field, propertyId);
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

        DefaultNavigationStrategy<T> defaultNavigationStrategy = new DefaultNavigationStrategy<T>(beanClass, this);

        BasicCrudNavigation nav = new BasicCrudNavigation(container)
                                    .withCrudListenersFrom(defaultNavigationStrategy)
                                    .withFindListenersFrom(defaultNavigationStrategy);

        nav.addCurrentItemChangeListener(defaultNavigationStrategy);

        return new ButtonBar(nav);
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

    private Map<Object, Class<?>> propertyTypes = new HashMap<Object, Class<?>>();

    @Override
    protected Class<?> getPropertyType(Object propertyId) {
        return dynaClass.getDynaProperty(propertyId.toString()).getType();
//        Class<?> t = propertyTypes.get(propertyId);
//        if (t == null) {
//            try {
//                t = (Class<?>) Introspector.getBeanInfo(beanClass).getPropertyDescriptors()[0].getReadMethod().getGenericReturnType();
//                propertyTypes.put(propertyId, t);
//            }  catch (IntrospectionException e) {
//                throw new IllegalArgumentException(e);
//            }
//        }
//
//        return t;
    }
//
//    private static Map<Object, PropertyDescriptor> createPropertyMap(BeanInfo beanInfo) {
//        Map<Object, PropertyDescriptor> propertyMap = new LinkedHashMap<Object, PropertyDescriptor>();
//
//        for (PropertyDescriptor descriptor: beanInfo.getPropertyDescriptors()) {
//            propertyMap.put(descriptor.getName(), descriptor);
//        }
//
//        return propertyMap;
//    }
}
