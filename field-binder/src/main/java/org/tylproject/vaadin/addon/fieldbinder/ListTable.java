package org.tylproject.vaadin.addon.fieldbinder;

import com.vaadin.data.Property;
import com.vaadin.data.Validator;
import com.vaadin.ui.*;

import org.tylproject.vaadin.addon.datanav.BasicDataNavigation;
import org.tylproject.vaadin.addon.datanav.CrudButtonBar;
import org.tylproject.vaadin.addon.datanav.DataNavigation;
import org.tylproject.vaadin.addon.datanav.events.CurrentItemChange;
import org.tylproject.vaadin.addon.fieldbinder.strategies.DefaultTableStrategy;
import org.vaadin.maddon.FilterableListContainer;

import java.util.List;

/**
 * A table wrapper where the value is a {@link java.util.List}
 *
 * Generally used together with {@link org.tylproject.vaadin.addon.fieldbinder.FieldBinder}
 */
public class ListTable<T> extends CustomField<List<T>> {

    protected final VerticalLayout compositionRoot = new VerticalLayout();
    protected final Table table;
    protected final Class<T> containedBeanClass;
    private Object[] visibleColumns;
    private final BasicDataNavigation navigation;

    public ListTable(Class<T> containedBeanClass) {
        table = new Table();
        table.setSizeFull();
        table.setHeight("300px");
        table.setSelectable(true);
        table.setMultiSelect(false);

        navigation = new BasicDataNavigation();

        compositionRoot.addComponent(table);
        this.containedBeanClass = containedBeanClass;


        navigation.setContainer(table);
        this.addValueChangeListener(new ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                navigation.setCurrentItemId(event.getProperty().getValue());
                table.setSelectable(true);
            }
        });
        table.addValueChangeListener(new ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                navigation.setCurrentItemId(event.getProperty().getValue());
            }
        });


    }

    public void setVisibleColumns(Object ... visibleColumns) {
        this.visibleColumns = visibleColumns;
        table.setVisibleColumns(visibleColumns);
        setAllHeadersFromColumns(visibleColumns);
    }

    private void setAllHeadersFromColumns(Object[] columns) {
        String[] headers = new String[columns.length];
        for (int i = 0; i < columns.length; i++) {
            Object propertyId = columns[i];
            headers[i] = DefaultFieldFactory.createCaptionByPropertyId(propertyId);
        }
        table.setColumnHeaders(headers);
    }

    @Override
    protected Component initContent() {
        return compositionRoot;
    }

    public Table getTable() {
        return table;
    }

    @Override
    public Class getType() {
        return List.class;
    }

    /**
     * @return the data type contained by the list
     */
    public Class<?> getListType() { return containedBeanClass; }

    @Override
    public void focus() {
        table.focus();
    }

    @Override
    protected void setInternalValue(List<T> newValue) {
        List<T> list = newValue;
        // reset the navigation status
        navigation.setCurrentItemId(null);

        super.setInternalValue(list);

        if (newValue == null) {
            table.setContainerDataSource(null);
        } else {
            FilterableListContainer<T> listContainer = new FilterableListContainer<T>(containedBeanClass);
            listContainer.setCollection(list);
            table.setContainerDataSource(listContainer);

            if (visibleColumns != null) {
                this.setVisibleColumns(visibleColumns);
            } else {
                setAllHeadersFromColumns(table.getVisibleColumns());
            }

            // clear selection
            table.select(null);
        }
    }

    @Override
    public void commit() throws SourceException, Validator.InvalidValueException {
        table.commit();
    }

    @Override
    public void discard() {
        table.discard();
    }

    /**
     * @return adds a default button bar to the bottom right of this component
     */
    public ListTable<T> withDefaultCrudBar() {
        CrudButtonBar buttonBar = buildDefaultCrudBar();
        compositionRoot.setSizeFull();

//        Label spacer = new Label("");
//        HorizontalLayout inner = new HorizontalLayout(spacer, buttonBar);
//        inner.setSizeFull();
//        inner.setWidth("100%");
//
////        inner.setExpandRatio(spacer, 1);
//        inner.setComponentAlignment(buttonBar, Alignment.BOTTOM_RIGHT);

        HorizontalLayout inner = new HorizontalLayout(buttonBar);
        inner.setSizeFull();
        inner.setComponentAlignment(buttonBar, Alignment.BOTTOM_RIGHT);

        compositionRoot.addComponent(inner);
        return this;
    }


    /**
     * build and returns a default button bar for this component
     * @return
     */
    public CrudButtonBar buildDefaultCrudBar() {
        navigation.withCrudListenersFrom(new DefaultTableStrategy<T>(containedBeanClass, table));
        final CrudButtonBar crudBar = new CrudButtonBar(navigation);
        navigation.addCurrentItemChangeListener(new CurrentItemChange.Listener() {
            @Override
            public void currentItemChange(CurrentItemChange.Event event) {
                table.select(event.getNewItemId());
            }
        });
        return crudBar;
    }

    /**
     * @return the DataNavigation instance bound to this component
     */
    public DataNavigation getNavigation() {
        return navigation;
    }

}
