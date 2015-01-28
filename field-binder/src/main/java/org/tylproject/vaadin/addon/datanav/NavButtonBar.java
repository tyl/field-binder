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
import org.tylproject.vaadin.addon.datanav.events.NavigationEnabled;

import javax.annotation.Nonnull;

/**
 * Created by evacchi on 04/12/14.
 */
public class NavButtonBar extends AbstractButtonBar {


    private final Button btnFirst = button("first");
    private final Button btnPrev  = button("prev");
    private final Button btnNext  = button("next");
    private final Button btnLast  = button("last");

    private final Button[] navButtons = {
            btnFirst,
            btnPrev,
            btnNext,
            btnLast
    };

    public NavButtonBar(final DataNavigation nav) {
        super(nav);
        Layout buttonLayout = getLayout();

        buttonLayout.addComponent(btnFirst);
        buttonLayout.addComponent(btnPrev);
        buttonLayout.addComponent(btnNext);
        buttonLayout.addComponent(btnLast);




        btnFirst.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                getNavigation().first();
            }
        });

        btnNext.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                getNavigation().next();
            }
        });

        btnPrev.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                getNavigation().prev();
            }
        });

        btnLast.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                getNavigation().last();
            }
        });

        setNavigation(nav);
    }

    @Override
    protected void attachNavigation(@Nonnull DataNavigation nav) {
        super.attachNavigation(nav);
        nav.addNavigationEnabledListener(buttonEnabler);
    }

    @Override
    protected void detachNavigation(@Nonnull DataNavigation nav) {
        nav.removeNavigationEnabledListener(buttonEnabler);
        super.detachNavigation(nav);
    }

    NavigationEnabled.Listener buttonEnabler = new NavigationEnabled.Listener() {
        @Override
        public void navigationEnabled(NavigationEnabled.Event event) {
            updateButtonStatus();
        }
    };

    @Override
    protected  void updateButtonStatus() {
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

        btnNext.setEnabled(hasNext);
        btnPrev.setEnabled(hasPrev);
        btnFirst.setEnabled(hasPrev);
        btnLast.setEnabled(hasNext);
    }
}
