package org.tylproject.demos.fieldbinder;

import com.mongodb.MongoClient;
import com.vaadin.annotations.Theme;
import com.vaadin.data.Container;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.tylproject.demos.fieldbinder.model.Address;
import org.tylproject.demos.fieldbinder.model.Person;
import org.tylproject.vaadin.addon.MongoContainer;
import org.tylproject.vaadin.addon.datanav.ButtonBar;
import org.tylproject.vaadin.addon.datanav.NavigationLabel;
import org.tylproject.vaadin.addon.fieldbinder.FieldBinder;
import org.tylproject.vaadin.addon.fieldbinder.FieldBinderFieldFactory;
import org.tylproject.vaadin.addon.fieldbinder.ListTable;
import org.tylproject.vaadin.addon.fieldbinder.behavior.Behavior;
import org.vaadin.spring.UIScope;
import org.vaadin.spring.VaadinUI;
import org.vaadin.spring.navigator.VaadinView;
import org.vaadin.viritin.layouts.MFormLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

@VaadinView(name = "/search-dialog")
@UIScope

@Theme("valo")
public class TutorialAlternativeFind extends MVerticalLayout implements View {

    // CONTAINER

    // setup a container instance; uncomment the following line to use an in-memory
    // container instead of Mongo
    final Container.Ordered container = MyDataSourceGenerator.makeDummyDataset();
    // final Container.Ordered container = MyDataSourceGenerator.makeMongoContainer();

    // FIELD BINDER (MASTER/DETAIL EDITOR)

    // initialize the FieldBinder for the masterDetail editor on the Person entity
    final FieldBinder<Person> binder = new FieldBinder<Person>(Person.class, container);

    DateField dateField;

    // initialize the layout, building the fields at the same time
    // for conciseness, we use Maddon.
    // Maddon wraps common Vaadin classes with fluent APIs
    // for the most common options
    {
        with(

                new ButtonBar(binder.getNavigation().withDefaultBehavior()),

                new MFormLayout(
                        binder.build("firstName"),
                        binder.build("lastName"),
                        dateField = binder.build("birthDate"),
                        binder.build("age"),
                        new NavigationLabel(binder.getNavigation())

                ).withFullWidth().withMargin(true),

                // initialize the addressList field with the built-in button bar
                binder.buildListOf(Address.class, "addressList").withDefaultEditorBar()


        ).withFullWidth().withMargin(true);


        dateField.setDateFormat("dd-MM-yyyy");

    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {}

}
