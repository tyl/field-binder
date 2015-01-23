package org.tylproject.vaadin.addon.fields;

import com.vaadin.data.Buffered;
import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.data.Validator;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.tylproject.vaadin.addon.fieldbinder.behavior.DefaultFilterFactory;
import org.tylproject.vaadin.addon.fieldbinder.behavior.FilterFactory;

import java.util.Collection;

/**
 * It is also possible to override validate(), setInternalValue(), commit(),
 * setPropertyDataSource, isEmpty() and other methods to implement different
 * functionalities in the field. Methods overriding setInternalValue() should call the
 * superclass method.
 */
public class CombinedField<T, FT, F extends AbstractField<FT>> extends FieldDecorator<T, FT, F> {
    final private CssLayout rootLayout = new CssLayout();
    final private Button button;
    final private Class<T> type;

    public CombinedField(final F field, final Button button, final Class<T> type) {
        super(field);
        this.button = button;
        this.type = type;

        this.getBackingField().setConverter(type);

        rootLayout.addStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
        rootLayout.addComponents(field, button);

    }

    public Button getButton() {
        return button;
    }

    @Override
    protected Component initContent() {
        return rootLayout;
    }

    @Override
    public Class<? extends T> getType() {
        return type;
    }

    @Override
    public T getValue() {
        Object value = getBackingField().getConvertedValue();
        // special-casing bug in TextField
        if (("").equals(value)) return null;
        else return (T) value;
    }

    @Override
    public void setValue(T newValue) throws ReadOnlyException {
        getBackingField().setConvertedValue(newValue);
    }

}
