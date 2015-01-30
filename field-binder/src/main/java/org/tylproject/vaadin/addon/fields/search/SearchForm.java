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
import com.vaadin.ui.*;
import org.tylproject.vaadin.addon.fieldbinder.FieldBinder;

import java.util.*;

/**
 * A form that automatically produces a collection of Filters
 */
public class SearchForm extends FormLayout {
    private final SearchFieldManager searchFieldManager;

    /**
     * Create a SearchForm for the fields of a FieldBinder
     */
    public SearchForm(FieldBinder<?> fieldBinder) {
        this.searchFieldManager = new FieldBinderSearchFieldManager (fieldBinder);
        addFieldsToLayout();
    }

    /**
     * Create a SearchForm for pairs of the form (propertyId, type)
     */
    public SearchForm(Map<Object, Class<?>> propertyIdToType) {
        this.searchFieldManager = new SearchFieldManager(propertyIdToType);
        addFieldsToLayout();
    }

    public SearchForm(Container container) {
        this.searchFieldManager = new SearchFieldManager(container);
        addFieldsToLayout();
    }

    private void addFieldsToLayout() {
        for (FilterPatternField f : searchFieldManager.getPropertyIdToFilterExpressionField().values()) {
            this.addComponent(f);
        }
    }


    /**
     * add a field to the SearchForm for the given propertyId, propertyType
     */
    public void addProperty(Object propertyId, Class<?> propertyType) {
        searchFieldManager.addProperty(propertyId, propertyType);
        this.addComponent(this.getSearchFieldManager().getPropertyIdToFilterExpressionField().get(propertyId));
    }



    /**
     * Return the inferred filters for the values currently in the form fields
     */
    public Map<Object, SearchPattern> getPatternsFromValues() {
        return searchFieldManager.getPatternsFromValues();
    }


    public void clear() {
        searchFieldManager.clear();
    }


    public SearchFieldManager getSearchFieldManager() {
        return searchFieldManager;
    }
}
