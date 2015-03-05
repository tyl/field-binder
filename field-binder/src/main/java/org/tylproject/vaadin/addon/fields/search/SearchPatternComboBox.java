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

package org.tylproject.vaadin.addon.fields.search;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.event.FieldEvents;
import com.vaadin.ui.ComboBox;

import java.util.EnumSet;

/**
 * A SearchPatternField that displays a ComboBox.
 *
 * To be used with Enums or a Container
 */
public class SearchPatternComboBox extends SearchPatternField<Object, ComboBox> {

    private static final String CAPTION_PROPERTY_ID = "Caption";

    public SearchPatternComboBox(Object propertyId, Class<? extends java.lang.Enum> propertyType) {
        super(new ComboBox(), Object.class, propertyId, propertyType);
        setFieldDefaults(getBackingField());
    }


    public SearchPatternComboBox(Object propertyId,
                                 Class<?> propertyType,
                                 Container.Filterable targetContainer) {

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

    private final Property.ValueChangeListener valueChangeListener = new Property.ValueChangeListener() {
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
