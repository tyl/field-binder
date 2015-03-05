package org.tylproject.vaadin.addon.fields.search;

import com.vaadin.ui.Field;

public interface SearchFieldFactory {
    /**
     * Create a SearchPatternField for the given propertyId, type
     */
    public SearchPatternField<?,?> createField(Object propertyId, Class<?> propertyType);

    /**
     * Create a SearchPatternField for the given propertyId, type, copying other settings
     * from the given originalField (e.g., its caption).
     *
     * It may not work for all kinds of fields. In this case the implementation
     * may warn the user via logging or other means
     * (e.g., displaying an "Unsupported Field" text instead of a real field).
     *
     */
    public SearchPatternField<?,?> createField(Object propertyId, Class<?> propertyType, Field<?> originalField);
}
