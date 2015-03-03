package org.tylproject.vaadin.addons.fieldbinder.tests;

import com.vaadin.ui.Button;
import org.junit.Before;
import org.junit.Test;
import org.tylproject.vaadin.addon.datanav.BasicDataNavigation;
import org.tylproject.vaadin.addon.datanav.CrudButtonBar;
import org.tylproject.vaadin.addon.datanav.NavButtonBar;
import org.tylproject.vaadin.addon.datanav.events.ItemCreate;
import org.vaadin.viritin.ListContainer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.fail;
import static org.tylproject.vaadin.addons.fieldbinder.tests.UIAssert
.assertButtonsDisabled;
import static org.tylproject.vaadin.addons.fieldbinder.tests.UIAssert
.assertButtonsEnabled;

/**
 * Created by evacchi on 02/03/15.
 */
public class CrudButtonBarTest {

    @Test
    public void testCreateOnEmpty() {
        List<Object> list = new ArrayList<>();
        final ListContainer<Object> listContainer = new ListContainer<Object>(list);

        final BasicDataNavigation basicDataNavigation = new BasicDataNavigation(listContainer);
        CrudButtonBar navButtonBar = new CrudButtonBar(basicDataNavigation);

        Button create  = navButtonBar.getCreateButton(),
               edit    = navButtonBar.getEditButton(),
               remove  = navButtonBar.getRemoveButton(),
               commit  = navButtonBar.getCommitButton(),
               discard = navButtonBar.getDiscardButton();

        assertButtonsDisabled(edit, remove, commit, discard);
        assertButtonsEnabled(create);


        // no item is in the container!
        try {
            basicDataNavigation.edit();
            // should throw exception, otherwise fail
            fail();
        } catch (IllegalStateException ex) {
            assertButtonsDisabled(edit, remove, commit, discard);
            assertButtonsEnabled(create);
        }

// let us disable this for now. It is dependent on the container
//        // should ignore attempt: create must add an item (no event is currently attached)
//        basicDataNavigation.create();
//        assertButtonsDisabled(edit, remove, commit, discard);
//        assertButtonsEnabled(create);
//
//
//        // let us add a creation routine
//        basicDataNavigation.addItemCreateListener(new ItemCreate.Listener() {
//            @Override
//            public void itemCreate(ItemCreate.Event event) {
//                Object o = new Object();
//                listContainer.addItem(o);
//                basicDataNavigation.setCurrentItemId(o);
//            }
//        });
        // the transition should now occur
        basicDataNavigation.create();
        assertButtonsDisabled(create, edit, remove);
        assertButtonsEnabled(commit, discard);



        basicDataNavigation.commit();
        assertButtonsEnabled(create, edit, remove);
        assertButtonsDisabled(commit, discard);

    }


    @Test
    public void testCreateOnNonEmpty() {
        List<Object> list = new ArrayList<Object>() {{
            add(new Object());
            add(new Object());
        }};
        ListContainer<Object> listContainer = new ListContainer<Object>(list);

        BasicDataNavigation basicDataNavigation = new BasicDataNavigation(listContainer);
        CrudButtonBar navButtonBar = new CrudButtonBar(basicDataNavigation);

        Button create  = navButtonBar.getCreateButton(),
                edit    = navButtonBar.getEditButton(),
                remove  = navButtonBar.getRemoveButton(),
                commit  = navButtonBar.getCommitButton(),
                discard = navButtonBar.getDiscardButton();

        basicDataNavigation.edit();
        assertButtonsDisabled(create, edit, remove);
        assertButtonsEnabled(commit, discard);

        basicDataNavigation.discard();
        assertButtonsEnabled(create, edit, remove);
        assertButtonsDisabled(commit, discard);

        basicDataNavigation.create();
        assertButtonsDisabled(create, edit, remove);
        assertButtonsEnabled(commit, discard);

        basicDataNavigation.commit();
        assertButtonsEnabled(create, edit, remove);
        assertButtonsDisabled(commit, discard);
    }



}
