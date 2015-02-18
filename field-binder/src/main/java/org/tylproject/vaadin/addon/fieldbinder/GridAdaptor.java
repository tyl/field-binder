package org.tylproject.vaadin.addon.fieldbinder;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.event.SelectionEvent;
import com.vaadin.ui.Grid;
import org.tylproject.vaadin.addon.datanav.BasicDataNavigation;
import org.tylproject.vaadin.addon.datanav.DataNavigation;
import org.tylproject.vaadin.addon.fields.FilterableGrid;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Created by evacchi on 18/02/15.
 */
public class GridAdaptor<T> implements TabularViewAdaptor<Grid>, SelectionEvent.SelectionListener{
    private final Grid grid;
    private final Class<T> beanClass;
    private DataNavigation navigation;

    public GridAdaptor(Grid grid, Class<T> beanClass) {
        this.grid = grid;
        this.beanClass = beanClass;
    }

    public GridAdaptor(Class<T> beanClass) {
        this(new Grid(), beanClass);
    }

    @Override
    public void attachNavigation(BasicDataNavigation navigation) {
        grid.addSelectionListener(this);
        this.setNavigation(navigation);
        // TODO: navigation.setBehaviorFactory(new ...);
    }

    public void setVisibleColumns(Object... propertyIds) {
        assertAllPropertyIdsExist(getContainerDataSource().getContainerPropertyIds(), propertyIds);
        grid.removeAllColumns();
        for (Object propertyId: propertyIds) {
            grid.addColumn(propertyId);
        }
    }

    protected void assertAllPropertyIdsExist(Collection<?> containerPropertyIds, Object[] propertyIds) {
        for (Object propertyId: propertyIds) {
            assertPropertyIdExists(containerPropertyIds, propertyId);
        }
    }

    protected void assertPropertyIdExists(Collection<?> containerPropertyIds, Object propertyId) {
        if (!containerPropertyIds.contains(propertyId)) {
            throw new AssertionError(
                    String.format(
                            "Property id %s does not exist in container", propertyId));
        }
    }

    @Override
    public Object[] getVisibleColumns() {
        List<Grid.Column> columns = grid.getColumns();


        Object[] propertyIds = new Object[columns.size()];
        for (int i = 0; i < columns.size(); i++) {
            propertyIds[i] = columns.get(i).getPropertyId();
        }

        return propertyIds;
    }

    @Override
    public void setColumnHeaders(String... columnHeaders) {

        List<Grid.Column> columns = grid.getColumns();

        if (columnHeaders.length != columns.size()) {
            throw new IllegalArgumentException(
                "The given headers did not match the number of declared columns");
        }

        for (int i = 0; i < columnHeaders.length; i++) {
            columns.get(i).setHeaderCaption(columnHeaders[i]);
        }
    }

    @Override
    public void setContainerDataSource(Container.Indexed dataSource) {
        if (dataSource == null) return;
        grid.setContainerDataSource(dataSource);
    }

    @Override
    public Container.Indexed getContainerDataSource() {
        return grid.getContainerDataSource();
    }

    @Override
    public void setEditable(boolean editable) {
//        if (editable) {
//            grid.editItem(navigation.getCurrentItemId());
//        } else {
//            grid.cancelEditor();
//        }
    }

    @Override
    public void setSelectable(boolean selectable) {
//        if (selectable) {
//            grid.setSelectionMode(Grid.SelectionMode.SINGLE);
//        } else {
//            grid.setSelectionMode(Grid.SelectionMode.NONE);
//        }
    }

    @Override
    public Grid getComponent() {
        return grid;
    }

    @Override
    public void focus() {

    }

    @Override
    public void select(Object itemId) {
        grid.select(itemId);
    }

    @Override
    public Object getSelectedItemId() {
        return grid.getSelectedRow();
    }

    @Override
    public Item getSelectedItem() {
        return grid.getContainerDataSource().getItem(getSelectedItemId());
    }

    @Override
    public void commit() {

    }

    @Override
    public void discard() {

    }

    @Override
    public void select(SelectionEvent selectionEvent) {
        if (selectionEvent.getSelected().iterator().hasNext()){
            navigation.setCurrentItemId(null);
        } else {
            navigation.setCurrentItemId(selectionEvent.getSelected().iterator().next());
        }
    }

    public void setNavigation(DataNavigation navigation) {
        this.navigation = navigation;
    }

}
