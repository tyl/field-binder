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
        FilterPatternField f ;

        Class<?> type = container.getType(propertyId);

        if (java.lang.Enum.class.isAssignableFrom(type)) {
            f = new FilterPatternComboBox(propertyId, (Class<java.lang.Enum>)type, container);
        } else {
            f = new FilterPatternTextField(propertyId, type, container);
        }

        return f;
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
