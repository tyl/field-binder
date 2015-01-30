package org.tylproject.demos.fieldbinder;

import com.mongodb.MongoClient;
import com.vaadin.data.Container;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Grid;
import org.joda.time.DateTime;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.tylproject.demos.fieldbinder.model.Person;
import org.tylproject.vaadin.addon.MongoContainer;
import org.tylproject.vaadin.addon.fields.FilterableGrid;
import org.tylproject.vaadin.addon.fields.zoom.GridZoomDialog;
import org.tylproject.vaadin.addon.fields.zoom.ZoomField;
import org.vaadin.spring.UIScope;
import org.vaadin.spring.navigator.VaadinView;
import org.vaadin.viritin.FilterableListContainer;
import org.vaadin.viritin.ListContainer;
import org.vaadin.viritin.layouts.MFormLayout;

import java.util.Locale;

@VaadinView(name = "/zoom")
@UIScope
public class TutorialZoom extends MFormLayout implements View {

    {
        final FilterableListContainer<Person> container = MyDataSourceGenerator.makeDummyDataset();

        final FilterableGrid grid = new FilterableGrid(container);

        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
        grid.setSizeFull();
        grid.setWidth("100%");
        grid.setHeight("100%");
        grid.setVisibileColumns("firstName", "lastName", "birthDate");

//        grid.setVisibileColumns("shortName", "discriminator", "createdDate");
//
//        grid.getColumn("createdDate").setConverter(new Converter<String, DateTime>() {
//
//            @Override
//            public DateTime convertToModel(String value, Class<? extends DateTime>
//            targetType, Locale locale) throws Converter.ConversionException {
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
        final ZoomField<Person> zoomField =
                new ZoomField<Person>(Person.class)
                    .withZoomDialog(new GridZoomDialog<Person>(grid, "firstName"));

        addComponents(
            zoomField
        );
    }


    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }


}
