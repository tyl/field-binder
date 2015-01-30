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

import com.vaadin.event.ShortcutAction;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.tylproject.vaadin.addon.datanav.resources.Strings;
import org.tylproject.vaadin.addon.fields.zoom.ZoomField;

import java.util.ResourceBundle;

/**
 * Created by evacchi on 27/01/15.
 */
public class DrillDownWindow<T> extends Window implements Button.ClickListener {
    private final ZoomField<T> field;

    protected static final ResourceBundle resourceBundle =
            ResourceBundle.getBundle(Strings.class.getCanonicalName());


    private final Label spacer = new Label();
    private final Button btnClose = new Button(resourceBundle.getString("close"));

    private final Panel content = new Panel();

    private final HorizontalLayout buttonBar = new HorizontalLayout(spacer, btnClose);
    private final VerticalLayout rootLayout = new VerticalLayout(content, buttonBar);


    public DrillDownWindow(ZoomField<T> field) {
        super(field.getCaption());

        makeLayout(field);

        setContent(rootLayout);
        setWidth("1200px");
        setHeight("400px");

        this.field = field;

        btnClose.addClickListener(this);
        btnClose.setClickShortcut(ShortcutAction.KeyCode.ESCAPE);
        btnClose.setClickShortcut(ShortcutAction.KeyCode.ENTER);
    }

    public Layout makeLayout(ZoomField<T> field) {

        btnClose.setStyleName(ValoTheme.BUTTON_PRIMARY);
        content.addStyleName(ValoTheme.PANEL_BORDERLESS);

        Component dialogContents = field.getZoomDialog();

        content.setContent(dialogContents);
        content.setSizeFull();

        buttonBar.setStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);
        buttonBar.setWidth("100%");
        buttonBar.setSpacing(true);
        buttonBar.setExpandRatio(spacer, 1);

        rootLayout.setSizeFull();
        rootLayout.setExpandRatio(content, 1);
        rootLayout.setMargin(new MarginInfo(true, false, true, false));

        return rootLayout;
    }

    public void show() {
        UI.getCurrent().addWindow(this);
        center();
        focus();
    }

    @Override
    public void buttonClick(Button.ClickEvent event) {
        this.close();
    }

}
