package org.tylproject.demos.fieldbinder;

import com.vaadin.annotations.Theme;
import com.vaadin.data.Container;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.DateField;
import org.tylproject.demos.fieldbinder.model.Address;
import org.tylproject.demos.fieldbinder.model.Person;
import org.tylproject.vaadin.addon.datanav.ButtonBar;
import org.tylproject.vaadin.addon.datanav.NavigationLabel;
import org.tylproject.vaadin.addon.fieldbinder.FieldBinder;
import org.tylproject.vaadin.addon.fieldbinder.behavior.commons.FieldBinders;
import org.tylproject.vaadin.addon.fieldbinder.behavior.commons.SearchWindowFindListeners;
import org.tylproject.vaadin.addon.fieldbinder.behavior.containers.listcontainer
.ListContainerCrud;
import org.vaadin.spring.annotation.VaadinUIScope;
import org.vaadin.spring.navigator.annotation.VaadinView;
import org.vaadin.viritin.layouts.MFormLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

@VaadinView(name = "/search-dialog")
@VaadinUIScope

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

    // initialize the layout, building the fields at the same time
    // for conciseness, we use Maddon.
    // Maddon wraps common Vaadin classes with fluent APIs
    // for the most common options
    {

        with(

                new ButtonBar(binder.getNavigation()),

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


        binder.getNavigation()
            .withCurrentItemChangeListenerFrom(new FieldBinders.CurrentItemChangeListener<>(binder))
            .withCrudListenersFrom(new ListContainerCrud<>(binder))
            .withFindListenersFrom(new SearchWindowFindListeners(binder));

    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {}

}
