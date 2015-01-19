package org.tylproject.vaadin.addon.fields;

import com.vaadin.data.Buffered;
import com.vaadin.data.Property;
import com.vaadin.data.Validator;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.util.Collection;

/**
 * It is also possible to override validate(), setInternalValue(), commit(),
 * setPropertyDataSource, isEmpty() and other methods to implement different
 * functionalities in the field. Methods overriding setInternalValue() should call the
 * superclass method.
 */
public class CombinedField<T> extends FieldDecorator<T, TextField, String> {
    final CssLayout rootLayout = new CssLayout();
    final TextField textField;
    final Button button;
    final Class<T> type;

    public CombinedField(final TextField textField, final Button button, final Class<T>
            type) {
        super(textField);
        this.textField = textField;
        this.button = button;
        this.type = type;

        this.textField.setConverter(type);

        rootLayout.addStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
        rootLayout.addComponents(textField, button);

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
        Object value = textField.getConvertedValue();
        // special-casing bug in TextField
        if (("").equals(value)) return null;
        else return (T) value;
    }

    @Override
    public void setValue(T newValue) throws ReadOnlyException {
        textField.setConvertedValue(newValue);
    }

}
