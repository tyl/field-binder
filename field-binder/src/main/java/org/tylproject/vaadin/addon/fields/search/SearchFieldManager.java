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
     * Create a SearchForm for the fields of a FieldBinder
     */
    public SearchFieldManager(FieldBinder<?> fieldBinder) {
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
    public SearchFieldManager(Map<Object, Class<?>> propertyIdToType) {
        this.propertyIdToType.putAll(propertyIdToType);
        makeAllFields(propertyIdToType);
    }

    public SearchFieldManager(Container container) {
        for (Object propertyId: container.getContainerPropertyIds()) {
            this.propertyIdToType.put(propertyId, container.getType(propertyId));
        }
        makeAllFields(propertyIdToType);
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

    public void replaceFields(FieldBinder<?> fieldBinder) {
        for (Map.Entry<Object, FilterPatternField> e : getPropertyIdToFilterExpressionField().entrySet()) {
            Object propertyId = e.getKey();
            Field<?> replacement = e.getValue();
            Field<?> original = fieldBinder.getPropertyIdToFieldBindings().get(propertyId);

            // this should be moved somewhere else
            replacement.setCaption(original.getCaption());

            replace(original, replacement);
        }
    }

    public void restoreFields(FieldBinder<?> fieldBinder) {
        for (Map.Entry<Object, FilterPatternField> e : getPropertyIdToFilterExpressionField().entrySet()) {
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
