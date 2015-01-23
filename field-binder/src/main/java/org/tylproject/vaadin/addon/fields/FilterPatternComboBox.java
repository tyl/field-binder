package org.tylproject.vaadin.addon.fields;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.event.FieldEvents;
import com.vaadin.ui.ComboBox;

import java.util.EnumSet;

/**
 * Created by evacchi on 23/01/15.
 */
public class FilterPatternComboBox extends FilterPatternField<Object, Object, ComboBox> {

    private static final String CAPTION_PROPERTY_ID = "Caption";

    public FilterPatternComboBox(Object propertyId, Class<? extends java.lang.Enum> propertyType) {
        super(new ComboBox(), Object.class, propertyId, propertyType);
        setFieldDefaults(getBackingField());
    }


    public FilterPatternComboBox(Object propertyId, Class<?> propertyType, Container
            .Filterable targetContainer) {
        super(new ComboBox(), Object.class, propertyId, propertyType, targetContainer);
        setFieldDefaults(getBackingField());
        addDefaultBackingFieldListeners(targetContainer);
    }

    private void setFieldDefaults(ComboBox backingField) {
        backingField.setImmediate(true);

        backingField.removeAllItems();
        for (Object p : backingField.getContainerPropertyIds()) {
            backingField.removeContainerProperty(p);
        }

        backingField.addContainerProperty(CAPTION_PROPERTY_ID, String.class, "");
        backingField.setItemCaptionPropertyId(CAPTION_PROPERTY_ID);
        @SuppressWarnings("unchecked")
        EnumSet<?> enumSet = EnumSet.allOf((Class<java.lang.Enum>) getTargetPropertyType());
        for (Object r : enumSet) {
            Item newItem = backingField.addItem(r);
            newItem.getItemProperty(CAPTION_PROPERTY_ID).setValue(r.toString());
        }
    }

    private void addDefaultBackingFieldListeners(Container.Filterable targetContainer) {
        if (!getBackingField().getListeners(
                FieldEvents.TextChangeEvent.class).contains(valueChangeListener)) {
            getBackingField().addValueChangeListener(valueChangeListener);
        }
    }

    private final ValueChangeListener valueChangeListener = new ValueChangeListener() {
        @Override
        public void valueChange(Property.ValueChangeEvent event) {
            applyFilterPattern(
                    getTargetPropertyType(),
                    getTargetPropertyId(),
                    event.getProperty().getValue(),
                    getTargetContainer());
        }
    };
}
