package org.tylproject.vaadin.addon.crudnav;

import com.vaadin.ui.Layout;

import javax.annotation.Nonnull;

/**
 * Created by evacchi on 04/12/14.
 */
public interface CrudNavButtonBar {
    Layout getLayout();

    void setNavigation(@Nonnull CrudNavigation nav);
}
