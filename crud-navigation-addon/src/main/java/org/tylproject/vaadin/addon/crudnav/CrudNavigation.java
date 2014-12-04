package org.tylproject.vaadin.addon.crudnav;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import org.tylproject.vaadin.addon.crudnav.events.*;

/**
 * Created by evacchi on 19/11/14.
 */
public interface CrudNavigation extends
        CurrentItemChange.Notifier,
        NextItem.Notifier,
        PrevItem.Notifier,
        FirstItem.Notifier,
        LastItem.Notifier,
        ItemCreate.Notifier,
        ItemEdit.Notifier,
        ItemRemove.Notifier,
        OnCommit.Notifier,
        BeforeCommit.Notifier,
        AfterCommit.Notifier,
        OnDiscard.Notifier,
        OnClearToFind.Notifier,
        BeforeFind.Notifier,
        OnFind.Notifier,
        AfterFind.Notifier{

    public Container.Ordered  getContainer();
    public void setContainer(Container.Ordered container);

    public Item getCurrentItem();
    public Object getCurrentItemId();
    public Object setCurrentItemId(Object itemId);

    // navigation
    public void first();
    public void last();
    public void next();
    public void prev();

    // search
    public void clearToFind();
    public void find();

    // CRUD
    public void create();
    public void commit();
    public void edit();
    public void discard();
    public void remove();

}
