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
import com.vaadin.data.Item;
import com.vaadin.ui.Field;
import org.tylproject.vaadin.addon.datanav.DataNavigation;
import org.tylproject.vaadin.addon.datanav.events.*;
import org.tylproject.vaadin.addon.fieldbinder.FieldBinder;

/**
 * Created by evacchi on 26/11/14.
 */
public abstract class AbstractBehavior<T> implements Behavior {

    protected final FieldBinder<T> fieldBinder;
    protected FilterApplier filterApplier = new FilterApplier();
    private final Class<T> beanClass;

    public Class<T> getBeanClass() {
        return beanClass;
    }

    public AbstractBehavior(FieldBinder<T> fieldBinder) {
        this.fieldBinder = fieldBinder;
        this.beanClass = fieldBinder.getType();
        fieldBinder.setReadOnly(true);
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


    public void clearToFind(ClearToFind.Event event) {

        if (fieldBinder.hasItemDataSource()) {
            fieldBinder.unbindAll();
        }

        fieldBinder.setReadOnly(false);
        event.getSource().setCurrentItemId(null);

        if (filterApplier.hasAppliedFilters()) {
            filterApplier.restorePatterns(fieldBinder.getPropertyIdToFieldBindings());
            filterApplier.clearPropertyIdToFilterPatterns();
        }
//
//
        fieldBinder.focus();
    }

    @Override
    public void onFind(OnFind.Event event) {
        filterApplier.applyFilters(fieldBinder.getPropertyIdToFieldBindings(), (Container.Filterable) event.getSource().getContainer());
        event.getSource().first();
        fieldBinder.setReadOnly(true);
        fieldBinder.bindAll();
    }



    @Override
    public void itemCreate(ItemCreate.Event event) {
        fieldBinder.setReadOnly(false);
        event.getSource().setCurrentItemId(null);
        T bean = createBean();
        fieldBinder.setBeanDataSource(bean);
    }

    protected T createBean() {
        try {
            return beanClass.newInstance();
        } catch (Exception ex) {
            throw new UnsupportedOperationException(ex);
        }
    }
}