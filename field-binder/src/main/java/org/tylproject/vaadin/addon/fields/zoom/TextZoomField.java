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

import com.vaadin.data.util.converter.Converter;
import com.vaadin.ui.TextField;

/**
 * Created by evacchi on 30/01/15.
 */
public class TextZoomField extends ZoomField<String> {
    public TextZoomField(TextField field) {
        super(field, String.class);
    }
    public TextZoomField() {
        super(String.class);
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
    public void setConverter(Converter<String, ?> converter) {
        this.getBackingField().setConverter(converter);
    }

    @Override
    public Converter<String, Object> getConverter() {
        return this.getBackingField().getConverter();
    }

    @Override
    public Object getConvertedValue() {
        return this.getBackingField().getConvertedValue();
    }

    @Override
    public void setConvertedValue(Object value) {
        this.getBackingField().setConvertedValue(value);
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
