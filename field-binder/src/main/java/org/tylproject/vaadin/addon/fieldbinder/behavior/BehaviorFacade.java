package org.tylproject.vaadin.addon.fieldbinder.behavior;

import org.tylproject.vaadin.addon.datanav.events.*;

/**
 * Created by evacchi on 28/01/15.
 */
public class BehaviorFacade implements Behavior {

    private final CurrentItemChange.Listener currentItemDelegate;
    private final CrudListeners crudDelegate;
    private final FindListeners findDelegate;

    public BehaviorFacade(
        CurrentItemChange.Listener currentItemDelegate,
        CrudListeners crudDelegate,
        FindListeners findDelegate) {
        this.currentItemDelegate = currentItemDelegate;
        this.crudDelegate = crudDelegate;
        this.findDelegate = findDelegate;
    }

    @Override
    public void currentItemChange(CurrentItemChange.Event event) {
        currentItemDelegate.currentItemChange(event);
    }

    @Override
    public void onDiscard(OnDiscard.Event event) {
        crudDelegate.onDiscard(event);
    }

    @Override
    public void onCommit(OnCommit.Event event) {
        crudDelegate.onCommit(event);
    }

    @Override
    public void itemRemove(ItemRemove.Event event) {
        crudDelegate.itemRemove(event);
    }

    @Override
    public void itemEdit(ItemEdit.Event event) {
        crudDelegate.itemEdit(event);
    }

    @Override
    public void itemCreate(ItemCreate.Event event) {
        crudDelegate.itemCreate(event);
    }

    @Override
    public void clearToFind(ClearToFind.Event event) {
        findDelegate.clearToFind(event);
    }

    @Override
    public void onFind(OnFind.Event event) {
        findDelegate.onFind(event);
    }
}
