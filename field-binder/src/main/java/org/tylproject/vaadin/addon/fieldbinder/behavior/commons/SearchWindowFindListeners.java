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

package org.tylproject.vaadin.addon.fieldbinder.behavior.commons;

import com.vaadin.data.Container;
import org.tylproject.vaadin.addon.datanav.DataNavigation;
import org.tylproject.vaadin.addon.fieldbinder.FieldBinder;
import org.tylproject.vaadin.addon.fieldbinder.behavior.FindListeners;
import org.tylproject.vaadin.addon.fields.search.SearchForm;
import org.tylproject.vaadin.addon.fields.search.SearchWindow;
import org.tylproject.vaadin.addon.datanav.events.ClearToFind;
import org.tylproject.vaadin.addon.datanav.events.OnFind;
import org.tylproject.vaadin.addon.fields.search.SearchPattern;

/**
 * Stand-alone search popup
 */
public class SearchWindowFindListeners implements FindListeners {
    protected final SearchWindow searchWindow;

    public SearchWindowFindListeners(SearchWindow searchWindow) {
        this.searchWindow = searchWindow;
    }

    public SearchWindowFindListeners(FieldBinder<?> fieldBinder) {
        this(new SearchWindow(new SearchForm(fieldBinder)));
    }

    @Override
    public void clearToFind(ClearToFind.Event event) {
        final DataNavigation navigation = event.getSource();
        searchWindow.callFindEventOnWindowClosed(navigation);
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
