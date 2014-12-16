package org.tylproject.vaadin.addon.datanav;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import org.tylproject.vaadin.addon.datanav.events.*;

/**
 * Created by evacchi on 19/11/14.
 */
public interface DataNavigation extends
        CurrentItemChange.Notifier,
        NextItem.Notifier,
        PrevItem.Notifier,
        FirstItem.Notifier,
        LastItem.Notifier,
        NavigationEnabled.Notifier,

        ItemCreate.Notifier,
        ItemEdit.Notifier,
        ItemRemove.Notifier,
        OnCommit.Notifier,
        BeforeCommit.Notifier,
        AfterCommit.Notifier,
        OnDiscard.Notifier,
        CrudEnabled.Notifier,
        EditingModeChange.Notifier,

        ClearToFind.Notifier,
        BeforeFind.Notifier,
        OnFind.Notifier,
        AfterFind.Notifier,
        FindEnabled.Notifier {

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
    public void enableNavigation();
    public void disableNavigation();
    public boolean isNavigationEnabled();

    // search
    public void clearToFind();
    public void find();
    public boolean isClearToFindMode();
    public void enableFind();
    public void disableFind();
    public boolean isFindEnabled();


    // CRUD
    public void create();
    public void commit();
    public void edit();
    public void discard();
    public void remove();
    public void enableCrud();
    public void disableCrud();
    public boolean isCrudEnabled();
    public void enterEditingMode();
    public void leaveEditingMode();
    public boolean isEditingMode();

}