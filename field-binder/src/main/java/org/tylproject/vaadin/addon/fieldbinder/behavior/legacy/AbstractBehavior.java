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

package org.tylproject.vaadin.addon.fieldbinder.behavior.legacy;

import com.vaadin.data.Item;
import org.tylproject.vaadin.addon.datanav.events.*;
import org.tylproject.vaadin.addon.fieldbinder.FieldBinder;
import org.tylproject.vaadin.addon.fieldbinder.behavior.Behavior;
import org.tylproject.vaadin.addon.fieldbinder.behavior.FindListeners;
import org.tylproject.vaadin.addon.fieldbinder.behavior.legacy.LegacyFindListeners;

/**
 * Created by evacchi on 26/11/14.
 */
public abstract class AbstractBehavior<T> implements Behavior {

    protected final FieldBinder<T> fieldBinder;
    private final Class<T> beanClass;
    private final FindListeners findListenersDelegate;

    public AbstractBehavior(FieldBinder<T> fieldBinder) {
        this.fieldBinder = fieldBinder;
        this.beanClass = fieldBinder.getType();
        this.findListenersDelegate = new LegacyFindListeners<>(fieldBinder);

        fieldBinder.setReadOnly(true);
    }

    public Class<T> getBeanClass() {
        return beanClass;
    }

    public FieldBinder<T> getFieldBinder() {
        return fieldBinder;
    }

    public FindListeners getFindListenersDelegate() {
        return findListenersDelegate;
    }

    @Override
    public void currentItemChange(CurrentItemChange.Event event) {
        fieldBinder.setItemDataSource(event.getNewItem());
    }

    @Override
    public void onCommit(OnCommit.Event event) {
        fieldBinder.commit();
        fieldBinder.setReadOnly(true);
    }

    @Override
    public void onDiscard(OnDiscard.Event event) {
        fieldBinder.discard();
        fieldBinder.setReadOnly(true);
        Item currentItem = event.getSource().getCurrentItem();
        if (currentItem == null) {
            event.getSource().first();
        } else {
            // restore item again (so that also the tables get updated)
            fieldBinder.setItemDataSource(currentItem);
        }
    }

    @Override
    public void itemRemove(ItemRemove.Event event) {
        event.getSource().getContainer().removeItem(event.getSource().getCurrentItemId());
    }

    @Override
    public void itemEdit(ItemEdit.Event event) {
        fieldBinder.setReadOnly(false);
        fieldBinder.focus();
    }

    @Override
    public void itemCreate(ItemCreate.Event event) {
        fieldBinder.setReadOnly(false);
        event.getSource().setCurrentItemId(null);
        T bean = createBean();
        fieldBinder.setBeanDataSource(bean);
    }


    @Override
    public void clearToFind(ClearToFind.Event event) {
        findListenersDelegate.clearToFind(event);
    }

    @Override
    public void onFind(OnFind.Event event) {
        findListenersDelegate.onFind(event);
    }

    protected T createBean() {
        try {
            return beanClass.newInstance();
        } catch (Exception ex) {
            throw new UnsupportedOperationException(ex);
        }
    }
}