package org.tylproject.demos.fieldbinder;

import com.vaadin.annotations.Theme;
import com.vaadin.data.Container;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.*;
import org.tylproject.data.mongo.party.GeographicAddress;
import org.tylproject.data.mongo.party.Party;
import org.tylproject.data.mongo.party.TelecomAddress;
import org.tylproject.vaadin.addon.MongoContainer;
import org.tylproject.vaadin.addon.SearchWindow;
import org.tylproject.vaadin.addon.datanav.ButtonBar;
import org.tylproject.vaadin.addon.datanav.NavigationLabel;
import org.tylproject.vaadin.addon.fieldbinder.FieldBinder;
import org.tylproject.vaadin.addon.fieldbinder.ListTable;
import org.tylproject.vaadin.addon.fieldbinder.behavior.Behavior;
import org.tylproject.vaadin.addon.fieldbinder.behavior.MongoBehavior;
import org.tylproject.vaadin.addon.fieldbinder.behavior.SearchWindowFindListeners;
import org.tylproject.vaadin.addon.fieldbinder.FieldBinderFieldFactory;
import org.vaadin.viritin.layouts.MFormLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;
import org.vaadin.spring.VaadinUI;

@VaadinUI
@Theme("valo")
public class TutorialAlternativeFind extends UI {

    // CONTAINER

    // setup a container instance; uncomment the following line to use an in-memory
    // container instead of Mongo
    final MongoContainer<Party> container = MyDataSourceGenerator.makeMongoContainer();


    // initialize the FieldBinder for the masterDetail editor on the Person entity
    final FieldBinder<Party> binder = new FieldBinder<Party>(Party.class, container);

    // initialize the Form input fields, each in its own class field
    final TextField shortName = binder.build("shortName");
    final ComboBox discriminator  = binder.build("discriminator");

    final ListTable<GeographicAddress> geographicAddress =
            binder.buildListOf(GeographicAddress.class, "geographicAddress");

    final ListTable<TelecomAddress> telecomAddress =
            binder.buildListOf(TelecomAddress.class, "telecomAddress");


    final TabSheet addressTabSheet = new TabSheet();



    @Override
    protected void init(VaadinRequest request) {



//        ButtonBar buttonBar =  new ButtonBar(binder.getNavigation().withDefaultBehavior());
//        NavigationLabel navigationLabel = new NavigationLabel(binder.getNavigation());
//
//
//
//        BeanTable<Party> beanTable = new BeanTable<>(Party.class, container);
//        beanTable.setVisibleColumns("shortName", "discriminator");


//        final FilterableGrid grid = new FilterableGrid (container);
//
//        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
//        grid.setSizeFull();
//        grid.setWidth("100%");
//        grid.setHeight("100%");
//
//        grid.setVisibileColumns("shortName", "discriminator", "createdDate");
//
//        grid.getColumn("createdDate").setConverter(new Converter<String, DateTime>() {
//
//            @Override
//            public DateTime convertToModel(String value, Class<? extends DateTime>
//            targetType, Locale locale) throws ConversionException {
//                if (value == null) return null;
//                else return DateTime.parse(value);
//            }
//
//            @Override
//            public String convertToPresentation(DateTime value, Class<? extends String> targetType, Locale locale) throws ConversionException {
//                return value == null? null : value.toString();
//            }
//
//            @Override
//            public Class<DateTime> getModelType() {
//                return DateTime.class;
//            }
//
//            @Override
//            public Class<String> getPresentationType() {
//                return String.class;
//            }
//        });



//        grid.removeAllColumns();
//        grid.addColumn("shortName");
//        grid.addColumn("discriminator");


//        ZoomField<String> myZoom = new ZoomField<>(String.class)
//            .withZoomDialog(new GridZoomDialog<String>(grid, "shortName"));
//
//        myZoom.getBackingField().setWidth(400, Unit.PIXELS);













        Behavior defaultBehavior = new MongoBehavior<>(binder);
        ButtonBar buttonBar = ButtonBar.forNavigation(binder.getNavigation());
        binder.getNavigation()
            .withCrudListenersFrom(defaultBehavior)
            .withFindListenersFrom(new SearchWindowFindListeners(new SearchWindow
            (binder)))
            .addCurrentItemChangeListener(defaultBehavior);




        // initialize the layout
        final VerticalLayout mainLayout = new MVerticalLayout(

               buttonBar,
//               search,

                new MFormLayout(
                        shortName,
                        discriminator,
                        new NavigationLabel(binder.getNavigation())
                ).withFullWidth().withMargin(true),

                addressTabSheet

        ).withFullWidth().withMargin(true);

        setContent(mainLayout);

        addressTabSheet.addTab(geographicAddress.withDefaultEditorBar(), "Geographic Address");
        addressTabSheet.addTab(telecomAddress.withDefaultEditorBar(), "Telecom Address");


        geographicAddress.getTable().setTableFieldFactory(new MyTableFieldFactory
                (geographicAddress.getTable()));
        telecomAddress.getTable().setTableFieldFactory(new MyTableFieldFactory(telecomAddress.getTable()));

    }

    class MyTableFieldFactory implements TableFieldFactory {

        private final Table table;
        private final FieldBinderFieldFactory fieldFactory = new FieldBinderFieldFactory();

        MyTableFieldFactory(Table t) {
            this.table = t;
        }

        @Override
        public Field<?> createField(Container container, Object itemId, Object propertyId, Component uiContext) {
            return fieldFactory.createField(container.getType(propertyId), Field.class);
        }
    }




}
