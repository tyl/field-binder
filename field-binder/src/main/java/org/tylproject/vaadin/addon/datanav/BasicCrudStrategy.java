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

package org.tylproject.vaadin.addon.datanav;

import com.vaadin.ui.Notification;
import org.tylproject.vaadin.addon.datanav.events.ItemRemove;
import org.tylproject.vaadin.addon.datanav.events.ItemCreate;
import org.tylproject.vaadin.addon.datanav.events.OnCommit;

/**
 * Created by evacchi on 19/11/14.
 */
public class BasicCrudStrategy implements ItemRemove.Listener,
        ItemCreate.Listener, OnCommit.Listener {



    private static BasicCrudStrategy instance = new BasicCrudStrategy();
    public static BasicCrudStrategy get() { return instance; }

    protected BasicCrudStrategy() {}

    @Override
    public void itemRemove(ItemRemove.Event event) {
        event.getSource().getContainer().removeItem(event.getSource().getCurrentItemId());
    }

    @Override
    public void itemCreate(ItemCreate.Event event) {
        Object id = event.getSource().getContainer().addItem();
        event.getSource().setCurrentItemId(id);
    }

    @Override
    public void onCommit(OnCommit.Event event) {
        Notification.show("committed");
    }
}
