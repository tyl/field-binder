package org.tylproject.vaadin.addon.fieldbinder.behavior;

import org.tylproject.vaadin.addon.datanav.events.CurrentItemChange;

/**
 * A shorthand interface for common editing operations on a
 * {@link org.tylproject.vaadin.addon.datanav.DataNavigation}
 */
public interface Behavior extends
        CurrentItemChange.Listener,
        CrudListeners,
        FindListeners {
}
