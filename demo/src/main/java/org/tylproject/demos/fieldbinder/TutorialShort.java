package org.tylproject.demos.fieldbinder;

import com.vaadin.annotations.Theme;
import com.vaadin.data.Container;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Grid;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import org.tylproject.demos.fieldbinder.model.Address;
import org.tylproject.demos.fieldbinder.model.Person;
import org.tylproject.vaadin.addon.datanav.ButtonBar;
import org.tylproject.vaadin.addon.datanav.NavigationLabel;
import org.tylproject.vaadin.addon.datanav.events.CurrentItemChange;
import org.tylproject.vaadin.addon.fieldbinder.CollectionTabularView;
import org.tylproject.vaadin.addon.fieldbinder.FieldBinder;
import org.tylproject.vaadin.addon.fieldbinder.ListTable;
import org.vaadin.spring.annotation.VaadinUIScope;
import org.vaadin.spring.navigator.annotation.VaadinView;
import org.vaadin.viritin.ListContainer;
import org.vaadin.viritin.layouts.MFormLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@VaadinView(name = "/simple")
@VaadinUIScope
@Theme("valo")
public class TutorialShort extends MVerticalLayout implements View {

    // CONTAINER
    
    // setup a container instance; uncomment the following line to use an in-memory
    // container instead of Mongo
    final Container.Ordered container = MyDataSourceGenerator.makeDummyDataset();
    // final Container.Ordered container = MyDataSourceGenerator.makeMongoContainer();

    // FIELD BINDER (MASTER/DETAIL EDITOR)

    // initialize the FieldBinder for the masterDetail editor on the Person entity
    final FieldBinder<Person> binder = new FieldBinder<>(Person.class, container).withGridSupport();
    final ListTable<Address> grid ;


    // initialize the layout, building the fields at the same time
    // for conciseness, we use Maddon.
    // Maddon wraps common Vaadin classes with fluent APIs
    // for the most common options
    {
        final Grid grid2 = new Grid();

        with(


                new ButtonBar(binder.getNavigation().withDefaultBehavior()),

                new MFormLayout(
                        binder.build("firstName"),
                        binder.build("lastName"),
                        binder.build("birthDate"),
                        binder.build("age"),
                        new NavigationLabel(binder.getNavigation())

                ).withFullWidth().withMargin(true),

                // initialize the addressList field with the built-in button bar

                (grid = binder.buildListOf(Address.class, "addressList").withDefaultEditorBar()),
                grid2

        ).withFullWidth().withMargin(true);

        grid2.setEditorEnabled(true);
        ArrayList<Address> addrs = new ArrayList<>();
        addrs.add(new Address("foo"));
        grid2.setContainerDataSource(new BeanItemContainer<>(Address.class, addrs));


//        binder.getNavigation().addCurrentItemChangeListener(new CurrentItemChange.Listener() {
//
//
//            @Override
//            public void currentItemChange(CurrentItemChange.Event event) {
//
//                bic.setCollection((List<Address>) event.getNewItem().getItemProperty("addressList").getValue());
////                grid.setContainerDataSource(new BeanItemContainer<>(Address.class, (List<Address>) event.getNewItem().getItemProperty("addressList").getValue()));
//            }
//        });

    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }
}
