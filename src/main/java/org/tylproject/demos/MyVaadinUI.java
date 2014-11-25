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
import org.tylproject.vaadin.addon.crudnav.*;
import org.tylproject.vaadin.addon.crudnav.events.*;
import org.tylproject.vaadin.addon.masterdetail.DetailContainerChange;
import org.tylproject.vaadin.addon.masterdetail.MasterDetail;

import java.util.Arrays;
import java.util.List;

import static org.tylproject.vaadin.addon.masterdetail.strategies.LeanBeanStrategy
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
    final FieldGroup fieldGroup = new FieldGroup();
    final Table table = new Table();

    final MasterDetail masterDetail =
            MasterDetail.Builder.forMaster(fieldGroup)
                    .withDetail(table)
                    .where(masterProperty("addressList")
                            .isCollectionOf(Address.class)).build();

    final VerticalLayout layout = new VerticalLayout();
    Focusable[] fieldOrder;


    final CrudNavigation detailNav = new BasicCrudNavigation();

    final ButtonBar buttonBar = ButtonBar.forNavigation(masterNav);
    final KeyBinder keyBinder = KeyBinder.forNavigation(masterNav);

    final Panel formPanel = new Panel();
    final Panel tablePanel = new Panel();

    private TabCycler tabCycler ;


    @Override
    protected void init(VaadinRequest request) {

        setupDummyDataset();

        setupLayout();

        setupDetailNav();
        setupDetailCrud();

        setupMasterNav();
        setupMasterCrud();


        setupMasterLayout();
        setupDetailLayout();

        setupFocusManagement();


        layout.setMargin(true);
        layout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        setContent(layout);
    }

    private void setupLayout() {
        layout.addComponent(buttonBar.getLayout());
        layout.addComponent(formPanel);
        layout.addComponent(tablePanel);
    }

    private void setupMasterLayout() {
        FormLayout formLayout = makeFormLayout(fieldGroup,
                masterDataSource.getItem(masterDataSource.firstItemId()));
        formPanel.setContent(formLayout);
    }

    private void setupDetailLayout() {

        tablePanel.setContent(table);
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


    }

    private void setupMasterNav() {
        masterNav.addCurrentItemChangeListener(new MasterUpdater(masterDetail));
        masterNav.first();

    }

    private void setupMasterCrud() {
        masterNav.addItemRemoveListener(BasicCrudStrategy.get());
    }

    private void setupDummyDataset() {

        masterDataSource.addAll(Arrays.asList(
                new Person("George", "Harrison", new Address("Liverpool")),
                new Person("John", "Lennon",     new Address("Liverpool 2")),
                new Person("Paul", "McCartney",  new Address("Liverpool sometimes")),
                new Person("Ringo", "Starr",     new Address("Who Cares, lol"))
        ));
    }

    private void setupDetailNav() {
        masterDetail.addDetailContainerChangeListener(new DetailContainerUpdater(detailNav));

        detailNav.addCurrentItemChangeListener(new Nav2TableSelectionUpdater(table));

        table.addValueChangeListener(new Table2NavCurrentItemUpdater(detailNav));
    }

    private void setupDetailCrud() {
        detailNav.addItemEditListener(new ItemEdit.Listener() {
            @Override
            public void itemEditListener(ItemEdit.Event event) {
                table.setEditable(true);
            }
        });
        detailNav.addItemCreateListener(new ItemCreate.Listener() {
            @Override
            public void itemCreateListener(ItemCreate.Event event) {
                Address address = new Address();
                detailNav.getContainer().addItem(address);
                table.select(address);
            }
        });
        detailNav.addOnCommitListener(new OnCommit.Listener() {
            @Override
            public void onCommitListener(OnCommit.Event event) {
                table.setEditable(false);
            }
        });
        detailNav.addOnDiscardListener(new OnDiscard.Listener() {
            @Override
            public void onDiscardListener(OnDiscard.Event event) {
                table.setEditable(false);
            }
        });
        detailNav.addItemRemoveListener(BasicCrudStrategy.get());
    }

    private void setupFocusManagement() {
        this.addActionHandler(keyBinder);

        this.addShortcutListener(new ShortcutListener(
                "focus-change", ShortcutAction.KeyCode.F9, new int[0]) {
            @Override
            public void handleAction(Object sender, Object target) {
                if (currentNav() == masterNav) {
                    // switch to table
                    // formPanel.removeAction(tabCycler);
                    focus(detailNav);
                    table.focus();
                    //detailNav.first();
                }
                else {
                    focus(masterNav);
                    formPanel.focus();
                }
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
            }
        });

        focus(masterNav);
    }

    private CrudNavigation currentNav() {
        return currentNav;
    }

    private void focus(CrudNavigation nav) {
        if (this.currentNav() == nav) return;

        if (nav == masterNav) {
            tablePanel.removeStyleName(FOCUS);
            formPanel.addStyleName(FOCUS);
            cycleThrough(fieldOrder);

        } else {
            tablePanel.addStyleName(FOCUS);
            formPanel.removeStyleName(FOCUS);
            cycleThrough(tablePanel);

        }
        this.currentNav = nav;
        buttonBar.setNavigation(nav);
        keyBinder.setNavigation(nav);
    }

    private void cycleThrough(Focusable... elements) {
        cycleThrough(Arrays.asList(elements));
    }

    private void cycleThrough(List<? extends Focusable> fieldOrder) {
        this.removeAction(tabCycler);
        this.tabCycler = new TabCycler(fieldOrder);
        this.addAction(tabCycler);

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

        this.fieldOrder = new Focusable[]{  firstName, lastName, one, two, three };

        return formLayout;
    }

    public static class MasterUpdater implements CurrentItemChange.Listener {
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
        private final CrudNavigation nav;

        public Table2NavCurrentItemUpdater(CrudNavigation nav) {
            this.nav = nav;
        }

        @Override
        public void valueChange(Property.ValueChangeEvent event) {
            nav.setCurrentItemId(event.getProperty().getValue());
        }
    }

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
            //if (sender == formPanel) {
                int indexOf = focusables.indexOf(target);
                if (indexOf < 0 || indexOf == focusables.size() - 1) {
                    first.focus();
                } else {
                    focusables.get(indexOf + 1).focus();
                }
            //}
        }
    }

    private class DetailContainerUpdater implements DetailContainerChange.Listener {
        private final CrudNavigation nav;

        public DetailContainerUpdater(CrudNavigation detailNav) {
            this.nav = detailNav;
        }

        @Override
        public void detailContainerChange(DetailContainerChange.Event event) {
            nav.setContainer((Container.Indexed) event.getNewContainer());
            nav.first();
        }
    }
}
