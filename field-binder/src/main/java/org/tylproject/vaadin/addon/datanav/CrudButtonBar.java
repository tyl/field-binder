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

    private final Button createButton = button("create");
    private final Button editButton = button("edit");
    private final Button removeButton = button("remove");
    private final Button commitButton = button("commit");
    private final Button discardButton = button("discard");


    private final Button[] crudButtons = {
            createButton,
            editButton,
            removeButton,
            commitButton,
            discardButton
    };


    public CrudButtonBar(final DataNavigation nav) {
        super(nav);
        Layout buttonLayout = getLayout();

        buttonLayout.addComponent(createButton);
        buttonLayout.addComponent(editButton);
        buttonLayout.addComponent(removeButton);
        buttonLayout.addComponent(commitButton);
        buttonLayout.addComponent(discardButton);



        createButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                nav().create();
            }
        });

        editButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                nav().edit();
            }
        });


        removeButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                nav().remove();
            }
        });

        commitButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                nav().commit();
            }
        });

        discardButton.addClickListener(new Button.ClickListener() {
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
        super.detachNavigation(nav);
        nav.removeCrudEnabledListener(buttonEnabler);
        nav.removeEditingModeChangeListener(editingListener);
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
                disable(createButton, editButton, removeButton);
                enable(commitButton, discardButton);
            } else {
                enable(createButton, editButton, removeButton);
                disable(commitButton, discardButton);
            }
        }
    };

    @Override
    protected void updateButtonStatus() {

        if (!nav().isCrudEnabled()) {
            disable(crudButtons);
            return;
        }

        if (nav().isEditingMode()) {
            return;
        }


        if (nav().getContainer() == null) {
            disable(crudButtons);
        } else {
            enable(createButton);
            disable(commitButton, discardButton);
            if (nav().getContainer().size() == 0) {
                disable(removeButton, editButton);
            } else {
                if (nav().getCurrentItemId() != null) {
                    enable(removeButton, editButton);
                }
            }
        }

    }


    public Button getCreateButton() {
        return createButton;
    }

    public Button getCommitButton() {
        return commitButton;
    }

    public Button getDiscardButton() {
        return discardButton;
    }

    public Button getEditButton() {
        return editButton;
    }

    public Button getRemoveButton() {
        return removeButton;
    }
}
