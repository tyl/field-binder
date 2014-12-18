package org.tylproject.demos.fieldbinder;

import com.vaadin.annotations.Theme;
import com.vaadin.data.Container;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import org.tylproject.demos.fieldbinder.model.Address;
import org.tylproject.demos.fieldbinder.model.Person;
import org.tylproject.vaadin.addon.datanav.ButtonBar;
import org.tylproject.vaadin.addon.datanav.NavigationLabel;
import org.tylproject.vaadin.addon.fieldbinder.FieldBinder;
import org.vaadin.maddon.layouts.MFormLayout;
import org.vaadin.maddon.layouts.MVerticalLayout;
import org.vaadin.spring.VaadinUI;

@VaadinUI( path = "/tutorial-1" )
@Theme("valo")
public class Tutorial1 extends UI {

    // CONTAINER
    
    // setup a container instance; uncomment the following line to use an in-memory
    // container instead of Mongo
    // final Container.Ordered container = MyDataSourceGenerator.makeDummyDataset();
    final Container.Ordered container = MyDataSourceGenerator.makeMongoContainer();

    // FIELD BINDER (MASTER/DETAIL EDITOR)

    // initialize the FieldBinder for the masterDetail editor on the Person entity
    final FieldBinder<Person> binder = new FieldBinder<Person>(Person.class, container);

    // initialize the layout, building the fields at the same time
    // for conciseness, we use Maddon.
    // Maddon wraps common Vaadin classes with fluent APIs
    // for the most common options
    final VerticalLayout mainLayout = new MVerticalLayout(

            new ButtonBar(binder.getNavigation().withDefaultBehavior()),

            new MFormLayout(
                    binder.build("firstName"),
                    binder.build("lastName"),
                    binder.build("birthDate"),
                    binder.build("age"),
                    new NavigationLabel(binder.getNavigation())

            ).withFullWidth().withMargin(true),

            // initialize the addressList field with the built-in button bar
            binder.buildListOf(Address.class, "addressList").withDefaultEditorBar()


    ).withFullWidth().withMargin(true);


    @Override
    protected void init(VaadinRequest request) {
        setContent(mainLayout);
    }



}