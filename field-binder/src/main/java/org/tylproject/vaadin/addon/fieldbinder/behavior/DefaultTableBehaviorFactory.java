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
import org.tylproject.vaadin.addon.datanav.events.CurrentItemChange;
import org.tylproject.vaadin.addon.fieldbinder.behavior.containers.jpacontainer.JPAContainerCrud;
import org.tylproject.vaadin.addon.fieldbinder.behavior.containers.mongocontainer.MongoCrud;
import org.tylproject.vaadin.addon.fields.collectiontables.adaptors.TabularViewAdaptor;
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
 * BehaviorFactory for Table-like widgets.
 *
 * It internally uses a TabularViewAdaptor to adapt the Table API to Grid instances.
 * Supported Containers:
 *
 * <ul>
 *  <li>{@link org.vaadin.viritin.ListContainer}</li>
 *  <li>{@link org.vaadin.viritin.FilterableListContainer}</li>
 *  <li>{@link org.tylproject.vaadin.addon.BufferedMongoContainer}</li>
 *  <li>{@link com.vaadin.addon.jpacontainer.JPAContainer}</li>
 * </ul>
 *
 *
 */
public class DefaultTableBehaviorFactory<U> implements BehaviorFactory<U> {

    final Class<U> beanClass;
    final TabularViewAdaptor<U,?> tabularViewAdaptor;

    public DefaultTableBehaviorFactory(Class<U> beanClass, TabularViewAdaptor<U,?> tabularViewAdaptor) {
        this.beanClass = beanClass;
        this.tabularViewAdaptor = tabularViewAdaptor;
    }

    @Override
    public Behavior forContainerType(@Nonnull Class<? extends Container> containerClass) {
        if (containerClass == null) throw new AssertionError("containerClass cannot be null");

        final CrudListeners crudListeners;

        // generates a search form for the columns that are visible in the table
        final SearchForm searchForm = new SearchForm(makePropertyIdToTypeMap(
                tabularViewAdaptor.getContainerDataSource(),
                Arrays.asList(tabularViewAdaptor.getVisibleColumns())));

        final FindListeners findListeners = new SearchWindowFindListeners(new SearchWindow(searchForm));
        final CurrentItemChange.Listener currentItemListener = new Tables.CurrentItemChangeListener(tabularViewAdaptor);

        // we hard-code type strings so that the Java linker does not
        // raise an error when the Mongo, JPA addons (which are optional dependencies)
        // are not available
        switch (containerClass.getCanonicalName()) {
            case "org.vaadin.viritin.ListContainer":
            case "org.vaadin.viritin.FilterableListContainer":
                crudListeners = new ListContainerTableCrud<U>(beanClass, tabularViewAdaptor);
                break;

            case "org.tylproject.vaadin.addon.BufferedMongoContainer":
                crudListeners = new BufferedMongoTableCrud<U>(beanClass, tabularViewAdaptor);
                break;

            case "com.vaadin.addon.jpacontainer.JPAContainer":
                crudListeners = new JPAContainerTableCrud<U>(beanClass, tabularViewAdaptor);
                break;

            default:
                throw new UnsupportedOperationException("Unknown container type: "+ containerClass.getCanonicalName());

        }

        return new BehaviorFacade(currentItemListener, crudListeners, findListeners);

    }


    protected CrudHandler findCrudListeners(Class<? extends Container> containerClass) {

        CrudHandler crudListeners;

        crudListeners = new ListContainerTableCrud<>(beanClass, tabularViewAdaptor);
        if (crudListeners.matches(containerClass)) return crudListeners;

        crudListeners = new BufferedMongoTableCrud<>(beanClass, tabularViewAdaptor);
        if (crudListeners.matches(containerClass)) return crudListeners;

        crudListeners = new JPAContainerTableCrud<>(beanClass, tabularViewAdaptor);
        if (crudListeners.matches(containerClass)) return crudListeners;

        throw new UnsupportedOperationException(
                "Unknown container type: "+ containerClass.getCanonicalName());

    }


    protected Map<Object, Class<?>> makePropertyIdToTypeMap(Container container, Collection<?> propertyIds) {

        LinkedHashMap<Object, Class<?>> map = new LinkedHashMap<>();
        for (Object propertyId: propertyIds) {
            map.put(propertyId, container.getType(propertyId));
        }

        return map;
    }
}
