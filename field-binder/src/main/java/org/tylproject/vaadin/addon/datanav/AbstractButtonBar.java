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

package org.tylproject.vaadin.addon.datanav;

import com.vaadin.ui.*;
import org.tylproject.vaadin.addon.datanav.events.CurrentItemChange;
import org.tylproject.vaadin.addon.datanav.resources.Strings;

import javax.annotation.Nonnull;
import javax.swing.*;
import java.util.ResourceBundle;

/**
 * Created by evacchi on 04/12/14.
 */
public abstract class AbstractButtonBar extends CustomComponent implements DataNavigationBar {

    private final HorizontalLayout buttonLayout = new HorizontalLayout();

    private Panel focusPanel;
    private FocusManager focusManager;
    private @Nonnull DataNavigation navigation;

    protected AbstractButtonBar(DataNavigation navigation) {
        this.navigation = navigation;
        setCompositionRoot(this.getLayout());
        this.setSizeUndefined();
    }



    @Override
    public void setNavigation(@Nonnull DataNavigation nav) {
        detachNavigation(this.navigation);
        this.navigation = nav;
        attachNavigation(nav);
    }

    public DataNavigation getNavigation() {
        return this.navigation;
    }



    @Override
    public Layout getLayout() {
        return buttonLayout;
    }

    protected static final ResourceBundle resourceBundle =
            ResourceBundle.getBundle(Strings.class.getCanonicalName());




    protected abstract void detachNavigation(@Nonnull DataNavigation nav) ;

    protected abstract void attachNavigation(@Nonnull DataNavigation nav) ;


    protected Button button(String labelIdentifier) {
        return new Button(resourceBundle.getString(labelIdentifier));
    }
    protected void enable(Button... btns) {
        for (Button btn: btns) btn.setEnabled(true);
    }
    protected void disable(Button... btns) {
        for (Button btn: btns) btn.setEnabled(false);
    }

}
