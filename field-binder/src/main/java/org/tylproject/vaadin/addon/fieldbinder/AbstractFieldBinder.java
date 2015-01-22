/*
 * Copyright (c) 2014 - Tyl Consulting s.a.s.
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

import com.vaadin.data.Item;
import com.vaadin.data.Validator;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.FieldGroupFieldFactory;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.DefaultFieldFactory;
import com.vaadin.ui.Field;

import java.io.Serializable;
import java.util.*;

/**
 * Base class for the FieldBinder
 */
public abstract class AbstractFieldBinder<T extends FieldGroup> implements Serializable {
    private final T fieldGroup;
    private final Map<Object, Field<?>> propertyIdToField;
    private final Map<Object, Class<?>> propertyIdToType;
    private final Map<Field<?>, Object> fieldToPropertyId;

    private Item itemDataSource = null;


    private FieldBinderFieldFactory fieldFactory = new FieldBinderFieldFactory();

    public AbstractFieldBinder(T fieldGroup) {
        this.fieldGroup = fieldGroup;
        this.propertyIdToField = new LinkedHashMap<Object, Field<?>>();
        this.propertyIdToType  = new LinkedHashMap<Object, Class<?>>();
        this.fieldToPropertyId = new LinkedHashMap<Field<?>, Object>();

    }

    public T getFieldGroup() {
        return fieldGroup;
    }


    public Collection<Object> getBindingPropertyIds() {
        return Collections.unmodifiableCollection(propertyIdToField.keySet());
    }

    public Map<Field<?>, Object> getFieldToPropertyIdBindings() {
        return Collections.unmodifiableMap(fieldToPropertyId);
    }


    public Map<Object, Field<?>> getPropertyIdToFieldBindings() {
        return Collections.unmodifiableMap(propertyIdToField);
    }


    public Map<Object, Class<?>> getPropertyIdToTypeBindings() {
        return Collections.unmodifiableMap(propertyIdToType);
    }


    public Field<?> getField(Object propertyId) {
        return propertyIdToField.get(propertyId);
    }

    public Object getPropertyId(Field<?> field) {
        return fieldToPropertyId.get(field);
    }


    /**
     * returns all the fields that are managed by this FieldBinder
     */
    public Collection<Field<?>> getFields() {
        return Collections.unmodifiableCollection(propertyIdToField.values());
    }

    /**
     * bind all the managed fields to the data source
     */
    public void bindAll() {
        for (Object fid: this.getBindingPropertyIds())
            bind(propertyIdToField.get(fid), fid);
    }

    /**
     * unbinds all the managed fields from
     */
    public void unbindAll() {
        for (Object fid: this.getBindingPropertyIds()) {
            Field<?> f = propertyIdToField.get(fid);
            unbind(f);
        }
    }

    public void clear() {
    	for (Object fid: this.getBindingPropertyIds()) {
            Field<?> f = propertyIdToField.get(fid);
            f.setValue(null);
    	}
    }

    protected void resetField(Field<?> field) {
        field.setBuffered(false);
        field.setEnabled(true);
        field.setReadOnly(false);
        field.setValue(null);
    }

    protected void configureField(Field<?> field) {
        field.setBuffered(isBuffered());

        field.setEnabled(isEnabled());

        if (field.getPropertyDataSource() != null
                && field.getPropertyDataSource().isReadOnly()) {
            field.setReadOnly(true);
        } else {
            field.setReadOnly(isReadOnly());
        }
    }


    /**
     * Updates the item that is used by this FieldBinder. Rebinds all fields to
     * the properties in the new item.
     *
     * @param itemDataSource
     *            The new item to use
     */
    public void setItemDataSource(Item itemDataSource) {
        this.itemDataSource = itemDataSource;
        if (itemDataSource == null) {
            unbindAll();
        } else {
            getFieldGroup().setItemDataSource(itemDataSource);
            bindAll();
        }
    }

    public void bind(Field<?> field, Object propertyId) {
        propertyIdToField.put(propertyId, field);
        fieldToPropertyId.put(field, propertyId);
        propertyIdToType.put(propertyId, field.getType());

        if (field instanceof AbstractField<?>) {
            ((AbstractField) field).setValidationVisible(false);
        }

        if (hasItemDataSource())
            getFieldGroup().bind(field, propertyId);

        configureField(field);
    }

    public void unbind(Field<?> field) {
        if (hasItemDataSource())
            getFieldGroup().unbind(field);


        if (field instanceof AbstractField<?>) {
            ((AbstractField) field).setValidationVisible(false);
        }

        field.setPropertyDataSource(null);
        resetField(field);
        configureField(field);
    }

    public boolean hasItemDataSource() {
        return this.itemDataSource != null;
    }

    /**
     * Build automatically a Field for the given propertyId
     *
     * The mapping between propertyIds and field is given
     * through a specialized {@link com.vaadin.ui.DefaultFieldFactory}
     * ({@link org.tylproject.vaadin.addon.fieldbinder.FieldBinderFieldFactory})
     *
     * The mapping between propertyIds and types is given by
     * overriding {@link #getPropertyType(Object)}
     *
     * Generates a caption as well, by splitting over
     * camelCased property ids.
     */
    public <T extends Field<?>> T build(Object propertyId) {
        String caption = DefaultFieldFactory
                .createCaptionByPropertyId(propertyId);
        return (T) build(caption, propertyId);
    }


    /**
     * Build automatically a Field for the given propertyId, with the
     * given caption.
     *
     */
    public <T extends Field<?>> T build(String caption, Object propertyId) {
        return (T) build(caption, propertyId, Field.class);
    }

    /**
     * Build a field of the given type, for the given propertyId,
     * with the given caption,
     *
     */
    public <T extends Field<?>> T build(String caption, Object propertyId, Class<T> fieldType) {
        Class<?> dataType = getPropertyType(propertyId);

        T field = getFieldFactory().createField(dataType, fieldType);
        if (field == null) {
            throw new BuildException("Unable to build a field of type "
                    + fieldType.getName() + " for editing "
                    + dataType.getName());
        }

        field.setCaption(caption);

        bind(field, propertyId);

        return field;
    }


    protected abstract Class<?> getPropertyType(Object propertyId);

    /**
     * Gets the item used by this FieldBinder. Note that you must call
     * {@link #commit()} for the item to be updated unless buffered mode has
     * been switched off.
     *
     * @see #setBuffered(boolean)
     * @see #commit()
     *
     * @return The item used by this FieldBinder
     */
    public Item getItemDataSource() {
        return itemDataSource;
    }

    /**
     * Checks the buffered mode for the bound fields.
     * <p>
     *
     * @see #setBuffered(boolean) for more details on buffered mode
     *
     * @see Field#isBuffered()
     * @return true if buffered mode is on, false otherwise
     *
     */
    public boolean isBuffered() {
        return getFieldGroup().isBuffered();
    }

    /**
     * Sets the buffered mode for the bound fields.
     * <p>
     * When buffered mode is on the item will not be updated until
     * {@link #commit()} is called. If buffered mode is off the item will be
     * updated once the fields are updated.
     * </p>
     * <p>
     * The default is to use buffered mode.
     * </p>
     *
     * @see Field#setBuffered(boolean)
     * @param buffered
     *            true to turn on buffered mode, false otherwise
     */
    public void setBuffered(boolean buffered) {
        getFieldGroup().setBuffered(true);
        for (Field<?> field : getFields()) {
            field.setBuffered(buffered);
        }
    }

    /**
     * Returns the enabled status for the fields.
     * <p>
     * Note that this will not accurately represent the enabled status of all
     * fields if you change the enabled status of the fields through some other
     * method than {@link #setEnabled(boolean)}.
     *
     * @return true if the fields are enabled, false otherwise
     */
    public boolean isEnabled() {
        return getFieldGroup().isEnabled();
    }

    /**
     * Updates the enabled state of all fields.
     *
     * @param fieldsEnabled
     *            true to enable all fields, false to disable them
     */
    public void setEnabled(boolean fieldsEnabled) {
        getFieldGroup().setEnabled(fieldsEnabled);
        for (Field<?> field : getFields()) {
            field.setEnabled(fieldsEnabled);
        }
    }

    /**
     * Returns the read only status that is used by default with all fields that
     * have a writable data source.
     * <p>
     * Note that this will not accurately represent the read only status of all
     * fields if you change the read only status of the fields through some
     * other method than {@link #setReadOnly(boolean)}.
     *
     * @return true if the fields are set to read only, false otherwise
     */
    public boolean isReadOnly() {
        return getFieldGroup().isReadOnly();
    }

    /**
     * Sets the read only state to the given value for all fields with writable
     * data source. Fields with read only data source will always be set to read
     * only.
     *
     * @param fieldsReadOnly
     *            true to set the fields with writable data source to read only,
     *            false to set them to read write
     */
    public void setReadOnly(boolean fieldsReadOnly) {
        getFieldGroup().setReadOnly(fieldsReadOnly);
        for (Field<?> field : getFields()) {
            if (field.getPropertyDataSource() != null
                    && field.getPropertyDataSource().isReadOnly()) {
                field.setReadOnly(true);
            } else {
                field.setReadOnly(fieldsReadOnly);
            }
        }
    }

    public void commit() {
        try {
            getFieldGroup().commit();
        } catch (FieldGroup.CommitException ex) {
            throw new CommitException(ex);
        }
    }


    public void discard() {
        getFieldGroup().discard();
    }


    /**
     * Checks the validity of the bound fields.
     * <p>
     * Call the {@link Field#validate()} for the fields to get the individual
     * error messages.
     *
     * @return true if all bound fields are valid, false otherwise.
     */
    public boolean isValid() {
        try {
            for (Field<?> field : getFields()) {
                field.validate();
            }
            return true;
        } catch (Validator.InvalidValueException e) {
            return false;
        }
    }

    /**
     * Checks if any bound field has been modified.
     *
     * @return true if at least one field has been modified, false otherwise
     */
    public boolean isModified() {
        for (Field<?> field : getFields()) {
            if (field.isModified()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the field factory for the {@link FieldGroup}. The field factory is
     * only used when {@link FieldGroup} creates a new field.
     *
     * @return The field factory in use
     *
     */
    public FieldBinderFieldFactory getFieldFactory() {
        return fieldFactory;
    }

    /**
     * Sets the field factory for the {@link FieldGroup}. The field factory is
     * only used when {@link FieldGroup} creates a new field.
     *
     * @param fieldFactory
     *            The field factory to use
     */
    public void FieldBinderFieldFactory(FieldBinderFieldFactory fieldFactory) {
        this.fieldFactory = fieldFactory;
    }

}
