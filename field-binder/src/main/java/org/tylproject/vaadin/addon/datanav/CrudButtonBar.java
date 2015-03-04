/*
 * Copyright (c) 2015 - Tyl Consulting s.a.s.
 *
 *   Authors: Edoardo Vacchi
 *   Contributors: Marco Pancotti, Daniele Zonca
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.tylproject.vaadin.addon.datanav;

import com.vaadin.ui.Button;
import com.vaadin.ui.Layout;
import org.tylproject.vaadin.addon.datanav.events.CrudEnabled;
import org.tylproject.vaadin.addon.datanav.events.CurrentItemChange;
import org.tylproject.vaadin.addon.datanav.events.EditingModeChange;

import javax.annotation.Nonnull;

/**
 * ButtonBar for Crud Events
 */
public class CrudButtonBar extends AbstractButtonBar implements CurrentItemChange.Listener, EditingModeChange.Listener, CrudEnabled.Listener {

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
                getNavigation().create();
            }
        });

        editButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                getNavigation().edit();
            }
        });


        removeButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                getNavigation().remove();
            }
        });

        commitButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                getNavigation().commit();
            }
        });

        discardButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                getNavigation().discard();
            }
        });

        setNavigation(nav);

    }

    @Override
    protected void attachNavigation(@Nonnull DataNavigation nav) {
        nav.addCrudEnabledListener(this);
        nav.addEditingModeChangeListener(this);
        nav.addCurrentItemChangeListener(this);
    }

    @Override
    protected void detachNavigation(@Nonnull DataNavigation nav) {
        nav.removeCurrentItemChangeListener(this);
        nav.removeCrudEnabledListener(this);
        nav.removeEditingModeChangeListener(this);
    }


    protected void updateButtonStatus() {
        if (getNavigation().isEditingMode()) {
            return;
        }

        if (!getNavigation().isCrudEnabled()) {
            return;
        }

        if (getNavigation().getContainer() == null) {
            disable(crudButtons);
        } else {
            enable(createButton);
            disable(commitButton, discardButton);
            if (getNavigation().getContainer().size() == 0) {
                disable(removeButton, editButton);
            } else {
                if (getNavigation().getCurrentItemId() != null) {
                    enable(removeButton, editButton);
                }
            }
        }
    }

    @Override
    public void crudEnabled(CrudEnabled.Event event) {
       if (!event.isCrudEnabled()) {
           disable(crudButtons);
       } else {
           updateButtonStatus();
       }

    }

    @Override
    public void currentItemChange(CurrentItemChange.Event event) {
        updateButtonStatus();
    }

    @Override
    public void editingModeChange(EditingModeChange.Event event) {
        if (!event.getSource().isCrudEnabled()) return;

        if (event.isEnteringEditingMode()) {
            disable(createButton, editButton, removeButton);
            enable(commitButton, discardButton);
        } else {
            enable(createButton, editButton, removeButton);
            disable(commitButton, discardButton);
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
