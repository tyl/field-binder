package org.tylproject.vaadin.addon.masterdetail.builder;

import com.vaadin.data.Property;
import com.vaadin.ui.Table;
import org.tylproject.vaadin.addon.crudnav.BasicCrudNavigation;
import org.tylproject.vaadin.addon.crudnav.CrudNavigation;
import org.tylproject.vaadin.addon.crudnav.events.CurrentItemChange;

/**
 * Created by evacchi on 26/11/14.
 */
public class NavigableTable extends NavigableContainerViewer<Table>
        implements CurrentItemChange.Listener, Property.ValueChangeListener {
    public NavigableTable(Table table, BasicCrudNavigation navigation) {
        super(table, navigation);
        table.addValueChangeListener(this);
        navigation.addCurrentItemChangeListener(this);
    }
    public Table getTable() {
        return super.getContainerViewer();
    }

    /**
     * updates the table selection when the navigation status changes
     */
    @Override
    public void currentItemChange(CurrentItemChange.Event event) {
        getTable().select(event.getNewItemId());
    }

    /**
     * updates the navigation current item when the table selection changes
     */
    @Override
    public void valueChange(Property.ValueChangeEvent event) {
        getNavigation().setCurrentItemId(event.getProperty().getValue());
    }
}
