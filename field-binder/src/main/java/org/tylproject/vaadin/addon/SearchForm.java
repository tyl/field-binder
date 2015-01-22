package org.tylproject.vaadin.addon;

import com.vaadin.ui.DefaultFieldFactory;
import com.vaadin.ui.FormLayout;
import org.tylproject.vaadin.addon.fields.FilterExpressionField;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by evacchi on 22/01/15.
 */
public class SearchForm extends FormLayout {
    final Map<Object, Class<?>> propertyIdToType = new LinkedHashMap<>();
    final Map<Object, FilterExpressionField> propertyIdToFilterExpressionField = new LinkedHashMap<>();

    public SearchForm(Map<Object, Class<?>> propertyIdToType) {
        this.propertyIdToType.putAll(propertyIdToType);
        makeFields(propertyIdToType);

        for (FilterExpressionField f : propertyIdToFilterExpressionField.values()) {
            this.addComponent(f);
        }
    }

    public Map<Object, Class<?>> getPropertyIdToType() {
        return Collections.unmodifiableMap(propertyIdToType);
    }

    public Map<Object, FilterExpressionField> getPropertyIdToFilterExpressionField() {
        return Collections.unmodifiableMap(propertyIdToFilterExpressionField);
    }

    private void makeFields(Map<Object, Class<?>> propertyIdToType) {
        for (Map.Entry<Object, Class<?>> e: propertyIdToType.entrySet()) {
            Object propertyId = e.getKey();

            FilterExpressionField f = new FilterExpressionField(propertyId, e.getValue());
            f.setCaption(DefaultFieldFactory.createCaptionByPropertyId(propertyId));

            propertyIdToFilterExpressionField.put(propertyId,f);
        }
    }


}
