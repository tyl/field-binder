package org.tylproject.vaadin.addon.crudnav;

import com.vaadin.ui.Button;
import com.vaadin.ui.Layout;

import javax.annotation.Nonnull;

/**
 * Created by evacchi on 04/12/14.
 */
public class FindButtonBar extends AbstractButtonBar {

    final Button btnClearToFind = button("clearToFind");
    final Button btnFind = button("find");


    private final Button[] crudButtons = {
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
    protected void updateButtonStatus() {
        if (nav().getCurrentItemId() != null) {
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
