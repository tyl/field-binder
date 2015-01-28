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

package org.tylproject.vaadin.addon.fields.drilldown;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.TextField;
import org.tylproject.vaadin.addon.fields.CombinedField;
import org.tylproject.vaadin.addon.fields.zoom.ZoomDialog;

/**
 * Decorates a TextField with a "zoom" button.
 *
 * The DrillDown field is a simplified ZoomField where no selection can be done
 * inside the popup. The popup is supposed to be only a view over details
 * that relate to the DrillDown field contents.
 *
 * Because the DrillDown field is basically a restricted
 * {@link org.tylproject.vaadin.addon.fields.zoom.ZoomField},
 * the popup window is still generate from a
 * {@link org.tylproject.vaadin.addon.fields.zoom.ZoomDialog} instance.
 *
 * <h3>Example Usage</h3>
 *
 * Usage is close to {@link org.tylproject.vaadin.addon.fields.zoom.ZoomField}.
 * For instance:
 *
 * <code>
 * <pre>
 * Grid myGrid = new Grid(someContainer);
 * DrillDownField<Person> firstName =
 *       new DrillDownField<>(Person.class)
 *           .withDrillDownDialog(new GridZoomDialog(myGrid, "firstName"));
 * </pre>
 * </code>
 *
 *
 * @see org.tylproject.vaadin.addon.fields.zoom.ZoomField
 *
 */
public class DrillDownField<T> extends CombinedField<T, String, TextField> {
    private ZoomDialog<T> dialog;

    public DrillDownField(TextField field, Class<T> type) {
        super(field, new Button(FontAwesome.ELLIPSIS_H), type);
        getButton().addClickListener(new ButtonClickListener<>(this));
    }

    public DrillDownField(Class<T> type) {
        this(new TextField(), type);
        getBackingField().setNullRepresentation("");
    }

    public ZoomDialog<T> getDrillDownDialog() {
        return dialog;
    }

    public void setDrillDownDialog(ZoomDialog<T> dialog) {
        this.dialog = dialog;
    }

    /**
     * "fluent" alias to {@link #setDrillDownDialog(ZoomDialog)}
     */
    public DrillDownField<T> withDrillDownDialog(ZoomDialog<T> dialog) {
        this.setDrillDownDialog(dialog);
        return this;
    }

    static class ButtonClickListener<T> implements Button.ClickListener {
        final DrillDownField<T> field;

        public ButtonClickListener(DrillDownField<T> field) {
            this.field = field;
        }

        @Override
        public void buttonClick(Button.ClickEvent event) {
            new DrillDownWindow<>(field).show();
        }
    }
}
