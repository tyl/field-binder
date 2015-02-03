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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * An extended {@link com.vaadin.data.fieldgroup.FieldGroupFieldFactory}
 * that supports {@link org.tylproject.vaadin.addon.fieldbinder.ListTable}
 */
public class FieldBinderFieldFactory extends DefaultFieldGroupFieldFactory {

    private final GridSupport gridSupport;

    public FieldBinderFieldFactory(GridSupport gridSupport) {
        this.gridSupport = gridSupport;
    }

    public FieldBinderFieldFactory() {
        this.gridSupport = GridSupport.UseTable;
    }

    public <T extends Field> T createField(Class<?> type, Class<T> fieldType) {
        Field<?> f;
        if (AbstractSelect.class.isAssignableFrom(fieldType)) {
            f = createCompatibleSelect((Class<AbstractSelect>) fieldType);
        } else {
            f = super.createField(type, fieldType);
        }

        if (Date.class.isAssignableFrom(type)) {
            // try to assign a locale-specific date pattern
            DateFormat dateFormat =
                DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault());



            if (dateFormat instanceof SimpleDateFormat) {
                String pattern = ((SimpleDateFormat)dateFormat).toLocalizedPattern();
                DateField dateField = (DateField) f;

                // hack: turn 2-digits year into 4-digits year
                dateField.setDateFormat(pattern.replace("yy", "yyyy"));
            }
        }


        ((AbstractField<?>) f).setImmediate(true);
        return (T)f;
    }

    @Override
    protected AbstractSelect createCompatibleSelect(
            Class<? extends AbstractSelect> fieldType) {

        AbstractSelect select;

        if (fieldType.isAssignableFrom(ComboBox.class)) {
            select = new ComboBox();
            select.setImmediate(true);
            select.setNullSelectionAllowed(false);
            return select;
        }
        else
        if (OptionGroup.class.isAssignableFrom(fieldType)){
            select = new OptionGroup();
            select.setMultiSelect(false);
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
