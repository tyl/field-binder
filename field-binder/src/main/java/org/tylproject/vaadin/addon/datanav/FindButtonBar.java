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

    public FindButtonBar(@Nonnull DataNavigation nav) {
        super(nav);

        Layout buttonLayout = getLayout();
        buttonLayout.addComponents(btnClearToFind, btnFind);

        btnClearToFind.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                nav().clearToFind();
            }
        });

        btnFind.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                nav().find();
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
        nav.removeFindEnabledListener(buttonEnabler);
        super.detachNavigation(nav);
    }

    FindEnabled.Listener buttonEnabler = new FindEnabled.Listener() {
        @Override
        public void findEnabled(FindEnabled.Event event) {
            if (event.isFindEnabled()) {
                Container.Ordered container = event.getSource().getContainer();
                if (container instanceof Container.Filterable) {

                    Container.Filterable filterable = (Container.Filterable) container;
                    if (filterable.getContainerFilters().isEmpty()
                            && filterable.size() > 0) {
                        enable(btnClearToFind);
                    }
                }
            } else {
                disable(findButtons);
            }
        }
    };

    @Override
    protected void updateButtonStatus() {

        if (!nav().isFindEnabled()) {
            disable(findButtons);
            return;
        }

        if (nav().isClearToFindMode()) {
            enable(btnFind);
            return;
        }


        if (nav().getContainer() == null) {
            disable(findButtons);
        } else {
            disable(btnFind);
            Container.Ordered container = nav().getContainer();

            if (container instanceof Container.Filterable) {

                Container.Filterable filterable = (Container.Filterable) container;
                if (filterable.getContainerFilters().isEmpty()
                        && filterable.size() == 0) {
                    disable(btnClearToFind);
                }
            }
        }
        
    }

    public Button getClearToFindButton() {
        return btnClearToFind;
    }

    public Button getFindButton() {
        return btnFind;
    }
}
