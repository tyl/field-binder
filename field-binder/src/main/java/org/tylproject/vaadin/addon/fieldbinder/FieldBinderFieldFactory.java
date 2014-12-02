package org.tylproject.vaadin.addon.fieldbinder;

import com.vaadin.data.fieldgroup.DefaultFieldGroupFieldFactory;
import com.vaadin.ui.AbstractTextField;
import com.vaadin.ui.DefaultFieldFactory;

/**
 * Created by evacchi on 02/12/14.
 */
public class FieldBinderFieldFactory extends DefaultFieldGroupFieldFactory {

    @Override
    protected <T extends AbstractTextField> T createAbstractTextField(
            Class<T> fieldType) {
        T field = super.createAbstractTextField(fieldType);
        field.setNullRepresentation("");
        return field;
    }

}
