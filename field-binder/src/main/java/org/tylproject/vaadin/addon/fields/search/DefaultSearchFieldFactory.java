package org.tylproject.vaadin.addon.fields.search;

import com.vaadin.ui.DefaultFieldFactory;
import com.vaadin.ui.Field;
import org.tylproject.vaadin.addon.fields.zoom.ZoomField;

/**
 * Created by evacchi on 10/02/15.
 */
public class DefaultSearchFieldFactory implements SearchFieldFactory {
    public SearchPatternField<?,?,?> createField(Object propertyId, Class<?> propertyType) {

        SearchPatternField f ;
        if (java.lang.Enum.class.isAssignableFrom(propertyType)) {
            f = new SearchPatternComboBox(propertyId, (Class<java.lang.Enum>)propertyType);
        } else {
            f = new SearchPatternTextField(propertyId, propertyType);
        }
        f.setCaption(DefaultFieldFactory.createCaptionByPropertyId(propertyId));
        return f;
    }

    @Override
    public SearchPatternField<?, ?, ?> createField(Object propertyId, Class<?> propertyType, Field<?> originalField) {
        if (originalField == null) throw new IllegalArgumentException("originalField must be non-null");

        SearchPatternField f = createField(propertyId, propertyType);
        //  hack: non-vaadin fields are currently unsupported
        if (originalField instanceof ZoomField) {
            ZoomField<?> zf = (ZoomField<?>) originalField;
            Object targetPropertyId = zf.getZoomDialog().getNestedPropertyId();
        } else
        if (!originalField.getClass().getCanonicalName().startsWith("com.vaadin")) {
            f.setValue("Unsupported Field");
            f.setReadOnly(true);
            f.setEnabled(false);
        }
        return f;
    }
}
