package org.tylproject.vaadin.addon.fields.filterpattern;

import com.vaadin.data.Container;
import com.vaadin.event.FieldEvents;
import com.vaadin.ui.TextField;

/**
 * Created by evacchi on 23/01/15.
 */
public class FilterPatternTextField extends FilterPatternField<String, String, TextField> {

    public FilterPatternTextField(Object propertyId, Class<?> propertyType) {
        super(new TextField(), String.class, propertyId, propertyType);
        setFieldDefaults(getBackingField());
    }


    public FilterPatternTextField(Object propertyId, Class<?> propertyType, Container.Filterable targetContainer) {
        super(new TextField(), String.class, propertyId, propertyType, targetContainer);
        setFieldDefaults(getBackingField());
        addDefaultBackingFieldListeners(getTargetContainer());
    }

    private void setFieldDefaults(TextField backingField) {
        backingField.setNullRepresentation("");
        backingField.setImmediate(true);
    }

    private void addDefaultBackingFieldListeners(Container.Filterable targetContainer) {
        if (!getBackingField().getListeners(
                FieldEvents.TextChangeEvent.class).contains(textChangeListener)) {
            getBackingField().addTextChangeListener(textChangeListener);
        }
    }

    private final FieldEvents.TextChangeListener textChangeListener = new FieldEvents.TextChangeListener() {
        @Override
        public void textChange(FieldEvents.TextChangeEvent event) {
            applyFilterPattern(
                    getTargetPropertyType(),
                    getTargetPropertyId(),
                    event.getText(),
                    getTargetContainer());
        }
    };
}
