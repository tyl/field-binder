package org.tylproject.vaadin.addon.crudnav;

import com.vaadin.data.Container;
import com.vaadin.event.Action;
import com.vaadin.event.ShortcutListener;
import com.vaadin.ui.Button;
import org.tylproject.vaadin.addon.crudnav.events.CurrentItemChange;

import javax.annotation.Nonnull;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static com.vaadin.event.ShortcutAction.KeyCode.*;
import static com.vaadin.event.ShortcutAction.ModifierKey.*;

/**
 * Created by evacchi on 19/11/14.
 */
public class KeyBinder implements Action.Handler {

    public static KeyBinder forNavigation(CrudNavigation nav) {
        return new KeyBinder(nav);
    }

    private @Nonnull CrudNavigation nav;

    private final int[] NoModifiers = {};
    private final int[] ShiftModifier = { SHIFT };

    private final NavShortcutListener first = new NavShortcutListener("First", F3, ShiftModifier) {
        @Override
        public void handle(Object sender, Object target) {
            nav().first();
        }
    };
    private final NavShortcutListener prev  = new NavShortcutListener("Prev", F3, NoModifiers) {
        @Override
        public void handle(Object sender, Object target) {
            nav().prev();
        }
    };
    private final NavShortcutListener next  = new NavShortcutListener("Next", F4, NoModifiers) {
        @Override
        public void handle(Object sender, Object target) {
            nav().next();
        }
    };
    private final NavShortcutListener last  = new NavShortcutListener("Last", F4, ShiftModifier) {
        @Override
        public void handle(Object sender, Object target) {
            nav().last();
        }
    };

    private final NavShortcutListener create  = new NavShortcutListener("Create", F10, NoModifiers) {
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


    private final NavShortcutListener[] allActions = {
        first,
        prev,
        next,
        last,
        create,
        edit,
        commit,
        discard
    };

    KeyBinder(final CrudNavigation nav) {
        this.nav = nav;


        attachNavigation(nav);

    }

    public void setNavigation(@Nonnull CrudNavigation nav) {
        detachNavigation(this.nav);
        this.nav = nav;
        attachNavigation(nav);
        updateButtonStatus();
    }

    private CrudNavigation nav() {
        return this.nav;
    }

    private void detachNavigation(@Nonnull CrudNavigation nav) {
        nav.removeCurrentItemChangeListener(buttonBarStatusUpdater);
    }

    private void attachNavigation(@Nonnull CrudNavigation nav) {
        nav.addCurrentItemChangeListener(buttonBarStatusUpdater);
    }

    private void enable(NavShortcutListener... acts) {
        for (NavShortcutListener act: acts) act.enable();
    }
    private void disable(NavShortcutListener... acts) {
        for (NavShortcutListener act: acts) act.disable();
    }

    private void updateButtonStatus() {
        Container.Indexed ctr = nav.getContainer();
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
        public void currentItemChangeListener(CurrentItemChange.Event event) {
            updateButtonStatus();
        }
    };

    @Override
    public Action[] getActions(Object target, Object sender) {
        return allActions;
    }

    @Override
    public void handleAction(Action action, Object sender, Object target) {
        if (action instanceof NavShortcutListener) {
            NavShortcutListener act = (NavShortcutListener) action;
            act.handleAction(sender, target);
            if (act.isEnabled()) updateButtonStatus();
        }
    }



}
