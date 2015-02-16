package org.tylproject.vaadin.addons.fieldbinder.tests;

import com.vaadin.ui.VerticalLayout;
import org.junit.Test;
import org.tylproject.vaadin.addon.datanav.ButtonBar;
import org.tylproject.vaadin.addon.datanav.NavigationLabel;
import org.tylproject.vaadin.addon.fieldbinder.BeanTable;
import org.tylproject.vaadin.addon.fieldbinder.FieldBinder;
import org.tylproject.vaadin.addon.fieldbinder.ListTable;
import org.tylproject.vaadin.addons.fieldbinder.tests.model.Address;
import org.tylproject.vaadin.addons.fieldbinder.tests.model.Person;
import org.vaadin.viritin.FilterableListContainer;
import org.vaadin.viritin.layouts.MFormLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by evacchi on 16/02/15.
 */
public class DelayedTableInitTest {

    @Test
    public void testInitTableColumn() {

        List<Address> addressList = new ArrayList<>();

        ListTable<Address> listTable = new ListTable<>(Address.class);
        listTable.setVisibleColumns("id","street", "zipCode", "city", "state");

        assertEquals(0, listTable.getTable().getVisibleColumns().length);


        Address a = new Address();
        a.setCity("Milan");

        addressList.add(a);

        listTable.setValue(addressList);


        assertEquals(5, listTable.getTable().getVisibleColumns().length);


    }

}
