package org.tylproject.demos;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.Container;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.*;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.tylproject.vaadin.addon.masterdetail.builder.Detail;
import org.tylproject.vaadin.addon.masterdetail.builder.Master;
import org.tylproject.vaadin.addon.masterdetail.builder.MasterDetail;
import org.tylproject.vaadin.addon.masterdetail.builder.crud.BeanDetailCrud;
import org.tylproject.vaadin.addon.masterdetail.builder.crud.BeanMasterCrud;
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

    final Container.Indexed masterDataSource  = // setupDummyDataset();
        MongoContainer.Builder.forEntity(Person.class, makeMongoTemplate()).build();

    final BeanFieldGroup<Person> fieldGroup = new BeanFieldGroup<Person>(Person.class);
    final Table table = new Table();

    final MasterDetail masterDetail = MasterDetail.with(
            Master.of(Person.class).fromContainer(masterDataSource).boundTo(fieldGroup).withDefaultCrud(),
            Detail.collectionOf(Address.class).fromMasterProperty("addressList").boundTo(table).withDefaultCrud()
    ).build();


    final VerticalLayout layout = new VerticalLayout();

    final ButtonBar buttonBar = ButtonBar.forNavigation(masterDetail.getMaster().getNavigation());
    final ButtonBar detailBar = ButtonBar.forNavigation(masterDetail.getDetail().getNavigation());

//    final KeyBinder keyBinder = KeyBinder.forNavigation(masterDetail.getMaster().getNavigation());

    final Panel masterPanel = new Panel();
    final Panel detailPanel = new Panel();


//    private FocusManager focusManager = new FocusManager(this);

    @Override
    protected void init(VaadinRequest request) {

        setupDummyDataset();
        setupLayout();

        setupMasterLayout();
        setupDetailLayout();

        setContent(layout);
    }


    public static MongoOperations makeMongoTemplate() {
        try {
            return new MongoTemplate(new MongoClient ("localhost"), "test");
        } catch (UnknownHostException ex) { throw new Error(ex); }
    }

    private void setupLayout() {
        layout.setMargin(true);
        layout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        layout.addComponent(buttonBar.getLayout());
        layout.addComponent(masterPanel);

        layout.addComponent(detailBar.getLayout());
        layout.addComponent(detailPanel);
    }

    private void setupMasterLayout() {
        FormLayout formLayout = makeFormLayout(fieldGroup);
        masterPanel.setContent(formLayout);
    }

    private void setupDetailLayout() {

        detailPanel.setContent(table);
        table.setSelectable(true);
        table.setSizeFull();
        table.setHeight("400px");

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


    private ListContainer<Person> setupDummyDataset() {
        ListContainer<Person> dataSource = new ListContainer<Person>(Person.class);
        dataSource.addAll(Arrays.asList(
                new Person("George", "Harrison"),
                new Person("John", "Lennon",     new Address("Liverpool 2")),
                new Person("Paul", "McCartney",  new Address("Liverpool sometimes")),
                new Person("Ringo", "Starr",     new Address("Who Cares, lol"))
        ));
        return dataSource;
    }



    protected FormLayout makeFormLayout(BeanFieldGroup fieldGroup) {

        //  masterDetail.getMaster().getNavigation().first();



        FormLayout formLayout = new FormLayout();
        formLayout.setMargin(true);
        formLayout.setHeightUndefined();

//        fieldGroup.setItemDataSource(new BeanItem<Person>(new Person()));

        final TextField firstName = (TextField) fieldGroup.buildAndBind("firstName");
        final TextField middleName = (TextField) fieldGroup.buildAndBind("middleName");
        final TextField lastName = (TextField) fieldGroup.buildAndBind("lastName");

        formLayout.addComponent(firstName);
        formLayout.addComponent(middleName);
        formLayout.addComponent(lastName);

        return formLayout;
    }



}
