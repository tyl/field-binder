package org.tylproject.demos.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by evacchi on 20/11/14.
 */

public class Person {
    private String firstName;
    private String lastName;
    private List<Address> addressList = new ArrayList<Address>();

    public Person() {}

    public Person(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Person(String firstName, String lastName, Address address) {
        this(firstName, lastName);
        this.addressList.add(address);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<Address> getAddressList() {
        return addressList;
    }

    public void setAddressList(List<Address> addressList) {
        this.addressList = addressList;
    }
}
