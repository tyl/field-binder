package org.tylproject.vaadin.addon.datanav;

import com.vaadin.ui.*;

import javax.annotation.Nonnull;

/**
 * Created by evacchi on 19/11/14.
 */
public class ButtonBar extends CustomComponent implements CrudNavButtonBar {


    private final NavButtonBar navBar;
    private final CrudButtonBar crudBar;
    private final FindButtonBar findBar;
    private final HorizontalLayout buttonLayout = new HorizontalLayout();
    private final CrudNavigation nav;

    public static ButtonBar forNavigation(CrudNavigation nav) {
        return new ButtonBar(nav);
    }

    public ButtonBar(CrudNavigation nav) {
        setCompositionRoot(getLayout());

        this.nav = nav;

        this.navBar = new NavButtonBar(nav);
        this.crudBar = new CrudButtonBar(nav);
        this.findBar = new FindButtonBar(nav);

        getLayout().addComponents(
                navBar,
                crudBar,
                findBar);

    }

    public void setNavigation(@Nonnull CrudNavigation nav) {
        navBar.setNavigation(nav);
        crudBar.setNavigation(nav);
        findBar.setNavigation(nav);
    }

    public CrudNavigation getNavigation() {
        return nav;
    }

    public Layout getLayout() {
        return buttonLayout;
    }

    public NavigationLabel buildNavigationLabel() {
        return new NavigationLabel(this.getNavigation());
    }




}



