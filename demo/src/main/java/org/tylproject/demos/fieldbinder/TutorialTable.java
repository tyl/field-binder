package org.tylproject.demos.fieldbinder;

import com.vaadin.annotations.Theme;
import com.vaadin.data.Container;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import org.tylproject.demos.fieldbinder.model.Person;
import org.tylproject.vaadin.addon.datanav.ButtonBar;
import org.tylproject.vaadin.addon.fieldbinder.BeanTable;
import org.vaadin.maddon.layouts.MVerticalLayout;
import org.vaadin.spring.VaadinUI;

/**
 * Created by evacchi on 19/12/14.
 */
@VaadinUI(path = "/tutorial-3")
@Theme("valo")
public class TutorialTable extends UI {

    @Override
    protected void init(VaadinRequest request) {
        final Container.Ordered container = MyDataSourceGenerator.makeDummyDataset();

        final BeanTable<Person> table = new BeanTable<>(Person.class, container);
        table.setVisibleColumns("firstName", "lastName", "age", "birthDate");
        final ButtonBar bar = new ButtonBar(table.getNavigation().withDefaultBehavior());


        // initialize the layout, building the fields at the same time
        // for conciseness, we use Maddon.
        // Maddon wraps common Vaadin classes with fluent APIs
        // for the most common options
        final VerticalLayout mainLayout = new MVerticalLayout(
                bar, table
        ).withFullWidth().withMargin(true);

        setContent(mainLayout);


    }

}
