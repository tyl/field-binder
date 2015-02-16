package org.tylproject.vaadin.addons.fieldbinder.tests;

import org.junit.Test;
import org.tylproject.vaadin.addon.datanav.BasicDataNavigation;
import org.tylproject.vaadin.addon.datanav.DataNavigation;

import static junit.framework.TestCase.assertTrue;

/**
 * Created by evacchi on 16/02/15.
 */
public class NavigationModeTransitionTest {

    /**
     * calling edit() several times should be invariant in the
     * status of isEditingMode() (should always be true)
     *
     * in order to leave editing mode you must commit() or discard()
     * ( or explicitly call leaveEditingMode() )
     *
     */
    @Test
    public void editShouldNotToggle() {
        DataNavigation dataNavigation = new BasicDataNavigation();

        dataNavigation.edit();
        assertTrue(dataNavigation.isEditingMode());

        dataNavigation.edit();
        assertTrue(dataNavigation.isEditingMode());

        dataNavigation.edit();
        assertTrue(dataNavigation.isEditingMode());

    }
}
