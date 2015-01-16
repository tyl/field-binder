package org.tylproject.vaadin.addon.fields;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.TextField;

/**
 * Created by evacchi on 16/01/15.
 */
public class ZoomField<T> extends CombinedField<T> {
    public ZoomField(TextField textField, Class<T> type) {
        super(textField, new Button(FontAwesome.SEARCH), type);
    }
    public ZoomField(Class<T> type) {
        this(new TextField(), type);
    }
}
