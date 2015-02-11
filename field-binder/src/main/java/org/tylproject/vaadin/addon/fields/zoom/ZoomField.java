/*
 * Copyright (c) 2015 - Tyl Consulting s.a.s.
 *
 *   Authors: Edoardo Vacchi
 *   Contributors: Marco Pancotti, Daniele Zonca
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.tylproject.vaadin.addon.fields.zoom;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import org.tylproject.vaadin.addon.fields.CombinedField;
import org.tylproject.vaadin.addon.fields.drilldown.DrillDownWindow;

/**
 * Decorates a TextField with a "zoom" button.
 *
 * The zoom button opens a customizable popup from where the user may select an element.
 * The selected element will be set as the value of the field.
 *
 * The popup is customizable through the {@link #setZoomDialog(ZoomDialog)}
 * and {@link #withZoomDialog(ZoomDialog)} methods.
 *
 * The ZoomField can be seen as an "extended" ComboBox where a
 * popup window is shown instead of a small menu. Depending on the user's choice
 * the popup window may do different things. The popup window is automatically generated
 * from an instance of the {@link org.tylproject.vaadin.addon.fields.zoom.ZoomDialog} interface.
 *
 * The default implementations are {@link org.tylproject.vaadin.addon.fields.zoom.TableZoomDialog}.
 * and {@link org.tylproject.vaadin.addon.fields.zoom.GridZoomDialog}, which is backed
 * by Vaadin's Grid widget.
 *
 * <code>
 * <pre>
 * Grid myGrid = new Grid(someContainer);
 * ZoomField<Person> firstName =
 *       new ZoomField<>(Person.class)
 *           .withZoomDialog(new GridZoomDialog(myGrid, "firstName"));
 * </pre>
 * </code>
 *
 * Most often, you will probably get an instance of a ZoomField from a
 * {@link org.tylproject.vaadin.addon.fieldbinder.FieldBinder}. <i>E.g,:</i>
 *
 * <code>
 * <pre>
 *
 * ZoomField<Person> binder.zoomField("firstName")
 *           .withZoomDialog(new GridZoomDialog(myGrid)).build();
 * </pre>
 * </code>
 *
 *
 */
public class ZoomField<T> extends CombinedField<T, String, TextField> {

    public static enum Mode {
        /**
         * The value is the complete bean
         */
        FullValue,
        /**
         * The value is only a property <i>inside</i> the bean
         */
        PropertyId;
    }

    private boolean nullSelectionEnabled = true;
    private ZoomDialog dialog;
    private boolean drillDownOnly = false;
    private Mode mode = Mode.FullValue;

    public ZoomField(TextField field, Class<T> type) {
        super(field, new Button(FontAwesome.SEARCH), type);
        getBackingField().setReadOnly(true);
        getButton().addClickListener(new ButtonClickListener());
    }

    public ZoomField(Class<T> type) {
        this(new TextField(), type);
        getBackingField().setNullRepresentation("");
    }

    public void setNullSelectionEnabled(boolean allowed) {
        this.nullSelectionEnabled = allowed;
    }

    public boolean isNullSelectionEnabled() {
        return nullSelectionEnabled;
    }

    public ZoomDialog getZoomDialog() {
        return dialog;
    }

    public void setZoomDialog(ZoomDialog dialog) {
        this.dialog = dialog;
    }


    public void setDisplayValue(Object displayValue) {
        getBackingField().setReadOnly(false);

        getBackingField().setValue(displayValue == null? null: displayValue.toString());

        getBackingField().setReadOnly(true);
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }
    public Mode getMode() {
        return mode;
    }


    @Override
    public void setReadOnly(boolean readOnly) {
        super.setReadOnly(readOnly);
        getBackingField().setReadOnly(true);
        getZoomDialog().setReadOnly(readOnly);
        if (readOnly) {
            getButton().setIcon(FontAwesome.ELLIPSIS_H);
        } else {
            getButton().setIcon(FontAwesome.SEARCH);
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        // the button is always enabled
        getButton().setEnabled(true);
    }

    public ZoomField<T> drillDownOnly() {
        this.drillDownOnly = true;
        getButton().setIcon(FontAwesome.ELLIPSIS_H);
        return this;
    }

    public void onZoomDialogDismissed() {
        final ZoomDialog zoomDialog = getZoomDialog();
        if (mode == Mode.FullValue) {
            setValue((T) zoomDialog.getSelectedBean());
            setDisplayValue(zoomDialog.getSelectedValue());
        } else if (mode == Mode.PropertyId) {
            setValue((T) zoomDialog.getSelectedValue());
        } else {
            throw new IllegalStateException("Unknown mode " + mode);
        }
    }

    class ButtonClickListener implements Button.ClickListener {
        @Override
        public void buttonClick(Button.ClickEvent event) {
            if (drillDownOnly || isReadOnly()) {
                new DrillDownWindow<>(ZoomField.this).show();
            } else {
                new ZoomWindow<>(ZoomField.this).show();
            }
        }
    }

}
