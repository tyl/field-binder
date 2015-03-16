/*
 * Copyright (c) 2015 - Tyl Consulting s.a.s.
 *
 *   Authors: Edoardo Vacchi
 *   Contributors: Marco Pancotti, Daniele Zonca
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.tylproject.vaadin.addon.fields;

import com.vaadin.data.Container;
import com.vaadin.ui.Field;
import com.vaadin.ui.Grid;
import org.tylproject.vaadin.addon.fields.search.*;

import java.util.*;

/**
 * Extensions to the Grid that supports default filters
 */
public class FilterableGrid extends Grid {

    final Map<Object,Field<?>> searchFieldMap = new HashMap<>();
    final Grid.HeaderRow searchFieldRow = this.appendHeaderRow();
    Collection<?> visibleColumnIds;
    final SearchFieldFactory searchFieldFactory = new DefaultSearchFieldFactory();


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

        if (!(dataSource instanceof Container.Filterable)) {
            throw new IllegalArgumentException("Container must be Filterable");
        }

        setSelectionMode(Grid.SelectionMode.SINGLE);
        setSizeFull();
        setWidth("100%");
        setHeight("100%");

        makeSearchHeader(
                (Container.Filterable) dataSource,
                dataSource.getContainerPropertyIds(),
                searchFieldRow);
    }

    public SearchFieldFactory getSearchFieldFactory() {
        return searchFieldFactory;
    }

    /**
     * shorthand à la {@link com.vaadin.ui.Table} to set visible grids
     * @param propertyIds
     */
    public void setVisibileColumns(Object... propertyIds) {
        assertAllPropertyIdsExist(getContainerDataSource().getContainerPropertyIds(), propertyIds);
        removeAllColumns();
        for (Object propertyId: propertyIds) {
            addColumn(propertyId);
        }
        visibleColumnIds = Arrays.asList(propertyIds);
        makeSearchHeader((Container.Filterable) getContainerDataSource(), visibleColumnIds,
                searchFieldRow);
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

    protected void makeSearchHeader(Container.Filterable container, Collection<?> visiblePropertyIds, HeaderRow filterRow) {
        final Map<Object,Field<?>> localFilterMap = makeSearchFields(container, visiblePropertyIds);
        prepareFilterHeader(visiblePropertyIds, localFilterMap, filterRow);

        setSearchFieldMap(localFilterMap);
    }


    protected void prepareFilterHeader(Collection<?> propertyIds, Map<Object, Field<?>> searchFieldMap, HeaderRow filterRow) {
        for (Object propertyId: propertyIds) {
            HeaderCell cell = filterRow.getCell(propertyId);
            Field<?> f = searchFieldMap.get(propertyId);
            cell.setComponent(f);
        }
    }

    protected Map<Object, Field<?>> makeSearchFields(Container.Filterable container, Collection<?> visiblePropertyIds) {
        final Map<Object, Field<?>> localSearchFieldMap = new HashMap<>();
        for (Object propertyId : visiblePropertyIds) {
            localSearchFieldMap.put(propertyId, makeSearchField(propertyId, container));
        }
        return localSearchFieldMap;
    }

    protected Field<?> makeSearchField(final Object propertyId, Container.Filterable container) {
        Class<?> type = container.getType(propertyId);
        return searchFieldFactory.createField(propertyId, type, container);
    }


    protected void setSearchFieldMap(Map<Object, ? extends Field<?>> searchFieldMap) {
        this.searchFieldMap.clear();
        this.searchFieldMap.putAll(searchFieldMap);
    }


}
