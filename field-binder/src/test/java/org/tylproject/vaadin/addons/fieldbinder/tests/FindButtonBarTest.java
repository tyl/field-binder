package org.tylproject.vaadin.addons.fieldbinder.tests;

import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.ui.Button;
import org.junit.Test;
import org.tylproject.vaadin.addon.datanav.BasicDataNavigation;
import org.tylproject.vaadin.addon.datanav.DataNavigation;
import org.tylproject.vaadin.addon.datanav.FindButtonBar;
import org.tylproject.vaadin.addons.fieldbinder.tests.model.Person;
import org.vaadin.viritin.FilterableListContainer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

import static org.junit.Assert.assertEquals;
import static org.tylproject.vaadin.addons.fieldbinder.tests.UIAssert
        .assertButtonsDisabled;
import static org.tylproject.vaadin.addons.fieldbinder.tests.UIAssert
        .assertButtonsEnabled;

/**
 * Created by evacchi on 19/12/14.
 */
public class FindButtonBarTest {



    @Test
    public void testClearToFindOnEmptyContainer() {
        final FilterableListContainer<Person> container =
                new FilterableListContainer <Person>(Person.class);
        final FindButtonBar findButtonBar = new FindButtonBar();
        findButtonBar.getNavigation().setContainer(container);
        findButtonBar.getNavigation().first();

        assertButtonsDisabled(findButtonBar.getClearToFindButton(), findButtonBar
                .getFindButton());
    }


    @Test
    public void testClearToFindOnEmptyFilteredContainer() {
        final FilterableListContainer<Person> container =
                new FilterableListContainer <Person>(Person.class);

        container.setCollection(new ArrayList<Person>(){{
            add(new Person("aa"));
            add(new Person("ab"));
        }});

        container.addContainerFilter(new SimpleStringFilter("name", "x", true, false));


        final FindButtonBar findButtonBar = new FindButtonBar();
        findButtonBar.getNavigation().setContainer(container);

        assertEquals(0, container.size());

        assertButtonsEnabled(findButtonBar.getClearToFindButton());
        assertButtonsDisabled(findButtonBar.getFindButton());
    }



}
