package org.tylproject.demos;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.Container;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.*;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.tylproject.demos.model.Address;
import org.tylproject.demos.model.Person;
import org.tylproject.vaadin.addon.MongoContainer;
import org.tylproject.vaadin.addon.crudnav.*;
import org.tylproject.vaadin.addon.crudnav.events.CurrentItemChange;
import org.tylproject.vaadin.addon.fieldbinder.FieldBinder;
import org.tylproject.vaadin.addon.fieldbinder.ListTable;
import org.tylproject.vaadin.addon.masterdetail.Detail;
import org.tylproject.vaadin.addon.masterdetail.Master;
import org.tylproject.vaadin.addon.masterdetail.MasterDetail;
import org.tylproject.vaadin.addon.masterdetail.crud.BeanDetailCrud;
import org.tylproject.vaadin.addon.masterdetail.crud.BeanMasterCrud;
import org.tylproject.vaadin.addon.masterdetail.crud.MongoMasterCrud;
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
//    final ListContainer<Person> masterDataSource = makeDummyDataset();
    final MongoContainer<Person> masterDataSource =
            MongoContainer.Builder.forEntity(Person.class, makeMongoTemplate()).build();
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
    final ListTable<Address> addressList = (ListTable<Address>) masterDetail.buildListOf(Address.class, "addressList");

    final CrudNavigation navigation = new BasicCrudNavigation();
    {

//        BeanMasterCrud<Person> crudObject = new BeanMasterCrud<Person>(Person.class, masterDetail, navigation);
        MongoMasterCrud<Person> crudObject = new MongoMasterCrud<Person>(Person.class, masterDetail, navigation);
        CrudNavigation masterNav = navigation;

        masterNav.addItemRemoveListener(crudObject);
        masterNav.addOnCommitListener(crudObject);
        masterNav.addOnDiscardListener(crudObject);
        masterNav.addItemEditListener(crudObject);
        masterNav.addItemCreateListener(crudObject);
//        masterNav.addCurrentItemCh -angeListener(crudObject);

        masterNav.addCurrentItemChangeListener(new CurrentItemChange.Listener() {
            @Override
            public void currentItemChange(CurrentItemChange.Event event) {
                masterDetail.setItemDataSource(event.getNewItem());
            }
        });


        navigation.setContainer(masterDataSource);

    }
    final ButtonBar buttonBar = ButtonBar.forNavigation(navigation);


    final CrudNavigation tableNavigation = new BasicCrudNavigation();
    {

        BeanDetailCrud<Address> crudObject = new BeanDetailCrud<Address>(Address.class, addressList.getTable(), tableNavigation);
        CrudNavigation detailNav = tableNavigation;

        detailNav.addItemRemoveListener(crudObject);
        detailNav.addOnCommitListener(crudObject);
        detailNav.addOnDiscardListener(crudObject);
        detailNav.addItemEditListener(crudObject);
        detailNav.addItemCreateListener(crudObject);

        tableNavigation.setContainer(addressList.getTable());

    }
    final CrudButtonBar tableBar = new CrudButtonBar(tableNavigation);


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
        mainLayout.addComponent(addressList);
        mainLayout.addComponent(tableBar.getLayout());

//        mainLayout.addComponent(detailBar.getLayout());
//        mainLayout.addComponent(masterDetail.getDetail().getTable());
    }

    private void setupFormLayout() {
        formLayout.setMargin(true);
        formLayout.setHeightUndefined();

        formLayout.addComponents(firstName, lastName);
    }

    private void setupTable() {



        final Table table = ((ListTable<Address>)addressList).getTable();
        //masterDetail.getDetail().getTable();
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

        table.addItemClickListener(new ItemClickEvent.ItemClickListener() {
            @Override
            public void itemClick(ItemClickEvent event) {
                Object itemId = event.getItemId();
                tableNavigation.setCurrentItemId(itemId);
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
