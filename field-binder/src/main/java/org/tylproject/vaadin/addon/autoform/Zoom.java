package org.tylproject.vaadin.addon.autoform;

import com.vaadin.data.Container;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import org.tylproject.vaadin.addon.datanav.DataNavigation;
import org.tylproject.vaadin.addon.fieldbinder.FieldBinder;

/**
 * Created by evacchi on 16/01/15.
 */
public interface Zoom<T> {
    Class<T> getType();
    Container.Ordered getContainer();
    void setContainer(Container.Ordered container);
    DataNavigation getNavigation();
    void setNavigation(DataNavigation navigation);
}
