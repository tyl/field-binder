package org.tylproject.vaadin.addon.masterdetail.crud;

import org.tylproject.vaadin.addon.crudnav.events.CurrentItemChange;
import org.tylproject.vaadin.addon.masterdetail.Master;

/**
 * Created by evacchi on 27/11/14.
 */
public interface MasterCrud extends CrudStrategy,
        CurrentItemChange.Listener {
    MasterCrud withMaster(Master<?> master);
}
