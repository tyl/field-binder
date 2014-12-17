package org.tylproject.vaadin.addon.fieldbinder;

import com.vaadin.data.fieldgroup.DefaultFieldGroupFieldFactory;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.AbstractTextField;
import com.vaadin.ui.DefaultFieldFactory;
import com.vaadin.ui.Field;

/**
 * An extended {@link com.vaadin.data.fieldgroup.FieldGroupFieldFactory}
 * that supports {@link org.tylproject.vaadin.addon.fieldbinder.ListTable}
 */
public class FieldBinderFieldFactory extends DefaultFieldGroupFieldFactory {

    public <T extends Field> T createField(Class<?> type, Class<T> fieldType) {
        T f = super.createField(type, fieldType);
        ((AbstractField<?>) f).setImmediate(true);
        return f;
    }

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
