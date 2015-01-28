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

package org.tylproject.vaadin.addon.fieldbinder;

import com.vaadin.data.fieldgroup.DefaultFieldGroupFieldFactory;
import com.vaadin.ui.*;

import java.util.Collection;
import java.util.List;

/**
 * An extended {@link com.vaadin.data.fieldgroup.FieldGroupFieldFactory}
 * that supports {@link org.tylproject.vaadin.addon.fieldbinder.ListTable}
 */
public class FieldBinderFieldFactory extends DefaultFieldGroupFieldFactory {

    public <T extends Field> T createField(Class<?> type, Class<T> fieldType) {
        T f = super.createField(type, fieldType);
        ((AbstractField<?>) f).setImmediate(true);
        return f;
    }

    @Override
    protected AbstractSelect createCompatibleSelect(
            Class<? extends AbstractSelect> fieldType) {
        if (fieldType.isAssignableFrom(ComboBox.class)) {
            AbstractSelect select;
            select = new ComboBox();
            select.setImmediate(true);
            select.setNullSelectionAllowed(false);
            return select;
        }
        else {
            return super.createCompatibleSelect(fieldType);
        }
    }


    @Override
    protected <T extends AbstractTextField> T createAbstractTextField(
            Class<T> fieldType) {
        T field = super.createAbstractTextField(fieldType);
        field.setNullRepresentation("");
        return field;
    }



    public <T,U extends Collection<T>> CollectionTable<T,U>
        createDetailField(Class<U> dataType, Class<T> containedBeanClass) {
        if (List.class.isAssignableFrom(dataType)) {
            return (CollectionTable<T, U>) new ListTable<T>(containedBeanClass);
        } else
        if (Collection.class.isAssignableFrom(dataType)) {
            return new CollectionTable<T,U>(containedBeanClass, dataType);
        }
        else throw new UnsupportedOperationException("Unsupported type "+ dataType);
    }
}
