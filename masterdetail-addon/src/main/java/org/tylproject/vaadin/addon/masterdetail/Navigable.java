package org.tylproject.vaadin.addon.masterdetail;

import org.tylproject.vaadin.addon.crudnav.CrudNavigation;

/**
 * Created by evacchi on 26/11/14.
 */
public interface Navigable<T> {
    T getNavigable();
    CrudNavigation getNavigation();
}
