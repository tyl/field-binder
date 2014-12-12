package org.tylproject.vaadin.addon.crudnav;

import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Layout;
import org.tylproject.vaadin.addon.crudnav.events.CrudEnabled;
import org.tylproject.vaadin.addon.crudnav.events.CurrentItemChange;
import org.tylproject.vaadin.addon.crudnav.events.NavigationEnabled;

import javax.annotation.Nonnull;

/**
 * Created by evacchi on 04/12/14.
 */
public class CrudButtonBar extends AbstractButtonBar {

    private final Button btnCreate  = button("create");
    private final Button btnEdit    = button("edit");
    private final Button btnRemove  = button("remove");
    private final Button btnCommit  = button("commit");
    private final Button btnDiscard = button("discard");


    private final Button[] crudButtons = {
            btnCreate,
            btnEdit,
            btnRemove,
            btnCommit,
            btnDiscard
    };


    public CrudButtonBar(final CrudNavigation nav) {
        super(nav);
        Layout buttonLayout = getLayout();

        buttonLayout.addComponent(btnCreate);
        buttonLayout.addComponent(btnEdit);
        buttonLayout.addComponent(btnRemove);
        buttonLayout.addComponent(btnCommit);
        buttonLayout.addComponent(btnDiscard);



        btnCreate.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                disable(btnCreate, btnEdit, btnRemove);
                enable(btnCommit, btnDiscard);

                nav().create();
            }
        });

        btnEdit.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                if (btnEdit.isEnabled()) {
                    disable(btnCreate, btnEdit, btnRemove);
                } else {
                    enable(btnCreate, btnEdit, btnRemove);
                }
                nav().edit();
            }
        });


        btnRemove.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                nav().remove();
            }
        });

        btnCommit.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                enable(btnCreate, btnEdit, btnRemove);
                nav().commit();
            }
        });

        btnDiscard.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                enable(btnCreate, btnEdit, btnRemove);
                nav().discard();
            }
        });

        setNavigation(nav);

    }

    @Override
    protected void attachNavigation(@Nonnull CrudNavigation nav) {
        nav.addCrudEnabledListener(buttonEnabler);
        super.attachNavigation(nav);
    }

    @Override
    protected void detachNavigation(@Nonnull CrudNavigation nav) {
        nav.removeCrudEnabledListener(buttonEnabler);
        super.detachNavigation(nav);
    }

    CrudEnabled.Listener buttonEnabler = new CrudEnabled.Listener() {
        @Override
        public void crudEnabled(CrudEnabled.Event event) {
            if (event.isCrudEnabled()) {
                enable(crudButtons);
            } else {
                disable(crudButtons);
            }
        }
    };

    @Override
    protected void updateButtonStatus() {

        if (!nav().isCrudEnabled()) {
            disable(crudButtons);
            return;
        }

        Object currentId = nav().getCurrentItemId();

        if (nav().getContainer() != null) {
            if (nav().getContainer().size() == 0) {
                disable(btnEdit, btnCommit, btnDiscard, btnRemove);
                enable(btnCreate);
            } else {
                enable(btnCreate);
                if (currentId != null) {
                    enable(btnEdit, btnCommit, btnDiscard, btnRemove);
                }
            }
        }
    }


}
