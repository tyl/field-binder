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

import javax.annotation.Nonnull;

/**
 * Created by evacchi on 19/11/14.
 */
public class ButtonBar extends CustomComponent implements DataNavigationBar {


    private final NavButtonBar navBar;
    private final CrudButtonBar crudBar;
    private final FindButtonBar findBar;
    private final HorizontalLayout buttonLayout = new HorizontalLayout();
    private final DataNavigation nav;

    public static ButtonBar forNavigation(DataNavigation nav) {
        return new ButtonBar(nav);
    }

    public ButtonBar(DataNavigation nav) {
        setCompositionRoot(getLayout());

        this.nav = nav;

        this.navBar = new NavButtonBar(nav);
        this.crudBar = new CrudButtonBar(nav);
        this.findBar = new FindButtonBar(nav);

        getLayout().addComponents(
                navBar,
                crudBar,
                findBar);

    }

    public void setNavigation(@Nonnull DataNavigation nav) {
        navBar.setNavigation(nav);
        crudBar.setNavigation(nav);
        findBar.setNavigation(nav);
    }

    public DataNavigation getNavigation() {
        return nav;
    }

    public Layout getLayout() {
        return buttonLayout;
    }

    public NavigationLabel buildNavigationLabel() {
        return new NavigationLabel(this.getNavigation());
    }

    public CrudButtonBar getCrudBar() {
        return crudBar;
    }

    public FindButtonBar getFindBar() {
        return findBar;
    }

    public NavButtonBar getNavigationBar() {
        return navBar;
    }
}



