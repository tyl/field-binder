package org.tylproject.vaadin.addon.fields.collectiontables.adaptors;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.ui.Component;
import org.tylproject.vaadin.addon.datanav.BasicDataNavigation;
import org.tylproject.vaadin.addon.datanav.DataNavigation;
import org.tylproject.vaadin.addon.fieldbinder.FieldBinder;

/**
 * An interface to bridge between the Table API and the Grid API
 */
public interface TabularViewAdaptor<T,U extends Component> {


    public void setVisibleColumns(Object... visibleColumns);

    Object[] getVisibleColumns();

    public void setColumnHeaders(String... columnHeaders);
    public void setContainerDataSource(Container.Indexed dataSource);

    Container.Indexed getContainerDataSource();

    public void setEditable(boolean editable);
    public void setSelectable(boolean selectable);

    public U getComponent();
    public void focus();

    public void select(Object itemId);
    public Object getSelectedItemId();
    public Item getSelectedItem();

    FieldBinder<T> getFieldBinder();

    FieldGroup getFieldGroup();

    public void commit();
    public void discard();


    void attachNavigation(BasicDataNavigation navigation);

    void setEditorDataSource(Item currentItem);

}
