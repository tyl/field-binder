package org.tylproject.demos.model;


import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by evacchi on 20/11/14.
 */

public class Person {

    @Id
    private ObjectId id;
    private String firstName;
    private String middleName;
    private String lastName;
    private Integer age;
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

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public List<Address> getAddressList() {
        return addressList;
    }

    public void setAddressList(List<Address> addressList) {
        this.addressList = addressList;
    }

    @Override
    public String toString() {
        return "Person{" +
                "firstName='" + firstName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", age=" + age +
                '}';
    }
}
