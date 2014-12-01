package org.tylproject.vaadin.addon.masterdetail.builder;

import com.vaadin.event.ShortcutListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.UI;

import java.util.Arrays;
import java.util.List;

/**
 * Created by evacchi on 26/11/14.
 */
public class FocusableNavigable<T, U extends Component.Focusable> {
    private final Navigable<T> navigable;
    private final Component.Focusable mainFocusable;
    private final List<Component.Focusable> focusableChildren;
    private final UI ui;
    private final TabCycler tabCycler;

    boolean hasFocus = false;

    public FocusableNavigable(
            Navigable<T> navigable,
            Component.Focusable mainFocusable,
            List<Component.Focusable> children,
            UI ui) {
        this.navigable = navigable;
        this.mainFocusable = mainFocusable;
        this.focusableChildren = children;
        this.tabCycler = new TabCycler(focusableChildren);
        this.ui = ui;
    }

    public Navigable<T> getNavigable() {
        return navigable;
    }

    public Component.Focusable getFocusable() {
        return mainFocusable;
    }

    public void focus() {
        if (hasFocus) return;

        mainFocusable.addStyleName(FocusManager.FOCUS);
        ui.addAction(tabCycler);

        this.hasFocus = true;
    }
    public void unfocus() {
        if (!hasFocus) return;

        mainFocusable.removeStyleName(FocusManager.FOCUS);
        ui.removeAction(tabCycler);

        this.hasFocus = false;
    }

    public boolean hasFocus() {
        return hasFocus;
    }


    private class TabCycler extends ShortcutListener {
        private final List<? extends Component.Focusable> focusables;
        private final Component.Focusable first;

        public TabCycler(List<? extends Component.Focusable> focusables) {
            super("tab", KeyCode.TAB, new int[0]);
            this.focusables = focusables;
            this.first = focusables.get(0);
        }

        @Override
        public void handleAction(Object sender, Object target) {
            //if (sender == masterPanel) {
            int indexOf = focusables.indexOf(target);
            if (indexOf < 0 || indexOf == focusables.size() - 1) {
                first.focus();
            } else {
                focusables.get(indexOf + 1).focus();
            }
            //}
        }
    }
}
