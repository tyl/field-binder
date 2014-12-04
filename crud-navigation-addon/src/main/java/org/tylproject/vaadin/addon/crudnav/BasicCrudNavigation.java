package org.tylproject.vaadin.addon.crudnav;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.event.EventRouter;
import org.tylproject.vaadin.addon.crudnav.events.*;

import javax.annotation.Nonnull;

/**
 * Created by evacchi on 19/11/14.
 */
final public class BasicCrudNavigation extends AbstractCrudNavigation implements CrudNavigation {

    private @Nonnull Container.Ordered container;
    private Object currentItemId;

    public BasicCrudNavigation() {

    }

    public BasicCrudNavigation(@Nonnull final Container.Indexed container) {
        setContainer(container);
    }

    @Override
    public Container.Ordered getContainer() {
        return container;
    }

    @Override
    public void setContainer(Container.Ordered container) {
        this.container = container;
        this.currentItemId = null;
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
        Object oldId = setCurrentItemId(container.firstItemId());
        getEventRouter().fireEvent(new FirstItem.Event(this, currentItemId, oldId));
    }

    @Override
    public void last() {
        Object oldId = setCurrentItemId(container.lastItemId());
        getEventRouter().fireEvent(new LastItem.Event(this, currentItemId, oldId));

    }

    @Override
    public void next() {
        Object oldId = setCurrentItemId(container.nextItemId(this.currentItemId));
        getEventRouter().fireEvent(new NextItem.Event(this, currentItemId, oldId));
    }

    @Override
    public void prev() {
        Object oldId = setCurrentItemId(container.prevItemId(this.currentItemId));
        getEventRouter().fireEvent(new PrevItem.Event(this, currentItemId, oldId));
    }

    @Override
    public void create() {
        getEventRouter().fireEvent(new ItemCreate.Event(this));
    }

    @Override
    public void commit() {
        try {
            getEventRouter().fireEvent(new BeforeCommit.Event(this));
            getEventRouter().fireEvent(new OnCommit.Event(this));
            getEventRouter().fireEvent(new AfterCommit.Event(this));
        } catch (RejectOperationException signal) {
            logger.info("Commit operation was interrupted by user");
        }
    }

    @Override
    public void discard() {
        getEventRouter().fireEvent(new OnDiscard.Event(this));
    }

    @Override
    public void edit() {
        getEventRouter().fireEvent(new ItemEdit.Event(this));
    }

    @Override
    public void remove() {
        Object currentItemId = this.getCurrentItemId();
        Object newItemId = container.nextItemId(currentItemId);
        if (newItemId == null) {
            newItemId = container.prevItemId(currentItemId);
        }
        getEventRouter().fireEvent(new ItemRemove.Event(this));
        this.setCurrentItemId(newItemId);
    }


    @Override
    public void clearToFind() {
        getEventRouter().fireEvent(new OnClearToFind.Event(this));
    }

    @Override
    public void find() {
        try {
            getEventRouter().fireEvent(new BeforeFind.Event(this));
            getEventRouter().fireEvent(new OnFind.Event(this));
            getEventRouter().fireEvent(new AfterFind.Event(this));
        } catch (RejectOperationException signal) {
            logger.info("Find operation was interrupted by user");
        }
    }
}
