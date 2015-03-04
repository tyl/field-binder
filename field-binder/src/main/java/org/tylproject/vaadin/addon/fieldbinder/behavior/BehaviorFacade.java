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

package org.tylproject.vaadin.addon.fieldbinder.behavior;

import org.tylproject.vaadin.addon.datanav.events.*;

/**
 * Composes CurrentItemChange.Listener, CrudListeners and FindListeners
 * into one single class
 *
 * It is basically a Java implementation of trait-like composition.
 * It is used by the default {@link org.tylproject.vaadin.addon.fieldbinder.behavior.BehaviorFactory}
 * implementations to compose pre-defined actions for each supported Container type
 *
 */
public class BehaviorFacade implements Behavior {

    private final CurrentItemChange.Listener currentItemDelegate;
    private final CrudListeners crudDelegate;
    private final FindListeners findDelegate;

    public BehaviorFacade(
        CurrentItemChange.Listener currentItemDelegate,
        CrudListeners crudDelegate,
        FindListeners findDelegate) {
        this.currentItemDelegate = currentItemDelegate;
        this.crudDelegate = crudDelegate;
        this.findDelegate = findDelegate;
    }

    @Override
    public void currentItemChange(CurrentItemChange.Event event) {
        currentItemDelegate.currentItemChange(event);
    }

    @Override
    public void onDiscard(OnDiscard.Event event) {
        crudDelegate.onDiscard(event);
    }

    @Override
    public void onCommit(OnCommit.Event event) {
        crudDelegate.onCommit(event);
    }

    @Override
    public void itemRemove(ItemRemove.Event event) {
        crudDelegate.itemRemove(event);
    }

    @Override
    public void itemEdit(ItemEdit.Event event) {
        crudDelegate.itemEdit(event);
    }

    @Override
    public void itemCreate(ItemCreate.Event event) {
        crudDelegate.itemCreate(event);
    }

    @Override
    public void clearToFind(ClearToFind.Event event) {
        findDelegate.clearToFind(event);
    }

    @Override
    public void onFind(OnFind.Event event) {
        findDelegate.onFind(event);
    }
}
