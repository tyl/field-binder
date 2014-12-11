package org.tylproject.vaadin.addon.fieldbinder.strategies;

import com.vaadin.data.Container;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Field;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by evacchi on 11/12/14.
 */
public class FilterApplier {


    private final FilterFactory filterFactory;

    /**
     * maps fieldId to filter
     */
    private final Map<Object, Object> propertyIdToFilterPattern = new HashMap<Object, Object>();



    public FilterApplier() {
        this.filterFactory = new DefaultFilterFactory();
    }
    public FilterApplier(FilterFactory filterFactory) {
        this.filterFactory = filterFactory;
    }

    public void restorePatterns(Map<Object, Field<?>> propertyIdToField) {
        for (Map.Entry<Object, Field<?>> e : propertyIdToField.entrySet()) {

            Object propertyId = e.getKey();
            Field field = e.getValue(); // raw type
            Object pattern = propertyIdToFilterPattern.get(propertyId);

            field.setValue(pattern);

        }
    }
    public void applyFilters(Map<Object, Field<?>> propertyIdToField, Container.Filterable container) {

        propertyIdToFilterPattern.clear();
        container.removeAllContainerFilters();

        for (Map.Entry<Object,Field<?>> e : propertyIdToField.entrySet()) {
            Object propertyId = e.getKey();
            Field<?> prop = e.getValue();
            Object pattern = prop.getValue();
            Class<?> modelType = getModelType(prop);
            if (pattern != null) {

                propertyIdToFilterPattern.put(propertyId, pattern);

                container.addContainerFilter(filterFactory.createFilter(modelType,
                        propertyId, pattern));
            }
        }

    }


    public Map<Object, Object> getPropertyIdToFilterPattern () {
        return propertyIdToFilterPattern;
    }
    public void clearPropertyIdToFilterPatterns() {
        propertyIdToFilterPattern.clear();
    }
    public boolean hasAppliedFilters() {
        return !propertyIdToFilterPattern.isEmpty();
    }

    private Class<?> getModelType(Field<?> prop) {
        if (prop instanceof AbstractField) {
            AbstractField<?> abstractField = (AbstractField<?>) prop;
            Converter<?, Object> converter = abstractField.getConverter();
            if (converter != null) {
                return converter.getModelType();
            }
        }

        // otherwise, fallback to the property type
        return prop.getType();

    }
}
