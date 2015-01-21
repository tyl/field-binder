package org.tylproject.vaadin.addon.fields;

import com.vaadin.data.Container;
import com.vaadin.ui.Field;
import com.vaadin.ui.Grid;

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

    public <T extends Container.Indexed & Container.Filterable>
        FilterableGrid(T dataSource) {

        this(null, dataSource);
    }

    public <T extends Container.Indexed & Container.Filterable>
        FilterableGrid(String caption, T dataSource) {

        super(caption, dataSource);
        makeFilters(dataSource, dataSource.getContainerPropertyIds(), filterRow);
    }

    public void setVisibileColumns(Object... propertyIds) {
        assertAllPropertyIdsExist(getContainerDataSource().getContainerPropertyIds(), propertyIds);
        removeAllColumns();
        for (Object propertyId: propertyIds) {
            addColumn(propertyId);
        }
        visibleColumnIds = Arrays.asList(propertyIds);
        makeFilters((Container.Filterable)getContainerDataSource(), visibleColumnIds, filterRow);
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

    protected void makeFilters(Container.Filterable container, Collection<?> visibilePropertyIds, HeaderRow filterRow) {

        final Map<Object,Field<?>> localFilterMap = makeFilterFields(container, visibilePropertyIds);
        prepareFilterHeader(visibilePropertyIds, localFilterMap, filterRow);

        setFilterMap(localFilterMap);
    }

    protected Map<Object, Field<?>> makeFilterFields(Container.Filterable container, Collection<?> visiblePropertyIds) {
        final Map<Object,Field<?>> localFilterMap = new HashMap<>();
        for (Object propertyId: visiblePropertyIds) {

            localFilterMap.put(propertyId, makeFilterField(propertyId, container));
        }
        return localFilterMap;
    }

    protected Field<?> makeFilterField(final Object propertyId, Container.Filterable container) {
//        final Button clearBtn = new Button(FontAwesome.TIMES_CIRCLE);
//
//
//        final TextField textField = new TextField();
//        textField.setNullRepresentation("");
//        textField.setImmediate(true);
//        textField.addTextChangeListener(new FieldEvents.TextChangeListener() {
//            @Override
//            public void textChange(FieldEvents.TextChangeEvent event) {
//                String text = event.getText();
//
//                Container.Filterable container = (Container.Filterable)
//                getContainerDataSource();
//
//                Object oldFilter = textField.getData();
//                container.removeContainerFilter((Container.Filter) oldFilter);
//                clearBtn.setVisible(false);
//
//
//                if (text != null && !text.isEmpty()) {
//                    Container.Filter newFilter = new SimpleStringFilter(propertyId,
//                    text, true, false);
//                    textField.setData(newFilter);
//                    container.addContainerFilter(newFilter);
//
//                    clearBtn.setVisible(true);
//
//                }
//            }
//        });
//
//        clearBtn.addClickListener(new Button.ClickListener() {
//            @Override
//            public void buttonClick(Button.ClickEvent event) {
//                textField.setValue(null);
//                Object oldFilter = textField.getData();
//                Container.Filterable container = (Container.Filterable)
//                        getContainerDataSource();
//                container.removeContainerFilter((Container.Filter) oldFilter);
//            }
//        });
//
//
//        CombinedField<String, String, TextField> searchField = new CombinedField<>(textField, clearBtn, String.class);
//        textField.setWidth("100%");
        return new FilterExpressionField(propertyId, container.getType(propertyId), container);
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
