package org.tylproject.vaadin.addon.fieldbinder.behavior.commons;

import com.vaadin.data.Container;
import org.tylproject.vaadin.addon.fieldbinder.FieldBinder;
import org.tylproject.vaadin.addon.fieldbinder.behavior.FindListeners;
import org.tylproject.vaadin.addon.fields.search.SearchForm;
import org.tylproject.vaadin.addon.fields.search.SearchWindow;
import org.tylproject.vaadin.addon.datanav.events.ClearToFind;
import org.tylproject.vaadin.addon.datanav.events.OnFind;
import org.tylproject.vaadin.addon.fields.search.SearchPattern;

/**
 * Created by evacchi on 23/01/15.
 */
public class SearchWindowFindListeners implements FindListeners {
    private final SearchWindow searchWindow;

    public SearchWindowFindListeners(SearchWindow searchWindow) {
        this.searchWindow = searchWindow;
    }

    public SearchWindowFindListeners(FieldBinder<?> fieldBinder) {
        this(new SearchWindow(new SearchForm(fieldBinder)));
    }

    @Override
    public void clearToFind(ClearToFind.Event event) {
        searchWindow.callFindOnClose(event.getSource());
        searchWindow.show();
    }

    @Override
    public void onFind(OnFind.Event event) {
        Container.Filterable container = (Container.Filterable)event.getSource().getContainer();
        container.removeAllContainerFilters();

        for (SearchPattern p : searchWindow.getSearchPatterns()) {
            container.addContainerFilter(p.getFilter());
        }

        event.getSource().first();
    }
}
