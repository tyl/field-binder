package org.tylproject.vaadin.addon.fieldbinder.behavior;

import com.vaadin.data.Container;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Field;

import java.util.HashMap;
import java.util.Map;

/**
 * Generates {@link com.vaadin.data.Container.Filter} from simple text patterns
 * and automatically applies them on a {@link com.vaadin.data.Container}
 *
 * Keeps track of which fields were pulled from a collection of fields, and
 * is able to restore them back into the collection using properyIds as keys
 *
 * It internally uses a {@link org.tylproject.vaadin.addon.fieldbinder.behavior.FilterFactory}
 * to convert the patterns
 *
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

    /**
     * restore patterns into the given field map automatically
     *
     * @param propertyIdToField a map propertyId -> Field<?>
     */
    public void restorePatterns(Map<Object, Field<?>> propertyIdToField) {
        for (Map.Entry<Object, Field<?>> e : propertyIdToField.entrySet()) {

            Object propertyId = e.getKey();
            Field field = e.getValue(); // raw type, so that we can just do setValue(Object)
            Object pattern = propertyIdToFilterPattern.get(propertyId);

            field.setValue(pattern);

        }
    }

    /**
     * converts the patterns pulled from a map of fields to a collection of
     * {@link Container.Filter} and applies them to the given {@link com.vaadin.data.Container}
     *
     * @param propertyIdToField a map propertyId -> Field
     */
    public void applyFilters(Map<Object, Field<?>> propertyIdToField, Container.Filterable container) {

        propertyIdToFilterPattern.clear();
        container.removeAllContainerFilters();

        for (Map.Entry<Object,Field<?>> e : propertyIdToField.entrySet()) {
            Object propertyId = e.getKey();
            Field<?> prop = e.getValue();
            Object pattern = ((AbstractField<?>)prop).getValue();
            Class<?> modelType = getModelType(prop);
            if (pattern != null && !pattern.toString().isEmpty()) {

                propertyIdToFilterPattern.put(propertyId, pattern);

                container.addContainerFilter(filterFactory.createFilter(modelType,
                        propertyId, pattern));
            }
        }

    }


    /**
     * @return the mapping between propertyId and applied pattern
     */
    public Map<Object, Object> getPropertyIdToFilterPattern () {
        return propertyIdToFilterPattern;
    }

    public void clearPropertyIdToFilterPatterns() {
        propertyIdToFilterPattern.clear();
    }

    /**
     * true when the internal state of this FilterApplier is non-empty
     */
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
