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

package org.tylproject.vaadin.addon.fieldbinder.behavior.containers.jpacontainer;

import com.vaadin.addon.jpacontainer.JPAContainer;
import org.tylproject.vaadin.addon.datanav.DataNavigation;
import org.tylproject.vaadin.addon.datanav.events.OnCommit;
import org.tylproject.vaadin.addon.fieldbinder.FieldBinder;
import org.tylproject.vaadin.addon.fieldbinder.behavior.commons.FieldBinders;

public class JPAContainerCrud<T> extends FieldBinders.BaseCrud<T> {
    public JPAContainerCrud(FieldBinder<T> fieldBinder) {
        super(fieldBinder);
    }

    @Override
    public void onCommit(OnCommit.Event event) {
        super.onCommit(event);
        DataNavigation nav = event.getSource();
        T bean = fieldBinder.getBeanDataSource();
        JPAContainer<T> container = (JPAContainer<T>) nav.getContainer();
        Object id = container.addEntity(bean);
        nav.setCurrentItemId(id);
    }

}
