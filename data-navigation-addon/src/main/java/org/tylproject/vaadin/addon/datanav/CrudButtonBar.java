package org.tylproject.vaadin.addon.datanav;

import com.vaadin.ui.Button;
import com.vaadin.ui.Layout;
import org.tylproject.vaadin.addon.datanav.events.CrudEnabled;
import org.tylproject.vaadin.addon.datanav.events.EditingModeChange;

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


    public CrudButtonBar(final DataNavigation nav) {
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
                nav().create();
            }
        });

        btnEdit.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
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
                nav().commit();
            }
        });

        btnDiscard.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                nav().discard();
            }
        });

        setNavigation(nav);

    }

    @Override
    protected void attachNavigation(@Nonnull DataNavigation nav) {
        nav.addCrudEnabledListener(buttonEnabler);
        nav.addEditingModeChangeListener(editingListener);
        super.attachNavigation(nav);
    }

    @Override
    protected void detachNavigation(@Nonnull DataNavigation nav) {
        nav.removeCrudEnabledListener(buttonEnabler);
        nav.removeEditingModeChangeListener(editingListener);
        super.detachNavigation(nav);
    }

    CrudEnabled.Listener buttonEnabler = new CrudEnabled.Listener() {
        @Override
        public void crudEnabled(CrudEnabled.Event event) {
            updateButtonStatus();

        }
    };

    EditingModeChange.Listener editingListener = new EditingModeChange.Listener() {

        @Override
        public void editingModeChange(EditingModeChange.Event event) {
            if (event.isEnteringEditingMode()) {
                disable(btnCreate, btnEdit, btnRemove);
                enable(btnCommit, btnDiscard);
            } else {
                enable(btnCreate, btnEdit, btnRemove);
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
