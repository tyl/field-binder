package org.tylproject.demos;

import com.vaadin.annotations.Theme;
import com.vaadin.data.Container;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.DateField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import org.joda.time.DateTime;
import org.joda.time.Years;
import org.tylproject.demos.model.Address;
import org.tylproject.demos.model.Person;
import org.tylproject.vaadin.addon.datanav.ButtonBar;
import org.tylproject.vaadin.addon.datanav.CrudButtonBar;
import org.tylproject.vaadin.addon.datanav.DataNavigation;
import org.tylproject.vaadin.addon.datanav.NavigationLabel;
import org.tylproject.vaadin.addon.datanav.events.BeforeCommit;
import org.tylproject.vaadin.addon.datanav.events.CurrentItemChange;
import org.tylproject.vaadin.addon.datanav.events.ItemCreate;
import org.tylproject.vaadin.addon.datanav.events.ItemEdit;
import org.tylproject.vaadin.addon.fieldbinder.FieldBinder;
import org.tylproject.vaadin.addon.fieldbinder.ListTable;
import org.vaadin.maddon.layouts.MFormLayout;
import org.vaadin.maddon.layouts.MVerticalLayout;
import org.vaadin.spring.VaadinUI;

@VaadinUI(path = "/tutorial-2")
@Theme("valo")
public class Tutorial2 extends UI {

    // CONTAINER

    // setup a container instance; uncomment the following line to use an in-memory
    // container instead of Mongo
//    final Container.Ordered container = MyDataSourceGenerator.makeDummyDataset();
    final Container.Ordered container = MyDataSourceGenerator.makeMongoContainer();

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

    // initialize the layout
    final VerticalLayout mainLayout = new MVerticalLayout(

            new ButtonBar(binder.getNavigation().withDefaultBehavior()),

            new MFormLayout(
                    firstName,
                    lastName,
                    birthDate,
                    age,
                    new NavigationLabel(binder.getNavigation())
            ).withFullWidth().withMargin(true),

            addressList,

            // create and position a button bar
            new CrudButtonBar(addressList.getNavigation().withDefaultBehavior())

    ).withFullWidth().withMargin(true);


    @Override
    protected void init(VaadinRequest request) {
        setContent(mainLayout);
        DataNavigation dataNav = binder.getNavigation();

        MyController controller = new MyController();

        dataNav.addItemEditListener(controller);
        dataNav.addItemCreateListener(controller);
        dataNav.addBeforeCommitListener(controller);
        dataNav.addCurrentItemChangeListener(controller);
    }


    class MyController implements ItemEdit.Listener, ItemCreate.Listener, BeforeCommit.Listener, CurrentItemChange.Listener {

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
            calcAge();
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
