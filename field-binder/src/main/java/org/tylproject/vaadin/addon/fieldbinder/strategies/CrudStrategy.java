package org.tylproject.vaadin.addon.fieldbinder.strategies;

import org.tylproject.vaadin.addon.datanav.events.*;

/**
 * Created by evacchi on 27/11/14.
 */
public interface CrudStrategy extends OnDiscard.Listener,
        OnCommit.Listener,
        ItemRemove.Listener,
        ItemEdit.Listener,
        ItemCreate.Listener {

}
