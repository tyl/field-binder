package org.tylproject.demos.fieldbinder;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.ExternalResource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.VerticalLayout;
import org.vaadin.spring.annotation.VaadinUIScope;
import org.vaadin.spring.navigator.annotation.VaadinView;
import org.vaadin.viritin.layouts.MVerticalLayout;


@VaadinView(name = "")
@VaadinUIScope
public class MainView extends MVerticalLayout implements View  {

    {

        addComponents(
        new Label("<h1>Field Binder Add-on Demo</h1>", ContentMode.HTML),
                makeIndexSection(
                    "Tutorial 1",
                    "First steps using the Field Binder",
                    "/simple"),
                makeIndexSection(
                    "Tutorial 2",
                    "An advanced example that hooks into the DataNavigation event handler",
                    "/custom-events"),
                makeIndexSection(
                    "Tutorial 3",
                    "Using resource bundles and attaching events to table fields",
                    "/custom-events-table"),
                makeIndexSection(
                    "Tutorial 4",
                    "A stand-alone table with a DataNavigation controller",
                    "/table"),
                makeIndexSection(
                    "Tutorial 5",
                    "Using a SearchDialog",
                    "/search-dialog"),
                makeIndexSection(
                    "Tutorial 6",
                    "ZoomField",
                    "/zoom"
                ));

        this.withMargin(true).withFullWidth().alignAll(Alignment.MIDDLE_CENTER);


    }

    private VerticalLayout makeIndexSection(String title, String description, String path) {
        return new VerticalLayout(
                new Link(title, new ExternalResource("#!"+path)),
                new Label(description));
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
    }
}
