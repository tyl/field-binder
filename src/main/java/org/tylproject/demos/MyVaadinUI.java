package org.tylproject.demos;

import javax.annotation.Nonnull;
import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.Container;
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
import org.tylproject.vaadin.addon.masterdetail.DetailContainerChange;
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
    private List<? extends Focusable> fieldOrder;

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
//    {
//        @Override
//        public void removeShortcutListener(ShortcutListener shortcut) {
//            getActionManager().removeAction(shortcut);
//        }
//    };

    final Panel tablePanel = new Panel();
    //endregion

    @Override
    protected void init(VaadinRequest request) {

        setupDummyDataset();

        final VerticalLayout layout = new VerticalLayout();

        layout.addComponent(buttonBar.getLayout());


        masterNav.addCurrentItemChangeListener(new MasterUpdater(masterDetail));
        masterDetail.addDetailContainerChangeListener(new DetailContainerChange.Listener() {
            @Override
            public void detailContainerChange(DetailContainerChange.Event event) {
                detailNav.setContainer((Container.Indexed) event.getNewContainer());
            }
        });

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

        table.setTableFieldFactory(new TableFieldFactory() {
            final DefaultFieldFactory fieldFactory = DefaultFieldFactory.get();
            @Override
            public Field<?> createField(Container container, Object itemId, Object propertyId, Component uiContext) {
                if (itemId == table.getValue())
                    return fieldFactory.createField(container, itemId, propertyId, uiContext);
                else return null;
            }
        });



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




        formPanel.addShortcutListener(new TabCycler(fieldOrder));


        this.addShortcutListener(new ShortcutListener(
                "focus-change", ShortcutAction.KeyCode.TAB, new int[] {ShortcutAction.ModifierKey.CTRL}) {
            @Override
            public void handleAction(Object sender, Object target) {
                Notification.show("hello");
                if (currentNav == masterNav) {
                    // switch to table
                    formPanel.removeAction(tabCycler);
                    focus(detailNav);
                    table.focus();
                    detailNav.first();
                }
                else {
                    formPanel.addShortcutListener(tabCycler);
                    focus(masterNav);
                    formPanel.focus();
                }
            }
        });

        this.addShortcutListener(new ShortcutListener(
                "next-item", ShortcutAction.KeyCode.F4, new int[0]
        ) {
            @Override
            public void handleAction(Object sender, Object target) {
                currentNav().next();
            }
        });

        this.addShortcutListener(new ShortcutListener(
                "next-item", ShortcutAction.KeyCode.F3, new int[0]
        ) {
            @Override
            public void handleAction(Object sender, Object target) {
                currentNav().prev();
            }
        });

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
//        tablePanel.setTabIndex(-1);
//        table.setTabIndex(-1);


        focus(masterNav);
    }

    private CrudNavigation currentNav() {
        return currentNav;
    }

    private void focus(CrudNavigation nav) {
        if (this.currentNav() == nav) return;

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

        final TextField firstName = (TextField) fieldGroup.buildAndBind("firstName");
        final TextField lastName = (TextField) fieldGroup.buildAndBind("lastName");
        formLayout.addComponent(firstName);
        formLayout.addComponent(lastName);

        TextField one = new TextField("one");

        TextField two = new TextField("two");

        TextField three = new TextField("three");

        formLayout.addComponent(one);
        formLayout.addComponent(two);
        formLayout.addComponent(three);

        this.fieldOrder = Arrays.asList(  firstName, lastName, one, two, three );
        this.tabCycler = new TabCycler(fieldOrder);


        return formLayout;
    }

    private class MasterUpdater implements CurrentItemChange.Listener {
        private final MasterDetail masterDetail;

        public MasterUpdater(MasterDetail masterDetail) {
            this.masterDetail = masterDetail;
        }

        @Override
        public void currentItemChangeListener(CurrentItemChange.Event event) {
            masterDetail.setMasterItemDataSource(event.getNewItem());
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
            nav.setCurrentItemId(event.getProperty().getValue());
        }
    }

    private TabCycler tabCycler ;

    private class TabCycler extends ShortcutListener {
        private final List<? extends Focusable> focusables;
        private final Focusable first;

        public TabCycler(List<? extends Focusable> focusables) {
            super("tab", KeyCode.TAB, new int[0]);
            this.focusables = focusables;
            this.first = focusables.get(0);
        }

        @Override
        public void handleAction(Object sender, Object target) {
            if (sender == formPanel) {
                int indexOf = focusables.indexOf(target);
                if (indexOf < 0 || indexOf == focusables.size() - 1) {
                    first.focus();
                } else {
                    focusables.get(indexOf + 1).focus();
                }
            }
        }
    }
}
