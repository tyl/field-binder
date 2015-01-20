package org.tylproject.vaadin.addon.fields.zoom;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Grid;

/**
 * Created by evacchi on 20/01/15.
 */
public class GridZoomDialog<T> extends CustomComponent implements ZoomDialog<T> {

    private final Grid grid;

    public GridZoomDialog(Grid grid) {
        this.grid = grid;
        if (!(grid.getSelectionModel() instanceof Grid.SingleSelectionModel)) {
            throw new AssertionError("Selection mode must be "+Grid.SelectionMode.SINGLE.getClass()+", "+grid.getSelectionModel().getClass() +" was given");
        }
        setCompositionRoot(grid);
    }

    public GridZoomDialog(Grid grid, String caption) {
        this(grid);
        this.setCaption(caption);
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
        getGrid().select(value);
    }

    /**
     * Returns the current selected itemId
     */
    @Override
    public T dismiss() {
        return (T) getGrid().getSelectedRow();
    }
}
