package org.tylproject.demos;

import javax.annotation.Nonnull;
import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.Property;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.*;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.*;
import org.tylproject.demos.model.Address;
import org.tylproject.demos.model.Person;
import org.tylproject.vaadin.addon.crudnav.BasicCrudNavigation;
import org.tylproject.vaadin.addon.crudnav.ButtonBar;
import org.tylproject.vaadin.addon.crudnav.CrudNavigation;
import org.tylproject.vaadin.addon.crudnav.events.*;
import org.tylproject.vaadin.addon.masterdetail.MasterDetail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.tylproject.vaadin.addon.masterdetail.strategies.BeanItemStrategy
        .masterProperty;

@Theme("mytheme")
@SuppressWarnings("serial")
public class MyVaadinUI extends UI
{


    //region Field declarations
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
    final MasterDetail masterDetail =
            MasterDetail.Builder.forMaster(fieldGroup)
                    .withDetail(table)
                    .where(masterProperty("addressList").isCollectionOf(Address.class)).build();


    final CrudNavigation detailNav = new BasicCrudNavigation();

    final ButtonBar buttonBar = ButtonBar.forNavigation(masterNav);

    final Panel formPanel = new Panel();
    final Panel tablePanel = new Panel();
    //endregion

    @Override
    protected void init(VaadinRequest request) {

        setupDummyDataset();


        final VerticalLayout layout = new VerticalLayout();

        layout.addComponent(buttonBar.getLayout());


        masterNav.addCurrentItemChangeListener(
                new MasterUpdater(masterNav, masterDetail));

        detailNav.addCurrentItemChangeListener(
                new Nav2TableSelectionUpdater(table));

        table.addValueChangeListener(
                new Table2NavCurrentItemUpdater(table, detailNav));

        setupDetailNavCrudListeners();

        masterNav.first();

        FormLayout formLayout = makeFormLayout(fieldGroup,
                masterDataSource.getItem(masterDataSource.firstItemId()));


        formPanel.setContent(formLayout);
        tablePanel.setContent(table);

        layout.addComponent(formPanel);
        layout.addComponent(tablePanel);

        table.setSelectable(true);
        table.setSizeFull();
        table.setHeight("400px");



        setupFocusManagement();


        layout.setMargin(true);
        layout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        setContent(layout);
    }

    private void setupDummyDataset() {
        //
//        masterDetail.setMaster(fieldGroup);
//        masterDetail.setDetail(table);
//
//        masterDetail.addMasterItemChangeListener(
//                new BeanItemStrategy.MasterListener<Person, Address>("addressList", Address.class));

        masterDataSource.addAll(Arrays.asList(
                new Person("George", "Harrison", new Address("Liverpool")),
                new Person("John", "Lennon",     new Address("Liverpool 2")),
                new Person("Paul", "McCartney",  new Address("Liverpool sometimes")),
                new Person("Ringo", "Starr",     new Address("Who Cares, lol"))
        ));
    }

    private void setupDetailNavCrudListeners() {
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
                ((BeanItemContainer<Address>) detailNav.getContainer()).addBean(addr);
                table.select(addr);
            }
        });
        detailNav.addOnCommitListener(new OnCommit.Listener() {
            @Override
            public void onCommitListener(OnCommit.Event event) {
                table.setEditable(false);
                List<Address> addressList = ((BeanItemContainer<Address>) detailNav.getContainer()).getItemIds();
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
    }

    private void setupFocusManagement() {
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


        tablePanel.addClickListener(new MouseEvents.ClickListener() {
            @Override
            public void click(MouseEvents.ClickEvent event) {
                focus(detailNav);
                //table.focus();
            }
        });
        tablePanel.setTabIndex(-1);
        table.setTabIndex(-1);


        focus(masterNav);
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

    private class MasterUpdater implements CurrentItemChange.Listener {
        private final CrudNavigation masterNav;
        private final MasterDetail masterDetail;

        public MasterUpdater(CrudNavigation masterNav, MasterDetail masterDetail) {
            this.masterNav = masterNav;
            this.masterDetail = masterDetail;
        }

        @Override
        public void currentItemChangeListener(CurrentItemChange.Event event) {
            masterDetail.setMasterItemDataSource(masterNav.getCurrentItem());
        }
    }

    private class Nav2TableSelectionUpdater implements CurrentItemChange.Listener {
        private final Table table;

        public Nav2TableSelectionUpdater(Table table) {
            this.table = table;
        }

        @Override
        public void currentItemChangeListener(CurrentItemChange.Event event) {
            table.select(event.getNewItemId());
        }
    }

    private class Table2NavCurrentItemUpdater implements Property.ValueChangeListener {
        private final Table table;
        private final CrudNavigation nav;

        public Table2NavCurrentItemUpdater(Table table, CrudNavigation nav) {
            this.table = table;
            this.nav = nav;
        }

        @Override
        public void valueChange(Property.ValueChangeEvent event) {
            detailNav.setCurrentItemId(event.getProperty().getValue());
        }
    }

}
