package org.tylproject.vaadin.addon.crudnav;

import com.vaadin.event.EventRouter;
import org.tylproject.vaadin.addon.crudnav.events.*;

import java.util.logging.Logger;

/**
 * Created by evacchi on 19/11/14.
 */
public abstract class AbstractCrudNavigation implements CrudNavigation {

    private final EventRouter eventRouter = new EventRouter();
    protected final Logger logger = Logger.getLogger(this.getClass().getCanonicalName());
    private boolean clearToFindMode;

    public EventRouter getEventRouter() {
        return eventRouter;
    }

    @Override
    public void addCurrentItemChangeListener(
            CurrentItemChange.Listener listener) {
        eventRouter.addListener(CurrentItemChange.Event.class, listener, CurrentItemChange.Listener.METHOD);

        // when a new listener is being hooked, fire immediately
        // the event on the listener so that it may update its internal status
        // even if it has missed last event firing
        Object id = getCurrentItemId();
        listener.currentItemChange(new CurrentItemChange.Event(this, id, null));
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

    @Override
    public void addClearToFindListener(ClearToFind.Listener listener) {
        eventRouter.addListener(ClearToFind.Event.class, listener, ClearToFind.Listener.METHOD);
    }

    @Override
    public void removeClearToFindListener(ClearToFind.Listener listener) {
        eventRouter.removeListener(ClearToFind.Event.class, listener, ClearToFind.Listener.METHOD);
    }

    @Override
    public void addBeforeFindListener(BeforeFind.Listener listener) {
        eventRouter.addListener(BeforeFind.Event.class, listener, BeforeFind.Listener.METHOD);
    }

    @Override
    public void removeBeforeFindListener(BeforeFind.Listener listener) {
        eventRouter.removeListener(BeforeFind.Event.class, listener, BeforeFind.Listener.METHOD);
    }

    @Override
    public void addOnFindListener(OnFind.Listener listener) {
        eventRouter.addListener(OnFind.Event.class, listener, OnFind.Listener.METHOD);
    }

    @Override
    public void removeOnFindListener(OnFind.Listener listener) {
        eventRouter.removeListener(OnFind.Event.class, listener, OnFind.Listener.METHOD);
    }

    @Override
    public void addAfterFindListener(AfterFind.Listener listener) {
        eventRouter.addListener(AfterFind.Event.class, listener, AfterFind.Listener.METHOD);
    }

    @Override
    public void removeAfterFindListener(AfterFind.Listener listener) {
        eventRouter.removeListener(AfterFind.Event.class, listener, AfterFind.Listener.METHOD);
    }


    public void enterClearToFind() {
        this.clearToFindMode = true;
    }
    public void leaveClearToFind() {
        this.clearToFindMode = false;
    }
    public boolean isClearToFindMode() {
        return clearToFindMode;
    }
}
