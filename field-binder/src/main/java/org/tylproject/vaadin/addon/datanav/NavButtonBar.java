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
import org.tylproject.vaadin.addon.datanav.events.NavigationEnabled;

import javax.annotation.Nonnull;

/**
 * ButtonBar for Simple Navigation events.
 */
public class NavButtonBar extends AbstractButtonBar implements NavigationEnabled.Listener, CurrentItemChange.Listener {


    private final Button firstButton = button("first");
    private final Button prevButton = button("prev");
    private final Button nextButton = button("next");
    private final Button lastButton = button("last");

    private final Button[] navButtons = {
            firstButton,
            prevButton,
            nextButton,
            lastButton
    };

    public NavButtonBar(final DataNavigation nav) {
        super(nav);
        Layout buttonLayout = getLayout();

        buttonLayout.addComponent(firstButton);
        buttonLayout.addComponent(prevButton);
        buttonLayout.addComponent(nextButton);
        buttonLayout.addComponent(lastButton);




        firstButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                getNavigation().first();
            }
        });

        nextButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                getNavigation().next();
            }
        });

        prevButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                getNavigation().prev();
            }
        });

        lastButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                getNavigation().last();
            }
        });

        setNavigation(nav);
    }

    @Override
    protected void attachNavigation(@Nonnull DataNavigation nav) {
        nav.addCurrentItemChangeListener(this);
        nav.addNavigationEnabledListener(this);
    }

    @Override
    protected void detachNavigation(@Nonnull DataNavigation nav) {
        nav.removeNavigationEnabledListener(this);
        nav.removeCurrentItemChangeListener(this);
    }


    @Override
    public void navigationEnabled(NavigationEnabled.Event event) {
        updateButtonStatus();
    }

    @Override
    public void currentItemChange(CurrentItemChange.Event event) {
        updateButtonStatus();
    }

    protected void updateButtonStatus() {
        Container.Ordered ctr = getNavigation().getContainer();
        if (ctr == null || !getNavigation().isNavigationEnabled()) {
            disable(navButtons);
            return;
        } else {
            enable(navButtons);
        }

        Object currentId = getNavigation().getCurrentItemId();
        if (currentId == null) {
            disable(navButtons);
            return;
        }

        boolean hasNext = false;
        boolean hasPrev = false;

        if(currentId != null){
            hasNext = null != ctr.nextItemId(currentId);
            hasPrev = null != ctr.prevItemId(currentId);
        }

        nextButton.setEnabled(hasNext);
        prevButton.setEnabled(hasPrev);
        firstButton.setEnabled(hasPrev);
        lastButton.setEnabled(hasNext);
    }

    public Button getFirstButton() {
        return firstButton;
    }

    public Button getLastButton() {
        return lastButton;
    }

    public Button getNextButton() {
        return nextButton;
    }

    public Button getPrevButton() {
        return prevButton;
    }
}
