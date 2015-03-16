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
 * The popup is customizable through the {@link #setZoomDialog(ZoomDialog)} method.
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
 * {@link org.tylproject.vaadin.addon.fieldbinder.FieldBinder}.
 *
 * Two modes of execution are supported. The default is FullValue: in this mode,
 * when you select a row in the ZoomWindow, the corresponding bean is
 * set as the <b>value</b> of the field. The propertyId that is given to the zoom dialog
 * is only used as a <b>display value</b>.
 *
 * The other mode is <b>PropertyId</b>, in this case, the propertyId
 * given in the ZoomDialog is both the source of the display value
 * <em>and</em> the value that will be assigned to the ZoomField.
 *
 *
 * See the online developer documentation for details.
 *
 *
 */
public class ZoomField<T> extends CombinedField<T, String, TextField> {

    public static enum Mode {
        /**
         * The value is the complete bean (default)
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

    /**
     * Allows/Disallows the "Select None" button in the ZoomWindow
     *
     * (this only makes sense when the drillDownOnly is NOT enabled!)
     */
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


    /**
     * set the value to show in the embedded TextField ({@link #getBackingField()})
     */
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

    /**
     * Switch the ZoomField to a DrillDown Field. A DrillDown Field
     * is a ZoomField where no selection is possible in the ZoomWindow.
     *
     *
     * It is meant to display the result of a query (e.g., the query
     * that caused the current value to be displayed in this ZoomField).
     *
     * Read-only ZoomFields turn temporarily into DrillDown fields, until
     * they turn back into writable state.
     *
     * In DrillDown state, the button is turned into a
     * {@link com.vaadin.server.FontAwesome#ELLIPSIS_H}
     *
     */
    public ZoomField<T> drillDownOnly() {
        this.drillDownOnly = true;
        getButton().setIcon(FontAwesome.ELLIPSIS_H);
        return this;
    }

    /**
     * Invoked by the {@link ZoomWindow} when it's closed.
     * Depending on the mode of execution, invokes {@link #setValue(Object)}
     * with the entire bean, or the contents of the zoomed Container propertyId
     */
    public void onZoomDialogDismissed() {
        final ZoomDialog zoomDialog = getZoomDialog();
        if (mode == Mode.FullValue) {
            setValue((T) zoomDialog.getSelectedBean());
//            setDisplayValue(zoomDialog.getSelectedValue());
        } else if (mode == Mode.PropertyId) {
            setValue((T) zoomDialog.getSelectedValue());
        } else {
            throw new IllegalStateException("Unknown mode " + mode);
        }
    }

    public void onZoomDialogNone() {
        setValue(null);
    }

    class ButtonClickListener implements Button.ClickListener {
        @Override
        public void buttonClick(Button.ClickEvent event) {
            if (drillDownOnly || isReadOnly()) {
                // if the button is clicked and it is readonly
                // or it is drill-down, then show the DrillDown
                // window decoration (only a close button is shown,
                // selection is not allowed)
                new DrillDownWindow<>(ZoomField.this).show();
            } else {
                // otherwise, display the zoom window,
                // where selection is allowed
                new ZoomWindow<>(ZoomField.this).show();
            }
        }
    }

}
