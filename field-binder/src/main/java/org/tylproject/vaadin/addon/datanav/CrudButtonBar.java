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
                disable(createButton, editButton, removeButton);
                enable(commitButton, discardButton);
            } else {
                enable(createButton, editButton, removeButton);
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
                disable(editButton, commitButton, discardButton, removeButton);
                enable(createButton);
            } else {
                enable(createButton);
                if (currentId != null) {
                    enable(editButton, commitButton, discardButton, removeButton);
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
