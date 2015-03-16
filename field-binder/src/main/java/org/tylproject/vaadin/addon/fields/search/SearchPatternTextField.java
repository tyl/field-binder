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
import com.vaadin.event.FieldEvents;
import com.vaadin.ui.TextField;

/**
 * A text field where users input a search pattern
 */
public class SearchPatternTextField extends SearchPatternField<String, TextField> {

    public SearchPatternTextField(Object propertyId, Class<?> propertyType) {
        super(new TextField(), String.class, propertyId, propertyType);
        setFieldDefaults(getBackingField());
    }


    public SearchPatternTextField(Object propertyId, Class<?> propertyType, Container.Filterable targetContainer) {
        super(new TextField(), String.class, propertyId, propertyType, targetContainer);
        setFieldDefaults(getBackingField());
        addDefaultBackingFieldListeners();
    }

    private void setFieldDefaults(TextField backingField) {
        backingField.setNullRepresentation("");
        backingField.setImmediate(true);
    }

    private void addDefaultBackingFieldListeners() {
        getBackingField().addTextChangeListener(textChangeListener);
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
