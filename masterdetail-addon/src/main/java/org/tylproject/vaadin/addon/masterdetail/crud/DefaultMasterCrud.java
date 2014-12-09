package org.tylproject.vaadin.addon.masterdetail.crud;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Validator;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.data.util.filter.And;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Field;
import org.tylproject.vaadin.addon.crudnav.events.*;
import org.tylproject.vaadin.addon.fieldbinder.FieldBinder;
import org.tylproject.vaadin.addon.masterdetail.Master;
import org.tylproject.vaadin.addon.crudnav.CrudNavigation;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by evacchi on 26/11/14.
 */
public abstract class DefaultMasterCrud implements MasterCrud, ClearToFind.Listener, OnFind.Listener {

//    protected CrudNavigation navigation;
    protected FieldBinder<?> fieldBinder;
    protected FilterFactory filterFactory = new DefaultFilterFactory();

    public DefaultMasterCrud() {}

    @Override
    public MasterCrud withMaster(Master<?> target) {
//        this.navigation = target.getNavigation();
        this.fieldBinder = target.getFieldBinder();
        fieldBinder.setReadOnly(true);
        return this;
    }

    @Override
    public void onCommit(OnCommit.Event event) {
        fieldBinder.commit();
        fieldBinder.setReadOnly(true);
    }

    @Override
    public void onDiscard(OnDiscard.Event event) {
        fieldBinder.discard();
        fieldBinder.setReadOnly(true);
        Item currentItem = event.getSource().getCurrentItem();
        if (currentItem == null) {
            event.getSource().first();
        }
    }

    @Override
    public void itemRemove(ItemRemove.Event event) {
        event.getSource().getContainer().removeItem(event.getSource().getCurrentItemId());
    }

    @Override
    public void itemEdit(ItemEdit.Event event) {
        fieldBinder.setReadOnly(!fieldBinder.isReadOnly());
        fieldBinder.focus();
    }

    public void clearToFind(ClearToFind.Event event) {
        if (event.getSource().getCurrentItemId() == null) {
            fieldBinder.unbindAll();
            fieldBinder.setReadOnly(false);
            event.getSource().setCurrentItemId(null);


        } else {
             fieldBinder.setReadOnly(false);
            event.getSource().setCurrentItemId(null);
            for (Field<?> f : fieldBinder.getFields())
                f.setValue(null);


            if (!getPropertyIdToFilterPattern().isEmpty()) {
                restorePatterns(fieldBinder, getPropertyIdToFilterPattern());
                clearPropertyIdToFilterPattern();
            }

        }

        fieldBinder.focus();
    }


    /**
     * maps fieldId to filter
     */
    Map<Object, Object> propertyIdToFilterPattern = new HashMap<Object, Object>();

    public Map<Object, Object> getPropertyIdToFilterPattern () {
        return propertyIdToFilterPattern;
    }


    private void clearPropertyIdToFilterPattern() {
        propertyIdToFilterPattern.clear();
    }


    private void restorePatterns(FieldBinder<?> fieldBinder,  Map<Object, Object> propertyIdToFilterPattern) {
        for (Map.Entry<Field<?>, Object> e : fieldBinder.getFieldToPropertyIdBindings().entrySet()) {

            Field field = e.getKey(); // raw type
            Object propertyId = e.getValue();
            Object pattern = propertyIdToFilterPattern.get(propertyId);

            field.setValue(pattern);

        }
    }

    @Override
    public void onFind(OnFind.Event event) {
        applyFilters(fieldBinder, (Container.Filterable) event.getSource().getContainer());
        fieldBinder.bindAll();
        fieldBinder.setReadOnly(true);
        event.getSource().first();
    }

    private void applyFilters(FieldBinder<?> fieldBinder, Container.Filterable container) {

        clearPropertyIdToFilterPattern();

        container.removeAllContainerFilters();
        for (Map.Entry<Field<?>,Object> e : fieldBinder.getFieldToPropertyIdBindings().entrySet()) {
            Field<?> prop = e.getKey();
            Object propertyId = e.getValue();
            Object pattern = prop.getValue();
            Class<?> modelType = getModelType(prop);
            if (pattern != null) {

                propertyIdToFilterPattern.put(propertyId, pattern);

                container.addContainerFilter(filterFactory.createFilter(modelType,
                        propertyId, pattern));
            }
        }

    }

    private Class<?> getModelType(Field<?> prop) {
        if (prop instanceof AbstractField) {
            AbstractField<?> abstractField = (AbstractField<?>) prop;
            Converter<?, Object> converter = abstractField.getConverter();
            if (converter != null) {
                return converter.getModelType();
            }
        }

        // otherwise, fallback to the property type
        return prop.getType();

    }

}
