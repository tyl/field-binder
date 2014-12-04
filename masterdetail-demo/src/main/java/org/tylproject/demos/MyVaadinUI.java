package org.tylproject.demos;

import com.mongodb.MongoClient;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.Container;
import com.vaadin.data.Property;
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
import org.tylproject.vaadin.addon.crudnav.events.OnClearToFind;
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
    final Field<?> age = masterDetail.build("age");

    final Label records = new Label();

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
//        final Button clearToFind = new Button("Clear to Find");
//        final Button find = new Button("Find");

        mainLayout.addComponent(buttonBar.getLayout());

//        find.setEnabled(false);


        final FieldBinder<Person> fieldBinder = masterDetail;

//        mainLayout.addComponents(clearToFind, find);

        navigation.addOnClearToFindListener(new OnClearToFind.Listener() {

            @Override
            public void onClearToFind(OnClearToFind.Event event) {
                if (event.getSource().getCurrentItemId() == null) {
                    fieldBinder.unbindAll();
                    fieldBinder.setReadOnly(false);
                    navigation.setCurrentItemId(null);
                } else {
                    fieldBinder.setReadOnly(false);
                    navigation.setCurrentItemId(null);
                    for (Field<?> f : fieldBinder.getFields())
                        f.setValue(null);
                }
            }

        });

        navigation.addOnFindListener(new OnFind.Listener() {
            @Override
            public void onFind(OnFind.Event event) {
                applyFilters(fieldBinder, masterDataSource);
                fieldBinder.bindAll();
                fieldBinder.setReadOnly(true);
                navigation.first();
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

        formLayout.addComponents(firstName, lastName, age, records);

        navigation.addCurrentItemChangeListener(new CurrentItemChange.Listener() {
            @Override
            public void currentItemChange(CurrentItemChange.Event event) {
                int current = masterDataSource.indexOfId(navigation.getCurrentItemId());
                records.setValue(String.format("%d of %d", current, masterDataSource.size()));
            }
        });
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

    private void applyFilters(FieldBinder<Person> fieldBinder, Container.Filterable container) {
        masterDataSource.removeAllContainerFilters();
        for (Map.Entry<Field<?>,Object> e : fieldBinder.getFieldToPropertyIdBindings().entrySet()) {
            Field<?> prop = e.getKey();
            Object propertyId = e.getValue();
            Object value = prop.getValue();
            Class<?> modelType = getModelType(prop);
            if (value != null) {
                masterDataSource.addContainerFilter(filterFromType(modelType, propertyId, value));
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


    private Object getConvertedValue(Field<?> prop) {
        if (prop instanceof AbstractField) {
            return ((AbstractField) prop).getConvertedValue();
        } else {
            return prop.getValue();
        }
    }

    private Container.Filter filterFromType(Class<?> type, Object propertyId, Object pattern) {
        if (String.class.isAssignableFrom(type)) {
            return new SimpleStringFilter(propertyId, pattern.toString(), true, true);
        } else
        if (Number.class.isAssignableFrom(type)) {
            return filterForNumber(propertyId, pattern.toString());
        } else {
            throw new UnsupportedOperationException("Unsupported value type: "+type.getCanonicalName());
        }
    }


    private Container.Filter filterForNumber(Object propertyId, String pattern) {
        Container.Filter filter = numberEqual(propertyId, pattern);
        if (filter != null) return filter;

        filter = intRange(propertyId, pattern);


        if (filter != null) return filter;

        throw new Validator.InvalidValueException(
                    String.format("'%s' is not an accepted numeric search pattern", pattern));

    }

    private Container.Filter numberEqual(Object propertyId, String pattern) {
        try {
            int i = Integer.parseInt(pattern);
            return new Compare.Equal(propertyId, i);
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    Pattern intRange = Pattern.compile("^(\\d+)\\.\\.(\\d+)$");
    private Container.Filter intRange(Object propertyId, String pattern) {
        Matcher matcher = intRange.matcher(pattern);
        matcher.find();
        String left = matcher.group(1);
        String right = matcher.group(2);
        try {
            int i = Integer.parseInt(left);
            int j = Integer.parseInt(right);

            if (i>j) {
                throw new Validator.InvalidValueException(
                        String.format("The given range '%s' is invalid", pattern));
            }

            return new And(
                    new Compare.GreaterOrEqual(propertyId, i),
                    new Compare.LessOrEqual(propertyId, j)
            );

        } catch (NumberFormatException ex) {
            return null;
        }

    }


}
