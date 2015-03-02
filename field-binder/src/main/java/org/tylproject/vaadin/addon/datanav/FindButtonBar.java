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

import com.vaadin.data.Container;
import com.vaadin.ui.Button;
import com.vaadin.ui.Layout;
import org.tylproject.vaadin.addon.datanav.events.CurrentItemChange;
import org.tylproject.vaadin.addon.datanav.events.FindEnabled;

import javax.annotation.Nonnull;

/**
 * Created by evacchi on 04/12/14.
 */
public class FindButtonBar extends AbstractButtonBar implements FindEnabled.Listener, CurrentItemChange.Listener {

    final Button btnClearToFind = button("clearToFind");
    final Button btnFind = button("find");


    private final Button[] findButtons = {
            btnClearToFind,
            btnFind
    };

    public FindButtonBar() { this(new BasicDataNavigation()); }

    public FindButtonBar(@Nonnull DataNavigation nav) {
        super(nav);

        Layout buttonLayout = getLayout();
        buttonLayout.addComponents(btnClearToFind, btnFind);

        btnClearToFind.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                getNavigation().clearToFind();
                enable(btnFind);
            }
        });

        btnFind.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                getNavigation().find();
            }
        });

        setNavigation(nav);
    }


    @Override
    protected void attachNavigation(@Nonnull DataNavigation nav) {
        nav.addCurrentItemChangeListener(this);
        nav.addFindEnabledListener(this);
    }

    @Override
    public void currentItemChange(CurrentItemChange.Event event) {
        updateButtonStatus();
    }

    @Override
    protected void detachNavigation(@Nonnull DataNavigation nav) {
        nav.removeFindEnabledListener(this);
        nav.removeCurrentItemChangeListener(this);
    }

    @Override
    public void findEnabled(FindEnabled.Event event) {
        if (event.isFindEnabled()) {
            // default to disabling clearToFind
            updateClearToFind(event.getSource().getContainer());
        } else {
            disable(findButtons);
        }
    }

    private void updateClearToFind(Container container) {
        disable(btnFind);
        disable(btnClearToFind);
        // if it is filterable
        if (container instanceof Container.Filterable) {
            Container.Filterable filterable = (Container.Filterable) container;
            if (filterable.size() > 0 // and it contains values
                    || !filterable.getContainerFilters().isEmpty()) { // when no filters have been applied
                enable(btnClearToFind);
            }

        }
    }

    protected void updateButtonStatus() {

        if (!getNavigation().isFindEnabled()) {
            disable(findButtons);
            return;
        }

        if (getNavigation().isClearToFindMode()) {
            enable(btnFind);
            return;
        }


        if (getNavigation().getContainer() == null) {
            disable(findButtons);
        } else {
            updateClearToFind(getNavigation().getContainer());
        }
        
    }

    public Button getClearToFindButton() {
        return btnClearToFind;
    }

    public Button getFindButton() {
        return btnFind;
    }
}
