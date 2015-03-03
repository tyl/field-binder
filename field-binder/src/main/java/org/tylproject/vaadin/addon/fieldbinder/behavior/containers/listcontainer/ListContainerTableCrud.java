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
import org.tylproject.vaadin.addon.fields.collectiontables.adaptors.TabularViewAdaptor;
import org.tylproject.vaadin.addon.fieldbinder.behavior.commons.Tables;

/**
 * The table field factory defines the default CRUD behavior for a table. It also injects
 * a modified TableFieldFactory that activates only one editable row at once.
 */
public class ListContainerTableCrud<T> extends Tables.BaseCrud<T> {

    protected T newEntity = null;

    public ListContainerTableCrud(final Class<T> beanClass, final TabularViewAdaptor<T,?> table) {
        super(beanClass, table);
    }


    @Override
    public void itemCreate(ItemCreate.Event event) {
        T bean = createBean();

        event.getSource().getContainer().addItem(bean);
        event.getSource().setCurrentItemId(bean);

        super.itemCreate(event);
    }

}
