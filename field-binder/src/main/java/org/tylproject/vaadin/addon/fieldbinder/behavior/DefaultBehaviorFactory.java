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
import org.tylproject.vaadin.addon.fieldbinder.FieldBinder;
import org.tylproject.vaadin.addon.fieldbinder.behavior.commons.FieldBinders;
import org.tylproject.vaadin.addon.fieldbinder.behavior.commons.SearchWindowFindListeners;
import org.tylproject.vaadin.addon.fieldbinder.behavior.containers.jpacontainer.JPAContainerCrud;

import org.tylproject.vaadin.addon.fieldbinder.behavior.containers.listcontainer
.ListContainerCrud;
import org.tylproject.vaadin.addon.fieldbinder.behavior.containers.mongocontainer.MongoCrud;



import javax.annotation.Nonnull;

/**
 * Default Behavior Factory for a FieldBinder.
 *
 * Supported Containers:
 *
 * <ul>
 *  <li>org.vaadin.viritin.ListContainer</li>
 *  <li>org.vaadin.viritin.FilterableListContainer</li>
 *  <li>org.tylproject.vaadin.addon.MongoContainer</li>
 *  <li>com.vaadin.addon.jpacontainer.JPAContainer</li>
 * </ul>
 *
 * The containerClass is checked by *name* so subclasses WILL NOT be recognized.
 */
public class DefaultBehaviorFactory<U> implements BehaviorFactory<U> {

    final FieldBinder<U> fieldBinder;

    public DefaultBehaviorFactory(FieldBinder<U> fieldBinder) {
        this.fieldBinder = fieldBinder;
    }

    @Override
    public Behavior forContainerType(@Nonnull Class<? extends Container> containerClass) {
        if (containerClass == null) throw new AssertionError("containerClass cannot be null");

        final CrudListeners crudListeners;
        final FindListeners findListeners = new FieldBinders.Find<>(fieldBinder);
        final CurrentItemChange.Listener currentItemListener = new FieldBinders.CurrentItemChangeListener<>(fieldBinder);

        switch (containerClass.getCanonicalName()) {
            case "org.vaadin.viritin.ListContainer":
            case "org.vaadin.viritin.FilterableListContainer":
                crudListeners = new ListContainerCrud<>(fieldBinder);
                break;

            case "org.tylproject.vaadin.addon.MongoContainer":
                crudListeners = new MongoCrud<>(fieldBinder);
                break;

            case "com.vaadin.addon.jpacontainer.JPAContainer":
                crudListeners = new JPAContainerCrud<>(fieldBinder);
                break;

            default:
                throw new UnsupportedOperationException(
                    "Unknown container type: "+ containerClass.getCanonicalName());
        }


        return new BehaviorFacade(currentItemListener, crudListeners, findListeners);

    }
}
