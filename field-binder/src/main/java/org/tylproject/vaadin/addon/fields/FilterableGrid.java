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
import org.tylproject.vaadin.addon.fields.search.SearchPatternComboBox;
import org.tylproject.vaadin.addon.fields.search.SearchPatternField;
import org.tylproject.vaadin.addon.fields.search.SearchPatternTextField;

import java.util.*;

/**
 * Extensions to the Grid that supports default filters
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

        if (!(dataSource instanceof Container.Filterable)) {
            throw new IllegalArgumentException("Container must be Filterable");
        }

        setSelectionMode(Grid.SelectionMode.SINGLE);
        setSizeFull();
        setWidth("100%");
        setHeight("100%");

        makeFilters((Container.Filterable)dataSource, dataSource.getContainerPropertyIds(), filterRow);
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
        SearchPatternField f ;

        Class<?> type = container.getType(propertyId);

        if (java.lang.Enum.class.isAssignableFrom(type)) {
            f = new SearchPatternComboBox(propertyId, type, container);
        } else {
            f = new SearchPatternTextField(propertyId, type, container);
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
