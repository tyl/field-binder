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
import org.tylproject.vaadin.addon.datanav.events.ItemCreate;
import org.tylproject.vaadin.addon.datanav.events.ItemRemove;
import org.tylproject.vaadin.addon.datanav.events.OnCommit;
import org.tylproject.vaadin.addon.datanav.events.OnDiscard;
import org.tylproject.vaadin.addon.fields.collectiontables.adaptors.TabularViewAdaptor;
import org.tylproject.vaadin.addon.fieldbinder.behavior.commons.Tables;

/**
 * Created by evacchi on 19/12/14.
 */
public class JPAContainerTableCrud<T> extends Tables.BaseCrud<T> {
    public JPAContainerTableCrud(Class<T> beanClass, TabularViewAdaptor<T,?> table) {
        super(beanClass, table);
    }

    @Override
    protected boolean verifyMatch(Class<?> clazz) {
        return JPAContainer.class.isAssignableFrom(clazz);
    }

    @Override
    public void itemCreate(ItemCreate.Event event) {
        T bean = createBean();

        JPAContainer<T> jc = (JPAContainer<T>) event.getSource().getContainer();
        Object itemId = jc.addEntity(bean);

        event.getSource().setCurrentItemId(itemId);

        super.itemCreate(event);
    }

    @Override
    public void itemRemove(ItemRemove.Event event) {
        super.itemRemove(event);
        JPAContainer<T> jc = (JPAContainer<T>) event.getSource().getContainer();
        jc.commit();
    }

    @Override
    public void onCommit(OnCommit.Event event) {
        super.onCommit(event);
        JPAContainer<T> jc = (JPAContainer<T>) event.getSource().getContainer();
        jc.commit();
    }

    @Override
    public void onDiscard(OnDiscard.Event event) {
        super.onDiscard(event);
        JPAContainer<T> jc = (JPAContainer<T>) event.getSource().getContainer();

        jc.discard();
    }
}
