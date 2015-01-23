package org.tylproject.vaadin.addon.fieldbinder.behavior;

import com.vaadin.data.Container;
import org.tylproject.vaadin.addon.SearchWindow;
import org.tylproject.vaadin.addon.datanav.events.ClearToFind;
import org.tylproject.vaadin.addon.datanav.events.OnFind;
import org.tylproject.vaadin.addon.fields.SearchPattern;

import java.util.Collection;

/**
 * Created by evacchi on 23/01/15.
 */
public class SearchWindowFindListeners implements FindListeners {
    private final SearchWindow searchWindow;

    public SearchWindowFindListeners(SearchWindow searchWindow) {
        this.searchWindow = searchWindow;
    }

    @Override
    public void clearToFind(ClearToFind.Event event) {
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
