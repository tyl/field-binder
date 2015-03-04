package org.tylproject.demos.fieldbinder;

import com.vaadin.annotations.Theme;
import com.vaadin.data.Container;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.TextField;
import org.tylproject.demos.fieldbinder.model.Person;
import org.tylproject.vaadin.addon.datanav.ButtonBar;
import org.tylproject.vaadin.addon.datanav.events.CurrentItemChange;
import org.tylproject.vaadin.addon.datanav.events.EditingModeChange;
import org.tylproject.vaadin.addon.datanav.events.ItemEdit;
import org.tylproject.vaadin.addon.fieldbinder.FieldBinder;
import org.tylproject.vaadin.addon.fields.collectiontables.BeanTable;
import org.vaadin.spring.annotation.VaadinUIScope;
import org.vaadin.spring.navigator.annotation.VaadinView;
import org.vaadin.viritin.layouts.MVerticalLayout;


/**
 * Created by evacchi on 19/12/14.
 */
@VaadinView(name = "/table")
@VaadinUIScope
@Theme("valo")
public class TutorialTable extends MVerticalLayout implements View {

    {
        final Container.Ordered container = MyDataSourceGenerator.makeDummyDataset();

        final BeanTable<Person> table = new BeanTable<>(Person.class, container);
        table.setVisibleColumns("firstName", "lastName", "age", "birthDate");
        final ButtonBar bar = new ButtonBar(table.getNavigation().withDefaultBehavior());
//
//        final FieldBinder<Person> fieldBinder = table.getFieldBinder();
//        final TextField firstName = fieldBinder.build("firstName"),
//                        lastName = fieldBinder.build("lastName"),
//                        age = fieldBinder.build("age");
//
//        final PopupDateField birthDate = fieldBinder.build("birthDate");
//
//        table.getNavigation().addEditingModeChangeListener(new EditingModeChange.Listener() {
//            @Override
//            public void editingModeChange(EditingModeChange.Event event) {
//                if (event.isEnteringEditingMode()) age.setReadOnly(true);
//            }
//        });





        // initialize the layout, building the fields at the same time
        // for conciseness, we use Maddon.
        // Maddon wraps common Vaadin classes with fluent APIs
        // for the most common options
        addComponents(bar, table);

        withFullWidth().withMargin(true);

    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }

}
