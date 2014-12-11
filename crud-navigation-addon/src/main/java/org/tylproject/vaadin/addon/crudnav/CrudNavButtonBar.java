package org.tylproject.vaadin.addon.crudnav;

import com.vaadin.ui.Layout;
import com.vaadin.ui.Panel;

import javax.annotation.Nonnull;
import javax.swing.*;

/**
 * Created by evacchi on 04/12/14.
 */
public interface CrudNavButtonBar {
    Layout getLayout();

    void setNavigation(@Nonnull CrudNavigation nav);
}
