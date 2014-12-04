package org.tylproject.vaadin.addon.crudnav;

import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Layout;
import org.tylproject.vaadin.addon.crudnav.events.CurrentItemChange;

import javax.annotation.Nonnull;

/**
 * Created by evacchi on 04/12/14.
 */
public abstract class AbstractButtonBar implements CrudNavButtonBar {

    private final HorizontalLayout buttonLayout = new HorizontalLayout();
    @Override
    public Layout getLayout() {
        return buttonLayout;
    }

    private @Nonnull
    CrudNavigation nav;


    protected AbstractButtonBar(CrudNavigation nav) {
        this.nav = nav;
    }



    @Override
    public void setNavigation(@Nonnull CrudNavigation nav) {
        detachNavigation(this.nav);
        this.nav = nav;
        attachNavigation(nav);
        updateButtonStatus();
    }

    protected CrudNavigation nav() {
        return this.nav;
    }


    protected void detachNavigation(@Nonnull CrudNavigation nav) {
        nav.removeCurrentItemChangeListener(buttonBarStatusUpdater);
    }

    protected void attachNavigation(@Nonnull CrudNavigation nav) {
        nav.addCurrentItemChangeListener(buttonBarStatusUpdater);
    }

    protected void enable(Button... btns) {
        for (Button btn: btns) btn.setEnabled(true);
    }
    protected void disable(Button... btns) {
        for (Button btn: btns) btn.setEnabled(false);
    }

    CurrentItemChange.Listener buttonBarStatusUpdater = new CurrentItemChange.Listener() {
        @Override
        public void currentItemChange(CurrentItemChange.Event event) {
            updateButtonStatus();
        }
    };

    protected abstract void updateButtonStatus();
}
