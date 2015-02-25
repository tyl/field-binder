package org.tylproject.vaadin.addon.fieldbinder;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.event.SelectionEvent;
import com.vaadin.server.Page;
import com.vaadin.ui.Grid;
import org.tylproject.vaadin.addon.datanav.BasicDataNavigation;
import org.tylproject.vaadin.addon.datanav.DataNavigation;
import org.tylproject.vaadin.addon.fieldbinder.behavior.DefaultTableBehaviorFactory;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * Adaptor to the Grid API
 *
 * This implementation contains hacks to workaround some early Grid rough-edges.
 * The most reliable way to enter and leave editing mode is to send click events
 * to the client side. This must and will change, as more reliable server-side
 * API will be made available.
 */
public class GridAdaptor<T> implements TabularViewAdaptor<Grid> {
    private final Grid grid;
    private final Class<T> beanClass;
    private DataNavigation navigation;

    private final String id = UUID.randomUUID().toString();
    private final String vClass = ".v-grid-"+id;
    private static final String BUTTON_SAVE_CLASS = ".v-grid-editor-save";
    private static final String BUTTON_CANCEL_CLASS = ".v-grid-editor-cancel";
    private static final String jsCode = "document.querySelector('%s %s').click();";

    private final String jsCodeSave = String.format(
        jsCode, vClass, BUTTON_SAVE_CLASS);
    private final String jsCodeCancel = String.format(
        jsCode, vClass, BUTTON_CANCEL_CLASS);

    private static final String EDITOR_FOOTER_CLASS = ".v-grid-editor-footer";
    private static final String cssHideButtons = "%s %s { display:none !important; }";

    private final String cssHideButtonsForThisGrid = String.format(
        cssHideButtons, vClass, EDITOR_FOOTER_CLASS);

    public GridAdaptor(Grid grid, Class<T> beanClass) {
        this.grid = grid;
        this.beanClass = beanClass;
        grid.setEditorEnabled(true);
        grid.addStyleName(id);

        Page.getCurrent().getStyles().add(cssHideButtonsForThisGrid);

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
//            grid.setEditorEnabled(false);
            grid.markAsDirty();
        }
    }

    @Override
    public void setSelectable(boolean selectable) {
        if (selectable) {
            grid.setSelectionMode(Grid.SelectionMode.SINGLE);
        } else {
            grid.setSelectionMode(Grid.SelectionMode.NONE);
        }
    }

    @Override
    public Grid getComponent() {
        return grid;
    }

    @Override
    public void focus() {
        // no focus() method is available for grid
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

          if (grid.getEditedItemId() == null) {
              grid.setEditorEnabled(false);
              return;
          }
        grid.setCellStyleGenerator(grid.getCellStyleGenerator());

            // ugliest hack in world history
            Page.getCurrent().getJavaScript().execute(jsCodeSave);

//            grid.setEditorEnabled(false);
    }

    @Override
    public void discard() {
        Page.getCurrent().getJavaScript().execute(jsCodeCancel);
//        grid.setEditorEnabled(false);
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
