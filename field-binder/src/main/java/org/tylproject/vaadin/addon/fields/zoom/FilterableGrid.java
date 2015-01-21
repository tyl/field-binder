package org.tylproject.vaadin.addon.fields.zoom;

import com.vaadin.data.Container;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.event.FieldEvents;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.Field;
import com.vaadin.ui.Grid;
import com.vaadin.ui.TextField;
import org.tylproject.vaadin.addon.fields.CombinedField;

import java.util.*;

/**
 * Created by evacchi on 21/01/15.
 */
public class FilterableGrid extends Grid {

    final Map<Object,Field<?>> filterMap = new HashMap<>();
    final Grid.HeaderRow filterRow = this.appendHeaderRow();
    Collection<?> visibleColumnIds;


    public FilterableGrid() {
        this(null, null);
    }

    public FilterableGrid(String caption) {
        this(caption, null);
    }

    public FilterableGrid(Container.Indexed dataSource) {
        this(null, dataSource);
    }

    public FilterableGrid(String caption, Container.Indexed dataSource) {
        super(caption, dataSource);
        makeFilters(getContainerDataSource().getContainerPropertyIds(), filterRow);
    }

    public void setVisibileColumns(Object... propertyIds) {
        assertAllPropertyIdsExist(getContainerDataSource().getContainerPropertyIds(), propertyIds);
        removeAllColumns();
        for (Object propertyId: propertyIds) {
            addColumn(propertyId);
        }
        visibleColumnIds = Arrays.asList(propertyIds);
        makeFilters(visibleColumnIds, filterRow);
    }

    protected void assertAllPropertyIdsExist(Collection<?> containerPropertyIds, Object[] propertyIds) {
        for (Object propertyId: propertyIds) {
            assertPropertyIdExists(containerPropertyIds, propertyId);
        }
    }

    protected void assertPropertyIdExists(Collection<?> containerPropertyIds, Object propertyId) {
        if (!containerPropertyIds.contains(propertyId)) {
            throw new AssertionError(
                String.format(
                "Property id %s does not exist in container", propertyId));
        }
    }

    protected void makeFilters(Collection<?> propertyIds, Grid.HeaderRow filterRow) {

        final Map<Object,Field<?>> localFilterMap = makeFilterFields(propertyIds);
        prepareFilterHeader(propertyIds, localFilterMap, filterRow);

        setFilterMap(localFilterMap);
    }

    private Map<Object, Field<?>> makeFilterFields(Collection<?> propertyIds) {
        final Map<Object,Field<?>> localFilterMap = new HashMap<>();
        for (Object propertyId: propertyIds) {
            localFilterMap.put(propertyId, makeFilterField(propertyId));
        }
        return localFilterMap;
    }

    private Field<?> makeFilterField(final Object propertyId) {
        final Button clearBtn = new Button(FontAwesome.TIMES_CIRCLE);


        final TextField textField = new TextField();
        textField.setNullRepresentation("");
        textField.setImmediate(true);
        textField.addTextChangeListener(new FieldEvents.TextChangeListener() {
            @Override
            public void textChange(FieldEvents.TextChangeEvent event) {
                String text = event.getText();

                Container.Filterable container = (Container.Filterable)
                getContainerDataSource();

                Object oldFilter = textField.getData();
                container.removeContainerFilter((Container.Filter) oldFilter);
                clearBtn.setVisible(false);


                if (text != null && !text.isEmpty()) {
                    Container.Filter newFilter = new SimpleStringFilter(propertyId,
                    text, true, false);
                    textField.setData(newFilter);
                    container.addContainerFilter(newFilter);

                    clearBtn.setVisible(true);

                }
            }
        });

        clearBtn.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                textField.setValue(null);
                Object oldFilter = textField.getData();
                Container.Filterable container = (Container.Filterable)
                        getContainerDataSource();
                container.removeContainerFilter((Container.Filter) oldFilter);
            }
        });


        CombinedField<String, String, TextField> searchField = new CombinedField<>(textField, clearBtn, String.class);
        textField.setWidth("100%");
        return searchField;
    }

    protected void prepareFilterHeader(Collection<?> propertyIds, Map<Object, Field<?>>
     localFilterMap, HeaderRow filterRow) {
        for (Object propertyId: propertyIds) {
            HeaderCell cell = filterRow.getCell(propertyId);
            Field<?> f = localFilterMap.get(propertyId);
            cell.setComponent(f);
        }
    }

    protected void setFilterMap(Map<Object, ? extends Field<?>> filterMap) {
        this.filterMap.clear();
        this.filterMap.putAll(filterMap);
    }


}
