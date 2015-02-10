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

import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Field;
import com.vaadin.ui.HasComponents;
import org.tylproject.vaadin.addon.fieldbinder.FieldBinder;

import java.util.Collections;
import java.util.Map;


/**
 * Create a SearchForm for the fields of a FieldBinder
 */
public class FieldBinderSearchFieldManager extends SearchFieldManager {
    final FieldBinder<?> fieldBinder;

    public FieldBinderSearchFieldManager(FieldBinder<?> fieldBinder) {
        super(Collections.<Object,Class<?>>emptyMap());
        this.fieldBinder = fieldBinder;
        makeSearchFieldsFromFieldBinder();
    }

    public void addPropertyFromField(Object propertyId, Class<?> propertyType, Field<?> field) {
        this.propertyIdToType.put(propertyId, propertyType);
        SearchPatternField<?,?,?> searchPatternField = getSearchFieldFactory().createField(propertyId, propertyType, field);
        this.propertyIdToSearchPatternField.put(propertyId, searchPatternField);
    }



    private void makeSearchFieldsFromFieldBinder() {
        final Map<Object, Class<?>> propertyIdToType = fieldBinder.getPropertyIdToTypeBindings();

        int numNonCollectionFields = propertyIdToType.size() - fieldBinder.getCollectionFields().size();
        int numSearchFields = this.getPropertyIdToSearchPatternField().size();

        // if there are more fields in the binder
        // than the fields there are in our maps, let us create them again
        if (numNonCollectionFields > numSearchFields) {
            this.getPropertyIdToType().clear();
            this.getPropertyIdToSearchPatternField().clear();

            for (Map.Entry<Object, Class<?>> e : propertyIdToType.entrySet()) {
                Object propertyId = e.getKey();
                Class<?> type = e.getValue();

                // ignore fields for "detail" type elements
                if (fieldBinder.getCollectionFields().keySet().contains(propertyId))
                    continue;

                addPropertyFromField(propertyId, type, fieldBinder.getField(propertyId));

            }
        }
    }

    public void replaceFields() {
        if (fieldBinder == null) {
            throw new IllegalStateException(
                    "Cannot replace fields. No FieldBinder instance was given");
        }

        makeSearchFieldsFromFieldBinder();

        for (Map.Entry<Object, SearchPatternField<?,?,?>> e : getPropertyIdToSearchPatternField().entrySet()) {
            Object propertyId = e.getKey();
            Field<?> replacement = e.getValue();
            Field<?> original = fieldBinder.getPropertyIdToFieldBindings().get(propertyId);

            // this should be moved somewhere else
            replacement.setCaption(original.getCaption());
            replacement.setWidth(original.getWidth(), original.getWidthUnits());

            replace(original, replacement);
        }
    }

    public void restoreFields() {

        if (fieldBinder == null) {
            throw new IllegalStateException(
                    "Cannot restore fields. No FieldBinder instance was given");
        }

        for (Map.Entry<Object, SearchPatternField<?,?,?>> e : getPropertyIdToSearchPatternField().entrySet()) {
            Object propertyId = e.getKey();
            Field<?> replacement = e.getValue();
            Field<?> original = fieldBinder.getPropertyIdToFieldBindings().get(propertyId);
            replace(replacement, original);
        }
    }


    private void replace(Field<?> original, Field<?> replacement) {
        HasComponents parent = original.getParent();
        if (parent instanceof ComponentContainer) {
            ((ComponentContainer) parent).replaceComponent(original, replacement);
        } else {
            throw new UnsupportedOperationException(
                    "Cannot replace Field "+original +
                            "; the parent does not support component replacement");
        }
    }

}
