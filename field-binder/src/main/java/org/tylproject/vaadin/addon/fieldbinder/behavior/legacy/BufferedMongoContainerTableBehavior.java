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

import com.vaadin.ui.Table;
import org.bson.types.ObjectId;
import org.tylproject.vaadin.addon.BufferedMongoContainer;
import org.tylproject.vaadin.addon.datanav.events.*;

/**
 * Created by evacchi on 19/12/14.
 */
public class BufferedMongoContainerTableBehavior<T> extends AbstractTableBehavior<T> {
    public BufferedMongoContainerTableBehavior(Class<T> beanClass, Table table) {
        super(beanClass, table);
    }

    @Override
    public void itemEdit(ItemEdit.Event event) {
        BufferedMongoContainer<T> bmc = (BufferedMongoContainer<T>) event.getSource().getContainer();

        // notify the BufferedMongoContainer
        // that we want to edit an item
        bmc.updateItem((ObjectId) event.getSource().getCurrentItemId());
        super.itemEdit(event);

    }

    @Override
    public void itemCreate(ItemCreate.Event event) {
        T bean = createBean();

        BufferedMongoContainer<T> bmc = (BufferedMongoContainer<T>) event.getSource().getContainer();
        ObjectId itemId = bmc.addItem();

        event.getSource().setCurrentItemId(itemId);


       super.itemCreate(event);


    }

    @Override
    public void itemRemove(ItemRemove.Event event) {
        super.itemRemove(event);
        BufferedMongoContainer<T> bmc = (BufferedMongoContainer<T>) event.getSource().getContainer();
        bmc.commit();
    }

    @Override
    public void onCommit(OnCommit.Event event) {
        super.onCommit(event);
        BufferedMongoContainer<T> bmc = (BufferedMongoContainer<T>) event.getSource().getContainer();
        bmc.commit();
    }

    @Override
    public void onDiscard(OnDiscard.Event event) {
        super.onDiscard(event);
        BufferedMongoContainer<T> bmc = (BufferedMongoContainer<T>) event.getSource().getContainer();
        bmc.discard();
    }
}
