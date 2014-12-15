package org.tylproject.vaadin.addon.datanav;

import com.vaadin.ui.*;

import javax.annotation.Nonnull;

/**
 * Created by evacchi on 19/11/14.
 */
public class ButtonBar extends CustomComponent implements DataNavigationBar {


    private final NavButtonBar navBar;
    private final CrudButtonBar crudBar;
    private final FindButtonBar findBar;
    private final HorizontalLayout buttonLayout = new HorizontalLayout();
    private final DataNavigation nav;

    public static ButtonBar forNavigation(DataNavigation nav) {
        return new ButtonBar(nav);
    }

    public ButtonBar(DataNavigation nav) {
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

    public void setNavigation(@Nonnull DataNavigation nav) {
        navBar.setNavigation(nav);
        crudBar.setNavigation(nav);
        findBar.setNavigation(nav);
    }

    public DataNavigation getNavigation() {
        return nav;
    }

    public Layout getLayout() {
        return buttonLayout;
    }

    public NavigationLabel buildNavigationLabel() {
        return new NavigationLabel(this.getNavigation());
    }

    public CrudButtonBar getCrudBar() {
        return crudBar;
    }

    public FindButtonBar getFindBar() {
        return findBar;
    }

    public NavButtonBar getNavigationBar() {
        return navBar;
    }
}



