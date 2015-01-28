package org.tylproject.vaadin.addon;

import com.vaadin.ui.*;
import org.tylproject.vaadin.addon.fieldbinder.FieldBinder;
import org.tylproject.vaadin.addon.fields.filterpattern.FilterPatternComboBox;
import org.tylproject.vaadin.addon.fields.filterpattern.FilterPatternField;
import org.tylproject.vaadin.addon.fields.filterpattern.FilterPatternTextField;
import org.tylproject.vaadin.addon.fields.SearchPattern;

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
        this.searchFieldManager = new SearchFieldManager(fieldBinder);


        for (FilterPatternField f : searchFieldManager.getPropertyIdToFilterExpressionField().values()) {
            this.addComponent(f);
        }
    }

    /**
     * Create a SearchForm for pairs of the form (propertyId, type)
     */
    public SearchForm(Map<Object, Class<?>> propertyIdToType) {
        this.searchFieldManager = new SearchFieldManager(propertyIdToType);


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
