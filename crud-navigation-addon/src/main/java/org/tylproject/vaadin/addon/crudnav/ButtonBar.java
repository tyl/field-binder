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
public class ButtonBar implements CrudNavButtonBar {

    private final NavButtonBar navBar;
    private final CrudButtonBar crudBar;
    private final FindButtonBar findBar;
    private final HorizontalLayout buttonLayout = new HorizontalLayout();

    public static ButtonBar forNavigation(CrudNavigation nav) {
        return new ButtonBar(nav);
    }

    public ButtonBar(CrudNavigation nav) {
        this.navBar = new NavButtonBar(nav);
        this.crudBar = new CrudButtonBar(nav);
        this.findBar = new FindButtonBar(nav);

        getLayout().addComponents(
                navBar.getLayout(),
                crudBar.getLayout(),
                findBar.getLayout());

    }

    public void setNavigation(@Nonnull CrudNavigation nav) {
        navBar.setNavigation(nav);
        crudBar.setNavigation(nav);
        findBar.setNavigation(nav);
    }

    public Layout getLayout() {
        return buttonLayout;
    }

}



