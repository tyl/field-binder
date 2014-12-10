package org.tylproject.vaadin.addon.masterdetail;

import com.vaadin.event.Action;
import com.vaadin.event.MouseEvents;
import com.vaadin.ui.*;
import org.tylproject.vaadin.addon.crudnav.CrudNavigation;
import org.tylproject.vaadin.addon.crudnav.KeyBinder;
import org.tylproject.vaadin.addon.crudnav.NavShortcutListener;

import java.util.*;

import static com.vaadin.event.ShortcutAction.KeyCode.TAB;
import static com.vaadin.event.ShortcutAction.ModifierKey.ALT;
import static com.vaadin.event.ShortcutAction.ModifierKey.SHIFT;

/**
 * Created by evacchi on 26/11/14.
 */
public class FocusManager implements Action.Handler {
    public static final String FOCUS = "masterdetail-focus";


    private final FocusManager.State currentState = new State();
    private final KeyBinder keyBinder = new KeyBinder();

    class State {
        private FocusGroup currentFocusGroup;
        private List<FocusGroup> focusGroups = new ArrayList<>();

        public FocusGroup getCurrentFocusGroup() {
            return currentFocusGroup;
        }

        public void setCurrentFocusGroup(FocusGroup currentFocusGroup) {
            this.currentFocusGroup = currentFocusGroup;
        }

        public List<FocusGroup> getFocusGroups() {
            return focusGroups;
        }

        public void setFocusGroups(List<FocusGroup> focusGroups) {
            this.focusGroups = focusGroups;
        }
    }



    private final NavShortcutListener tabber = new NavShortcutListener("Focus", TAB) {
        @Override
        protected void handle(Object sender, Object target) {
            List<? extends Component.Focusable> focusables = getCurrentFocusGroup().getFocusables();
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
            List<? extends Component.Focusable> focusables = getCurrentFocusGroup().getFocusables();
            int indexOf = focusables.indexOf(target);
            if (indexOf == 0) {
                focusables.get(focusables.size() - 1).focus();
            } else {
                focusables.get(indexOf - 1).focus();
            }
        }
    };

    private final NavShortcutListener tabGroupCycle = new NavShortcutListener("Focus", /* full stop */190, ALT) {


        @Override
        protected void handle(Object sender, Object target) {
            int indexOf = getFocusGroups().indexOf(getCurrentFocusGroup());
            if (indexOf < 0 || indexOf == getFocusGroups().size() - 1) {
                setCurrentFocusGroup(getFocusGroups().get(0));
            } else {
                setCurrentFocusGroup(getFocusGroups().get(indexOf + 1));
            }
            focusCurrentGroup();
        }
    };

    private final NavShortcutListener[] allActions = {
            tabber, tabberShift, tabGroupCycle
    };

    public FocusManager() {}

    public KeyBinder getKeyBinder() {
        return this.keyBinder;
    }

    public void setCurrentFocusGroup(FocusGroup currentFocusGroup) {
        this.currentState.setCurrentFocusGroup(currentFocusGroup);
    }

    public List<FocusGroup> getFocusGroups() {
        return currentState.getFocusGroups();
    }


    public void focusCurrentGroup() {
        for (FocusGroup fg: getFocusGroups()) fg.unfocus();
        getCurrentFocusGroup().focus();
        if (getCurrentFocusGroup().getNavigation() != null) {
            getKeyBinder().setNavigation(getCurrentFocusGroup().getNavigation());
        }
    }

    public FocusGroup getCurrentFocusGroup() {
        return currentState.getCurrentFocusGroup();
    }

    public Configurator configure() {
        return new Configurator();
    }

    @Override
    public Action[] getActions(Object target, Object sender) {
        return allActions;
    }

    @Override
    public void handleAction(Action action, Object sender, Object target) {
        if (action instanceof NavShortcutListener && Arrays.asList(allActions).contains(action)) {
            NavShortcutListener act = (NavShortcutListener) action;
            act.handleAction(sender, target);
        }
    }


    public class Configurator {
        private Configurator(){}

        public Configurator constrainTab(Field<?>... targets) {
            FocusGroup focusGroup = new FocusGroup(targets);
            getFocusGroups().add(focusGroup);
            if (getCurrentFocusGroup() == null) {
                setCurrentFocusGroup(focusGroup);
            }
            return this;
        }

        public Configurator onPanel(Panel panel) {
            getFocusGroups().get(getFocusGroups().size() - 1).setPanel(panel);
            panel.addClickListener(clickToFocusListener);
            return this;
        }

        public Configurator forNavigation(CrudNavigation nav) {
            getFocusGroups().get(getFocusGroups().size() - 1).setNavigation(nav);
            return this;
        }

        public Configurator andThen(Field<?>... targets) {
            constrainTab(targets);
            return this;
        }

    }

    private class FocusGroup {
        /* optional */ Panel panel;
        CrudNavigation navigation;
        final private List<? extends Component.Focusable> focusables;
        final private Component.Focusable first;

        public FocusGroup(Component.Focusable... focusables) {
            this(Arrays.asList(focusables));
        }

        public FocusGroup(List<? extends Component.Focusable> focusables) {
            this.focusables = focusables;
            this.first = focusables.get(0);
        }

        public List<? extends Component.Focusable> getFocusables() {
            return focusables;
        }

        public Panel getPanel() {
            return panel;
        }

        public void setPanel(Panel panel) {
            this.panel = panel;
        }

        public void focus() {
            first.focus();
            focusPanel();
        }

        public void unfocus() {
            unfocusPanel();
        }

        public void focusPanel() {
            if (panel != null)
                panel.addStyleName(FOCUS);
        }

        public void unfocusPanel() {
            if (panel != null)
                panel.removeStyleName(FOCUS);
        }


        public CrudNavigation getNavigation() {
            return navigation;
        }

        public void setNavigation(CrudNavigation navigation) {
            this.navigation = navigation;
        }

    }

    MouseEvents.ClickListener clickToFocusListener = new MouseEvents.ClickListener() {
        @Override
        public void click(MouseEvents.ClickEvent event) {
            for (FocusGroup fg: getFocusGroups()) {
                if (fg.getPanel() == event.getSource()) {
                    setCurrentFocusGroup(fg);
                    focusCurrentGroup();
                } else {
                    fg.unfocus();
                }
            }
        }
    };

//    private UI ui;
//    private final MasterDetail<T,U> masterDetail;
//    private final FocusableNavigable<FieldGroup, Panel> masterFocus;
//    private final FocusableNavigable<Table, Panel> detailFocus;
//
//    final KeyBinder keyBinder;
//    private Navigable<?> currentNavigable;
//
//
//    public FocusManager(UI ui, MasterDetail<T,U> masterDetail) {
//        this.ui = ui;
//        this.masterDetail = masterDetail;
//        NavigableFieldGroup<FieldGroup> masterNavigable = masterDetail.getMaster();
//        FieldGroup masterFieldGroup = masterDetail.getMaster().getFieldBinder();
//
//        this.masterFocus = new FocusableNavigable<FieldGroup, Panel>(
//                masterNavigable,
//                new Panel(),
//                new ArrayList<Component.Focusable>(masterFieldGroup.getFields()),
//                ui
//        );
//
//        this.detailFocus = new FocusableNavigable<Table, Panel>(
//                masterDetail.getDetail(),
//                new Panel(),
//                Collections.<Component.Focusable>singletonList(masterDetail.getDetail().getTable()),
//                ui);
//
//        this.keyBinder = KeyBinder.forNavigation(masterFocus.getNavigable()
//                .getNavigation());
//        attach(ui, masterDetail);
//    }
//
//    private void attach(final UI ui, final MasterDetail masterDetail) {
////        if (this.ui != null) detach(this.ui);
////        this.ui = ui;
//        ui.addActionHandler(keyBinder);
//        ui.addShortcutListener(new ShortcutListener(
//                "focus-change", ShortcutAction.KeyCode.F9, new int[0]) {
//            @Override
//            public void handleAction(Object sender, Object target) {
//                if (getCurrentNavigable() == masterFocus.getNavigable()) {
//                    detailFocus.focus();
//                } else {
//                    masterFocus.focus();
//                }
//            }
//        });
//
//        final Component.Focusable masterFocusable = masterFocus.getFocusable();
//        if (masterFocusable instanceof Panel) {
//            ((Panel) masterFocusable).addClickListener(new MouseEvents.ClickListener() {
//                @Override
//                public void click(MouseEvents.ClickEvent event) {
//                    masterFocusable.focus();
//                }
//            });
//        }
//
//        final Component.Focusable detailFocusable = detailFocus.getFocusable();
//        if (detailFocusable instanceof Panel) {
//            ((Panel)detailFocusable).addClickListener(new MouseEvents.ClickListener() {
//                @Override
//                public void click(MouseEvents.ClickEvent event) {
//                    detailFocusable.focus();
//                }
//            });
//        }
//
//        //focus(masterDetail.getMaster().getNavigation());
//        masterFocusable.focus();
//
//    }
//
//
//
//
//
////    private void focus(CrudNavigation nav) {
////        if (getCurrentNav() == nav) return;
////
////        if (nav == masterDetail.getMaster().getNavigation()) {
////            ui.detailPanel.removeStyleName(FOCUS);
////            ui.masterPanel.addStyleName(FOCUS);
////            cycleThrough(getUI().fieldOrder);
////
////        } else {
////            ui.detailPanel.addStyleName(FOCUS);
////            ui.masterPanel.removeStyleName(FOCUS);
////            cycleThrough(getUI().detailPanel);
////
////        }
////        setCurrentNav(nav);
////        // ui.buttonBar.setNavigation(nav);
////        this.keyBinder.setNavigation(nav);
////    }
//
//
//
//    private Navigable<?> getCurrentNavigable() {
//        return this.currentNavigable;
//    }
//
//    public void setCurrentNavigable(Navigable<?> navigable) {
//        this.currentNavigable = navigable;
//    }
//

}
