package org.tylproject.vaadin.addon.fieldbinder.behavior.legacy;

import com.vaadin.data.Container;
import org.tylproject.vaadin.addon.datanav.events.ClearToFind;
import org.tylproject.vaadin.addon.datanav.events.OnFind;
import org.tylproject.vaadin.addon.fieldbinder.FieldBinder;
import org.tylproject.vaadin.addon.fieldbinder.behavior.FindListeners;

/**
 * Created by evacchi on 28/01/15.
 */
public class LegacyFindListeners<T> implements FindListeners {
    private final FieldBinder<T> fieldBinder;
    protected FilterApplier filterApplier = new FilterApplier();

    public LegacyFindListeners(FieldBinder<T> fieldBinder) {
        this.fieldBinder = fieldBinder;
    }

    public void clearToFind(ClearToFind.Event event) {

        if (fieldBinder.hasItemDataSource()) {
            fieldBinder.unbindAll();
        }

        fieldBinder.setReadOnly(false);
        event.getSource().setCurrentItemId(null);

        if (filterApplier.hasAppliedFilters()) {
            filterApplier.restorePatterns(fieldBinder.getPropertyIdToFieldBindings());
            filterApplier.clearPropertyIdToFilterPatterns();
        }
//
//
        fieldBinder.focus();
    }

    @Override
    public void onFind(OnFind.Event event) {
        filterApplier.applyFilters(fieldBinder.getPropertyIdToFieldBindings(), (Container.Filterable) event.getSource().getContainer());
        event.getSource().first();
        fieldBinder.setReadOnly(true);
        fieldBinder.bindAll();
    }
}
