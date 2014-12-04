package org.tylproject.vaadin.addon.crudnav;

import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Layout;
import org.tylproject.vaadin.addon.crudnav.events.CurrentItemChange;

import javax.annotation.Nonnull;

/**
 * Created by evacchi on 04/12/14.
 */
public class CrudButtonBar extends AbstractButtonBar {

    private final Button btnCreate  = new Button("Create");
    private final Button btnEdit    = new Button("Edit");
    private final Button btnRemove  = new Button("Remove");
    private final Button btnCommit  = new Button("Commit");
    private final Button btnDiscard = new Button("Discard");


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
    protected void updateButtonStatus() {

        Object currentId = nav().getCurrentItemId();
        if (currentId == null) {
            disable(btnCreate);
        } else {
            enable(btnCreate);
        }
    }


}
