package org.tylproject.vaadin.addon.fieldbinder;

import com.vaadin.data.Property;
import com.vaadin.data.Validator;
import com.vaadin.ui.*;

import org.tylproject.vaadin.addon.datanav.BasicDataNavigation;
import org.tylproject.vaadin.addon.datanav.CrudButtonBar;
import org.tylproject.vaadin.addon.fieldbinder.strategies.TableNavigationStrategyFactory;
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
        table.setBuffered(true);
        table.setSizeFull();
        table.setHeight("300px");
        table.setSelectable(true);
        table.setMultiSelect(false);

        navigation = new BasicDataNavigation();
        navigation.setNavigationStrategyFactory(new TableNavigationStrategyFactory(this));

        compositionRoot.addComponent(table);
        this.containedBeanClass = containedBeanClass;


        // when the value of this wrapper (the list of values!)
        // changes, restore the table state:
        // (selectable = true, select id = null)
        this.addValueChangeListener(new ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                navigation.setCurrentItemId(null);
                table.select(null);
                table.setSelectable(true);
            }
        });

        // when someone selects an item on the actual table widget,
        // then update the navigator accordingly
        table.addValueChangeListener(new ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                navigation.setCurrentItemId(event.getProperty().getValue());
            }
        });


    }

    /**
     * Mimicks {@link com.vaadin.ui.Table} but automatically infer column names like a
     * {@link com.vaadin.data.fieldgroup.FieldGroup} or a {@link org.tylproject.vaadin.addon.fieldbinder.FieldBinder}
     */
    public void setVisibleColumns(Object ... visibleColumns) {
        this.visibleColumns = visibleColumns;
        table.setVisibleColumns(visibleColumns);
        setAllHeadersFromColumns(visibleColumns);
    }

    /**
     * Infers the column names from the column ids.
     *
     * Internally relies upon {@link com.vaadin.ui.DefaultFieldFactory}
     */
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

    /**
     * Returns the actual {@link com.vaadin.ui.Table} instance
     * @return
     */
    public Table getTable() {
        return table;
    }

    @Override
    public Class getType() {
        return List.class;
    }

    /**
     * return the type parameter for the List that this table contains
     */
    /**
     * @return the data type contained by the list
     */
    public Class<T> getListType() { return containedBeanClass; }

    @Override
    public void focus() {
        table.focus();
    }

    @Override
    protected void setInternalValue(List<T> list) {
        // reset the navigation status
        navigation.setCurrentItemId(null);

        super.setInternalValue(list);

        if (list == null) {
            // clears the table contents
            table.setContainerDataSource(null);
            table.select(null);
            navigation.setContainer(null);

        } else {
            FilterableListContainer<T> listContainer = new FilterableListContainer<T>(containedBeanClass);
            listContainer.setCollection(list);

            table.setContainerDataSource(listContainer);
            // clear selection
            table.select(null);
            navigation.setContainer(listContainer);

            if (visibleColumns != null) {
                this.setVisibleColumns(visibleColumns);
            } else {
                setAllHeadersFromColumns(table.getVisibleColumns());
            }

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
    public ListTable<T> withDefaultEditorBar() {
        CrudButtonBar buttonBar = buildDefaultCrudBar();
        compositionRoot.setSizeFull();

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
        return new CrudButtonBar(getNavigation().withDefaultBehavior());
    }

    /**
     * @return the DataNavigation instance bound to this component
     */
    public BasicDataNavigation getNavigation() {
        return navigation;
    }

}
