package org.tylproject.vaadin.addon.fields.zoom;

import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.tylproject.vaadin.addon.datanav.resources.Strings;
import org.tylproject.vaadin.addon.fieldbinder.BeanTable;

import java.util.ResourceBundle;

/**
 * Displays a table, allows to select an Item in the table,
 * returns the itemId of the selection
 */
public class TableZoomDialog<T> extends CustomComponent implements ZoomDialog<T> {

    private final BeanTable<?> beanTable;

    public TableZoomDialog(BeanTable<?> beanTable) {
        this.beanTable = beanTable;
        beanTable.setSizeFull();
        setCompositionRoot(beanTable);
    }

    public TableZoomDialog(BeanTable<?> beanTable, String caption) {
        this(beanTable);
        this.setCaption(caption);
    }

    public BeanTable<?> getTable() {
        return beanTable;
    }


    /**
     * Assumes the given value to be an itemId; tries to select it
     * in the table. A null value clears the selection
     *
     */
    @Override
    public void show(T value) {
        getTable().select(value);
    }

    /**
     * Returns the current selected itemId
     */
    @Override
    public T dismiss() {
        return (T) getTable().getSelectedItemId();
    }
}
