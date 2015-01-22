package org.tylproject.vaadin.addon;

import com.vaadin.data.Container;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.tylproject.vaadin.addon.fieldbinder.FieldBinder;
import org.tylproject.vaadin.addon.fields.FilterExpressionField;

import java.util.*;

/**
 * Created by evacchi on 22/01/15.
 */
public class SearchForm extends FormLayout {
    final Map<Object, Class<?>> propertyIdToType = new LinkedHashMap<>();
    final Map<Object, FilterExpressionField> propertyIdToFilterExpressionField = new LinkedHashMap<>();


    public SearchForm(FieldBinder<?> fieldBinder) {
        final Map<Object, Class<?>> propertyIdToType = fieldBinder.getPropertyIdToTypeBindings();
//        final Map<Object, Field<?>> propertyIdToFieldBindings = fieldBinder.getPropertyIdToFieldBindings();

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

    public SearchForm(Map<Object, Class<?>> propertyIdToType) {
        this.propertyIdToType.putAll(propertyIdToType);
        makeAllFields(propertyIdToType);

        for (FilterExpressionField f : propertyIdToFilterExpressionField.values()) {
            this.addComponent(f);
        }
    }

    public void addProperty(Object propertyId, Class<?> propertyType) {
        this.propertyIdToType.put(propertyId, propertyType);
        FilterExpressionField f = makeField(propertyId, propertyType);
        this.propertyIdToFilterExpressionField.put(propertyId, f);
        this.addComponent(f);
    }

    public Map<Object, Container.Filter> getFiltersFromValues() {
        final Map<Object, Container.Filter> filtersFromValues = new LinkedHashMap<>();
        for (Map.Entry<Object, FilterExpressionField> e : getPropertyIdToFilterExpressionField().entrySet()) {
            Container.Filter filter = e.getValue().getFilterFromValue();
            filtersFromValues.put(e.getKey(), filter);
        }
        return Collections.unmodifiableMap(filtersFromValues);
    }

    public Map<Object, Class<?>> getPropertyIdToType() {
        return Collections.unmodifiableMap(propertyIdToType);
    }

    public Map<Object, FilterExpressionField> getPropertyIdToFilterExpressionField() {
        return Collections.unmodifiableMap(propertyIdToFilterExpressionField);
    }

    private void makeAllFields(Map<Object, Class<?>> propertyIdToType) {
        for (Map.Entry<Object, Class<?>> e: propertyIdToType.entrySet()) {
            Object propertyId = e.getKey();
            Class<?> type =  e.getValue();
            FilterExpressionField f = makeField(propertyId, type);
            propertyIdToFilterExpressionField.put(propertyId,f);
        }
    }

    private FilterExpressionField makeField(Object propertyId, Class<?> type) {
        FilterExpressionField f = new FilterExpressionField(propertyId, type);
        f.setCaption(DefaultFieldFactory.createCaptionByPropertyId(propertyId));
        return f;
    }


    public void clear() {
        for (FilterExpressionField f: getPropertyIdToFilterExpressionField().values()) {
            f.clear();
        }
    }
}
