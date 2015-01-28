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

package org.tylproject.vaadin.addon.datanav;

import com.vaadin.data.Container;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import org.tylproject.vaadin.addon.datanav.events.CurrentItemChange;
import org.tylproject.vaadin.addon.datanav.resources.Strings;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.ResourceBundle;

/**
 * Created by evacchi on 05/12/14.
 */
public class NavigationLabel extends CustomComponent {

    private static final ResourceBundle resourceBundle =
            ResourceBundle.getBundle(Strings.class.getCanonicalName());

    private final DataNavigation navigation;
    private final Label label;
    private static final String recordCounter = resourceBundle.getString("recordCounter");
    private static final String recordCounterFiltered = resourceBundle.getString("recordCounterFiltered");

    public NavigationLabel(final DataNavigation navigation) {
        this.label = new Label();
        this.navigation = navigation;

        setCompositionRoot(label);

        navigation.addCurrentItemChangeListener(new CurrentItemChange.Listener() {
            @Override
            public void currentItemChange(CurrentItemChange.Event event) {
                Object currentItemId = navigation.getCurrentItemId();
                Container container = navigation.getContainer();

                if ( container != null && !navigation.isClearToFindMode() ) {
                    if (! (container instanceof Container.Indexed)) {
                        throw new IllegalArgumentException(
                                "Label cannot be updated: " +
                                        "the given navigation  " + navigation +
                                        "contains a non-Indexed container");
                    }

                    Container.Indexed indexedContainer = (Container.Indexed) container;

                    int currentIndex = (currentItemId == null)?
                            0 :
                            1 + indexedContainer.indexOfId(currentItemId);

                    String message = isFiltered(container) ?
                            recordCounterFiltered
                            : recordCounter;

                    label.setValue(MessageFormat.format(
                            message,
                            currentIndex,
                            indexedContainer.size()));
                } else {
                    label.setValue("");
                }
            }
        });

    }


    public boolean isFiltered(Container container) {
        if (container instanceof Container.Filterable) {
            Collection<Container.Filter> filters = ((Container.Filterable) container).getContainerFilters();
            return filters != null && !filters.isEmpty();
        }
        return false;
    }


}
