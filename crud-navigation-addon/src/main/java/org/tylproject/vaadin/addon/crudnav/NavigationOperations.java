package org.tylproject.vaadin.addon.crudnav;

import org.tylproject.vaadin.addon.crudnav.events.RejectOperationException;

/**
 * Created by evacchi on 02/12/14.
 */
public class NavigationOperations {
    public static void reject() {
        throw new RejectOperationException();
    }
}
