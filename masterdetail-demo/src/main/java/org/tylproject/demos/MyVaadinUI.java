package org.tylproject.demos;

import com.mongodb.MongoClient;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.Container;
import com.vaadin.data.Validator;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.data.util.filter.And;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.data.util.filter.SimpleStringFilter;
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
import org.tylproject.vaadin.addon.crudnav.events.ClearToFind;
import org.tylproject.vaadin.addon.crudnav.events.OnFind;
import org.tylproject.vaadin.addon.fieldbinder.FieldBinder;
import org.tylproject.vaadin.addon.fieldbinder.ListTable;
import org.tylproject.vaadin.addon.masterdetail.crud.BeanDetailCrud;
import org.tylproject.vaadin.addon.masterdetail.crud.MongoMasterCrud;
import org.vaadin.maddon.ListContainer;

import javax.servlet.annotation.WebServlet;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


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



    final Field<?> firstName = masterDetail.build("firstName");
    final Field<?> lastName = masterDetail.build("lastName");
    final Field<?> age = masterDetail.build("age");


    final ListTable<Address> addressList = (ListTable<Address>) masterDetail.buildListOf(Address.class, "addressList");

    final BasicCrudNavigation masterNavigation = new BasicCrudNavigation();

    final NavigationLabel records = new NavigationLabel(masterNavigation);
    final ButtonBar buttonBar = ButtonBar.forNavigation(masterNavigation);



    final BasicCrudNavigation detailNavigation = new BasicCrudNavigation();
    
    final CrudButtonBar tableBar = new CrudButtonBar(detailNavigation);

    final VerticalLayout mainLayout = new VerticalLayout();
    final FormLayout formLayout = new FormLayout();

    
    public MyVaadinUI() {

        final MongoMasterCrud<Person> masterCrudListeners = new MongoMasterCrud<Person>(Person.class, masterDetail);
    	
        masterNavigation
        	.withCrudListenersFrom(masterCrudListeners)
        	.withFindListenersFrom(masterCrudListeners);
    	
        masterNavigation.addCurrentItemChangeListener(masterCrudListeners);
        masterNavigation.setContainer(masterDataSource);
        
        final BeanDetailCrud<Address> detailCrudListeners = new BeanDetailCrud<Address>(Address.class, addressList.getTable());
        detailNavigation.withCrudListenersFrom(detailCrudListeners);
        detailNavigation.setContainer(addressList.getTable());

    }


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
        mainLayout.addComponent(addressList);
        mainLayout.addComponent(tableBar.getLayout());

    }

    private void setupFormLayout() {
        formLayout.setMargin(true);
        formLayout.setHeightUndefined();

        formLayout.addComponents(firstName, lastName, age, records);


    }

    private void setupTable() {



        final Table table = addressList.getTable();
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
                detailNavigation.setCurrentItemId(itemId);
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
