package org.tylproject.vaadin.addon.fieldbinder;

import com.vaadin.ui.Grid;
import com.vaadin.ui.Table;

import java.util.Collection;

/**
 * Created by evacchi on 18/02/15.
 */
public class CollectionGrid<T,U extends Collection<T>> extends CollectionTabularView<T,U> {

    final GridAdaptor adaptor;

    public CollectionGrid(Class<T> containedBeanClass, Class<U> collectionType) {
        super(containedBeanClass, collectionType, new GridAdaptor<T>(containedBeanClass));

        adaptor = (GridAdaptor<T>) super.getAdaptor();

        Grid gridComponent = adaptor.getComponent();

        gridComponent.setSizeFull();
        gridComponent.setWidth("100%");
        gridComponent.setHeight("300px");
        gridComponent.setSelectionMode(Grid.SelectionMode.SINGLE);



    }


    /**
     * Returns the actual {@link com.vaadin.ui.Grid} instance
     */
    public Grid getTable() {
        return adaptor.getComponent();
    }
}
