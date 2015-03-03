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
import com.vaadin.event.Action;
import org.tylproject.vaadin.addon.datanav.events.CurrentItemChange;

import javax.annotation.Nonnull;

import java.util.*;

import static com.vaadin.event.ShortcutAction.KeyCode.*;
import static com.vaadin.event.ShortcutAction.ModifierKey.*;

/**
 * Created by evacchi on 19/11/14.
 */
public class KeyBinder implements Action.Handler {

    private DataNavigation nav;

    private final int[] NoModifiers = {};
    private final int[] ShiftModifier = { SHIFT };

    private final NavShortcutListener first = new NavShortcutListener("First", NUM1, ALT) {
        @Override
        public void handle(Object sender, Object target) {
            nav().first();
        }
    };
    private final NavShortcutListener prev  = new NavShortcutListener("Prev", NUM2, ALT) {
        @Override
        public void handle(Object sender, Object target) {
            nav().prev();
        }
    };
    private final NavShortcutListener next  = new NavShortcutListener("Next", NUM3, ALT) {
        @Override
        public void handle(Object sender, Object target) {
            nav().next();
        }
    };
    private final NavShortcutListener last  = new NavShortcutListener("Last", NUM4, ALT) {
        @Override
        public void handle(Object sender, Object target) {
            nav().last();
        }
    };

    private final NavShortcutListener create  = new NavShortcutListener("Create", NUM5, ALT) {
        @Override
        public void handle(Object sender, Object target) {
            nav().create();
        }
    };
    private final NavShortcutListener edit    = new NavShortcutListener("Edit", F2, NoModifiers) {
        @Override
        public void handle(Object sender, Object target) {
            nav().edit();
        }
    };
    private final NavShortcutListener remove    = new NavShortcutListener("Remove", DELETE, NoModifiers) {
        @Override
        public void handle(Object sender, Object target) {
            nav().edit();
        }
    };
    private final NavShortcutListener commit  = new NavShortcutListener("Commit", S, new int[] { CTRL }) {
        @Override
        public void handle(Object sender, Object target) {
            nav().commit();
        }
    };
    private final NavShortcutListener discard = new NavShortcutListener("Discard", ESCAPE, NoModifiers) {
        @Override
        public void handle(Object sender, Object target) {
            nav().discard();
        }
    };
    private final NavShortcutListener clearToFind = new NavShortcutListener("Clear to Find", F, ALT) {
        @Override
        public void handle(Object sender, Object target) {
            nav().clearToFind();
            KeyBinder.this.enable(find);
        }
    };
    private final NavShortcutListener find = new NavShortcutListener("Find", ENTER) {
        @Override
        public void handle(Object sender, Object target) {
            if (nav().isClearToFindMode()) nav().find();
            KeyBinder.this.disable(find);
        }
    };

            
    private final NavShortcutListener[] allActions = {
        // nav
        first,
        prev,
        next,
        last,
        // crud
        create,
        edit,
        remove,
        commit,
        discard,
        // find
        clearToFind,
        find
    };

    public KeyBinder() {}
    public KeyBinder(@Nonnull DataNavigation nav) {
        this.nav = nav;
    }

    public void setNavigation(@Nonnull DataNavigation nav) {
        detachNavigation(this.nav);
        this.nav = nav;
        attachNavigation(nav);
        updateButtonStatus();
    }

    private DataNavigation nav() {
        return this.nav;
    }

    private void detachNavigation(DataNavigation nav) {
        if (nav == null) return;
        nav.removeCurrentItemChangeListener(buttonBarStatusUpdater);
    }

    private void attachNavigation(@Nonnull DataNavigation nav) {
        nav.addCurrentItemChangeListener(buttonBarStatusUpdater);
    }

    private void enable(NavShortcutListener... acts) {
        for (NavShortcutListener act: acts) act.enable();
    }
    private void disable(NavShortcutListener... acts) {
        for (NavShortcutListener act: acts) act.disable();
    }

    private void updateButtonStatus() {
        Container.Ordered ctr = nav.getContainer();
        if (ctr == null) {
            disable(allActions);
            return;
        } else {
            enable(allActions);
        }

        Object currentId = nav.getCurrentItemId();

        boolean hasNext = false;
        boolean hasPrev = false;

        if(currentId != null){
            hasNext = null != ctr.nextItemId(currentId);
            hasPrev = null != ctr.prevItemId(currentId);
        }

        next.setEnabled(hasNext);
        prev.setEnabled(hasPrev);
        first.setEnabled(hasPrev);
        last.setEnabled(hasNext);
    }

    CurrentItemChange.Listener buttonBarStatusUpdater = new CurrentItemChange.Listener() {
        @Override
        public void currentItemChange(CurrentItemChange.Event event) {
            updateButtonStatus();
        }
    };

    @Override
    public Action[] getActions(Object target, Object sender) {
        return allActions;
    }

    @Override
    public void handleAction(Action action, Object sender, Object target) {
        if (action instanceof NavShortcutListener && Arrays.asList(allActions).contains(action)) {
            NavShortcutListener act = (NavShortcutListener) action;
            act.handleAction(sender, target);
            if (act.isEnabled()) updateButtonStatus();
        }
    }

}
