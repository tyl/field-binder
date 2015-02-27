package org.tylproject.demos.fieldbinder;

import com.mongodb.MongoClient;
import org.joda.time.DateTime;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.tylproject.demos.fieldbinder.model.Address;
import org.tylproject.demos.fieldbinder.model.Person;
import org.tylproject.vaadin.addon.MongoContainer;
import org.vaadin.viritin.FilterableListContainer;

import java.util.Arrays;
import java.util.Date;

/**
 * Created by evacchi on 17/12/14.
 */
public class MyDataSourceGenerator {

    public static FilterableListContainer<Person> makeDummyDataset() {
        FilterableListContainer<Person> dataSource = new FilterableListContainer<Person>(Person.class);
        dataSource.addAll(Arrays.asList(
                new Person("George", "Harrison", new DateTime(1943, 2, 25, 0, 0).toDate(), new Address("Liverpool", "Abbey Road")),
                new Person("John", "Lennon",     new DateTime(1940, 10, 9, 0, 0).toDate(), new Address("Liverpool", "Liverpool St.")),
                new Person("Paul", "McCartney", new DateTime(1942, 6, 18, 0, 0).toDate(), new Address("Liverpool")),
                new Person("Ringo", "Starr", new DateTime(1940, 6, 7, 0, 0).toDate(), new Address("Test")),
                new Person("Lucy", "Indeskij", new DateTime(1967, 6, 1, 0, 0).toDate(), new Address("Diamonds"))
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

    public static MongoContainer<Person> makeBufferedMongoContainer() {
        try {
            return MongoContainer.Builder.forEntity(
                    Person.class,
                    new MongoTemplate(new MongoClient("localhost"), "scratch"))
                    .buildBuffered();
        } catch (Exception ex) { throw new Error(ex); }
    }

}
