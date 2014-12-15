package org.tylproject.demos;

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
import org.tylproject.vaadin.addon.datanav.*;
import org.tylproject.vaadin.addon.fieldbinder.FieldBinder;
import org.tylproject.vaadin.addon.fieldbinder.ListTable;
import org.tylproject.vaadin.addon.masterdetail.FocusManager;
import org.tylproject.vaadin.addon.masterdetail.crud.BeanDetailCrud;
import org.tylproject.vaadin.addon.masterdetail.crud.TableFindStrategy;
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

    // CONTAINER
    
    // setup a container instance; uncomment the following line to use an in-memory
    // container instead of Mongo
//    final ListContainer<Person> masterDataSource = makeDummyDataset();
    final MongoContainer<Person> masterDataSource =
            MongoContainer.Builder.forEntity(Person.class, makeMongoTemplate()).build();
    
    
    // FIELD BINDER (MASTER/DETAIL EDITOR)
    
    // initialize the FieldBinder for the masterDetail editor on the Person entity
    final FieldBinder<Person> masterDetail = new FieldBinder<Person>(Person.class);
    
    // MASTER
    
    // create a field for each property in the Person bean that we want to bind
    // the build() method returns a Field<?>. 
    // If you need a more specific type (e.g., TextField), you'll have to cast it
    final Field<?> firstName = masterDetail.build("firstName");
    final Field<?> lastName = masterDetail.build("lastName");
    final Field<?> age = masterDetail.build("age");
    final Panel masterPanel = new Panel();
    
    // prepare the form layout that will contain the fields defined above
    final FormLayout formLayout = new FormLayout();

    // DETAIL

    // create field for "collection" property (currently only lists are supported!)
    // addressList. buildListOf() returns a Field<?> instance like build()
    // The underlying field is ListTable<T>, where, in this case, T is Address
    final ListTable<Address> addressList = (ListTable<Address>) masterDetail.buildListOf(Address.class, "addressList");
    final Panel tablePanel = new Panel();
    
    
    // MASTER CONTROLLER AND BUTTONS (and n. of record indicator)

    // Build the navigation controller for the Person entity
    final BasicDataNavigation masterNavigation = new BasicDataNavigation();
    
    // Bind the label "Current# of Total#" to the navigation controller instance
    final NavigationLabel records = new NavigationLabel(masterNavigation);
    
    // Buttons:
    // generate a full button bar: the ButtonBar wraps together 3 separate Button Bars:
    // NavButtonBar CrudButtonBar and FindButtonBar. These can be instantiated separately as well.
    final ButtonBar buttonBar = ButtonBar.forNavigation(masterNavigation);
    
    
    // DETAIL CONTROLLER AND BUTTONS


    // let us create a "detail" navigation to hook into the table-related events
    final BasicDataNavigation detailNavigation = new BasicDataNavigation();
    
    // Buttons:
    // For the detailNavigation we do not need the NavButtonBar, 
    // let us create only the CrudButtonBar and the FindButtonBar
    final CrudButtonBar tableBar = new CrudButtonBar(detailNavigation);

//    final FindButtonBar tableFindBar = new FindButtonBar(detailNavigation);
    final Button btnClearToFind =  new FindButtonBar(detailNavigation).getClearToFindButton();

    // generic main outer layout 
    final VerticalLayout mainLayout = new VerticalLayout();

//    final KeyBinder keyBinder = KeyBinder.forNavigation(masterNavigation);
//    final FocusManager focusManager = new FocusManager(keyBinder);

    // FINISH UP THE INITIALIZATION
    public MyVaadinUI() {

//        final MongoNavigationStrategy<Person> masterCrudListeners = new MongoNavigationStrategy<Person>(Person.class, masterDetail);
//
//        masterNavigation
//        	.withCrudListenersFrom(masterCrudListeners)
//        	.withFindListenersFrom(masterCrudListeners);
//
//        masterNavigation.addCurrentItemChangeListener(masterCrudListeners);
        masterNavigation.setContainer(masterDataSource);
        
        final BeanDetailCrud<Address> detailCrudListeners = new BeanDetailCrud<Address>(Address.class, addressList.getTable());
        detailNavigation.withCrudListenersFrom(detailCrudListeners);
        detailNavigation.setContainer(addressList.getTable());

        final FocusManager focusManager = new FocusManager();

        focusManager.configure()
                .constrainTab(firstName, lastName, age).onPanel(masterPanel).forNavigation(masterNavigation)
                .andThen(addressList).onPanel(tablePanel).forNavigation(detailNavigation);
        focusManager.focusCurrentGroup();

        this.addActionHandler(focusManager);
        this.addActionHandler(focusManager.getKeyBinder());


    }
    


    @Override
    protected void init(VaadinRequest request) {

        setupMainLayout();

        setupFormLayout();
        setupTable();
        setupFindForTable();

        setContent(mainLayout);

    }


    private void setupMainLayout() {
        mainLayout.setMargin(true);
        mainLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        mainLayout.addComponent(buttonBar.getLayout());

        masterPanel.setContent(formLayout);

        mainLayout.addComponent(masterPanel);
        mainLayout.addComponent(tablePanel);
        mainLayout.addComponent(new HorizontalLayout(tableBar.getLayout(), btnClearToFind));

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

        addressList.setVisibleColumns("city", "state", "street", "zipCode");

        
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

        tablePanel.setContent(addressList);


    }
    
    private void setupFindForTable() {
        
    	detailNavigation.withFindListenersFrom(new TableFindStrategy<Address>(Address.class, detailNavigation, addressList.getTable()));
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
