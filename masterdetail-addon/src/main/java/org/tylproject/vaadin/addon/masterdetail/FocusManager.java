package org.tylproject.vaadin.addon.masterdetail;

import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.event.MouseEvents;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.ui.*;
import org.tylproject.vaadin.addon.crudnav.KeyBinder;

import java.util.*;

/**
 * Created by evacchi on 26/11/14.
 */
public class FocusManager<T,U> {
    public static final String FOCUS = "masterdetail-focus";
//
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
//        FieldGroup masterFieldGroup = masterDetail.getMaster().getFieldGroup();
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
