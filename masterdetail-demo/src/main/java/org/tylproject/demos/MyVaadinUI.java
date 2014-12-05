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
import com.vaadin.ui.Button.ClickEvent;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.tylproject.demos.model.Address;
import org.tylproject.demos.model.Person;
import org.tylproject.vaadin.addon.MongoContainer;
import org.tylproject.vaadin.addon.crudnav.*;
import org.tylproject.vaadin.addon.crudnav.events.CurrentItemChange;
import org.tylproject.vaadin.addon.crudnav.events.ClearToFind;
import org.tylproject.vaadin.addon.crudnav.events.OnFind;
import org.tylproject.vaadin.addon.crudnav.events.ClearToFind.Event;
import org.tylproject.vaadin.addon.fieldbinder.FieldBinder;
import org.tylproject.vaadin.addon.fieldbinder.ListTable;
import org.tylproject.vaadin.addon.masterdetail.crud.BeanDetailCrud;
import org.tylproject.vaadin.addon.masterdetail.crud.DefaultFilterFactory;
import org.tylproject.vaadin.addon.masterdetail.crud.FilterFactory;
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
    
    // prepare the form layout that will contain the fields defined above
    final FormLayout formLayout = new FormLayout();

    // DETAIL

    // create field for "collection" property (currently only lists are supported!)
    // addressList. buildListOf() returns a Field<?> instance like build()
    // The underlying field is ListTable<T>, where, in this case, T is Address
    final ListTable<Address> addressList = (ListTable<Address>) masterDetail.buildListOf(Address.class, "addressList");
    
    
    
    // MASTER CONTROLLER AND BUTTONS (and n. of record indicator)

    // Build the navigation controller for the Person entity
    final BasicCrudNavigation masterNavigation = new BasicCrudNavigation();
    
    // Bind the label "Current# of Total#" to the navigation controller instance
    final NavigationLabel records = new NavigationLabel(masterNavigation);
    
    // Buttons:
    // generate a full button bar: the ButtonBar wraps together 3 separate Button Bars:
    // NavButtonBar CrudButtonBar and FindButtonBar. These can be instantiated separately as well.
    final ButtonBar buttonBar = ButtonBar.forNavigation(masterNavigation);
    
    
    // DETAIL CONTROLLER AND BUTTONS


    // let us create a "detail" navigation to hook into the table-related events
    final BasicCrudNavigation detailNavigation = new BasicCrudNavigation();
    
    // Buttons:
    // For the detailNavigation we do not need the NavButtonBar, 
    // let us create only the CrudButtonBar and the FindButtonBar
    final CrudButtonBar tableBar = new CrudButtonBar(detailNavigation);
    final FindButtonBar tableFindBar = new FindButtonBar(detailNavigation);

    // generic main outer layout 
    final VerticalLayout mainLayout = new VerticalLayout();

    // FINISH UP THE INITIALIZATION
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
        setupFindForTable();

        setContent(mainLayout);

    }


    private void setupMainLayout() {
        mainLayout.setMargin(true);
        mainLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        mainLayout.addComponent(buttonBar.getLayout());

        mainLayout.addComponent(new Panel(formLayout));
        mainLayout.addComponent(addressList);
        mainLayout.addComponent(new HorizontalLayout(tableBar.getLayout(), tableFindBar.getLayout()));

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

        table.setVisibleColumns("city", "state", "street", "zipCode");

        
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
    
    private void setupFindForTable() {
        

        
        final FindWindow<Address> w = new FindWindow<Address>(new FieldBinder<Address>(Address.class));

        
        detailNavigation.addClearToFindListener(new ClearToFind.Listener() {
			@Override
			public void clearToFind(ClearToFind.Event event) {
				w.fieldBinder.clear();
				w.center();
				UI.getCurrent().addWindow(w);
				detailNavigation.setCurrentItemId(null);
				
			}
        });
        
        detailNavigation.addOnFindListener(new OnFind.Listener() {
        	
        	FilterFactory filterFactory = new DefaultFilterFactory();
        	
			@Override
			public void onFind(OnFind.Event event) {
				applyFilters(w.fieldBinder, (Container.Filterable)addressList.getTable().getContainerDataSource());
				w.close();
				detailNavigation.first();
			}
			
			

		    private void applyFilters(FieldBinder<?> fieldBinder, Container.Filterable container) {
		        container.removeAllContainerFilters();
		        for (Map.Entry<Field<?>,Object> e : fieldBinder.getFieldToPropertyIdBindings().entrySet()) {
		            Field<?> prop = e.getKey();
		            Object propertyId = e.getValue();
		            Object value = prop.getValue();
		            Class<?> modelType = getModelType(prop);
		            if (value != null) {
		                container.addContainerFilter(filterFactory.createFilter(modelType,
		                        propertyId, value));
		            }
		        }
		    }

		    private Class<?> getModelType(Field<?> prop) {
		        if (prop instanceof AbstractField) {
		            AbstractField<?> abstractField = (AbstractField<?>) prop;
		            Converter<?, Object> converter = abstractField.getConverter();
		            if (converter != null) {
		                return converter.getModelType();
		            }
		        }

		        // otherwise, fallback to the property type
		        return prop.getType();

		    }

			
			
			
			
		});
    }
    

    class FindWindow<T> extends Window {
    	
    	FieldBinder<T> fieldBinder;
    	
    	public FindWindow(FieldBinder<T> fieldBinder) {
    		this.fieldBinder = fieldBinder;
    		
    		setClosable(false);
    		setModal(true);
    		setDraggable(false);
    		setResizable(false);
			VerticalLayout layout = new VerticalLayout();
			layout.addComponents(
					fieldBinder.build("city"),
					fieldBinder.build("state"),
					fieldBinder.build("street"),
					fieldBinder.build("zipCode")
			);
			layout.setMargin(true);

			Button find = new Button("Find");
			layout.addComponent(find);
			
			find.addClickListener(new Button.ClickListener() {
				@Override
				public void buttonClick(ClickEvent event) {
					detailNavigation.find();
				}
			});
			
			setContent(layout);
    	}
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
