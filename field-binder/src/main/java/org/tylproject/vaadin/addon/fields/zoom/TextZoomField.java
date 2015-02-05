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

package org.tylproject.vaadin.addon.fields.zoom;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.ui.TextField;

import java.util.Locale;

/**
 * Created by evacchi on 30/01/15.
 */
public class TextZoomField extends ZoomField<Object> {
    public TextZoomField(TextField field) {
        super(field, Object.class);
    }
    public TextZoomField() {
        super(Object.class);
//        this.setConverter(defaultConverter);
    }

    @Override
    public void setValue(Object newValue) throws ReadOnlyException {
        super.setValue(newValue);
        getPropertyDataSource().setValue(newValue);

        boolean isReadOnly = getBackingField().isReadOnly();

        if (isReadOnly) {
            getBackingField().setReadOnly(false);
        }

        String stringValue;
        if (newValue == null) {

            stringValue = null;

        } else {
            String propertyId = getZoomDialog().getPropertyId().toString();
            stringValue = new BeanItem(newValue, propertyId).getItemProperty(propertyId).getValue().toString();
        }

        getBackingField().setValue(stringValue);
        getBackingField().setReadOnly(isReadOnly);
    }

    @Override
    public void setPropertyDataSource(Property newDataSource) {
        super.setPropertyDataSource(newDataSource);
        if (newDataSource == null) setValue(null);
        setValue(newDataSource.getValue());
    }

    @Override
    public TextZoomField withZoomDialog(ZoomDialog dialog) {
        return (TextZoomField)super.withZoomDialog(dialog);
    }
    @Override
    public TextZoomField drillDownOnly() {
        return (TextZoomField)super.drillDownOnly();
    }

    @Override
    public String getConversionError() {
        return this.getBackingField().getConversionError();
    }

    @Override
    public void setConversionError(String valueConversionError) {
        this.getBackingField().setConversionError(valueConversionError);
    }

    private Converter<String,Object> defaultConverter = new Converter<String, Object>() {
        @Override
        public Object convertToModel(String value, Class<?> targetType, Locale locale) throws ConversionException {

            if (value == null) return null;

            Container c = getZoomDialog().getContainer();
            Object propertyId = getZoomDialog().getPropertyId();

            Item item = null;

            if (c instanceof Container.Filterable) {
                Container.Filterable cc = (Container.Filterable) c;
                cc.removeAllContainerFilters();
                cc.addContainerFilter(new Compare.Equal(propertyId, value));

                if (c instanceof Container.Ordered) {
                    Object itemId = ((Container.Ordered) c).firstItemId();
                    item = c.getItem(itemId);
                }

                cc.removeAllContainerFilters();
            }
            if (item instanceof BeanItem) {

                return ((BeanItem) item).getBean();

            } else {
                if (item == null) {
//                    throw new IllegalArgumentException("Unknown value");
                    return null;
                }

                throw new ConversionException(
                        "Item of this container is of an unsupported type: "
                                + item.getClass());
            }

        }

        @Override
        public String convertToPresentation(Object value, Class<? extends String> targetType, Locale locale) throws ConversionException {
            if (value == null) return null;
            String propertyId = getZoomDialog().getPropertyId().toString();
            return new BeanItem(value, propertyId).getItemProperty(propertyId).getValue().toString();
        }

        @Override
        public Class<Object> getModelType() {
            return Object.class;
        }

        @Override
        public Class<String> getPresentationType() {
            return String.class;
        }
    };
}
