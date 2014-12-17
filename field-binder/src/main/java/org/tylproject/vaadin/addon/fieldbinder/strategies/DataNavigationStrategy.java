package org.tylproject.vaadin.addon.fieldbinder.strategies;

import org.tylproject.vaadin.addon.datanav.events.CurrentItemChange;

/**
 * A shorthand interface for common editing operations on a
 * {@link org.tylproject.vaadin.addon.datanav.DataNavigation}
 */
public interface DataNavigationStrategy extends
        CurrentItemChange.Listener,
        CrudStrategy,
        FindStrategy {
}
