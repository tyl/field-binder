package org.tylproject.vaadin.addon.datanav;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import org.tylproject.vaadin.addon.datanav.events.*;
import org.tylproject.vaadin.addon.fieldbinder.strategies.DataNavigationStrategy;
import org.tylproject.vaadin.addon.fieldbinder.strategies.DataNavigationStrategyFactory;

import javax.annotation.Nonnull;

/**
 * Created by evacchi on 19/11/14.
 */
final public class BasicDataNavigation extends AbstractDataNavigation implements DataNavigation {

    private @Nonnull Container.Ordered container;
    private Object currentItemId;
    private boolean navigationEnabled = true;
    private boolean crudEnabled = true;
    private boolean findEnabled = true;
    private boolean editingMode = false;
    private boolean clearToFindMode = false;
    private DataNavigationStrategyFactory navigationStrategyFactory;



    public BasicDataNavigation() {
//        disableNavigation();
//        disableCrud();
//        disableFind();
    }

    public BasicDataNavigation(@Nonnull final Container.Ordered container) {
        setContainer(container);
    }

    public void setNavigationStrategyFactory(DataNavigationStrategyFactory
                                                     navigationStrategyFactory) {
        this.navigationStrategyFactory = navigationStrategyFactory;
    }

    @Override
    public Container.Ordered getContainer() {
        return container;
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
        enterEditingMode();
        getEventRouter().fireEvent(new ItemCreate.Event(this));
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

        if (isEditingMode()) leaveEditingMode();
        else enterEditingMode();

        getEventRouter().fireEvent(new ItemEdit.Event(this));
    }

    @Override
    public void remove() {
        if (!isCrudEnabled()) return;
        if (isEditingMode()) leaveEditingMode();
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

    public <X extends
            ClearToFind.Listener
            & OnFind.Listener> BasicDataNavigation withFindListenersFrom(X findListenersObject) {
        this.addClearToFindListener(findListenersObject);
        this.addOnFindListener(findListenersObject);
        return this;
    }

    public <T> BasicDataNavigation withDefaultBehavior() {

        if (navigationStrategyFactory == null) {
            throw new IllegalStateException("Cannot automatically assign a default behavior");
        }

        DataNavigationStrategy strategy = navigationStrategyFactory.forContainer(container);

        this.withCrudListenersFrom(strategy).withFindListenersFrom(strategy);
        this.addCurrentItemChangeListener(strategy);

        return this;
    }

}
