package org.tylproject.vaadin.addon.fieldbinder;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.ui.Field;
import org.apache.commons.beanutils.DynaProperty;
import org.apache.commons.beanutils.WrapDynaClass;

import java.beans.*;
import java.util.*;

/**
 * Created by evacchi on 02/12/14.
 */
public class FieldBinder<T> extends AbstractFieldBinder<FieldGroup> {

    private final WrapDynaClass dynaClass ;

    public FieldBinder(Class<T> beanClass) {
        super(new FieldGroup());
        this.dynaClass = WrapDynaClass.createDynaClass(beanClass);
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

    private static Map<Object, PropertyDescriptor> createPropertyMap(BeanInfo beanInfo) {
        Map<Object, PropertyDescriptor> propertyMap = new LinkedHashMap<Object, PropertyDescriptor>();

        for (PropertyDescriptor descriptor: beanInfo.getPropertyDescriptors()) {
            propertyMap.put(descriptor.getName(), descriptor);
        }

        return propertyMap;
    }
}
