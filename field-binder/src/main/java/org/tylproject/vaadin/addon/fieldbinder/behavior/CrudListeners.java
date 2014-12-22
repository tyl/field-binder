package org.tylproject.vaadin.addon.fieldbinder.behavior;

import org.tylproject.vaadin.addon.datanav.events.*;

/**
 * Created by evacchi on 27/11/14.
 */
public interface CrudListeners extends OnDiscard.Listener,
        OnCommit.Listener,
        ItemRemove.Listener,
        ItemEdit.Listener,
        ItemCreate.Listener {

}
