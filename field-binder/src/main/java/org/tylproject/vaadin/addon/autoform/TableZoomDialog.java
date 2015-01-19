package org.tylproject.vaadin.addon.autoform;

import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Window;
import org.tylproject.vaadin.addon.fieldbinder.BeanTable;

/**
 * Created by evacchi on 19/01/15.
 */
public class TableZoomDialog<T> extends CustomComponent implements ZoomDialog {

    private final BeanTable<T> beanTable;
    private final Class<T> beanClass;

    public TableZoomDialog(BeanTable<T> beanTable) {
        this.beanTable = beanTable;
        this.beanClass = beanTable.getType();
        setCompositionRoot(beanTable);
    }

    public BeanTable<T> getTable() {
        return beanTable;
    }

    @Override
    public Object dismiss() {
        return getTable().getSelectedItemId();
    }

    @Override
    public void show(Object itemId) {
        getTable().select(itemId);
    }
}
