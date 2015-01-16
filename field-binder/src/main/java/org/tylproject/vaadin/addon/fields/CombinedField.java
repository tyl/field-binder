package org.tylproject.vaadin.addon.fields;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Created by evacchi on 16/01/15.
 */
public class CombinedField<T> extends CustomField<T> {
    final CssLayout rootLayout = new CssLayout();
    final TextField textField;
    final Button button;
    final Class<T> type;

    public CombinedField(final TextField textField, final Button button, final Class<T>
            type) {
        this.textField = textField;
        this.button = button;
        this.type = type;

        this.textField.setConverter(type);

        rootLayout.addStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
        rootLayout.addComponents(textField, button);

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
    protected void setInternalValue(T value) {
        textField.setConvertedValue(value);
    }
}
