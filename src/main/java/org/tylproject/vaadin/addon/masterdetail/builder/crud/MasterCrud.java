package org.tylproject.vaadin.addon.masterdetail.builder.crud;

import org.tylproject.vaadin.addon.masterdetail.builder.Master;

/**
 * Created by evacchi on 27/11/14.
 */
public interface MasterCrud extends CrudStrategy {
    MasterCrud withMaster(Master<?> master);
}
