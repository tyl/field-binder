package org.tylproject.demos;

import javax.annotation.Nonnull;
import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.Property;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.*;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import org.tylproject.demos.model.Address;
import org.tylproject.demos.model.Person;
import org.tylproject.vaadin.addon.crudnav.BasicCrudNavigation;
import org.tylproject.vaadin.addon.crudnav.ButtonBar;
import org.tylproject.vaadin.addon.crudnav.CrudNavigation;
import org.tylproject.vaadin.addon.crudnav.events.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Theme("mytheme")
@SuppressWarnings("serial")
public class MyVaadinUI extends UI
{


    private static final String FOCUS = "masterdetail-focus";

    @WebServlet(value = "/*", asyncSupported = true)
    @VaadinServletConfiguration(productionMode = false, ui = MyVaadinUI.class)
    public static class Servlet extends VaadinServlet {
    }

    private @Nonnull CrudNavigation currentNav;

    final BeanItemContainer<Person> masterDataSource = new BeanItemContainer<Person>(Person.class);

    final CrudNavigation masterNav = new BasicCrudNavigation(masterDataSource);
    final Table table = new Table();
    final FieldGroup fieldGroup = new FieldGroup();

    final BeanItemContainer<Address> detailDataSource = new BeanItemContainer<Address>(Address.class);
    final CrudNavigation detailNav = new BasicCrudNavigation(detailDataSource);

    final ButtonBar buttonBar = ButtonBar.forNavigation(masterNav);

    final Panel formPanel = new Panel();
    final Panel tablePanel = new Panel();

    @Override
    protected void init(VaadinRequest request) {

        masterDataSource.addAll(Arrays.asList(
                new Person("George", "Harrison", new Address("Liverpool")),
                new Person("John", "Lennon",     new Address("Liverpool 2")),
                new Person("Paul", "McCartney",  new Address("Liverpool sometimes")),
                new Person("Ringo", "Starr",     new Address("Who Cares, lol"))
        ));


        final VerticalLayout layout = new VerticalLayout();

        layout.addComponent(buttonBar.getLayout());

        for (Component c: buttonBar.getLayout()) {
            if (c instanceof Field.Focusable)
                ((Focusable) c).setTabIndex(-1);
        }

        this.addShortcutListener(new ShortcutListener(
                "focus-change", ShortcutAction.KeyCode.F9,
                new int[]{  }) {
            @Override
            public void handleAction(Object sender, Object target) {
                Notification.show("hello");
                if (currentNav == masterNav) {
                    focus(detailNav);
                    table.focus();
                    detailNav.first();
                }
                else {
                    focus(masterNav);
                    formPanel.focus();
                }
            }
        });

        masterNav.addCurrentItemChangeListener(new CurrentItemChange.Listener() {
            @Override
            public void currentItemChangeListener(CurrentItemChange.Event event) {
                Object masterItemId = masterNav.getCurrentItem();
                BeanItem<Person> personBeanItem = (BeanItem<Person>) masterItemId;
                fieldGroup.setItemDataSource(personBeanItem);

                Person p = personBeanItem.getBean();
                detailDataSource.removeAllItems();

                if (p.getAddressList() != null)
                    detailDataSource.addAll(p.getAddressList());

                detailNav.first();
            }
        });

        masterNav.first();


        detailNav.addCurrentItemChangeListener(new CurrentItemChange.Listener() {
            @Override
            public void currentItemChangeListener(CurrentItemChange.Event event) {
                table.select(event.getNewItemId());
            }
        });
        detailNav.addItemEditListener(new ItemEdit.Listener() {
            @Override
            public void itemEditListener(ItemEdit.Event event) {
                table.setEditable(true);
            }
        });
        detailNav.addItemCreateListener(new ItemCreate.Listener() {
            @Override
            public void itemCreateListener(ItemCreate.Event event) {
                Address addr = new Address();
                detailDataSource.addBean(addr);
                table.select(addr);
            }
        });
        detailNav.addOnCommitListener(new OnCommit.Listener() {
            @Override
            public void onCommitListener(OnCommit.Event event) {
                table.setEditable(false);
                List<Address> addressList = detailDataSource.getItemIds();
                BeanItem<Person> personBeanItem = (BeanItem<Person>) masterNav
                        .getCurrentItem();
                personBeanItem.getItemProperty("addressList").setValue(new
                        ArrayList<Address>(addressList));
            }
        });
        detailNav.addOnDiscardListener(new OnDiscard.Listener() {
            @Override
            public void onDiscardListener(OnDiscard.Event event) {
                table.setEditable(false);
            }
        });

        table.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                detailNav.setCurrentItemId(event.getProperty().getValue());
            }
        });


        FormLayout formLayout = makeFormLayout(fieldGroup,
                masterDataSource.getItem(masterDataSource.firstItemId()));


        formPanel.setContent(formLayout);
        tablePanel.setContent(table);

        layout.addComponent(formPanel);
        layout.addComponent(tablePanel);

        table.setSelectable(true);
        table.setContainerDataSource(detailDataSource);
        table.setSizeFull();
        table.setHeight("400px");


        tablePanel.addClickListener(new MouseEvents.ClickListener() {
            @Override
            public void click(MouseEvents.ClickEvent event) {
                focus(detailNav);
                //table.focus();
            }
        });
        tablePanel.setTabIndex(-1);
        table.setTabIndex(-1);


        (table).addListener(FieldEvents.BlurEvent.class, new FieldEvents.BlurListener() {
            @Override
            public void blur(FieldEvents.BlurEvent event) {
                System.out.println("Blur " + event);
            }
        }, FieldEvents.BlurListener.blurMethod);
        (table).addListener(FieldEvents.FocusEvent.class, new FieldEvents.FocusListener() {
            @Override
            public void focus(FieldEvents.FocusEvent event) {
                System.out.println("Focus " + event);
            }
        }, FieldEvents.FocusListener.focusMethod);


        formPanel.addClickListener(new MouseEvents.ClickListener() {
            @Override
            public void click(MouseEvents.ClickEvent event) {
                focus(masterNav);
            }
        });


        layout.setMargin(true);
        layout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        focus(masterNav);

        setContent(layout);
    }

    private void focus(CrudNavigation nav) {
        if (this.currentNav == nav) return;

        //Notification.show( (nav == masterNav) ? "Master" : "Detail" );
        if (nav == masterNav) {
            tablePanel.removeStyleName(FOCUS);
            formPanel.addStyleName(FOCUS);
        } else {
            tablePanel.addStyleName(FOCUS);
            formPanel.removeStyleName(FOCUS);
        }
        this.currentNav = nav;
        buttonBar.setNavigation(nav);
    }

    protected FormLayout makeFormLayout(FieldGroup fieldGroup, BeanItem<Person> item) {
        FormLayout formLayout = new FormLayout();
        formLayout.setMargin(true);
        formLayout.setHeightUndefined();

        formLayout.addComponent(fieldGroup.buildAndBind("firstName"));
        formLayout.addComponent(fieldGroup.buildAndBind("lastName"));

        return formLayout;
    }

}
