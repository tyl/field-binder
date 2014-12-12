package org.tylproject.vaadin.addon.fieldbinder;

import com.vaadin.data.Property;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import org.tylproject.vaadin.addon.crudnav.BasicCrudNavigation;
import org.tylproject.vaadin.addon.crudnav.CrudButtonBar;
import org.tylproject.vaadin.addon.crudnav.events.CurrentItemChange;
import org.tylproject.vaadin.addon.fieldbinder.strategies.DefaultTableStrategy;

import java.util.List;

/**
 * Created by evacchi on 12/12/14.
 */
public class NavigableListTable<T> extends ListTable<T> {
    private BasicCrudNavigation navigation;

    public NavigableListTable(Class<T> containedBeanClass) {
        super(containedBeanClass);
        navigation.setContainer(getTable());
    }

    @Override
    protected void setInternalValue(List<T> newValue) {
        super.setInternalValue(newValue);

        navigation.setCurrentItemId(null);
    }

    public ListTable<T> withDefaultCrudBar() {
        CrudButtonBar buttonBar = buildDefaultCrudBar();
        compositionRoot.setSizeFull();

        Label spacer = new Label("");
        HorizontalLayout inner = new HorizontalLayout(spacer, buttonBar);
        inner.setSizeFull();
        inner.setWidth("100%");

//        inner.setExpandRatio(spacer, 1);
        inner.setComponentAlignment(buttonBar, Alignment.BOTTOM_RIGHT);

        compositionRoot.addComponent(inner);
        return this;
    }


    public CrudButtonBar buildDefaultCrudBar() {
        final BasicCrudNavigation nav = new BasicCrudNavigation(table);
        nav.withCrudListenersFrom(new DefaultTableStrategy<T>(containedBeanClass, table));
        final CrudButtonBar crudBar = new CrudButtonBar(nav);
        nav.addCurrentItemChangeListener(new CurrentItemChange.Listener() {
            @Override
            public void currentItemChange(CurrentItemChange.Event event) {
                table.select(event.getNewItemId());
            }
        });
        table.addValueChangeListener(new NavUpdater(nav));

        return crudBar;
    }

    static class NavUpdater implements ValueChangeListener {
        private final BasicCrudNavigation nav;

        NavUpdater(BasicCrudNavigation nav) {
            this.nav = nav;
        }
        @Override
        public void valueChange(Property.ValueChangeEvent event) {
            nav.setCurrentItemId(event.getProperty().getValue());
        }
    }
}
