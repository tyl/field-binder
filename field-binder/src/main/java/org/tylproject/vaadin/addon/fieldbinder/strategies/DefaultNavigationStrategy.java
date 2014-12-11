package org.tylproject.vaadin.addon.fieldbinder.strategies;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.ui.Field;
import org.tylproject.vaadin.addon.crudnav.events.*;
import org.tylproject.vaadin.addon.fieldbinder.FieldBinder;

/**
 * Created by evacchi on 26/11/14.
 */
public class DefaultNavigationStrategy<T> implements
        CurrentItemChange.Listener,
        CrudStrategy,
        FindStrategy {

    protected final FieldBinder<T> fieldBinder;
    protected FilterApplier filterApplier = new FilterApplier();
    private final Class<T> beanClass;

    public Class<T> getBeanClass() {
        return beanClass;
    }

    public DefaultNavigationStrategy(Class<T> beanClass, FieldBinder<T> fieldBinder) {
        this.fieldBinder = fieldBinder;
        this.beanClass = beanClass;
        fieldBinder.setReadOnly(true);
    }

    @Override
    public void currentItemChange(CurrentItemChange.Event event) {
        fieldBinder.setItemDataSource(event.getNewItem());
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
        // if the navigator does not point to a valid id
        // FIXME I don't recall what this particular check was supposed to mean: it was a hack
        if (event.getSource().getCurrentItemId() == null) {
            fieldBinder.unbindAll();
            fieldBinder.setReadOnly(false);
            event.getSource().setCurrentItemId(null);



        } else {
            // fields are already unbound, then
            // jus clear their contents
            fieldBinder.setReadOnly(false);
            event.getSource().setCurrentItemId(null);
            for (Field<?> f : fieldBinder.getFields())
                f.setValue(null);


            if (!filterApplier.hasAppliedFilters()) {
                filterApplier.restorePatterns(fieldBinder.getPropertyIdToFieldBindings());
                filterApplier.clearPropertyIdToFilterPatterns();
            }

        }

        fieldBinder.focus();
    }

    @Override
    public void onFind(OnFind.Event event) {
        filterApplier.applyFilters(fieldBinder.getPropertyIdToFieldBindings(), (Container.Filterable) event.getSource().getContainer());
        fieldBinder.bindAll();
        fieldBinder.setReadOnly(true);
        event.getSource().first();
    }



    @Override
    public void itemCreate(ItemCreate.Event event) {
        T bean = createBean();
        fieldBinder.setReadOnly(false);
        event.getSource().getContainer().addItem(bean);
        event.getSource().setCurrentItemId(bean);

    }

    protected T createBean() {
        try {
            return beanClass.newInstance();
        } catch (Exception ex) {
            throw new UnsupportedOperationException(ex);
        }
    }
}