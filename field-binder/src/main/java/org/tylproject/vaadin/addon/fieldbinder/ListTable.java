package org.tylproject.vaadin.addon.fieldbinder;

import com.vaadin.data.Property;
import com.vaadin.data.Validator;
import com.vaadin.ui.*;

import org.tylproject.vaadin.addon.datanav.BasicCrudNavigation;
import org.tylproject.vaadin.addon.datanav.CrudButtonBar;
import org.tylproject.vaadin.addon.datanav.CrudNavigation;
import org.tylproject.vaadin.addon.datanav.events.CurrentItemChange;
import org.tylproject.vaadin.addon.fieldbinder.strategies.DefaultTableStrategy;
import org.vaadin.maddon.FilterableListContainer;

import java.util.List;

/**
 * Created by evacchi on 03/12/14.
 */
public class ListTable<T> extends CustomField<List<T>> {

    protected final VerticalLayout compositionRoot = new VerticalLayout();
    protected final Table table;
    protected final Class<T> containedBeanClass;
    private Object[] visibleColumns;
    private final CrudNavigation navigation;

    public ListTable(Class<T> containedBeanClass) {
        table = new Table();
        table.setSizeFull();
        table.setHeight("300px");
        table.setSelectable(true);
        table.setMultiSelect(false);

        navigation = new BasicCrudNavigation();

        compositionRoot.addComponent(table);
        this.containedBeanClass = containedBeanClass;


        this.addValueChangeListener(new ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                System.out.println("yay");
            }
        });

        navigation.setContainer(table);


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
            //FIXME: event won't fire if the last selection was already null!
            //MEMO: would it be worth to attach a navigation by default?
            table.setValue(null);
            table.select(null);
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


    public ListTable<T> withDefaultCrudBar() {
        CrudButtonBar buttonBar = buildDefaultCrudBar();
        compositionRoot.setSizeFull();

        Label spacer = new Label("");
        HorizontalLayout inner = new HorizontalLayout(spacer, buttonBar);
        inner.setSizeFull();
        inner.setWidth("100%");

//        inner.setExpandRatio(spacer, 1);
        inner.setComponentAlignment(buttonBar, Alignment.BOTTOM_RIGHT);

        compositionRoot.addComponent(inner);
        return this;
    }


    public CrudButtonBar buildDefaultCrudBar() {
        final BasicCrudNavigation nav = new BasicCrudNavigation(table);
        nav.withCrudListenersFrom(new DefaultTableStrategy<T>(containedBeanClass, table));
        final CrudButtonBar crudBar = new CrudButtonBar(nav);
        nav.addCurrentItemChangeListener(new CurrentItemChange.Listener() {
            @Override
            public void currentItemChange(CurrentItemChange.Event event) {
                table.select(event.getNewItemId());
            }
        });
        this.addValueChangeListener(new NavUpdater(nav));

        return crudBar;
    }

    static class NavUpdater implements ValueChangeListener {
        private final BasicCrudNavigation nav;

        NavUpdater(BasicCrudNavigation nav) {
            this.nav = nav;
        }
        @Override
        public void valueChange(Property.ValueChangeEvent event) {
            nav.setCurrentItemId(event.getProperty().getValue());
        }
    }


}
