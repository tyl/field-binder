package org.tylproject.vaadin.addon.fields.collectiontables.adaptors;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.ui.Table;
import org.tylproject.vaadin.addon.datanav.BasicDataNavigation;
import org.tylproject.vaadin.addon.datanav.DataNavigation;
import org.tylproject.vaadin.addon.fieldbinder.FieldBinder;
import org.tylproject.vaadin.addon.fieldbinder.behavior.DefaultTableBehaviorFactory;
import org.tylproject.vaadin.addon.fieldbinder.behavior.commons.Tables;

/**
 * Created by evacchi on 18/02/15.
 */
public class TableAdaptor<T> implements TabularViewAdaptor<T,Table>, Property.ValueChangeListener {
    final Table table;
    final Class<T> beanClass;
    final FieldBinder<T> fieldBinder;
    private DataNavigation navigation;

    public TableAdaptor(Table table, Class<T> beanClass, FieldBinder<T> fieldBinder) {
        this.table = table;
        this.beanClass = beanClass;
        this.fieldBinder = fieldBinder;
    }
    public TableAdaptor(Class<T> beanClass, FieldBinder<T> fieldBinder) {
        this(new Table(), beanClass, fieldBinder);
    }

    @Override
    public FieldBinder<T> getFieldBinder() {
        return fieldBinder;
    }

    @Override
    public FieldGroup getFieldGroup() {
        return fieldBinder.getFieldGroup();
    }

    @Override
    public void commit() {
        table.commit();
        fieldBinder.commit();
    }

    @Override
    public void setEditorDataSource(Item currentItem) {
        fieldBinder.setItemDataSource(currentItem);
    }

    @Override
    public void setVisibleColumns(Object... visibleColumns) {
        table.setVisibleColumns(visibleColumns);
    }

    @Override
    public Object[] getVisibleColumns() {
        return table.getVisibleColumns();
    }

    @Override
    public void setColumnHeaders(String... columnHeaders) {
        table.setColumnHeaders(columnHeaders);
    }

    @Override
    public void setContainerDataSource(Container.Indexed dataSource) {
        table.setContainerDataSource(dataSource);
    }

    @Override
    public Container.Indexed getContainerDataSource() {
        return (Container.Indexed) table.getContainerDataSource();
    }

    @Override
    public void setEditable(boolean editable) {
        table.setEditable(editable);
    }

    @Override
    public void setSelectable(boolean selectable) {
        table.setSelectable(selectable);
    }

    @Override
    public Table getComponent() {
        return table;
    }

    @Override
    public void focus() {
        fieldBinder.focus();
    }

    @Override
    public void select(Object itemId) {
        table.select(itemId);
    }

    @Override
    public Object getSelectedItemId() {
        return table.getValue();
    }

    @Override
    public Item getSelectedItem() {
        return table.getItem(table.getValue());
    }

    @Override
    public void discard() {
        table.discard();
    }

    @Override
    public void attachNavigation(final BasicDataNavigation navigation) {
        if (this.navigation != null) throw new IllegalStateException("Cannot attach navigation twice");

        table.addValueChangeListener(this);
        this.setNavigation(navigation);
        navigation.setBehaviorFactory(new DefaultTableBehaviorFactory(beanClass, this));
        table.setTableFieldFactory(new Tables.SingleLineFieldFactory(fieldBinder));


//        this.table.setTableFieldFactory(
//            new Tables.SingleLineFieldFactory(table.getTableFieldFactory(), navigation));


    }

    public void valueChange(Property.ValueChangeEvent event) {
        navigation.setCurrentItemId(event.getProperty().getValue());
    }

    public void setNavigation(DataNavigation navigation) {
        this.navigation = navigation;
    }
}
