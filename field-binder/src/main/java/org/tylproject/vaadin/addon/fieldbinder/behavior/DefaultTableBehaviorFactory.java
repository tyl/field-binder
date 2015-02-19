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

package org.tylproject.vaadin.addon.fieldbinder.behavior;

import com.vaadin.data.Container;
import com.vaadin.ui.Table;
import org.tylproject.vaadin.addon.datanav.events.CurrentItemChange;
import org.tylproject.vaadin.addon.fieldbinder.TabularViewAdaptor;
import org.tylproject.vaadin.addon.fieldbinder.behavior.commons.SearchWindowFindListeners;
import org.tylproject.vaadin.addon.fieldbinder.behavior.commons.Tables;
import org.tylproject.vaadin.addon.fieldbinder.behavior.containers.jpacontainer
.JPAContainerTableCrud;
import org.tylproject.vaadin.addon.fieldbinder.behavior.containers.listcontainer
.ListContainerTableCrud;
import org.tylproject.vaadin.addon.fieldbinder.behavior.containers.mongocontainer
.BufferedMongoTableCrud;
import org.tylproject.vaadin.addon.fields.search.SearchForm;
import org.tylproject.vaadin.addon.fields.search.SearchWindow;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 */
public class DefaultTableBehaviorFactory<U> implements BehaviorFactory<U> {

    final Class<U> beanClass;
    final TabularViewAdaptor<?> table;

    public DefaultTableBehaviorFactory(Class<U> beanClass, TabularViewAdaptor<?> table) {
        this.beanClass = beanClass;
        this.table = table;
    }

    @Override
    public Behavior forContainerType(@Nonnull Class<? extends Container> containerClass) {
        if (containerClass == null) throw new AssertionError("containerClass cannot be null");

        final CrudListeners crudListeners;

        // generates a search form for the columns that are visible in the table
        final SearchForm searchForm = new SearchForm(makePropertyIdToTypeMap(
                table.getContainerDataSource(),
                Arrays.asList(table.getVisibleColumns())));

        final FindListeners findListeners = new SearchWindowFindListeners(new SearchWindow(searchForm));
        final CurrentItemChange.Listener currentItemListener = new Tables.CurrentItemChangeListener(table);

        // we hard-code type strings so that the Java linker does not
        // raise an error when the Mongo, JPA addons (which are optional dependencies)
        // are not available
        switch (containerClass.getCanonicalName()) {
            case "org.vaadin.viritin.ListContainer":
            case "org.vaadin.viritin.FilterableListContainer":
                crudListeners = new ListContainerTableCrud<U>(beanClass, table);
                break;

            case "org.tylproject.vaadin.addon.BufferedMongoContainer":
                crudListeners = new BufferedMongoTableCrud<U>(beanClass, table);
                break;

            case "com.vaadin.addon.jpacontainer.JPAContainer":
                crudListeners = new JPAContainerTableCrud<U>(beanClass, table);
                break;

            default:
                throw new UnsupportedOperationException("Unknown container type: "+ containerClass.getCanonicalName());

        }

        return new BehaviorFacade(currentItemListener, crudListeners, findListeners);

    }

    protected Map<Object, Class<?>> makePropertyIdToTypeMap(Container container, Collection<?> propertyIds) {

        LinkedHashMap<Object, Class<?>> map = new LinkedHashMap<>();
        for (Object propertyId: propertyIds) {
            map.put(propertyId, container.getType(propertyId));
        }

        return map;
    }
}
