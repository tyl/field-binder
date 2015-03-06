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
import com.vaadin.ui.Component;

/**
 * Interface for the contents of a {@link org.tylproject.vaadin.addon.fields.zoom.ZoomWindow}
 * (displayed by clicking the button of a {@link org.tylproject.vaadin.addon.fields.zoom.ZoomField})
 */
public interface ZoomDialog {

    /**
     * Return the current value when a nestedPropertyId is given,
     * or the selectedItemId otherwise
     */
    public void dismiss();

    /**
     * Show the window, and pass in the currently selected value
     */
    public void show(Object value);

    public Component getDialogContents();

    public boolean isReadOnly();
    public void setReadOnly(boolean readOnly);

    public Container getContainer();
    public ZoomDialog withContainerPropertyId(Object propertyId, Class<?> propertyType);
    public Class<?> getContainerPropertyType();
    public Object getContainerPropertyId();
    public Property<?> getContainerProperty();

    public Object getSelectedItemId();
    public Item getSelectedItem();

    /**
     * Return the selected item as a bean (if possible!)
     */
    public Object getSelectedBean();

    /**
     * Return the selected value associated to the NestedPropertyId
     */
    public Object getSelectedValue();

    void setNullSelectionAllowed(boolean allowed);
    boolean isNullSelectionAllowed();
}
