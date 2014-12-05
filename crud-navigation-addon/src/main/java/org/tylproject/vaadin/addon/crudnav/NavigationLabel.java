package org.tylproject.vaadin.addon.crudnav;

import com.vaadin.data.Container;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import org.tylproject.vaadin.addon.crudnav.events.CurrentItemChange;

/**
 * Created by evacchi on 05/12/14.
 */
public class NavigationLabel extends CustomComponent {
    private final CrudNavigation navigation;
    private final Label label;
    public NavigationLabel(final CrudNavigation navigation) {
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
                    label.setValue(String.format("%d of %d", current, indexedContainer.size()));
                } else {
                    label.setValue("");
                }
            }
        });
    }
}
