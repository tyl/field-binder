package org.tylproject.demos.fieldbinder;

import com.vaadin.annotations.Theme;
import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.*;
import org.joda.time.DateTime;
import org.joda.time.Years;
import org.tylproject.demos.fieldbinder.model.Address;
import org.tylproject.demos.fieldbinder.model.Person;
import org.tylproject.vaadin.addon.datanav.ButtonBar;
import org.tylproject.vaadin.addon.datanav.CrudButtonBar;
import org.tylproject.vaadin.addon.datanav.DataNavigation;
import org.tylproject.vaadin.addon.datanav.NavigationLabel;
import org.tylproject.vaadin.addon.datanav.events.*;
import org.tylproject.vaadin.addon.fieldbinder.FieldBinder;
import org.tylproject.vaadin.addon.fieldbinder.ListTable;
import org.tylproject.vaadin.addon.fieldbinder.TableAdaptor;
import org.tylproject.vaadin.addon.fieldbinder.behavior.commons.Tables;
import org.vaadin.spring.annotation.VaadinUIScope;
import org.vaadin.spring.navigator.annotation.VaadinView;
import org.vaadin.viritin.layouts.MFormLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.util.Arrays;
import java.util.ListResourceBundle;
import java.util.ResourceBundle;

@VaadinView(name = "/custom-events")
@VaadinUIScope
@Theme("valo")
public class TutorialExtended extends MVerticalLayout implements View {

    private static final String[][] STRINGS =
    {
//            {"firstName", "Nome"},
//            {"street", "Strada"},
            {"org.tylproject.demos.fieldbinder.model.Gender.MALE",   "♂ Male"},
            {"org.tylproject.demos.fieldbinder.model.Gender.FEMALE", "♀ Female"}
    };

    final ResourceBundle resourceBundle = new ListResourceBundle() {
        final ResourceBundle resourceBundle = new ListResourceBundle() {
            @Override
            protected Object[][] getContents() {
                return STRINGS;
            }
        };
        @Override
        protected Object[][] getContents() {
            return STRINGS;
        }
    };

    // CONTAINER

    // setup a container instance; uncomment the following line to use an in-memory
    // container instead of Mongo
    final Container.Ordered container = MyDataSourceGenerator.makeDummyDataset();
    // final Container.Ordered container = MyDataSourceGenerator.makeMongoContainer();


    // initialize the FieldBinder for the masterDetail editor on the Person entity
    final FieldBinder<Person> binder =
                new FieldBinder<Person>(Person.class, container)
                            .withResourceBundle(resourceBundle);

    // initialize the Form input fields, each in its own class field
    final TextField firstName = binder.build("firstName"),
                    lastName  = binder.build("lastName"),
                    age       = binder.build("age");

    final DateField birthDate = binder.build("birthDate");
    final ComboBox gender     = binder.build("gender");


    final ListTable<Address> addressList =
            binder.buildListOf(Address.class, "addressList");

    final FieldBinder<Address> addressListBinder = addressList.getFieldBinder();

    final TextField street  = addressListBinder.build("street"),
                    zipCode = addressListBinder.build("zipCode");

    final ComboBox state   = addressListBinder.build("State", "state", ComboBox.class);
    final ComboBox city    = addressListBinder.build("City", "city", ComboBox.class);


    {
        addComponents(new ButtonBar(binder.getNavigation().withDefaultBehavior()),

                new MFormLayout(
                        firstName,
                        lastName,
                        birthDate,
                        age,
                        gender,
                        new NavigationLabel(binder.getNavigation())
                ).withFullWidth().withMargin(true),

                addressList,

                // create and position a button bar
                new CrudButtonBar(addressList.getNavigation().withDefaultBehavior()));

        zipCode.setInputPrompt("zip code");


        this.withFullWidth().withMargin(true);

        DataNavigation dataNav = binder.getNavigation();

        MyController controller = new MyController();

        dataNav.addItemEditListener(controller);
        dataNav.addItemCreateListener(controller);
        dataNav.addBeforeCommitListener(controller);
        dataNav.addCurrentItemChangeListener(controller);

        state.addItems(Arrays.asList("England", "Scotland", "Wales", "Northern Ireland"));
        state.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                city.removeAllItems();
                if ("England".equals(event.getProperty().getValue())) {
                    city.addItems(Arrays.asList("London", "Liverpool", "Oxford"));
                    city.select("London");
                }
            }
        });


        addressList.getNavigation().addOnCommitListener(new MyDetailController());

    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {


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

    private class MyDetailController implements OnCommit.Listener {
        @Override
        public void onCommit(OnCommit.Event event) {
            Notification.show("The street was: "+street.getValue());
        }
    }

}
