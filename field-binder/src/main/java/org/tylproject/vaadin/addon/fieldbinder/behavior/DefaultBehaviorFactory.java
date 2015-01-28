/*
 * Copyright (c) 2014 - Tyl Consulting s.a.s.
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
import org.tylproject.vaadin.addon.fields.search.SearchForm;
import org.tylproject.vaadin.addon.fields.search.SearchWindow;

/**
 * Created by evacchi on 15/12/14.
 */
public class DefaultBehaviorFactory<U> implements BehaviorFactory<U> {

    final FieldBinder<U> fieldBinder;

    public DefaultBehaviorFactory(FieldBinder<U> fieldBinder) {
        this.fieldBinder = fieldBinder;
    }

    @Override
    public Behavior forContainerType(
                                    Class<? extends Container> containerClass) {

        final CrudListeners crudListeners;
        final FindListeners findListeners = new SearchWindowFindListeners(fieldBinder);
        final CurrentItemChange.Listener currentItemListener = new FieldBinderCurrentItemChangeListener<>(fieldBinder);

        if (containerClass != null) {

            switch (containerClass.getCanonicalName()) {
//                case "org.vaadin.viritin.ListContainer":
//                case "org.vaadin.viritin.FilterableListContainer":
//                    return (T) new ListContainerBehavior<U>(fieldBinder);
                case "org.tylproject.vaadin.addon.MongoContainer":
                    crudListeners = new MongoCrud<U>(fieldBinder);
                    break;

//                case "com.vaadin.addon.jpacontainer.JPAContainer":
//                    return (T) new JPAContainerBehavior<U>(fieldBinder);

                default:
                    throw new UnsupportedOperationException(
                        "Unknown container type: "+ containerClass.getCanonicalName());
            }


            return new BehaviorFacade(currentItemListener, crudListeners, findListeners);

        }

        throw new UnsupportedOperationException("Unknown container type: "+ containerClass.getCanonicalName());
    }
}
