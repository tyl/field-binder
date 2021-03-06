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
import com.vaadin.data.Item;
import com.vaadin.data.Validator;
import com.vaadin.event.ListenerMethod;
import org.tylproject.vaadin.addon.datanav.events.*;
import org.tylproject.vaadin.addon.fieldbinder.behavior.Behavior;
import org.tylproject.vaadin.addon.fieldbinder.behavior.BehaviorFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Main implementation of the {@link org.tylproject.vaadin.addon.datanav.DataNavigation}
 * interface.
 *
 */
final public class BasicDataNavigation extends AbstractDataNavigation implements DataNavigation {

    private @Nonnull Container.Ordered container;

    private Object currentItemId;
    private boolean navigationEnabled = true;
    private boolean crudEnabled = true;
    private boolean findEnabled = true;
    private boolean editingMode = false;
    private boolean clearToFindMode = false;
    private BehaviorFactory behaviorFactory;



    public BasicDataNavigation() {}

    public BasicDataNavigation(@Nonnull final Container.Ordered container) {
        setContainer(container);
    }

    public void setBehaviorFactory(BehaviorFactory behaviorFactory) {
        this.behaviorFactory = behaviorFactory;
    }

    @Override
    public Container.Ordered getContainer() {
        return container;
    }

    /**
     * Return the container type for this DataNavigation
     */
    public @Nonnull Class<? extends Container.Ordered> getContainerType() {
        if (container != null) {
            return container.getClass();
        }
        else {
            throw new IllegalStateException("The container type is currently unknown: " +
                    "a container must be set with setContainer(), " +
                    "or an upper bound must be given with restrictContainerType()");
        }
    }

    @Override
    public void setContainer(Container.Ordered container) {

        this.container = container;
        this.currentItemId = null;
        if (container == null || container.size() == 0) disableNavigation();
        if (container instanceof Container.Filterable) enableFind();
        else disableFind();

        first();
    }

    @Override
    public Item getCurrentItem() {
        Object currentId = getCurrentItemId();
        return currentId == null? null : container.getItem(currentId);
    }

    @Override
    public Object getCurrentItemId() {
        return currentItemId;
    }

    @Override
    public Object setCurrentItemId(Object itemId) {
        Object prevId = this.currentItemId;
        this.currentItemId = itemId;
        getEventRouter().fireEvent(
                new CurrentItemChange.Event(this, currentItemId, prevId));


        return prevId;
    }

    @Override
    public void first() {
        if (!isNavigationEnabled()) return;
        Object oldId = setCurrentItemId(container.firstItemId());
        getEventRouter().fireEvent(new FirstItem.Event(this, currentItemId, oldId));
    }

    @Override
    public void last() {
        if (!isNavigationEnabled()) return;
        Object oldId = setCurrentItemId(container.lastItemId());
        getEventRouter().fireEvent(new LastItem.Event(this, currentItemId, oldId));

    }

    @Override
    public void next() {
        if (!isNavigationEnabled()) return;
        Object oldId = setCurrentItemId(container.nextItemId(this.currentItemId));
        getEventRouter().fireEvent(new NextItem.Event(this, currentItemId, oldId));
    }

    @Override
    public void prev() {
        if (!isNavigationEnabled()) return;
        Object oldId = setCurrentItemId(container.prevItemId(this.currentItemId));
        getEventRouter().fireEvent(new PrevItem.Event(this, currentItemId, oldId));
    }

    @Override
    public void disableNavigation() {
        this.navigationEnabled = false;
        getEventRouter().fireEvent(new NavigationEnabled.Event(this, navigationEnabled));
    }

    @Override
    public void enableNavigation() {
        this.navigationEnabled = true;
        getEventRouter().fireEvent(new NavigationEnabled.Event(this, navigationEnabled));
    }

    @Override
    public boolean isNavigationEnabled() {
        return navigationEnabled;
    }

    @Override
    public void create() {
        if (!isCrudEnabled()) return;
        getEventRouter().fireEvent(new ItemCreate.Event(this));
        // if currentItemId has actually been set; i.e., an item has been actually created
        enterEditingMode();
    }

    @Override
    public void commit() {
        if (!isCrudEnabled()) return;

        try {
            getEventRouter().fireEvent(new BeforeCommit.Event(this));
            getEventRouter().fireEvent(new OnCommit.Event(this));
            getEventRouter().fireEvent(new AfterCommit.Event(this));

            leaveEditingMode();

        } catch (RejectOperationException signal) {
            logger.info("Commit operation was interrupted by user");
        } catch (ListenerMethod.MethodException ex) {
            if (ex.getCause() instanceof Validator.InvalidValueException) {
                // ignore: Vaadin has already taken care of it!
            } else throw ex; // otherwise propagate!
        }
    }

    @Override
    public void discard() {
        if (!isCrudEnabled()) return;
        leaveEditingMode();

        getEventRouter().fireEvent(new OnDiscard.Event(this));
    }


    @Override
    public void edit() {
        if (!isCrudEnabled()) return;
        if (container == null || container.size() == 0) {
            throw new IllegalStateException("Container is empty or null");
        }

        getEventRouter().fireEvent(new ItemEdit.Event(this));

        if (!isEditingMode()) {
            enterEditingMode();
        }
    }

    @Override
    public void remove() {
        if (!isCrudEnabled()) return;
        if (isEditingMode()) leaveEditingMode();

        if (container == null || container.size() == 0) {
            throw new IllegalStateException("Container is empty or null");
        }

        Object currentItemId = this.getCurrentItemId();
        Object newItemId = container.nextItemId(currentItemId);
        if (newItemId == null) {
            newItemId = container.prevItemId(currentItemId);
        }
        getEventRouter().fireEvent(new ItemRemove.Event(this));
        this.setCurrentItemId(newItemId);
    }


    @Override
    public void disableCrud() {
        this.crudEnabled = false;
        getEventRouter().fireEvent(new CrudEnabled.Event(this, crudEnabled));
    }

    @Override
    public void enableCrud() {
        this.crudEnabled = true;
        getEventRouter().fireEvent(new CrudEnabled.Event(this, crudEnabled));
    }

    @Override
    public boolean isCrudEnabled() {
        return this.crudEnabled;
    }

    public boolean isEditingMode() {
        return editingMode;
    }
    public void enterEditingMode() {
        editingMode = true;
        disableNavigation();
        disableFind();
        getEventRouter().fireEvent(new EditingModeChange.Event(this, EditingModeChange
                .Status.Entering));
    }
    public void leaveEditingMode() {
        editingMode = false;
        enableNavigation();
        enableFind();
        getEventRouter().fireEvent(new EditingModeChange.Event(this, EditingModeChange
                .Status.Leaving));
    }

    @Override
    public void clearToFind() {
        if (!isFindEnabled()) return;
        enterClearToFind();
        getEventRouter().fireEvent(new ClearToFind.Event(this));
    }

    @Override
    public void find() {
        if (!isFindEnabled()) return;
        try {
            if (!isClearToFindMode()) throw new IllegalStateException("Cannot find() when not in ClearToFind mode");
            leaveClearToFind();
            getEventRouter().fireEvent(new BeforeFind.Event(this));
            getEventRouter().fireEvent(new OnFind.Event(this));
            getEventRouter().fireEvent(new AfterFind.Event(this));

            this.first();

        } catch (RejectOperationException signal) {
            logger.info("Find operation was interrupted by user");
        }
    }


    @Override
    public void disableFind() {
        this.findEnabled = false;
        getEventRouter().fireEvent(new FindEnabled.Event(this, findEnabled));
    }

    @Override
    public void enableFind() {
        this.findEnabled = getContainer() instanceof Container.Filterable;
        getEventRouter().fireEvent(new FindEnabled.Event(this, findEnabled));
    }



    public void enterClearToFind() {
        disableNavigation();
        disableCrud();
        this.clearToFindMode = true;
    }
    public void leaveClearToFind() {
        enableCrud();
        enableNavigation();
        this.clearToFindMode = false;
    }
    public boolean isClearToFindMode() {
        return clearToFindMode;
    }

    @Override
    public boolean isFindEnabled() {
        return this.findEnabled;
    }

    /**
     * assign all CRUD-related listeners defined in the given object
     */
    public <X extends OnDiscard.Listener
            & OnCommit.Listener
            & ItemRemove.Listener
            & ItemEdit.Listener
            & ItemCreate.Listener> BasicDataNavigation withCrudListenersFrom(X crudListenersObject) {

        this.addItemRemoveListener(crudListenersObject);
        this.addOnCommitListener(crudListenersObject);
        this.addOnDiscardListener(crudListenersObject);
        this.addItemEditListener(crudListenersObject);
        this.addItemCreateListener(crudListenersObject);

        return this;
    }

    /**
     * assign Find and ClearToFind listeners defined in the given object
     */
    public <X extends
            ClearToFind.Listener
            & OnFind.Listener> BasicDataNavigation withFindListenersFrom(X findListenersObject) {
        this.addClearToFindListener(findListenersObject);
        this.addOnFindListener(findListenersObject);
        return this;
    }

    /**
     * "fluent" alias to
     * {@link #addCurrentItemChangeListener(org.tylproject.vaadin.addon.datanav.events.CurrentItemChange.Listener)}
     */
    public <X extends
            CurrentItemChange.Listener> BasicDataNavigation
            withCurrentItemChangeListenerFrom(X itemChangeListenerObject) {
            this.addCurrentItemChangeListener(itemChangeListenerObject);
            return this;
    }

    /**
     * associate all the listeners defined in the given behavior object
     */
    public BasicDataNavigation withBehavior(Behavior behavior) {
        this.withCrudListenersFrom(behavior).withFindListenersFrom(behavior);
        this.addCurrentItemChangeListener(behavior);
        return this;
    }

    /**
     * Assign a default behavior according to the current known container type.
     *
     * Uses a {@link org.tylproject.vaadin.addon.fieldbinder.behavior.BehaviorFactory}.
     * There are two BehaviorFactories:
     *
     *  <ul>
     *      <li>{@link org.tylproject.vaadin.addon.fieldbinder.behavior.DefaultBehaviorFactory}</li>
     *      <li>{@link org.tylproject.vaadin.addon.fieldbinder.behavior.DefaultTableBehaviorFactory}</li>
     *  </ul>
     *
     */
    public BasicDataNavigation withDefaultBehavior() {

        if (behaviorFactory == null) {
            throw new IllegalStateException(
                    "Cannot automatically assign a default behavior: no BehaviorFactory");
        }

        try {
            Class<? extends Container.Ordered> containerClass = getContainerType();
            Behavior behavior = behaviorFactory.forContainerType(containerClass);
            return this.withBehavior(behavior);

        } catch (IllegalStateException ex) {
            throw new IllegalStateException(
                    "Cannot automatically assign a default behavior.", ex);
        }


    }

}
