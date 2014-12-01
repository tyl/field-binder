package org.tylproject.vaadin.addon.masterdetail;

import com.vaadin.data.Container;
import org.tylproject.vaadin.addon.crudnav.BasicCrudNavigation;
import org.tylproject.vaadin.addon.crudnav.CrudNavigation;

/**
 * Created by evacchi on 26/11/14.
 */
public class NavigableContainerViewer<T extends Container.Viewer>
        implements Navigable<T> {
    private final T viewer;
    private final CrudNavigation navigation;

    public NavigableContainerViewer(T viewer, BasicCrudNavigation navigation) {
        this.viewer = viewer;
        this.navigation = navigation;
    }

    @Override
    public T getNavigable() {
        return this.getContainerViewer();
    }

    public T getContainerViewer() {
        return this.viewer;
    }
    public CrudNavigation getNavigation() {
        return this.navigation;
    }
}
