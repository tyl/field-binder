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

import com.vaadin.event.ShortcutAction;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.tylproject.vaadin.addon.datanav.resources.Strings;

import java.util.ResourceBundle;

/**
 * Created by evacchi on 27/01/15.
 */
public class ZoomWindow<T> extends com.vaadin.ui.Window implements Button.ClickListener {
    private final ZoomField<T> field;

    protected static final ResourceBundle resourceBundle =
            ResourceBundle.getBundle(Strings.class.getCanonicalName());


    private final Label spacer = new Label();
    private final Button btnSelect = new Button(resourceBundle.getString("select"));
    private final Button btnSelectNone = new Button(resourceBundle.getString("selectNone"));
    private final Button btnCancel = new Button(resourceBundle.getString("cancel"));

    private final Panel content = new Panel();

    private final HorizontalLayout buttonBar = new HorizontalLayout(spacer);
    private final VerticalLayout rootLayout = new VerticalLayout(content, buttonBar);


    public ZoomWindow(ZoomField<T> field) {
        super(field.getCaption());

        makeLayout(field);

        setContent(rootLayout);
        setWidth("1200px");
        setHeight("400px");

        this.field = field;

        btnSelect.addClickListener(this);
        btnSelectNone.addClickListener(this);
        btnCancel.addClickListener(this);

        btnCancel.setClickShortcut(ShortcutAction.KeyCode.ESCAPE);
        btnSelect.setClickShortcut(ShortcutAction.KeyCode.ENTER);



    }

    public Layout makeLayout(ZoomField<T> field) {

        btnSelect.addStyleName(ValoTheme.BUTTON_PRIMARY);
        content.addStyleName(ValoTheme.PANEL_BORDERLESS);

        Component dialogContents = field.getZoomDialog().getDialogContents();

        content.setContent(dialogContents);
        content.setSizeFull();
//            rootLayout.setMargin(true);


        rootLayout.setSizeFull();
        rootLayout.setExpandRatio(content, 1);
        rootLayout.setMargin(new MarginInfo(true, false, true, false));

        return rootLayout;
    }

    public void show() {

        buttonBar.removeAllComponents();

        if (field.isNullSelectionEnabled()) {
            buttonBar.addComponents(spacer, btnSelect, btnSelectNone, btnCancel);
            btnSelectNone.setClickShortcut(ShortcutAction.KeyCode.ENTER,
                    ShortcutAction.ModifierKey.SHIFT);
        } else {
            buttonBar.addComponents(spacer, btnSelect, btnCancel);
        }



        if (field.isNullSelectionEnabled()) {
            buttonBar.addComponents(btnSelect, btnSelectNone, btnCancel);
            btnSelectNone.setClickShortcut(ShortcutAction.KeyCode.ENTER,
                    ShortcutAction.ModifierKey.SHIFT);
        } else {
            buttonBar.addComponents(spacer, btnSelect, btnCancel);
        }

        buttonBar.setStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);
        buttonBar.setWidth("100%");
        buttonBar.setSpacing(true);
        buttonBar.setExpandRatio(spacer, 1);

        field.getZoomDialog().show(this.field.getValue());
        UI.getCurrent().addWindow(this);
        center();
        focus();
    }

    @Override
    public void buttonClick(Button.ClickEvent event) {
        if (event.getSource() == btnSelect) {
            final ZoomDialog zd = field.getZoomDialog();
            zd.dismiss();
            field.onZoomDialogDismissed();
        } else
        if (event.getSource() == btnSelectNone) {
            field.setValue(null);
        }
        this.close();
    }
}