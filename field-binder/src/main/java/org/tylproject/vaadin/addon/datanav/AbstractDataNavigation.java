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

import com.vaadin.event.EventRouter;
import org.tylproject.vaadin.addon.datanav.events.*;

import java.util.logging.Logger;

/**
 * Defines all the event handlers for a DataNavigation.
 */
public abstract class AbstractDataNavigation implements DataNavigation {

    private final EventRouter eventRouter = new EventRouter();
    protected final Logger logger = Logger.getLogger(this.getClass().getCanonicalName());

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
    public void addNavigationEnabledListener(NavigationEnabled.Listener listener) {
        eventRouter.addListener(NavigationEnabled.Event.class, listener, NavigationEnabled.Listener.METHOD);
    }

    @Override
    public void removeNavigationEnabledListener(NavigationEnabled.Listener listener) {
        eventRouter.removeListener(NavigationEnabled.Event.class, listener, NavigationEnabled.Listener.METHOD);
    }


    // CRUD


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
    public void addCrudEnabledListener(CrudEnabled.Listener listener) {
        eventRouter.addListener(CrudEnabled.Event.class, listener, CrudEnabled.Listener.METHOD);
        eventRouter.fireEvent(new CrudEnabled.Event(this, this.isCrudEnabled()));
    }

    @Override
    public void removeCrudEnabledListener(CrudEnabled.Listener listener) {
        eventRouter.removeListener(CrudEnabled.Event.class, listener, CrudEnabled.Listener.METHOD);
        eventRouter.fireEvent(new CrudEnabled.Event(this, this.isCrudEnabled()));
    }

    @Override
    public void addEditingModeChangeListener(EditingModeChange.Listener listener) {
        eventRouter.addListener(EditingModeChange.Event.class, listener, EditingModeChange.Listener.METHOD);
    }

    @Override
    public void removeEditingModeChangeListener(EditingModeChange.Listener listener) {
        eventRouter.removeListener(EditingModeChange.Event.class, listener, EditingModeChange.Listener.METHOD);
    }

    // FIND

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

    @Override
    public void addFindEnabledListener(FindEnabled.Listener listener) {
        eventRouter.addListener(FindEnabled.Event.class, listener, FindEnabled.Listener.METHOD);
    }

    @Override
    public void removeFindEnabledListener(FindEnabled.Listener listener) {
        eventRouter.removeListener(FindEnabled.Event.class, listener, FindEnabled.Listener.METHOD);
    }

}
