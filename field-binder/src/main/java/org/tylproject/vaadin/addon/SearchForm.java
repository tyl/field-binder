package org.tylproject.vaadin.addon;

import com.vaadin.ui.*;
import org.tylproject.vaadin.addon.fieldbinder.FieldBinder;
import org.tylproject.vaadin.addon.fields.FilterPatternComboBox;
import org.tylproject.vaadin.addon.fields.FilterPatternField;
import org.tylproject.vaadin.addon.fields.FilterPatternTextField;
import org.tylproject.vaadin.addon.fields.SearchPattern;

import java.util.*;

/**
 * A form that automatically produces a collection of Filters
 */
public class SearchForm extends FormLayout {
    final Map<Object, Class<?>> propertyIdToType = new LinkedHashMap<>();
    final Map<Object, FilterPatternField> propertyIdToFilterExpressionField = new LinkedHashMap<>();

    /**
     * Create a SearchForm for the fields of a FieldBinder
     */
    public SearchForm(FieldBinder<?> fieldBinder) {
        final Map<Object, Class<?>> propertyIdToType = fieldBinder.getPropertyIdToTypeBindings();

        for (Map.Entry<Object, Class<?>> e: propertyIdToType.entrySet()) {
            Object propertyId = e.getKey();
            Class<?> type = e.getValue();

            // ignore fields for "detail" type elements
            if (List.class.isAssignableFrom(type)
                || Collection.class.isAssignableFrom(type)
                || Map.class.isAssignableFrom(type)) {
                continue;
            }

            addProperty(propertyId, type);

        }

    }

    /**
     * Create a SearchForm for pairs of the form (propertyId, type)
     */
    public SearchForm(Map<Object, Class<?>> propertyIdToType) {
        this.propertyIdToType.putAll(propertyIdToType);
        makeAllFields(propertyIdToType);

        for (FilterPatternField f : propertyIdToFilterExpressionField.values()) {
            this.addComponent(f);
        }
    }

    /**
     * add a field to the SearchForm for the given propertyId, propertyType
     */
    public void addProperty(Object propertyId, Class<?> propertyType) {
        this.propertyIdToType.put(propertyId, propertyType);
        FilterPatternField f = makeField(propertyId, propertyType);
        this.propertyIdToFilterExpressionField.put(propertyId, f);
        this.addComponent(f);
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

    private void makeAllFields(Map<Object, Class<?>> propertyIdToType) {
        for (Map.Entry<Object, Class<?>> e: propertyIdToType.entrySet()) {
            Object propertyId = e.getKey();
            Class<?> type =  e.getValue();
            FilterPatternField f = makeField(propertyId, type);
            propertyIdToFilterExpressionField.put(propertyId,f);
        }
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
