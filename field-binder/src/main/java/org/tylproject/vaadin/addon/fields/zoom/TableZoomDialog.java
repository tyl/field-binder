package org.tylproject.vaadin.addon.fields.zoom;

import com.vaadin.data.Item;
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
    private Object propertyId;

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


    public TableZoomDialog<T> withPropertyId(Object propertyId) {
        this.propertyId = propertyId;
        return this;
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
        Object itemId = beanTable.getSelectedItemId();
        if (itemId == null) return null;
        Item item = beanTable.getContainerDataSource().getItem(itemId);
        return (T) item.getItemProperty(propertyId).getValue();
    }}
