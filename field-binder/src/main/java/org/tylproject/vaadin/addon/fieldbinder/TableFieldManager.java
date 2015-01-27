package org.tylproject.vaadin.addon.fieldbinder;

import com.vaadin.data.Container;
import com.vaadin.data.fieldgroup.FieldGroupFieldFactory;
import com.vaadin.ui.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by evacchi on 27/01/15.
 */
public class TableFieldManager implements TableFieldFactory {
    final FieldGroupFieldFactory fieldFactory ;
    final List<Field<?>> fields = new ArrayList<>();
    final Table table;


    public TableFieldManager(Table table) {
        this(table, new FieldBinderFieldFactory());
    }

    public TableFieldManager(Table table, FieldGroupFieldFactory fieldFactory) {
        this.fieldFactory = fieldFactory;
        this.table = table;
    }

    @Override
    public Field<?> createField(Container container, Object itemId, Object propertyId, Component uiContext) {
        if (itemId == null || !itemId.equals(table.getValue())) return null;

        Class<?> dataType = container.getType(propertyId);
        Field<?> f = fieldFactory.createField(dataType, Field.class);
        if (f instanceof AbstractTextField) {
            ((AbstractTextField) f).setNullRepresentation("");
            ((AbstractTextField) f).setImmediate(true);
        }
        f.setBuffered(true);
        fields.add(f);
        return f;
    }


    public void commitFields() {
        for (Field<?> f: fields) {
            f.commit();
        }
        fields.clear();
    }
    public void discardFields() {
        for (Field<?> f: fields) {
            f.discard();
        }
        fields.clear();
    }

}
