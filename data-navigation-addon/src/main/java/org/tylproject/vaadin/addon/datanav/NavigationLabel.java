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
                Object id = navigation.getCurrentItemId();
                Container container = navigation.getContainer();

                if ( id != null ) {
                    if (! (container instanceof Container.Indexed)) {
                        throw new IllegalArgumentException(
                                "Label cannot be updated: " +
                                        "the given navigation  " + navigation +
                                        "contains a non-Indexed container");
                    }

                    Container.Indexed indexedContainer = (Container.Indexed) container;
                    int current = 1 + indexedContainer.indexOfId(navigation.getCurrentItemId());

                    String message = isFiltered(container) ?
                            recordCounterFiltered
                            : recordCounter;

                    label.setValue(MessageFormat.format(
                            message,
                            current,
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
