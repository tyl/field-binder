package org.tylproject.vaadin.addon.fieldbinder;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.ui.Component;
import org.tylproject.vaadin.addon.datanav.BasicDataNavigation;
import org.tylproject.vaadin.addon.datanav.DataNavigation;

/**
 * An interface to bridge between the Table API and the Grid API
 */
public interface TabularViewAdaptor<T extends Component> {

    public void setVisibleColumns(Object... visibleColumns);

    Object[] getVisibleColumns();

    public void setColumnHeaders(String... columnHeaders);
    public void setContainerDataSource(Container.Indexed dataSource);

    Container.Indexed getContainerDataSource();

    public void setEditable(boolean editable);
    public void setSelectable(boolean selectable);

    public T getComponent();
    public void focus();

    public void select(Object itemId);
    public Object getSelectedItemId();
    public Item getSelectedItem();

    public void commit();
    public void discard();

    void attachNavigation(BasicDataNavigation navigation);
}
