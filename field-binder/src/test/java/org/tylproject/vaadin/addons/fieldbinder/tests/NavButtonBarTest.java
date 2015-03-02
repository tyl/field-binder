package org.tylproject.vaadin.addons.fieldbinder.tests;

import com.vaadin.ui.Button;
import org.junit.Test;
import org.tylproject.vaadin.addon.datanav.BasicDataNavigation;
import org.tylproject.vaadin.addon.datanav.NavButtonBar;
import org.vaadin.viritin.ListContainer;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.tylproject.vaadin.addons.fieldbinder.tests.UIAssert.assertButtonsDisabled;
import static org.tylproject.vaadin.addons.fieldbinder.tests.UIAssert.assertButtonsEnabled;

/**
 * Created by evacchi on 02/03/15.
 */
public class NavButtonBarTest {

    @Test
    public void navTransitions() {
        List<Object> list = Arrays.asList(new Object(), new Object(), new Object(), new Object());
        ListContainer<Object> listContainer = new ListContainer<Object>(list);

        BasicDataNavigation basicDataNavigation = new BasicDataNavigation(listContainer);
        NavButtonBar navButtonBar = new NavButtonBar(basicDataNavigation);

        Button first = navButtonBar.getFirstButton(),
                last = navButtonBar.getLastButton(),
                next = navButtonBar.getNextButton(),
                prev = navButtonBar.getPrevButton();

        // should be on first()
        assertEquals(basicDataNavigation.getContainer().firstItemId(), basicDataNavigation.getCurrentItemId());

        assertButtonsDisabled(first, prev);
        assertButtonsEnabled(next, last);

        basicDataNavigation.first(); // should not vary state
        assertButtonsDisabled(first, prev);
        assertButtonsEnabled(next, last);

        basicDataNavigation.next();
        assertButtonsEnabled(next, last, first, prev);

        basicDataNavigation.next();
        assertButtonsEnabled(next, last, first, prev);

        basicDataNavigation.next();
        assertButtonsDisabled(next, last);
        assertButtonsEnabled(first, prev);

        // we are already on last: should not change state
        basicDataNavigation.last();
        assertButtonsDisabled(next, last);
        assertButtonsEnabled(first, prev);


        basicDataNavigation.prev();
        basicDataNavigation.last();
        assertButtonsDisabled(next, last);
        assertButtonsEnabled(first, prev);

    }


}
