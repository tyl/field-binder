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
import com.vaadin.ui.*;
import org.tylproject.vaadin.addon.datanav.events.*;

import java.util.*;

/**
 * The table field factory defines the default CRUD behavior for a table. It also injects
 * a modified TableFieldFactory that activates only one editable row at once.
 */
public class ListContainerTableBehavior<T> implements Behavior {

    final Table table;
    final Class<T> beanClass;
    final TableFindBehavior<T> findStrategy;

    protected T newEntity = null;
    final List<Field<?>> fields = new ArrayList<>();

    public ListContainerTableBehavior(final Class<T> beanClass, final Table table) {
        this.beanClass = beanClass;
        this.table = table;
        this.findStrategy = new TableFindBehavior<>(beanClass);

        table.setTableFieldFactory(new TableFieldFactory() {
//            final DefaultFieldFactory fieldFactory = DefaultFieldFactory.get();
            final TableFieldFactory fieldFactory = table.getTableFieldFactory();
            @Override
            public Field<?> createField(Container container, Object itemId, Object propertyId, Component uiContext) {
//                TableFieldFactory fieldFactory = table.getTableFieldFactory();
                if (itemId.equals(table.getValue())) {
                    Field<?> f = fieldFactory.createField(container, itemId, propertyId,
                            uiContext);
                    if (f instanceof AbstractTextField) {
                        ((AbstractTextField) f).setNullRepresentation("");
                        ((AbstractTextField) f).setImmediate(true);
                    }
                    f.setBuffered(true);
                    fields.add(f);
                    return f;
                }
                else return null;
            }
        });
    }

    protected void commitFields() {
        for (Field<?> f: fields) {
            f.commit();
        }
        fields.clear();
    }
    protected void discardFields() {
        for (Field<?> f: fields) {
            f.discard();
        }
        fields.clear();
    }



    @Override
    public void itemEdit(ItemEdit.Event event) {
        table.setEditable(true);
        table.setSelectable(false);
        table.focus();
    }


    @Override
    public void itemCreate(ItemCreate.Event event) {
        T bean = createBean();

        event.getSource().getContainer().addItem(bean);
        event.getSource().setCurrentItemId(bean);
        table.select(bean);
        table.setEditable(true);
        table.setSelectable(false);
        table.focus();


    }

    protected T createBean() {
        try {
            T bean = beanClass.newInstance();
            newEntity = bean;
            return bean;
        } catch (Exception ex) {
            throw new UnsupportedOperationException(ex);
        }
    }

    public void onDiscard(OnDiscard.Event event) {
        this.table.discard();

        discardFields();
        this.table.setEditable(false);

        this.table.setSelectable(true);
        if (newEntity != null) {
            newEntity = null;
            event.getSource().remove();
        }
    }

    public void onCommit(OnCommit.Event event) {
        this.table.commit();

        commitFields();
        this.table.setEditable(false);

        this.table.setSelectable(true);

        newEntity = null;
    }

    public void itemRemove(ItemRemove.Event event) {
        this.table.removeItem(event.getSource().getCurrentItemId());
    }

    @Override
    public void currentItemChange(CurrentItemChange.Event event) {
        table.select(event.getNewItemId());
    }

    @Override
    public void onFind(OnFind.Event event) {
        findStrategy.onFind(event);
    }

    @Override
    public void clearToFind(ClearToFind.Event event) {
        findStrategy.clearToFind(event);
    }
}
