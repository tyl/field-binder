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
import com.vaadin.data.util.converter.Converter;
import com.vaadin.ui.*;
import org.joda.time.DateTime;

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

    public <T extends Field> T createField(Class<?> type, Class<T> fieldType) {
        Field<?> f;
        if (AbstractSelect.class.isAssignableFrom(fieldType)) {
            f = createCompatibleSelect((Class<AbstractSelect>) fieldType);
        } else {
            f = super.createField(type, fieldType);
        }

        boolean isJoda = DateTime.class.isAssignableFrom(type);

        if (isJoda) {
            DateField dateField = createField(Date.class, DateField.class) ;
            f = dateField;
            dateField.setConverter(new Converter<Date, DateTime>() {
                @Override
                public DateTime convertToModel(Date date, Class<? extends DateTime> aClass, Locale locale) throws ConversionException {
                    return date == null? null : new DateTime(date);
                }

                @Override
                public Date convertToPresentation(DateTime o, Class<? extends
                        Date> aClass, Locale locale) throws ConversionException {
                    return o == null? null : o.toDate();
                }

                @Override
                public Class<DateTime> getModelType() {
                    return DateTime.class;
                }

                @Override
                public Class<Date> getPresentationType() {
                    return Date.class;
                }
            });
        }


        if (Date.class.isAssignableFrom(type)
            || isJoda) {

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
        createDetailField(Class<U> dataType, Class<T> containedBeanClass, GridSupport  gridSupport) {
        if (List.class.isAssignableFrom(dataType)) {
            return (CollectionTable<T, U>) new ListTable<T>(containedBeanClass, gridSupport);
        } else
        if (Collection.class.isAssignableFrom(dataType)) {
            return new CollectionTable<T,U>(containedBeanClass, dataType, gridSupport);
        }
        else throw new UnsupportedOperationException("Unsupported type "+ dataType);
    }

//
//    public <T,U extends Collection<T>> CollectionGrid<T,U>
//        createGridDetailField(Class<U> dataType, Class<T> containedBeanClass) {
////        if (List.class.isAssignableFrom(dataType)) {
////            return (CollectionGrid<T, U>) new ListTable<T>(containedBeanClass);
////        } else
//        if (Collection.class.isAssignableFrom(dataType)) {
//            return new CollectionGrid<T,U>(containedBeanClass, dataType);
//        }
//        else throw new UnsupportedOperationException("Unsupported type "+ dataType);
//    }
}


