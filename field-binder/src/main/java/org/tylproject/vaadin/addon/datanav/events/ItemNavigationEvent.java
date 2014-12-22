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

package org.tylproject.vaadin.addon.datanav.events;

import com.vaadin.data.Item;
import org.tylproject.vaadin.addon.datanav.DataNavigation;

import java.util.EventObject;

/**
 * Created by evacchi on 19/11/14.
 */
public class ItemNavigationEvent extends EventObject {
    private final Object newItemId;
    private final Object oldItemId;
    private Item newItem;
    private Item oldItem;

    public ItemNavigationEvent(DataNavigation source, Object newItemId, Object oldItemId)  {
        super(source);
        this.newItemId = newItemId;
        this.oldItemId = oldItemId;
    }

    public Object getNewItemId() {
        return newItemId;
    }
    public Item getNewItem() {
        if (newItemId == null) return null;
        if (newItem == null) {
            newItem = getSource().getContainer().getItem(newItemId);
        }
        return newItem;
    }

    public Object getOldItemId() {
        return oldItemId;
    }
    public Item getOldItem() {
        if (oldItemId == null) return null;
        if (oldItem == null) {
            oldItem = getSource().getContainer().getItem(oldItemId);
        }
        return oldItem;
    }

    @Override
    public DataNavigation getSource() {
        return (DataNavigation) super.getSource();
    }
}