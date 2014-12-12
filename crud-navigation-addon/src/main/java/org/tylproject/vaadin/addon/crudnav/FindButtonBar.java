package org.tylproject.vaadin.addon.crudnav;

import com.vaadin.ui.Button;
import com.vaadin.ui.Layout;
import org.tylproject.vaadin.addon.crudnav.events.FindEnabled;

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

    public FindButtonBar(@Nonnull CrudNavigation nav) {
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
    protected void attachNavigation(@Nonnull CrudNavigation nav) {
        super.attachNavigation(nav);
        nav.addFindEnabledListener(buttonEnabler);
    }

    @Override
    protected void detachNavigation(@Nonnull CrudNavigation nav) {
        nav.removeFindEnabledListener(buttonEnabler);
        super.detachNavigation(nav);
    }

    FindEnabled.Listener buttonEnabler = new FindEnabled.Listener() {
        @Override
        public void findEnabled(FindEnabled.Event event) {
            if (event.isFindEnabled()) {
                enable(btnClearToFind);
            } else {
                disable(findButtons);
            }
        }
    };

    @Override
    protected void updateButtonStatus() {
        if (!nav().isFindEnabled() || nav().getCurrentItemId() != null) {
            disable(btnFind);
        } else {
            enable(btnClearToFind, btnFind);
        }
    }

    public Button getClearToFindButton() {
        return btnClearToFind;
    }

    public Button getFindButton() {
        return btnFind;
    }
}
