package org.tylproject.demos.fieldbinder;

import com.mongodb.MongoClient;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.tylproject.demos.fieldbinder.model.Address;
import org.tylproject.demos.fieldbinder.model.Person;
import org.tylproject.vaadin.addon.MongoContainer;
import org.vaadin.maddon.FilterableListContainer;

import java.util.Arrays;

/**
 * Created by evacchi on 17/12/14.
 */
public class MyDataSourceGenerator {

    public static FilterableListContainer<Person> makeDummyDataset() {
        FilterableListContainer<Person> dataSource = new FilterableListContainer<Person>(Person.class);
        dataSource.addAll(Arrays.asList(
                new Person("George", "Harrison"),
                new Person("John", "Lennon", new Address("Liverpool 2")),
                new Person("Paul", "McCartney", new Address("Liverpool sometimes")),
                new Person("Ringo", "Starr", new Address("Who Cares, lol"))
        ));
        return dataSource;
    }

    public static MongoContainer<Person> makeMongoContainer() {
        try {
            return MongoContainer.Builder.forEntity(
                    Person.class,
                    new MongoTemplate(new MongoClient("localhost"), "scratch"))
                    .build();
        } catch (Exception ex) { throw new Error(ex); }
    }



}
