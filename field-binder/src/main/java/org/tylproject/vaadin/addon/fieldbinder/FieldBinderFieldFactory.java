package org.tylproject.vaadin.addon.fieldbinder;

import com.vaadin.data.fieldgroup.DefaultFieldGroupFieldFactory;
import com.vaadin.ui.AbstractTextField;
import com.vaadin.ui.DefaultFieldFactory;

/**
 * An extended {@link com.vaadin.data.fieldgroup.FieldGroupFieldFactory}
 * that supports {@link org.tylproject.vaadin.addon.fieldbinder.ListTable}
 */
public class FieldBinderFieldFactory extends DefaultFieldGroupFieldFactory {

    @Override
    protected <T extends AbstractTextField> T createAbstractTextField(
            Class<T> fieldType) {
        T field = super.createAbstractTextField(fieldType);
        field.setNullRepresentation("");
        return field;
    }

    public <T> ListTable<T> createDetailField(Class<?> dataType, Class<T> containedBeanClass) {
        return new ListTable<T>(containedBeanClass);
    }
}
