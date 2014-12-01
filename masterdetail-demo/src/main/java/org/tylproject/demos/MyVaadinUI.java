package org.tylproject.demos;

import com.mongodb.MongoClient;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.Container;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.*;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.tylproject.vaadin.addon.masterdetail.Detail;
import org.tylproject.vaadin.addon.masterdetail.Master;
import org.tylproject.vaadin.addon.masterdetail.MasterDetail;
import org.tylproject.demos.model.Address;
import org.tylproject.demos.model.Person;
import org.tylproject.vaadin.addon.MongoContainer;
import org.tylproject.vaadin.addon.crudnav.ButtonBar;
import org.vaadin.maddon.ListContainer;

import javax.servlet.annotation.WebServlet;
import java.net.UnknownHostException;
import java.util.Arrays;



@Theme("mytheme")
@SuppressWarnings("serial")
public class MyVaadinUI extends UI {

    @WebServlet(value = "/*", asyncSupported = true)
    @VaadinServletConfiguration(productionMode = false, ui = MyVaadinUI.class)
    public static class Servlet extends VaadinServlet {}

    // setup a container instance
    final ListContainer<Person> masterDataSource  =  makeDummyDataset();
//        MongoContainer.Builder.forEntity(Person.class, makeMongoTemplate()).build();

    // instantiate field group for master
    final FieldGroup fieldGroup = new FieldGroup();

    // instantiate table for detail
    final Table table = new Table();

    // generates the MasterDetail class
    final MasterDetail masterDetail = MasterDetail.with(
            Master.of(Person.class).fromContainer(masterDataSource).boundTo(fieldGroup).withDefaultCrud(),
            Detail.collectionOf(Address.class).fromMasterProperty("addressList").boundTo(table).withDefaultCrud()
    ).build();

    final TextField firstName = (TextField) fieldGroup.buildAndBind("firstName");
    final TextField lastName = (TextField) fieldGroup.buildAndBind("lastName");

    final ButtonBar buttonBar = ButtonBar.forNavigation(masterDetail.getMaster().getNavigation());
    final ButtonBar detailBar = ButtonBar.forNavigation(masterDetail.getDetail().getNavigation());


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
        mainLayout.addComponent(new Panel(formLayout));

        mainLayout.addComponent(detailBar.getLayout());
        mainLayout.addComponent(table);
    }

    private void setupFormLayout() {
        formLayout.setMargin(true);
        formLayout.setHeightUndefined();

        formLayout.addComponent(firstName);
        formLayout.addComponent(lastName);
    }

    private void setupTable() {

        table.setSelectable(true);
        table.setSizeFull();
        table.setHeight("400px");

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
