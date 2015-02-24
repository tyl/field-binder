package org.tylproject.vaadin.addon.fieldbinder;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.event.SelectionEvent;
import com.vaadin.server.ServerRpcManager;
import com.vaadin.ui.Grid;
import org.tylproject.vaadin.addon.datanav.BasicDataNavigation;
import org.tylproject.vaadin.addon.datanav.DataNavigation;
import org.tylproject.vaadin.addon.fieldbinder.behavior.DefaultTableBehaviorFactory;
import org.tylproject.vaadin.addon.fields.FilterableGrid;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Created by evacchi on 18/02/15.
 */
public class GridAdaptor<T> implements TabularViewAdaptor<Grid> {
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
        grid.addSelectionListener(selectionListener);
        this.setNavigation(navigation);
        navigation.setBehaviorFactory(new DefaultTableBehaviorFactory(beanClass, this));
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
        if (editable) {
            grid.setEditorEnabled(true);
            grid.editItem(navigation.getCurrentItemId());
        } else {
            grid.setEditorEnabled(false);
//            grid.markAsDirty();
        }
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
        try {
            grid.saveEditor();


            grid.cancelEditor();


            grid.setEditorEnabled(false);



            grid.setCellStyleGenerator(grid.getCellStyleGenerator());
//            grid.setEditorEnabled(false);
        } catch (FieldGroup.CommitException ex) {
            throw new CommitException(ex);
        }
    }

    @Override
    public void discard() {
        grid.cancelEditor();
        grid.setEditorEnabled(false);
    }

    private final SelectionEvent.SelectionListener selectionListener = new SelectionEvent.SelectionListener() {

        @Override
        public void select(SelectionEvent selectionEvent) {
            if (selectionEvent.getSelected().iterator().hasNext()) {
                navigation.setCurrentItemId(selectionEvent.getSelected().iterator().next());
            } else {
                navigation.setCurrentItemId(null);

            }
        }
    };

    public void setNavigation(DataNavigation navigation) {
        this.navigation = navigation;
    }

}
