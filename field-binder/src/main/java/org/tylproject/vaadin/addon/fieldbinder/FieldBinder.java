package org.tylproject.vaadin.addon.fieldbinder;

import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.ui.Field;
import org.apache.commons.beanutils.DynaProperty;
import org.apache.commons.beanutils.WrapDynaClass;

import java.beans.*;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
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

    public Field<?> buildListOf(Class<?> containedBeanClass, Object propertyId) {
        Class<?> dataType = getPropertyType(propertyId);
        Field<?> field = getFieldFactory().createDetailField(dataType, containedBeanClass);
        bind(field, propertyId);
        return field;
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