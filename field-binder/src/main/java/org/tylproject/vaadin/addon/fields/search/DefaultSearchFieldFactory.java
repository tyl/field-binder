package org.tylproject.vaadin.addon.fields.search;

import com.vaadin.data.Container;
import com.vaadin.ui.DefaultFieldFactory;
import com.vaadin.ui.Field;
import org.tylproject.vaadin.addon.fields.zoom.ZoomField;

public class DefaultSearchFieldFactory implements SearchFieldFactory {

    /**
     * In this implementation, {@link com.vaadin.ui.ComboBox}es are distinguished
     * from other Fields.
     *
     * ComboBoxes get their own {@link SearchPatternComboBox};
     * other fields are rendered using a {@link SearchPatternTextField}.
     */
    public SearchPatternField<?,?> createField(Object propertyId, Class<?> propertyType) {

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
    public SearchPatternField<?, ?> createField(Object propertyId, Class<?> propertyType, Container.Filterable container) {

        SearchPatternField f ;
        if (java.lang.Enum.class.isAssignableFrom(propertyType)) {
            f = new SearchPatternComboBox(propertyId, propertyType, container);
        } else {
            f = new SearchPatternTextField(propertyId, propertyType, container);
        }
        f.setCaption(DefaultFieldFactory.createCaptionByPropertyId(propertyId));
        return f;
    }

    /**
     * Properly configures search fields, depending on the settings/type of the given originalField
     */
    @Override
    public SearchPatternField<?, ?> createField(Object propertyId, Class<?> propertyType, Field<?> originalField) {
        if (originalField == null) throw new IllegalArgumentException("originalField must be non-null");

        SearchPatternField f = createField(propertyId, propertyType);
        //  hack: non-vaadin fields are currently unsupported
        if (originalField instanceof ZoomField) {
            ZoomField<?> zf = (ZoomField<?>) originalField;
            Object containerPropertyId = zf.getZoomDialog().getContainerPropertyId();
            ZoomField.Mode mode = zf.getMode();
            if (mode == ZoomField.Mode.PropertyId) {
                // do nothing, it's already fine
            } else if (mode == ZoomField.Mode.FullValue) {
                // should filter using a nested property
                // must extract correct propertyType
                f = createField(propertyId + "." + containerPropertyId, ((ZoomField<?>) originalField).getZoomDialog().getContainerPropertyType());
            } else {
                // fallback
                f = createField(propertyId, propertyType);
                unsupportedField(f);
            }



        } else

        // heuristic to ignore unknown fields
        if (!originalField.getClass().getCanonicalName().startsWith("com.vaadin")) {
            unsupportedField(f);
        }
        return f;
    }

    private void unsupportedField(SearchPatternField f) {
        f.setValue("Unsupported Field");
        f.setReadOnly(true);
        f.setEnabled(false);
    }
}
