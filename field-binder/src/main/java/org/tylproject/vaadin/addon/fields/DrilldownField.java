package org.tylproject.vaadin.addon.fields;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.TextField;

/**
 * Created by evacchi on 16/01/15.
 */
public class DrilldownField<T> extends CombinedField<T> {
    public DrilldownField(TextField textField, Class<T> type) {
        super(textField, new Button(FontAwesome.ELLIPSIS_H), type);
    }
    public DrilldownField(Class<T> type) {
        this(new TextField(), type);
    }
}
