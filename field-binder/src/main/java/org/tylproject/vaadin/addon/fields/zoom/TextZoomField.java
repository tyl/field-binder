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

import javax.annotation.Nullable;
import java.util.Locale;

/**
 * Shows a non-editable TextField that zooms on a data source
 */
public class TextZoomField extends ZoomField<Object> {
    public TextZoomField(TextField field) {
        super(field, Object.class);
    }
    public TextZoomField() {
        super(Object.class);
    }

    // value is a structured object *from which* we extract the display value
    @Override
    public void setValue(@Nullable Object value) throws ReadOnlyException {



        super.setValue(value);

        boolean isReadOnly = getBackingField().isReadOnly();
        getBackingField().setReadOnly(false);


        ZoomDialog zd = getZoomDialog();

        String stringValue;
        if (value == null) {
            stringValue = null;
        } else  {

            stringValue = value.toString();
        }

        setDisplayValue(stringValue);
        getBackingField().setReadOnly(isReadOnly);
    }

    @Override
    public void setPropertyDataSource(Property newDataSource) {
        super.setPropertyDataSource(newDataSource);
        if (newDataSource == null) setValue(null);
        else setValue(newDataSource.getValue());
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

}
