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
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.DefaultFieldFactory;
import com.vaadin.ui.Field;
import com.vaadin.ui.HasComponents;
import org.tylproject.vaadin.addon.fieldbinder.FieldBinder;

import java.util.*;

/**
 * Created by evacchi on 28/01/15.
 */
public class SearchFieldManager {
    final Map<Object, Class<?>> propertyIdToType = new LinkedHashMap<>();
    final Map<Object, FilterPatternField> propertyIdToFilterExpressionField = new LinkedHashMap<>();

    /**
     * Create a SearchForm for pairs of the form (propertyId, type)
     */
    public SearchFieldManager(Map<Object, Class<?>> propertyIdToType) {
        for (Map.Entry<Object, Class<?>> e: propertyIdToType.entrySet()) {
            addProperty(e.getKey(), e.getValue());
        }
    }

    public SearchFieldManager(Container container) {
        for (Object propertyId: container.getContainerPropertyIds()) {
            addProperty(propertyId, container.getType(propertyId));
        }
    }


    /**
     * add a field to the SearchForm for the given propertyId, propertyType
     */
    public void addProperty(Object propertyId, Class<?> propertyType) {
        this.propertyIdToType.put(propertyId, propertyType);
        FilterPatternField f = makeField(propertyId, propertyType);
        this.propertyIdToFilterExpressionField.put(propertyId, f);
    }

    /**
     * Return the inferred filters for the values currently in the form fields
     */
    public Map<Object, SearchPattern> getPatternsFromValues() {
        final Map<Object, SearchPattern> filtersFromValues = new LinkedHashMap<>();

        for (Map.Entry<Object, FilterPatternField> e : getPropertyIdToFilterExpressionField().entrySet()) {

            SearchPattern searchPattern = e.getValue().getPatternFromValue();
            if (searchPattern.isEmpty()) continue;

            filtersFromValues.put(e.getKey(), searchPattern);

        }
        return Collections.unmodifiableMap(filtersFromValues);
    }

    public Map<Object, Class<?>> getPropertyIdToType() {
        return Collections.unmodifiableMap(propertyIdToType);
    }

    public Map<Object, FilterPatternField> getPropertyIdToFilterExpressionField() {
        return Collections.unmodifiableMap(propertyIdToFilterExpressionField);
    }


    private FilterPatternField makeField(Object propertyId, Class<?> type) {
        FilterPatternField f ;
        if (java.lang.Enum.class.isAssignableFrom(type)) {
            f = new FilterPatternComboBox(propertyId, (Class<java.lang.Enum>)type);
        } else {
            f = new FilterPatternTextField(propertyId, type);
        }
        f.setCaption(DefaultFieldFactory.createCaptionByPropertyId(propertyId));
        return f;
    }

    public void clear() {
        for (FilterPatternField f: getPropertyIdToFilterExpressionField().values()) {
            f.clear();
        }
    }

}
