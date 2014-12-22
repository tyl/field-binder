package org.tylproject.vaadin.addon.datanav;

import com.vaadin.data.Container;
import com.vaadin.ui.Button;
import com.vaadin.ui.Layout;
import org.tylproject.vaadin.addon.datanav.events.FindEnabled;

import javax.annotation.Nonnull;

/**
 * Created by evacchi on 04/12/14.
 */
public class FindButtonBar extends AbstractButtonBar {

    final Button btnClearToFind = button("clearToFind");
    final Button btnFind = button("find");


    private final Button[] findButtons = {
            btnClearToFind,
            btnFind
    };

    public FindButtonBar() { this(new BasicDataNavigation()); }

    public FindButtonBar(@Nonnull DataNavigation nav) {
        super(nav);

        Layout buttonLayout = getLayout();
        buttonLayout.addComponents(btnClearToFind, btnFind);

        btnClearToFind.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                getNavigation().clearToFind();
            }
        });

        btnFind.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                getNavigation().find();
            }
        });

        setNavigation(nav);
    }


    @Override
    protected void attachNavigation(@Nonnull DataNavigation nav) {
        super.attachNavigation(nav);
        nav.addFindEnabledListener(buttonEnabler);
    }

    @Override
    protected void detachNavigation(@Nonnull DataNavigation nav) {
        if (nav == null) return;
        nav.removeFindEnabledListener(buttonEnabler);
        super.detachNavigation(nav);
    }

    FindEnabled.Listener buttonEnabler = new FindEnabled.Listener() {
        @Override
        public void findEnabled(FindEnabled.Event event) {
            if (event.isFindEnabled()) {
                // default to disabling clearToFind
                updateClearToFind(event.getSource().getContainer());
            } else {
                disable(findButtons);
            }
        }
    };

    private void updateClearToFind(Container container) {
        disable(btnFind);
        disable(btnClearToFind);
        // if it is filterable
        if (container instanceof Container.Filterable) {
            Container.Filterable filterable = (Container.Filterable) container;
            if (filterable.size() > 0 // and it contains values
                    || !filterable.getContainerFilters().isEmpty()) { // when no filters have been applied
                enable(btnClearToFind);
            }

        }
    }

    @Override
    protected void updateButtonStatus() {

        if (!getNavigation().isFindEnabled()) {
            disable(findButtons);
            return;
        }

        if (getNavigation().isClearToFindMode()) {
            enable(btnFind);
            return;
        }


        if (getNavigation().getContainer() == null) {
            disable(findButtons);
        } else {
            updateClearToFind(getNavigation().getContainer());
        }
        
    }

    public Button getClearToFindButton() {
        return btnClearToFind;
    }

    public Button getFindButton() {
        return btnFind;
    }
}
