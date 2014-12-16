package org.tylproject.demos;

import com.mongodb.MongoClient;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.tylproject.demos.model.Address;
import org.tylproject.demos.model.Person;
import org.tylproject.vaadin.addon.MongoContainer;
import org.tylproject.vaadin.addon.datanav.ButtonBar;
import org.tylproject.vaadin.addon.datanav.NavigationLabel;
import org.tylproject.vaadin.addon.fieldbinder.FieldBinder;
import org.vaadin.maddon.FilterableListContainer;
import org.vaadin.maddon.layouts.MFormLayout;
import org.vaadin.maddon.layouts.MVerticalLayout;

import javax.servlet.annotation.WebServlet;
import java.util.Arrays;


@Theme("mytheme")
@SuppressWarnings("serial")
public class MyVaadinUI extends UI {

    @WebServlet(value = "/*", asyncSupported = true)
    @VaadinServletConfiguration(productionMode = false, ui = MyVaadinUI.class)
    public static class Servlet extends VaadinServlet {
    }

    // CONTAINER
    
    // setup a container instance; uncomment the following line to use an in-memory
    // container instead of Mongo
//    final FilterableListContainer<Person> container = makeDummyDataset();
    final MongoContainer<Person> container = makeMongoContainer();

    // FIELD BINDER (MASTER/DETAIL EDITOR)

    // initialize the FieldBinder for the masterDetail editor on the Person entity
    final FieldBinder<Person> binder = new FieldBinder<Person>(Person.class, container);


    final VerticalLayout mainLayout = new MVerticalLayout(

            new ButtonBar(binder.getNavigation().withDefaultBehavior()),

            new MFormLayout(
                    binder.build("firstName"),
                    binder.build("lastName"),
                    binder.build("age"),
                    new NavigationLabel(binder.getNavigation())

            ).withFullWidth().withMargin(true),

            binder.buildListOf(Address.class, "addressList").withDefaultEditorBar()


    ).withFullWidth().withMargin(true);


    @Override
    protected void init(VaadinRequest request) {
        setContent(mainLayout);
    }

    private static FilterableListContainer<Person> makeDummyDataset() {
        FilterableListContainer<Person> dataSource = new FilterableListContainer<Person>(Person.class);
        dataSource.addAll(Arrays.asList(
                new Person("George", "Harrison"),
                new Person("John", "Lennon",     new Address("Liverpool 2")),
                new Person("Paul", "McCartney",  new Address("Liverpool sometimes")),
                new Person("Ringo", "Starr",     new Address("Who Cares, lol"))
        ));
        return dataSource;
    }

    private MongoContainer<Person> makeMongoContainer() {
        try {
            return MongoContainer.Builder.forEntity(
                            Person.class,
                            new MongoTemplate(new MongoClient("localhost"), "scratch"))
                            .build();
        } catch (Exception ex) { throw new Error(ex); }
    }





}
