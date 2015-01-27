package org.tylproject.vaadin.addon.fields.zoom;

import com.vaadin.data.Item;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.DefaultFieldFactory;
import com.vaadin.ui.Grid;
import com.vaadin.ui.VerticalLayout;

/**
 * Created by evacchi on 20/01/15.
 */
public class GridZoomDialog<T> extends VerticalLayout implements ZoomDialog<T> {

    private final Grid grid;
    private Object propertyId;


    public GridZoomDialog(Grid grid) {
        this.grid = grid;

        if (!(grid.getSelectionModel() instanceof Grid.SingleSelectionModel)) {
            throw new AssertionError(
                    "Selection mode must be "+Grid.SelectionMode.SINGLE.getClass()+", "+
                            grid.getSelectionModel().getClass() +" was given");
        }

        setCaption(DefaultFieldFactory.createCaptionByPropertyId(propertyId));
        addComponent(grid);
        setExpandRatio(getGrid(), 1f);
        setSizeFull();

    }


    public GridZoomDialog(Grid grid, Object propertyId) {
        this(grid);
        this.propertyId = propertyId;
    }

    public GridZoomDialog(Grid grid, Object propertyId, String caption) {
        this(grid, propertyId);
        this.setCaption(caption);
    }

    public GridZoomDialog withPropertyId(Object propertyId) {
        this.propertyId = propertyId;
        return this;
    }


    public Grid getGrid() {
        return grid;
    }


    /**
     * Assumes the given value to be an itemId; tries to select it
     * in the table. A null value clears the selection
     *
     */
    @Override
    public void show(T value) {

    }

    /**
     * Returns the current selected value
     */
    @Override
    public T dismiss() {
        Object itemId = getGrid().getSelectedRow();
        if (itemId == null) return null;
        Item item = getGrid().getContainerDataSource().getItem(itemId);
        return (T) item.getItemProperty(propertyId).getValue();
    }
}
