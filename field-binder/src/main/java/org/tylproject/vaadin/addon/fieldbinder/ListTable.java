package org.tylproject.vaadin.addon.fieldbinder;

import com.vaadin.data.Buffered;
import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.data.Validator;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.Table;
import org.vaadin.maddon.ListContainer;

import java.util.List;

/**
 * Created by evacchi on 03/12/14.
 */
public class ListTable<T> extends CustomField<List<T>> {

    protected final Table table = new Table();
    protected final Class<T> containedBeanClass;

    public ListTable(Class<T> containedBeanClass) {
        this.containedBeanClass = containedBeanClass;
    }

    @Override
    protected Component initContent() {
        return table;
    }

    public Table getTable() {
        return table;
    }

    @Override
    public Class getType() {
        return List.class;
    }

    @Override
    protected void setInternalValue(List<T> newValue) {
        List<T> list = newValue;
        super.setInternalValue(list);

        if (newValue == null) {
            table.setContainerDataSource(null);
        } else {
            ListContainer<?> listContainer = new ListContainer<T>(containedBeanClass, list);
            table.setContainerDataSource(listContainer);
        }
    }

    @Override
    public void setPropertyDataSource(Property newDataSource) {
        super.setPropertyDataSource(newDataSource);
    }

    @Override
    public void commit() throws SourceException, Validator.InvalidValueException {
        table.commit();
    }


}
