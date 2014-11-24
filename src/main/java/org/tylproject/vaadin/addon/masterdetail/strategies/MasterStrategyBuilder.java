package org.tylproject.vaadin.addon.masterdetail.strategies;

import org.tylproject.vaadin.addon.masterdetail.MasterItemChange;

/**
 * Created by evacchi on 24/11/14.
 */
public interface MasterStrategyBuilder {
    public MasterItemChange.Listener build();
}
