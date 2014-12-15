package org.tylproject.vaadin.addon.fieldbinder.strategies;

import org.tylproject.vaadin.addon.datanav.events.CurrentItemChange;

/**
 * Created by evacchi on 15/12/14.
 */
public interface DataNavigationStrategy extends
        CurrentItemChange.Listener,
        CrudStrategy,
        FindStrategy {
}
