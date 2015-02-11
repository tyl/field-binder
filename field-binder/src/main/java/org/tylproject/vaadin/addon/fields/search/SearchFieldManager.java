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
import com.vaadin.ui.DefaultFieldFactory;
import org.tylproject.vaadin.addon.fieldbinder.behavior.DefaultFilterFactory;
import org.tylproject.vaadin.addon.fieldbinder.behavior.FilterFactory;

import java.util.*;

/**
 * Created by evacchi on 28/01/15.
 */
public class SearchFieldManager {
    protected final Map<Object, Class<?>> propertyIdToType = new LinkedHashMap<>();
    protected final Map<Object, SearchPatternField<?,?>> propertyIdToSearchPatternField = new LinkedHashMap<>();
    protected FilterFactory filterFactory = new DefaultFilterFactory();
    protected SearchFieldFactory searchFieldFactory = new DefaultSearchFieldFactory();

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

    public void setFilterFactory(FilterFactory filterFactory) {
        this.filterFactory = filterFactory;
        for (SearchPatternField fef: propertyIdToSearchPatternField.values()) {
            fef.setFilterFactory(filterFactory);
        }
    }

    public FilterFactory getFilterFactory() {
        return filterFactory;
    }

    public void setSearchFieldFactory(SearchFieldFactory searchFieldFactory) {
        this.searchFieldFactory = searchFieldFactory;
    }
    public SearchFieldFactory getSearchFieldFactory() {
        return searchFieldFactory;
    }

    /**
     * add a field to the SearchForm for the given propertyId, propertyType
     */
    public void addProperty(Object propertyId, Class<?> propertyType) {
        this.propertyIdToType.put(propertyId, propertyType);
        SearchPatternField f = searchFieldFactory.createField(propertyId, propertyType);
        this.propertyIdToSearchPatternField.put(propertyId, f);
    }

    /**
     * Return the inferred filters for the values currently in the form fields
     */
    public Map<Object, SearchPattern> getPatternsFromValues() {
        final Map<Object, SearchPattern> filtersFromValues = new LinkedHashMap<>();

        for (Map.Entry<Object, SearchPatternField<?,?>> e : getPropertyIdToSearchPatternField().entrySet()) {

            SearchPattern searchPattern = e.getValue().getPatternFromValue();
            if (searchPattern.isEmpty()) continue;

            filtersFromValues.put(e.getKey(), searchPattern);

        }
        return Collections.unmodifiableMap(filtersFromValues);
    }

    public Map<Object, Class<?>> getPropertyIdToType() {
        return (propertyIdToType);
    }

    public Map<Object, SearchPatternField<?,?>> getPropertyIdToSearchPatternField() {
        return (propertyIdToSearchPatternField);
    }


    public void clear() {
        for (SearchPatternField f: getPropertyIdToSearchPatternField().values()) {
            f.clear();
        }
    }

}
