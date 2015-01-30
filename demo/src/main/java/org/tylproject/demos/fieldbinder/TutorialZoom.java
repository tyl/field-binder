package org.tylproject.demos.fieldbinder;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import org.tylproject.demos.fieldbinder.model.Person;
import org.tylproject.vaadin.addon.fields.zoom.GridZoomDialog;
import org.tylproject.vaadin.addon.fields.zoom.TableZoomDialog;
import org.tylproject.vaadin.addon.fields.zoom.TextZoomField;
import org.vaadin.spring.UIScope;
import org.vaadin.spring.navigator.VaadinView;
import org.vaadin.viritin.FilterableListContainer;
import org.vaadin.viritin.layouts.MFormLayout;

@VaadinView(name = "/zoom")
@UIScope
public class TutorialZoom extends MFormLayout implements View {

    private static final String TARGET_PROPERTY_ID = "firstName";

    {
        final FilterableListContainer<Person> container = MyDataSourceGenerator.makeDummyDataset();

        final TextZoomField zoomField =
                new TextZoomField()
                    .withZoomDialog(new TableZoomDialog(TARGET_PROPERTY_ID, container));

        final TextZoomField drillDownField =
                new TextZoomField()
                    .drillDownOnly()
                    .withZoomDialog(new GridZoomDialog(TARGET_PROPERTY_ID, container));


        addComponents(zoomField, drillDownField);
    }


    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }


}
