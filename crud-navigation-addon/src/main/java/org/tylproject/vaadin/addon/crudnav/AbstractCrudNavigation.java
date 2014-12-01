package org.tylproject.vaadin.addon.crudnav;

import com.vaadin.event.EventRouter;
import org.tylproject.vaadin.addon.crudnav.events.*;

/**
 * Created by evacchi on 19/11/14.
 */
public abstract class AbstractCrudNavigation implements CrudNavigation {

    private final EventRouter eventRouter = new EventRouter();

    public EventRouter getEventRouter() {
        return eventRouter;
    }

    @Override
    public void addCurrentItemChangeListener(
            CurrentItemChange.Listener listener) {
        eventRouter.addListener(CurrentItemChange.Event.class, listener, CurrentItemChange.Listener.METHOD);
    }

    @Override
    public void removeCurrentItemChangeListener(CurrentItemChange.Listener listener) {
        eventRouter.removeListener(CurrentItemChange.Event.class, listener, CurrentItemChange.Listener.METHOD);
    }

    @Override
    public void addNextItemListener(NextItem.Listener listener) {
        eventRouter.addListener(NextItem.Event.class, listener, NextItem.Listener.METHOD);
    }

    @Override
    public void removeNextItemListener(NextItem.Listener listener) {
        eventRouter.removeListener(NextItem.Event.class, listener, NextItem.Listener.METHOD);
    }


    @Override
    public void addPrevItemListener(PrevItem.Listener listener) {
        eventRouter.addListener(PrevItem.Event.class, listener, PrevItem.Listener.METHOD);
    }

    @Override
    public void removePrevItemListener(PrevItem.Listener listener) {
        eventRouter.removeListener(PrevItem.Event.class, listener, PrevItem.Listener.METHOD);
    }


    @Override
    public void addLastItemListener(LastItem.Listener listener) {
        eventRouter.addListener(LastItem.Event.class, listener, LastItem.Listener.METHOD);
    }

    @Override
    public void removeLastItemListener(LastItem.Listener listener) {
        eventRouter.removeListener(LastItem.Event.class, listener, LastItem.Listener.METHOD);
    }


    @Override
    public void addFirstItemListener(FirstItem.Listener listener) {
        eventRouter.addListener(FirstItem.Event.class, listener, FirstItem.Listener.METHOD);
    }

    @Override
    public void removeFirstItemListener(FirstItem.Listener listener) {
        eventRouter.removeListener(FirstItem.Event.class, listener, FirstItem.Listener.METHOD);
    }

    @Override
    public void removeAfterCommitListener(AfterCommit.Listener listener) {
        eventRouter.removeListener(AfterCommit.Event.class, listener, AfterCommit.Listener.METHOD);
    }

    @Override
    public void addAfterCommitListener(AfterCommit.Listener listener) {
        eventRouter.addListener(AfterCommit.Event.class, listener, AfterCommit.Listener.METHOD);
    }

    @Override
    public void removeBeforeCommitListener(BeforeCommit.Listener listener) {
        eventRouter.removeListener(BeforeCommit.Event.class, listener, BeforeCommit.Listener.METHOD);
    }

    @Override
    public void addBeforeCommitListener(BeforeCommit.Listener listener) {
        eventRouter.addListener(BeforeCommit.Event.class, listener, BeforeCommit.Listener.METHOD);
    }

    @Override
    public void addItemCreateListener(ItemCreate.Listener listener) {
        eventRouter.addListener(ItemCreate.Event.class, listener, ItemCreate.Listener.METHOD);
    }

    @Override
    public void removeItemCreateListener(ItemCreate.Listener listener) {
        eventRouter.removeListener(ItemCreate.Event.class, listener, ItemCreate.Listener.METHOD);
    }

    @Override
    public void addItemEditListener(ItemEdit.Listener listener) {
        eventRouter.addListener(ItemEdit.Event.class, listener, ItemEdit.Listener.METHOD);
    }

    @Override
    public void removeItemEditListener(ItemEdit.Listener listener) {
        eventRouter.removeListener(ItemEdit.Event.class, listener, ItemEdit.Listener.METHOD);
    }

    @Override
    public void addItemRemoveListener(ItemRemove.Listener listener) {
        eventRouter.addListener(ItemRemove.Event.class, listener, ItemRemove.Listener.METHOD);
    }

    @Override
    public void removeItemRemoveListener(ItemRemove.Listener listener) {
        eventRouter.removeListener(ItemRemove.Event.class, listener, ItemRemove.Listener.METHOD);
    }

    @Override
    public void addOnCommitListener(OnCommit.Listener listener) {
        eventRouter.addListener(OnCommit.Event.class, listener, OnCommit.Listener.METHOD);
    }

    @Override
    public void removeOnCommitListener(OnCommit.Listener listener) {
        eventRouter.removeListener(OnCommit.Event.class, listener, OnCommit.Listener.METHOD);
    }

    @Override
    public void addOnDiscardListener(OnDiscard.Listener listener) {
        eventRouter.addListener(OnDiscard.Event.class, listener, OnDiscard.Listener.METHOD);
    }

    @Override
    public void removeOnDiscardListener(OnDiscard.Listener listener) {
        eventRouter.removeListener(OnDiscard.Event.class, listener, OnDiscard.Listener.METHOD);
    }


}
