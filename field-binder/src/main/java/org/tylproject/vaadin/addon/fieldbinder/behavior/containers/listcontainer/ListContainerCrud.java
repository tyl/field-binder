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

package org.tylproject.vaadin.addon.fieldbinder.behavior.containers.listcontainer;

import org.tylproject.vaadin.addon.datanav.events.ItemCreate;
import org.tylproject.vaadin.addon.fieldbinder.FieldBinder;
import org.tylproject.vaadin.addon.fieldbinder.behavior.commons.FieldBinders;
import org.vaadin.viritin.ListContainer;

/**
 * Created by evacchi on 15/12/14.
 */
public class ListContainerCrud<T> extends FieldBinders.BaseCrud<T> {
    public ListContainerCrud(FieldBinder<T> fieldBinder) {
        super(fieldBinder);
    }

    @Override
    protected boolean verifyMatch(Class<?> clazz) {
        return ListContainer.class.isAssignableFrom(clazz);
    }

    @Override
    public void itemCreate(ItemCreate.Event event) {
        super.itemCreate(event);
        T bean = createBean();
        ListContainer<T> container = ((ListContainer<T>) event.getSource().getContainer());
        container.addItem(bean);
        event.getSource().setCurrentItemId(bean);
    }
}
