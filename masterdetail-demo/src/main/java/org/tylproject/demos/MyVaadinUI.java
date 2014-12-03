package org.tylproject.demos;

import com.mongodb.MongoClient;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.Container;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.*;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.tylproject.demos.model.Address;
import org.tylproject.demos.model.Person;
import org.tylproject.vaadin.addon.crudnav.BasicCrudNavigation;
import org.tylproject.vaadin.addon.crudnav.ButtonBar;
import org.tylproject.vaadin.addon.crudnav.CrudNavigation;
import org.tylproject.vaadin.addon.crudnav.events.CurrentItemChange;
import org.tylproject.vaadin.addon.fieldbinder.FieldBinder;
import org.tylproject.vaadin.addon.masterdetail.Detail;
import org.tylproject.vaadin.addon.masterdetail.Master;
import org.tylproject.vaadin.addon.masterdetail.MasterDetail;
import org.vaadin.maddon.ListContainer;

import javax.servlet.annotation.WebServlet;
import java.net.UnknownHostException;
import java.util.Arrays;


@Theme("mytheme")
@SuppressWarnings("serial")
public class MyVaadinUI extends UI {

    @WebServlet(value = "/*", asyncSupported = true)
    @VaadinServletConfiguration(productionMode = false, ui = MyVaadinUI.class)
    public static class Servlet extends VaadinServlet {
    }

    // setup a container instance
    final ListContainer<Person> masterDataSource = makeDummyDataset();
    final FieldBinder<Person> masterDetail = new FieldBinder<Person>(Person.class);

    // generates the MasterDetail class
//    final MasterDetail<Person, Address> masterDetail = MasterDetail.with(
//            Master.of(Person.class)
//                    .fromContainer(masterDataSource)
//                    .withDefaultCrud(),
//            Detail.collectionOf(Address.class)
//                    .fromMasterProperty("addressList")
//                    .withDefaultCrud()
//    ).build();

//    final Field<?> firstName = masterDetail.getMaster().getFieldBinder().build("firstName");
//    final Field<?> lastName = masterDetail.getMaster().getFieldBinder().build("lastName");

    final Field<?> firstName = masterDetail.build("firstName");
    final Field<?> lastName = masterDetail.build("lastName");
    final Field<?> addressList = masterDetail.build("addressList");

    final CrudNavigation navigation = new BasicCrudNavigation();
    {
        navigation.addCurrentItemChangeListener(new CurrentItemChange.Listener() {
            @Override
            public void currentItemChange(CurrentItemChange.Event event) {
                masterDetail.setItemDataSource(event.getSource().getCurrentItem());
            }
        });
        navigation.setContainer(masterDataSource);

    }
    final ButtonBar buttonBar = ButtonBar.forNavigation(navigation);

//
//    final ButtonBar buttonBar = ButtonBar.forNavigation(masterDetail.getMaster().getNavigation());
//    final ButtonBar detailBar = ButtonBar.forNavigation(masterDetail.getDetail().getNavigation());

    final VerticalLayout mainLayout = new VerticalLayout();
    final FormLayout formLayout = new FormLayout();


    @Override
    protected void init(VaadinRequest request) {

        setupMainLayout();

        setupFormLayout();
        setupTable();

        setContent(mainLayout);

    }


    private void setupMainLayout() {
        mainLayout.setMargin(true);
        mainLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        mainLayout.addComponent(buttonBar.getLayout());


        final Button find = new Button("Find");
        mainLayout.addComponent(find);
        find.addClickListener(new Button.ClickListener() {
            boolean isBound = true;
            @Override
            public void buttonClick(Button.ClickEvent event) {
                FieldBinder<Person> fieldBinder = masterDetail;
                if (isBound) {
                    fieldBinder.unbindAll();
                    find.setCaption("Search");
                } else {
                    fieldBinder.bindAll();
                    find.setCaption("Find");
                }
                isBound = !isBound;
            }
        });



        mainLayout.addComponent(new Panel(formLayout));

//        mainLayout.addComponent(detailBar.getLayout());
//        mainLayout.addComponent(masterDetail.getDetail().getTable());
    }

    private void setupFormLayout() {
        formLayout.setMargin(true);
        formLayout.setHeightUndefined();

        formLayout.addComponents(firstName, lastName, addressList);
    }

    private void setupTable() {
//        final Table table = masterDetail.getDetail().getTable();
//        table.setSelectable(true);
//        table.setSizeFull();
//        table.setHeight("400px");
//
//        // inline editing
//        table.setTableFieldFactory(new TableFieldFactory() {
//            final DefaultFieldFactory fieldFactory = DefaultFieldFactory.get();
//            @Override
//            public Field<?> createField(Container container, Object itemId, Object propertyId, Component uiContext) {
//                if (itemId == table.getValue())
//                    return fieldFactory.createField(container, itemId, propertyId, uiContext);
//                else return null;
//            }
//        });
    }

    private static MongoOperations makeMongoTemplate() {
        try {
            return new MongoTemplate(new MongoClient ("localhost"), "scratch");
        } catch (UnknownHostException ex) { throw new Error(ex); }
    }

    private static ListContainer<Person> makeDummyDataset() {
        ListContainer<Person> dataSource = new ListContainer<Person>(Person.class);
        dataSource.addAll(Arrays.asList(
                new Person("George", "Harrison"),
                new Person("John", "Lennon",     new Address("Liverpool 2")),
                new Person("Paul", "McCartney",  new Address("Liverpool sometimes")),
                new Person("Ringo", "Starr",     new Address("Who Cares, lol"))
        ));
        return dataSource;
    }


}
