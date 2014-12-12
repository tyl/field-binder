package org.tylproject.demos;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.Container;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.*;
import org.tylproject.demos.model.Address;
import org.tylproject.demos.model.Person;
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
    final FilterableListContainer<Person> masterDataSource = makeDummyDataset();


    // FIELD BINDER (MASTER/DETAIL EDITOR)

    // visual navigation control

    // initialize the FieldBinder for the masterDetail editor on the Person entity
    final FieldBinder<Person> binder = new FieldBinder<Person>(Person.class);

    // create a field for each property in the Person bean that we want to bind
    // the build() method returns a Field<?>. 
    // If you need a more specific type (e.g., TextField), you'll have to cast it
//    final Field<?> firstName = binder.build("firstName");
//    final Field<?> lastName = binder.build("lastName");
//    final Field<?> age = binder.build("age");
//
//    final ButtonBar buttonBar = binder.buildDefaultButtonBar(masterDataSource);
//
////    final NavigationLabel recordCount = buttonBar.buildNavigationLabel();
//
//
//
//    // prepare the form layout that will contain the fields defined above
//    final FormLayout formLayout = new FormLayout();
//
//    // DETAIL
//
//    // create field for "collection" property (currently only lists are supported!)
//    // addressList. buildListOf() returns a Field<?> instance like build()
//    // The underlying field is ListTable<T>, where, in this case, T is Address
//    final ListTable<Address> addressList = (ListTable<Address>) binder.buildListOf(Address.class, "addressList");
//    final CrudButtonBar addressListBar = addressList.buildDefaultCrudBar();


    final VerticalLayout mainLayout = new MVerticalLayout(
            binder.buildDefaultButtonBar(masterDataSource),

            new MFormLayout(
                    binder.build("firstName"),
                    binder.build("lastName"),
                    binder.build("age")
            ).withFullWidth().withMargin(true),

            binder.buildListOf(Address.class, "addressList").withDefaultCrudBar()


    ).withFullWidth().withMargin(true);


    @Override
    protected void init(VaadinRequest request) {

        setupMainLayout();
        setContent(mainLayout);

    }


    private void setupMainLayout() {
        mainLayout.setMargin(true);
        mainLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

//        mainLayout.addComponents(buttonBar.getLayout(), formLayout, addressList, addressListBar.getLayout());


    }

    private void setupTable(final Table table) {

//        final Table table = addressList.getTable();
//        table.setSelectable(true);
//        table.setSizeFull();
//        table.setHeight("300px");

//        addressList.setVisibleColumns("city", "state", "street", "zipCode");

        
        // inline editing
        table.setTableFieldFactory(new TableFieldFactory() {
            final DefaultFieldFactory fieldFactory = DefaultFieldFactory.get();
            @Override
            public Field<?> createField(Container container, Object itemId, Object propertyId, Component uiContext) {
                if (itemId == table.getValue())
                    return fieldFactory.createField(container, itemId, propertyId, uiContext);
                else return null;
            }
        });




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




}
