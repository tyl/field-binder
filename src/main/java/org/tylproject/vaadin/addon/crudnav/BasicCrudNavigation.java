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

    private final Container.Indexed container;
    private Object currentItemId;

    public BasicCrudNavigation(Container.Indexed container) {
        this.container = container;
    }

    @Override
    public Container.Indexed getContainer() {
        return container;
    }

    @Override
    public Item getCurrentItem() {
        return container.getItem(getCurrentItemId());
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
        getEventRouter().fireEvent(new BeforeCommit.Event(this));
        getEventRouter().fireEvent(new OnCommit.Event(this));
        getEventRouter().fireEvent(new AfterCommit.Event(this));
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

}
