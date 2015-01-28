package org.tylproject.vaadin.addon.fieldbinder.behavior;

import com.vaadin.data.Container;
import org.tylproject.vaadin.addon.datanav.events.ClearToFind;
import org.tylproject.vaadin.addon.datanav.events.OnFind;
import org.tylproject.vaadin.addon.fieldbinder.FieldBinder;
import org.tylproject.vaadin.addon.fields.search.SearchFieldManager;
import org.tylproject.vaadin.addon.fields.search.SearchPattern;

/**
 * Created by evacchi on 28/01/15.
 */
public class FieldBinderFindListeners<T> implements FindListeners {
    //                    FilterPatternField shortNameSearch = new FilterPatternTextField("shortName", String.class);
//                    FilterPatternComboBox discriminatorSearch = new FilterPatternComboBox("discriminator", PartyDiscriminator.class);
    boolean clearToFindMode = false;
    final FieldBinder<T> binder;
    final SearchFieldManager searchFieldManager;
    public FieldBinderFindListeners(FieldBinder<T> binder) {
        this.binder = binder;
        this.searchFieldManager = new SearchFieldManager(binder);
    }


    @Override
    public void clearToFind(ClearToFind.Event event) {
        if (clearToFindMode) {
            searchFieldManager.clear();
            return;
        } else {
            clearToFindMode = true;
        }

        ((Container.Filterable)event.getSource().getContainer()).removeAllContainerFilters();

        searchFieldManager.replaceFields(binder);

    }

    @Override
    public void onFind(OnFind.Event event) {

        clearToFindMode = false;

        for (SearchPattern sp : searchFieldManager.getPatternsFromValues().values()) {
            ((Container.Filterable)event.getSource().getContainer())
                .addContainerFilter(sp.getFilter());
        }

        searchFieldManager.restoreFields(binder);
        binder.getNavigation().first();

    }
}
