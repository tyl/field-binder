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

package org.tylproject.vaadin.addon.fieldbinder.behavior.legacy;

import com.vaadin.data.Container;
import com.vaadin.ui.Table;
import org.tylproject.vaadin.addon.fieldbinder.behavior.Behavior;
import org.tylproject.vaadin.addon.fieldbinder.behavior.BehaviorFactory;

/**
 * Created by evacchi on 15/12/14.
 */
public class TableBehaviorFactory<U> implements BehaviorFactory<U> {

    final Class<U> beanClass;
    final Table table;

    public TableBehaviorFactory(Class<U> beanClass, Table table) {
        this.beanClass = beanClass;
        this.table = table;
    }


    @Override
    public Behavior forContainerType(Class<? extends Container>
                                                               containerClass) {
            switch (containerClass.getCanonicalName()) {
                case "org.vaadin.viritin.ListContainer":
                case "org.vaadin.viritin.FilterableListContainer":
                    return new ListContainerTableBehavior<U>(beanClass, table);
                case "org.tylproject.vaadin.addon.BufferedMongoContainer":
                    return new BufferedMongoContainerTableBehavior<U>(beanClass, table);
                case "com.vaadin.addon.jpacontainer.JPAContainer":
                    return new JPAContainerTableBehavior<U>(beanClass, table);
            }

        throw new UnsupportedOperationException("Unknown container type: "+ containerClass.getCanonicalName());
    }



}
