package org.tylproject.vaadin.addon.fields.search;

import com.vaadin.addon.jpacontainer.fieldfactory.FieldFactory;
import com.vaadin.data.fieldgroup.FieldGroupFieldFactory;
import com.vaadin.ui.Field;

/**
 * Created by evacchi on 10/02/15.
 */
public interface SearchFieldFactory {
    public SearchPatternField<?,?,?> createField(Object propertyId, Class<?> propertyType);
    public SearchPatternField<?,?,?> createField(Object propertyId, Class<?> propertyType, Field<?> originalField);
}
