package org.tylproject.vaadin.addons.fieldbinder.tests.model;

import java.util.List;

/**
 * Created by evacchi on 16/02/15.
 */
public class Person {

    private String name;
    private List<Address> addressList;

    public Person(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
            this.name = name;
        }

    public List<Address> getAddressList() {
        return addressList;
    }

    public void setAddressList(List<Address> addressList) {
        this.addressList = addressList;
    }
}
