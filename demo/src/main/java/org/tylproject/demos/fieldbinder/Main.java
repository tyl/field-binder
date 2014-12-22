package org.tylproject.demos.fieldbinder;

import com.vaadin.annotations.Theme;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import org.vaadin.maddon.layouts.MVerticalLayout;
import org.vaadin.spring.VaadinUI;


@VaadinUI
@Theme("valo")
public class Main extends UI {
    @Override
    protected void init(VaadinRequest request) {
        setContent(new MVerticalLayout(
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

        ).withMargin(true).withFullWidth().alignAll(Alignment.MIDDLE_CENTER));
    }
}
