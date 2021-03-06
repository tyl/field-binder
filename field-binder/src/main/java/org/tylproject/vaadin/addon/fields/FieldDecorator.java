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

import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.data.Validator;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.server.Resource;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.CustomField;
import org.tylproject.vaadin.addon.fieldbinder.behavior.DefaultFilterFactory;
import org.tylproject.vaadin.addon.fieldbinder.behavior.FilterFactory;

import java.util.Collection;

/**
 * A Field implementation that delegates most of its methods to a backingField
 */
public abstract class FieldDecorator<T, FT, F extends AbstractField<FT>> extends CustomField<T> {

    private final F backingField;
    public FieldDecorator(F backingField) {
        super();
        this.backingField = backingField;
    }

    public F getBackingField() {
        return backingField;
    }


    @Override
    public abstract Class<? extends T> getType();

    @Override
    public abstract T getValue();

    @Override
    public abstract void setValue(T newValue) throws ReadOnlyException ;


    @Override
    public String getRequiredError() {
        return getBackingField().getRequiredError();
    }

    @Override
    public boolean isRequired() {
        return getBackingField().isRequired();
    }

    @Override
    public void setRequired(boolean required) {
        getBackingField().setRequired(required);
    }

    @Override
    public void setRequiredError(String requiredMessage) {
        getBackingField().setRequiredError(requiredMessage);
    }

    @Override
    public void addStyleName(String style) {
        getBackingField().addStyleName(style);
    }

    @Override
    public String getDescription() {
        return getBackingField().getDescription();
    }

    @Override
    public Resource getIcon() {
        return getBackingField().getIcon();
    }

    @Override
    public String getPrimaryStyleName() {
        return getBackingField().getPrimaryStyleName();
    }

    @Override
    public String getStyleName() {
        return getBackingField().getStyleName();
    }

    @Override
    public boolean isEnabled() {
        return getBackingField().isEnabled();
    }

//    @Override
//    public boolean isReadOnly() {
//        return getBackingField().isReadOnly();
//    }

    @Override
    public boolean isVisible() {
        return getBackingField().isVisible();
    }

//    @Override
//    public void removeListener(Listener listener) {
//        getBackingField().removeListener(listener);
//    }

    @Override
    public void removeStyleName(String style) {
        getBackingField().removeStyleName(style);
    }

    @Override
    public void setEnabled(boolean enabled) {
        getBackingField().setEnabled(enabled);
    }

    @Override
    public void setIcon(Resource icon) {
        getBackingField().setIcon(icon);
    }

    @Override
    public void setId(String id) {
        getBackingField().setId(id);
    }

    @Override
    public void setPrimaryStyleName(String style) {
        getBackingField().setPrimaryStyleName(style);
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        super.setReadOnly(readOnly);
        getBackingField().setReadOnly(readOnly);
    }

    @Override
    public void setStyleName(String style) {
        getBackingField().setStyleName(style);
    }

    @Override
    public void setVisible(boolean visible) {
        getBackingField().setVisible(visible);
    }

    @Override
    public boolean isInvalidCommitted() {
        return getBackingField().isInvalidCommitted();
    }

    @Override
    public void setInvalidCommitted(boolean isCommitted) {
        getBackingField().setInvalidCommitted(isCommitted);
    }

    @Override
    public void commit() throws SourceException, Validator.InvalidValueException {
        getBackingField().commit();
    }

    @Override
    public void discard() throws SourceException {
        getBackingField().discard();
    }

    @Override
    public boolean isBuffered() {
        return getBackingField().isBuffered();
    }

    @Override
    public boolean isModified() {
        return getBackingField().isModified();
    }

    @Override
    public void setBuffered(boolean buffered) {
        getBackingField().setBuffered(buffered);
    }

    @Override
    public void addValidator(Validator validator) {
        getBackingField().addValidator(validator);
    }

    @Override
    public Collection<Validator> getValidators() {
        return getBackingField().getValidators();
    }

    @Override
    public boolean isInvalidAllowed() {
        return getBackingField().isInvalidAllowed();
    }

    @Override
    public boolean isValid() {
        return getBackingField().isValid();
    }

    @Override
    public void removeAllValidators() {
        getBackingField().removeAllValidators();
    }

    @Override
    public void removeValidator(Validator validator) {
        getBackingField().removeValidator(validator);
    }

    @Override
    public void setInvalidAllowed(boolean invalidValueAllowed) throws UnsupportedOperationException {
        getBackingField().setInvalidAllowed(invalidValueAllowed);
    }

    @Override
    public void validate() throws Validator.InvalidValueException {
        getBackingField().validate();
    }
//
//    @Override
//    @Deprecated
//    public void addListener(ValueChangeListener listener) {
//        getBackingField().addListener(listener);
//    }
//
//    @Override
//    public void addValueChangeListener(ValueChangeListener listener) {
//        getBackingField().addValueChangeListener(listener);
//    }
//
//    @Override
//    @Deprecated
//    public void removeListener(ValueChangeListener listener) {
//        getBackingField().removeListener(listener);
//    }
//
//    @Override
//    public void removeValueChangeListener(ValueChangeListener listener) {
//        getBackingField().removeValueChangeListener(listener);
//    }
//
//    @Override
//    public void valueChange(Property.ValueChangeEvent event) {
//        getBackingField().valueChange(event);
//    }

//    @Override
//    public Property getPropertyDataSource() {
//        return getBackingField().getPropertyDataSource();
//    }
//
//    @Override
//    public void setPropertyDataSource(Property newDataSource) {
//        getBackingField().setPropertyDataSource(newDataSource);
//    }

    @Override
    public void focus() {
        getBackingField().focus();
    }

    @Override
    public int getTabIndex() {
        return getBackingField().getTabIndex();
    }

    @Override
    public void setTabIndex(int tabIndex) {
        getBackingField().setTabIndex(tabIndex);
    }

    @Override
    public void setImmediate(boolean immediate) {
        super.setImmediate(immediate);
        getBackingField().setImmediate(immediate);
    }
}
