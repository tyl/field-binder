package org.tylproject.vaadin.addon.fieldbinder.behavior;

import com.vaadin.data.Container;
import com.vaadin.ui.Table;
import org.tylproject.vaadin.addon.datanav.events.*;
import org.tylproject.vaadin.addon.fieldbinder.TableFieldManager;
import org.tylproject.vaadin.addon.fields.search.SearchForm;
import org.tylproject.vaadin.addon.fields.search.SearchWindow;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by evacchi on 28/01/15.
 */
public abstract class AbstractTableBehavior<T> implements Behavior {
    final protected Table table;
    final protected Class<T> beanClass;
//    final protected TableFindListeners<T> findListeners;
    final protected SearchForm searchForm;
    final protected TableFieldManager fieldManager;

    protected T newEntity = null;
    protected FindListeners findListeners;

    public AbstractTableBehavior(final Class<T> beanClass, final Table table) {
        this.beanClass = beanClass;
        this.table = table;
//        this.findListeners = new TableFindListeners<>(beanClass);
        this.searchForm =
            new SearchForm(makePropertyIdToTypeMap(
                table.getContainerDataSource(),
                Arrays.asList(table.getVisibleColumns())));


        this.fieldManager = new TableFieldManager(table);

        table.setTableFieldFactory(fieldManager);
    }


    protected Map<Object, Class<?>>
            makePropertyIdToTypeMap(Container container, Collection<?> propertyIds) {

        LinkedHashMap<Object, Class<?>> map = new LinkedHashMap<>();
        for (Object propertyId: propertyIds) {
            map.put(propertyId, container.getType(propertyId));
        }

        return map;
    }



    @Override
    public void itemEdit(ItemEdit.Event event) {
        table.setEditable(true);
        table.setSelectable(false);
        table.focus();
    }


    @Override
    public void itemCreate(ItemCreate.Event event) {
//        event.getSource().getContainer().addItem(...);
//        event.getSource().setCurrentItemId(...);

        table.setEditable(true);
        table.setSelectable(false);
        table.focus();
    }


    public void onDiscard(OnDiscard.Event event) {
        this.table.discard();

        fieldManager.discardFields();
        this.table.setEditable(false);

        this.table.setSelectable(true);
        if (newEntity != null) {
            newEntity = null;
            event.getSource().remove();
        }
    }


    public void onCommit(OnCommit.Event event) {

        fieldManager.commitFields();
        this.table.commit();
        this.table.setEditable(false);

        this.table.setSelectable(true);

        newEntity = null;
    }

    public void itemRemove(ItemRemove.Event event) {
        this.table.removeItem(event.getSource().getCurrentItemId());
    }

    @Override
    public void currentItemChange(CurrentItemChange.Event event) {
        table.select(event.getNewItemId());
    }


    @Override
    public void clearToFind(ClearToFind.Event event) {
        // lazily initialize the SearchWindow
        if (this.findListeners == null) {
            final SearchWindow searchWindow =
                    new SearchWindow(searchForm).callFindOnClose(event.getSource());
            this.findListeners = new SearchWindowFindListeners(searchWindow);
        }
        this.findListeners.clearToFind(event);
    }


    @Override
    public void onFind(OnFind.Event event) {
        this.findListeners.onFind(event);
    }



    protected T createBean() {
        try {
            T bean = beanClass.newInstance();
            newEntity = bean;
            return bean;
        } catch (Exception ex) {
            throw new UnsupportedOperationException(ex);
        }
    }
}