package org.tylproject.vaadin.addon.datanav;

import org.tylproject.vaadin.addon.datanav.events.RejectOperationException;

/**
 * Created by evacchi on 02/12/14.
 */
public class NavigationOperations {
    public static void reject() {
        throw RejectOperationException.Instance;
    }
}
