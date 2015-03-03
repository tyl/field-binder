package org.tylproject.demos.fieldbinder;

import com.vaadin.annotations.Theme;
import com.vaadin.data.Container;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.DateField;
import com.vaadin.ui.TextField;
import org.joda.time.DateTime;
import org.joda.time.Years;
import org.tylproject.demos.fieldbinder.model.Address;
import org.tylproject.demos.fieldbinder.model.Person;
import org.tylproject.vaadin.addon.datanav.ButtonBar;
import org.tylproject.vaadin.addon.datanav.CrudButtonBar;
import org.tylproject.vaadin.addon.datanav.DataNavigation;
import org.tylproject.vaadin.addon.datanav.NavigationLabel;
import org.tylproject.vaadin.addon.datanav.events.BeforeCommit;
import org.tylproject.vaadin.addon.datanav.events.CurrentItemChange;
import org.tylproject.vaadin.addon.datanav.events.ItemCreate;
import org.tylproject.vaadin.addon.datanav.events.ItemEdit;
import org.tylproject.vaadin.addon.fieldbinder.FieldBinder;
import org.tylproject.vaadin.addon.fields.collectiontables.ListTable;
import org.vaadin.spring.annotation.VaadinUIScope;
import org.vaadin.spring.navigator.annotation.VaadinView;
import org.vaadin.viritin.layouts.MFormLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

@VaadinView(name = "/custom-events")
@VaadinUIScope
@Theme("valo")
public class TutorialExtended extends MVerticalLayout implements View {

    // CONTAINER

    // setup a container instance; uncomment the following line to use an in-memory
    // container instead of Mongo
    final Container.Ordered container = MyDataSourceGenerator.makeDummyDataset();
    // final Container.Ordered container = MyDataSourceGenerator.makeMongoContainer();

    // FIELD BINDER (MASTER/DETAIL EDITOR)

    // initialize the FieldBinder for the masterDetail editor on the Person entity
    final FieldBinder<Person> binder = new FieldBinder<Person>(Person.class, container);

    // initialize the Form input fields, each in its own class field
    final TextField firstName = binder.build("firstName");
    final TextField lastName  = binder.build("lastName");
    final DateField birthDate = binder.build("birthDate");
    final TextField age       = binder.build("age");
    final ListTable<Address> addressList =
            binder.buildListOf(Address.class, "addressList");

    {
        addComponents(new ButtonBar(binder.getNavigation().withDefaultBehavior()),

                new MFormLayout(
                        firstName,
                        lastName,
                        birthDate,
                        age,
                        new NavigationLabel(binder.getNavigation())
                ).withFullWidth().withMargin(true),

                addressList,

                // create and position a button bar
                new CrudButtonBar(addressList.getNavigation().withDefaultBehavior()));

        this.withFullWidth().withMargin(true);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

        DataNavigation dataNav = binder.getNavigation();

        MyController controller = new MyController();

        dataNav.addItemEditListener(controller);
        dataNav.addItemCreateListener(controller);
        dataNav.addBeforeCommitListener(controller);
        dataNav.addCurrentItemChangeListener(controller);
    }

    private class MyController implements ItemEdit.Listener, ItemCreate.Listener, BeforeCommit.Listener, CurrentItemChange.Listener {

        @Override
        public void itemEdit(ItemEdit.Event event) {
            age.setReadOnly(true);
        }

        @Override
        public void itemCreate(ItemCreate.Event event) {
            age.setReadOnly(true);
        }

        @Override
        public void beforeCommit(BeforeCommit.Event event) {
            calcAge();
        }

        @Override
        public void currentItemChange(CurrentItemChange.Event event) {
            if (event.getNewItemId() == null) {
                age.setValue(null);
            } else {
                calcAge();
            }
        }

        private void calcAge() {
            DateTime birthDateValue = new DateTime(birthDate.getValue());
            int ageValue = Years.yearsBetween(birthDateValue, DateTime.now()).getYears();

            age.setReadOnly(false);
            age.setConvertedValue(ageValue);
            age.setReadOnly(true);
        }
    }

}
