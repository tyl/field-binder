package org.tylproject.vaadin.addons.fieldbinder.tests;

import com.vaadin.ui.VerticalLayout;
import org.junit.Test;
import org.tylproject.vaadin.addon.datanav.ButtonBar;
import org.tylproject.vaadin.addon.datanav.NavigationLabel;
import org.tylproject.vaadin.addon.fieldbinder.FieldBinder;
import org.tylproject.vaadin.addon.fieldbinder.ListTable;
import org.tylproject.vaadin.addons.fieldbinder.tests.model.Address;
import org.tylproject.vaadin.addons.fieldbinder.tests.model.Person;
import org.vaadin.viritin.FilterableListContainer;
import org.vaadin.viritin.layouts.MFormLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * Created by evacchi on 16/02/15.
 */
public class DelayedTableInitTest {

    @Test
    public void testInitTableColumn() {
        FilterableListContainer<Person> container = new FilterableListContainer<>(Person.class);
        FieldBinder<Person> binder = new FieldBinder<>(Person.class, container);


        ListTable<Address> listTable = binder.buildListOf(Address.class, "addressList");
        listTable.setVisibleColumns("id","street", "zipCode", "city", "state");
        binder.getNavigation().withDefaultBehavior();
        binder.build("name");

    }

}
