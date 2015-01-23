package org.tylproject.demos.fieldbinder;

import com.vaadin.annotations.Theme;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.VaadinUI;
import org.vaadin.spring.navigator.SpringViewProvider;


@VaadinUI
@Theme("valo")
public class Main extends UI {

    @Autowired
    SpringViewProvider viewProvider;

    @Override
    protected void init(VaadinRequest request) {


        final Navigator nav = new Navigator(this, this);
        nav.addProvider(viewProvider);

        setNavigator(nav);
    }
}
