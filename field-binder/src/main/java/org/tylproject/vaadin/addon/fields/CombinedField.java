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

package org.tylproject.vaadin.addon.fields;

import com.vaadin.data.Buffered;
import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.data.Validator;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.tylproject.vaadin.addon.fieldbinder.behavior.DefaultFilterFactory;
import org.tylproject.vaadin.addon.fieldbinder.behavior.FilterFactory;

import java.util.Collection;

/**
 * A Generic Field with a Button component on its right
 */
public abstract class CombinedField<T, FT, F extends AbstractField<FT>> extends FieldDecorator<T, FT, F> {
    final private CssLayout rootLayout = new CssLayout();
    final private Button button;
    final private Class<T> type;
    private Object value;

    public CombinedField(final F field, final Button button, final Class<T> type) {
        super(field);
        this.button = button;
        this.type = type;

        rootLayout.addStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
        rootLayout.addComponents(field, button);

    }

    public Button getButton() {
        return button;
    }

    @Override
    protected Component initContent() {
        return rootLayout;
    }

    @Override
    public Class<? extends T> getType() {
        return type;
    }

    @Override
    public T getValue() {
        return (T) value;
    }

    @Override
    public void setValue(T newValue) throws ReadOnlyException {
        if (value == newValue || value != null && value.equals(newValue)) {
            fireValueChange(true);
        } else {
            this.value = newValue;
            fireValueChange(false);
        }

        Property p = getPropertyDataSource();
        if (p != null) p.setValue(value);

    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        getButton().setEnabled(enabled);
    }
}
