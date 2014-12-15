package org.tylproject.vaadin.addon.datanav;

import com.vaadin.ui.*;
import org.tylproject.vaadin.addon.datanav.events.CurrentItemChange;
import org.tylproject.vaadin.addon.datanav.resources.Strings;

import javax.annotation.Nonnull;
import javax.swing.*;
import java.util.ResourceBundle;

/**
 * Created by evacchi on 04/12/14.
 */
public abstract class AbstractButtonBar extends CustomComponent implements CrudNavButtonBar {

    private final HorizontalLayout buttonLayout = new HorizontalLayout();

    private Panel focusPanel;
    private FocusManager focusManager;

    @Override
    public Layout getLayout() {
        return buttonLayout;
    }

    private @Nonnull
    DataNavigation nav;
    protected static final ResourceBundle resourceBundle =
            ResourceBundle.getBundle(Strings.class.getCanonicalName());


    protected AbstractButtonBar(DataNavigation nav) {
        this.nav = nav;
        setCompositionRoot(this.getLayout());
    }




    @Override
    public void setNavigation(@Nonnull DataNavigation nav) {
        detachNavigation(this.nav);
        this.nav = nav;
        attachNavigation(nav);
        updateButtonStatus();
    }

    protected DataNavigation nav() {
        return this.nav;
    }


    protected void detachNavigation(@Nonnull DataNavigation nav) {
        nav.removeCurrentItemChangeListener(buttonBarStatusUpdater);
    }

    protected void attachNavigation(@Nonnull DataNavigation nav) {
        nav.addCurrentItemChangeListener(buttonBarStatusUpdater);
    }


    protected Button button(String labelIdentifier) {
        return new Button(resourceBundle.getString(labelIdentifier));
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
