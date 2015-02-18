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

package org.tylproject.vaadin.addon.fieldbinder;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Validator;
import com.vaadin.ui.*;
import org.tylproject.vaadin.addon.datanav.BasicDataNavigation;
import org.tylproject.vaadin.addon.datanav.CrudButtonBar;
import org.tylproject.vaadin.addon.fieldbinder.behavior.DefaultTableBehaviorFactory;
import org.vaadin.viritin.FilterableListContainer;

import java.util.Collection;

/**
 * A table wrapper where the value is a {@link java.util.List}
 *
 * Generally used together with {@link FieldBinder}
 */
public class CollectionTable<T,U extends Collection<T>> extends CollectionTabularView<T,U> {

    final TableAdaptor adaptor;

    public CollectionTable(Class<T> containedBeanClass, Class<U> collectionType) {
        super(containedBeanClass, collectionType, new TableAdaptor(containedBeanClass));

        adaptor = (TableAdaptor) super.getAdaptor();

        Table tableComponent = adaptor.getComponent();

        tableComponent.setBuffered(true);
        tableComponent.setSizeFull();
        tableComponent.setHeight("300px");
        tableComponent.setSelectable(true);
        tableComponent.setMultiSelect(false);


    }


    /**
     * Returns the actual {@link com.vaadin.ui.Table} instance
     * @return
     */
    public Table getTable() {
        return adaptor.getComponent();
    }

}
