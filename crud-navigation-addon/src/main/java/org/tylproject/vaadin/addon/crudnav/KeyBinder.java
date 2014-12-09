package org.tylproject.vaadin.addon.crudnav;

import com.vaadin.data.Container;
import com.vaadin.event.Action;
import com.vaadin.event.ShortcutListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import org.tylproject.vaadin.addon.crudnav.events.CurrentItemChange;

import javax.annotation.Nonnull;

import java.util.*;

import static com.vaadin.event.ShortcutAction.KeyCode.*;
import static com.vaadin.event.ShortcutAction.ModifierKey.*;

/**
 * Created by evacchi on 19/11/14.
 */
public class KeyBinder implements Action.Handler {

    private List<? extends Component.Focusable> focusables;

    public static KeyBinder forNavigation(CrudNavigation nav) {
        return new KeyBinder(nav);
    }

    private @Nonnull CrudNavigation nav;

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

    private final NavShortcutListener clearToFind = new NavShortcutListener("Clear to Find", NUM9, ALT) {
        @Override
        public void handle(Object sender, Object target) {
            nav().clearToFind();
        }
    };
    private final NavShortcutListener find = new NavShortcutListener("Find", NUM0, ALT) {
        @Override
        public void handle(Object sender, Object target) {
            if (nav().isClearToFindMode()) nav().find();
        }
    };

    private final NavShortcutListener tabber = new NavShortcutListener("Focus", TAB) {
        @Override
        protected void handle(Object sender, Object target) {
            List<? extends Component.Focusable> focusables = getFocusables();
            int indexOf = focusables.indexOf(target);
            if (indexOf < 0 || indexOf == focusables.size() - 1) {
                focusables.get(0).focus();
            } else {
                focusables.get(indexOf + 1).focus();
            }
        }
    };

    private final NavShortcutListener tabberShift = new NavShortcutListener("Focus", TAB, SHIFT) {
        @Override
        protected void handle(Object sender, Object target) {
            List<? extends Component.Focusable> focusables = getFocusables();
            int indexOf = focusables.indexOf(target);
            if (indexOf == 0) {
                focusables.get(focusables.size() - 1).focus();
            } else {
                focusables.get(indexOf - 1).focus();
            }
        }
    };


//    private final NavShortcutListener tabGroupCycle = new NavShortcutListener("Focus", ALT, ARROW_DOWN) {
//        @Override
//        protected void handle(Object sender, Object target) {
//            List<? extends Component.Focusable> focusables = getFocusables();
//            int indexOf = focusables.indexOf(target);
//            if (indexOf == 0) {
//                focusables.get(focusables.size() - 1).focus();
//            } else {
//                focusables.get(indexOf - 1).focus();
//            }
//        }
//    };

            
            
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
        find,

        //tab navigation
        tabber,tabberShift
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
        if (action instanceof NavShortcutListener) {
            NavShortcutListener act = (NavShortcutListener) action;
            act.handleAction(sender, target);
            if (act.isEnabled()) updateButtonStatus();
        }
    }

    public void constrainTab(Field<?>... targets) {
        this.focusables = Arrays.asList(targets);
    }

    public void constrainTab(Collection<Field<?>> targets) {
        this.focusables = new ArrayList<Component.Focusable>(targets);
    }


    public List<? extends Component.Focusable> getFocusables() {
        return focusables;
    }
}
