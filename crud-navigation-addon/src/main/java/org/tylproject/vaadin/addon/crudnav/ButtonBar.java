package org.tylproject.vaadin.addon.crudnav;

import com.vaadin.data.Container;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Layout;
import org.tylproject.vaadin.addon.crudnav.events.CurrentItemChange;

import javax.annotation.Nonnull;

/**
 * Created by evacchi on 19/11/14.
 */
public class ButtonBar {

    public static ButtonBar forNavigation(CrudNavigation nav) {
        return new ButtonBar(nav);
    }

    private final HorizontalLayout buttonLayout;
    private @Nonnull CrudNavigation nav;

    private final Button btnFirst = new Button("First");
    private final Button btnPrev  = new Button("Prev");
    private final Button btnNext  = new Button("Next");
    private final Button btnLast  = new Button("Last");
    private final Button btnCreate  = new Button("Create");
    private final Button btnEdit  = new Button("Edit");
    private final Button btnRemove  = new Button("Remove");
    private final Button btnCommit  = new Button("Commit");
    private final Button btnDiscard = new Button("Discard");
    private final Button[] allButtons = {
            btnFirst,
            btnPrev,
            btnNext,
            btnLast,
            btnCreate,
            btnEdit,
            btnRemove,
            btnCommit,
            btnDiscard
    };

    ButtonBar(final CrudNavigation nav) {
        this.nav = nav;
        this.buttonLayout = new HorizontalLayout();

        buttonLayout.addComponent(btnFirst);

        buttonLayout.addComponent(btnPrev);
        buttonLayout.addComponent(btnNext);

        buttonLayout.addComponent(btnLast);
        buttonLayout.addComponent(btnCreate);
        buttonLayout.addComponent(btnEdit);
        buttonLayout.addComponent(btnRemove);
        buttonLayout.addComponent(btnCommit);
        buttonLayout.addComponent(btnDiscard);



        btnFirst.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                nav().first();
            }
        });

        btnNext.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                nav().next();
            }
        });

        btnPrev.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                nav().prev();
            }
        });

        btnLast.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                nav().last();
            }
        });

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

    public Layout getLayout() {
        return buttonLayout;
    }

    public void setNavigation(@Nonnull CrudNavigation nav) {
        detachNavigation(this.nav);
        this.nav = nav;
        attachNavigation(nav);
        updateButtonStatus();
    }

    private CrudNavigation nav() {
        return this.nav;
    }

    private void detachNavigation(@Nonnull CrudNavigation nav) {
        nav.removeCurrentItemChangeListener(buttonBarStatusUpdater);
    }

    private void attachNavigation(@Nonnull CrudNavigation nav) {
        nav.addCurrentItemChangeListener(buttonBarStatusUpdater);
    }

    private void enable(Button... btns) {
        for (Button btn: btns) btn.setEnabled(true);
    }
    private void disable(Button... btns) {
        for (Button btn: btns) btn.setEnabled(false);
    }

    private void updateButtonStatus() {
        Container.Indexed ctr = nav.getContainer();
        if (ctr == null) {
            disable(allButtons);
            return;
        } else {
            enable(allButtons);
        }

        Object currentId = nav.getCurrentItemId();
        if (currentId == null) {
            disable(allButtons);
            enable(btnCreate);
            return;
        }

        boolean hasNext = false;
        boolean hasPrev = false;

        if(currentId != null){
            hasNext = null != ctr.nextItemId(currentId);
            hasPrev = null != ctr.prevItemId(currentId);
        }

        btnNext.setEnabled(hasNext);
        btnPrev.setEnabled(hasPrev);
        btnFirst.setEnabled(hasPrev);
        btnLast.setEnabled(hasNext);
    }

    CurrentItemChange.Listener buttonBarStatusUpdater = new CurrentItemChange.Listener() {
        @Override
        public void currentItemChange(CurrentItemChange.Event event) {
            updateButtonStatus();
        }
    };

    class NextButtonClickListener implements Button.ClickListener {
        @Override
        public void buttonClick(Button.ClickEvent event) {
            nav().next();
        }
    }

}



