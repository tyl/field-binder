package org.tylproject.vaadin.addon.masterdetail.crud;

import org.tylproject.vaadin.addon.crudnav.events.*;

/**
 * Created by evacchi on 27/11/14.
 */
public interface CrudStrategy extends OnDiscard.Listener,
        OnCommit.Listener,
        ItemRemove.Listener,
        ItemEdit.Listener,
        ItemCreate.Listener {

}
