package org.tylproject.demos.fieldbinder;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.ExternalResource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.VerticalLayout;
import org.vaadin.maddon.layouts.MVerticalLayout;
import org.vaadin.spring.navigator.VaadinView;


@VaadinView
public class MainView extends MVerticalLayout implements View  {
    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        addComponents(
                new Label("<h1>Field Binder Add-on Demo</h1>", ContentMode.HTML),
                new VerticalLayout(
                        new Link("Tutorial 1", new ExternalResource("/tutorial-1")),
                        new Label("First steps using the Field Binder")),
                new VerticalLayout(
                        new Link("Tutorial 2", new ExternalResource("/tutorial-2")),
                        new Label("An advanced example that hooks into the DataNavigation event handler")),
                new VerticalLayout(
                        new Link("Tutorial 3", new ExternalResource("/tutorial-3")),
                        new Label("A stand-alone table with a DataNavigation controller"))

        );
        this.withMargin(true).withFullWidth().alignAll(Alignment.MIDDLE_CENTER);
    }
}
