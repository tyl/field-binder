package org.tylproject.vaadin.addon.fieldbinder.behavior.commons;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.FieldGroupFieldFactory;
import com.vaadin.ui.*;
import org.tylproject.vaadin.addon.datanav.events.OnCommit;
import org.tylproject.vaadin.addon.datanav.events.OnDiscard;
import org.tylproject.vaadin.addon.fieldbinder.FieldBinderFieldFactory;
import org.tylproject.vaadin.addon.fieldbinder.GridAdaptor;
import org.tylproject.vaadin.addon.fieldbinder.TableAdaptor;
import org.tylproject.vaadin.addon.fieldbinder.TabularViewAdaptor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by evacchi on 25/02/15.
 */
public class ManagedTableCrud<T> extends Tables.BaseCrud<T> {
    final protected FieldManager fieldManager;


    public ManagedTableCrud(final Class<T> beanClass, final TabularViewAdaptor<?> tableAdaptor) {
        super(beanClass, tableAdaptor);
        fieldManager = initFieldManager();

    }

    @Override
    public void onDiscard(OnDiscard.Event event) {
        getFieldManager().discardFields();
        super.onDiscard(event);
    }

    @Override
    public void onCommit(OnCommit.Event event) {
        getFieldManager().commitFields();
        super.onCommit(event);
    }

    private FieldManager initFieldManager() {

        if (tableAdaptor instanceof TableAdaptor) {
            FieldManager fieldManager;

            Table table = ((Table)tableAdaptor.getComponent());

            TableFieldFactory tff = table.getTableFieldFactory();

            // if a field manager has been already set, then just attach to that
            // (might be user-defined)
            if (tff instanceof FieldManager) {
                fieldManager = ((FieldManager) tff);
            } else if (tff == null || tff.getClass() == DefaultFieldFactory.class) {
                // otherwise if the factory is **exactly** the default,
                // replace with *our* defaults
                final FieldGroupFieldFactory fieldFactory = new FieldBinderFieldFactory();
                fieldManager = new FieldManager(tableAdaptor, fieldFactory);
            } else {
                throw new IllegalStateException("Cannot manage fields in Table. " +
                        "Use Tables.setFieldFactory() to use a custom FieldFactory");
            }

            table.setTableFieldFactory(fieldManager);
            return fieldManager;

        } else if (tableAdaptor instanceof GridAdaptor) {
            FieldManager fieldManager = new FieldManager(tableAdaptor, new FieldBinderFieldFactory());
            ((Grid)tableAdaptor.getComponent()).setEditorFieldGroup(fieldManager);
            return fieldManager;
        } else {
            throw new IllegalArgumentException("Unsupported adaptor. Cannot initialize FieldManager");
        }
    }

    public FieldManager getFieldManager() {
        return fieldManager;
    }


    /**
     * Mimics a (lighter-weight) FieldBinder for Table inline-editing
     */
    public static class FieldManager extends FieldGroup implements TableFieldFactory {
        final List<Field<?>> fields = new ArrayList<>();
        final TabularViewAdaptor<?> tableAdaptor;
        final Table table;


        public FieldManager(TabularViewAdaptor<?> tableAdaptor) {
            this(tableAdaptor, new FieldBinderFieldFactory());
        }

        public FieldManager(TabularViewAdaptor<?> tableAdaptor, FieldGroupFieldFactory fieldFactory) {
            this.tableAdaptor = tableAdaptor;
            if (tableAdaptor instanceof TableAdaptor) {
                this.table = (Table) tableAdaptor.getComponent();
            } else {
                table = null;
            }
            setFieldFactory(fieldFactory);
        }

        public FieldManager(Table table) {
            this(table, new FieldBinderFieldFactory());
        }

        public FieldManager(Table table, FieldGroupFieldFactory fieldFactory) {
            this.tableAdaptor = null;
            this.table = table;
            setFieldFactory(fieldFactory);
        }


        public Object getSelectedItemId() {
            if (tableAdaptor != null) {
                return tableAdaptor.getSelectedItemId();
            } else {
                return Objects.requireNonNull(table).getValue();
            }
        }


        public Item getSelectedItem() {
            if (tableAdaptor != null) {
                return tableAdaptor.getSelectedItem();
            } else {
                return table.getContainerDataSource().getItem(Objects.requireNonNull
                        (table).getValue());
            }
        }


        @Override
        public Field<?> createField(Container container, Object itemId, Object propertyId, Component uiContext) {
            // no item selected: skip
            if (itemId == null) return null;
            // the given item is NOT the selected item: skip
            if (!itemId.equals(getSelectedItemId())) return null;

            this.setItemDataSource(getSelectedItem());



            // otherwise
            Class<?> dataType = container.getType(propertyId);
            Field<?> f = this.buildAndBind(propertyId);


//            Field<?> f = getFieldFactory().createField(dataType, Field.class);
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

//
//    public static <T,U extends Collection<T>,X extends CollectionTabularView<T,U>>
//        X setFieldFactory(X table, FieldGroupFieldFactory fieldFactory) {
//
//        TabularViewAdaptor<?> adaptor = table.getAdaptor();
//        if (adaptor instanceof TableAdaptor) {
//            setFieldFactory((Table) adaptor.getComponent(), fieldFactory);
//        } else if (adaptor instanceof GridAdaptor) {
//            setFieldFactory((Grid) adaptor.getComponent(), fieldFactory);
//        } else throw new IllegalStateException("Cannot set FieldGroupFieldFactory. "+
//                    "Unknown adaptor class: "+adaptor);
//
//        return table;
//    }
//
//
//
//
//    /**
//     * adapts a FieldGroupFieldFactory to work with a FieldBinder-generated Table
//     */
//    public static Table setFieldFactory(Table table, FieldGroupFieldFactory fieldFactory) {
//        TableFieldFactory tff = table.getTableFieldFactory();
//        if (tff instanceof Tables.FieldManager) {
//            ((FieldManager) tff).setFieldFactory(fieldFactory);
//        } else {
//            throw new IllegalArgumentException("Cannot set FieldGroupFieldFactory. " +
//                    "The table must contain a valid Tables.FieldManager instance. " +
//                    "Was: " + tff == null? null: tff.getClass().getCanonicalName());
//        }
//
//        return table;
//    }
//
//    public static Grid setFieldFactory(Grid grid, FieldGroupFieldFactory fieldFactory) {
//        FieldGroup fieldGroup = grid.getEditorFieldGroup();
//        if (fieldGroup instanceof Tables.FieldManager) {
//            fieldGroup.setFieldFactory(fieldFactory);
//        } else {
//            throw new IllegalArgumentException("Cannot set FieldGroupFieldFactory. " +
//                    "The Grid must contain a valid Tables.FieldManager instance. " +
//                    "Was: " + fieldGroup == null? null: fieldGroup.getClass().getCanonicalName());
//        }
//
//        return grid;
//    }
//
//    public static FieldManager getFieldManager(CollectionTabularView<?,?> collectionTable) {
//        TabularViewAdaptor<?> adaptor = collectionTable.getAdaptor();
//        if (adaptor instanceof TableAdaptor) {
//            FieldManager fieldManager;
//
//            Table table = ((Table)adaptor.getComponent());
//
//            TableFieldFactory tff = table.getTableFieldFactory();
//
//            // if a field manager has been already set, then just attach to that
//            // (might be user-defined)
//            if (tff instanceof Tables.FieldManager) {
//                return ((FieldManager) tff);
//            }
//        } else if (adaptor instanceof GridAdaptor) {
//            FieldGroup fieldGroup  = ((Grid) adaptor.getComponent()).getEditorFieldGroup();
//            if (fieldGroup instanceof Tables.FieldManager) {
//                return (FieldManager)fieldGroup;
//            }
//        }
//        throw new IllegalArgumentException("Unsupported adaptor. Cannot get FieldManager");
//
//    }
}

