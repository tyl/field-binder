package org.tylproject.vaadin.addon.utils;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.filter.UnsupportedFilterException;
import org.tylproject.vaadin.addon.datanav.DataNavigation;
import org.tylproject.vaadin.addon.datanav.events.CurrentItemChange;

import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.WeakHashMap;
 
/**
 * Keeps the currently selected item "on hold" to prevent lazy containers from re-generating the instance each time
 * {@link #getItem(Object)} is invoked.
 *
 * The currently selected item is implemented by passing a Navigator instance.
 *
 */
public class CachingContainerProxy<T extends Container.Indexed
                                           & Container.Filterable
                                           & Container.Sortable
                                           & Container.ItemSetChangeNotifier>
                                  implements Container.Indexed,
                                             Container.Filterable,
                                             Container.Sortable,
                                             Container.ItemSetChangeNotifier {

    private final DataNavigation navigation;
    private Object itemIdOnHold;
    private Item itemOnHold;
    
    public CachingContainerProxy(DataNavigation dataNavigation) {
        this.navigation = dataNavigation;
        navigation.addCurrentItemChangeListener(new CurrentItemChange.Listener() {
            @Override
            public void currentItemChange(CurrentItemChange.Event event) {
                itemIdOnHold = event.getNewItemId();
                itemOnHold = event.getNewItem();
            }
        });
    }

    public T getDelegate() {
        return (T) navigation.getContainer();
    }

 
    @Override
    public Object addItemAt(int i) throws UnsupportedOperationException {
        return getDelegate().addItemAt(i);
    }
 
    @Override
    public Item addItemAt(int i, Object o) throws UnsupportedOperationException {
        return getDelegate().addItemAt(i, o);
    }
 
    @Override
    public Object getIdByIndex(int i) {
        return getDelegate().getIdByIndex(i);
    }
 
    @Override
    public List<?> getItemIds(int i, int i1) {
        return getDelegate().getItemIds(i, i1);
    }
 
    @Override
    public int indexOfId(Object o) {
        return getDelegate().indexOfId(o);
    }
 
    @Override
    public Object addItemAfter(Object o) throws UnsupportedOperationException {
        return getDelegate().addItemAfter(o);
    }
 
    @Override
    public Item addItemAfter(Object o, Object o1) throws UnsupportedOperationException {
        return getDelegate().addItemAfter(o, o1);
    }
 
    @Override
    public Object firstItemId() {
        return getDelegate().firstItemId();
    }
 
    @Override
    public boolean isFirstId(Object o) {
        return getDelegate().isFirstId(o);
    }
 
    @Override
    public boolean isLastId(Object o) {
        return getDelegate().isLastId(o);
    }
 
    @Override
    public Object lastItemId() {
        return getDelegate().lastItemId();
    }
 
    @Override
    public Object nextItemId(Object o) {
        return getDelegate().nextItemId(o);
    }
 
    @Override
    public Object prevItemId(Object o) {
        return getDelegate().prevItemId(o);
    }
 
    @Override
    public boolean addContainerProperty(Object o, Class<?> aClass, Object o1) throws
    UnsupportedOperationException {
        return getDelegate().addContainerProperty(o, aClass, o1);
    }
 
    @Override
    public Object addItem() throws UnsupportedOperationException {
        return getDelegate().addItem();
    }
 
    @Override
    public Item addItem(Object o) throws UnsupportedOperationException {
        return getDelegate().addItem(o);
    }
 
    @Override
    public boolean containsId(Object o) {
        return getDelegate().containsId(o);
    }
 
    @Override
    public Property getContainerProperty(Object o, Object o1) {
        return getDelegate().getContainerProperty(o, o1);
    }
 
    @Override
    public Collection<?> getContainerPropertyIds() {
        return getDelegate().getContainerPropertyIds();
    }
 
    @Override
    public Item getItem(Object o) {
        if (o.equals(itemIdOnHold)) return itemOnHold;
        return getDelegate().getItem(o);
    }
 
    @Override
    public Collection<?> getItemIds() {
        return getDelegate().getItemIds();
    }
 
    @Override
    public Class<?> getType(Object o) {
        return getDelegate().getType(o);
    }
 
    @Override
    public boolean removeAllItems() throws UnsupportedOperationException {
        return getDelegate().removeAllItems();
    }
 
    @Override
    public boolean removeContainerProperty(Object o) throws UnsupportedOperationException {
        return getDelegate().removeContainerProperty(o);
    }
 
    @Override
    public boolean removeItem(Object o) throws UnsupportedOperationException {
        return getDelegate().removeItem(o);
    }
 
    @Override
    public int size() {
        return getDelegate().size();
    }
 
    @Override
    public void addContainerFilter(Filter filter) throws UnsupportedFilterException {
        getDelegate().addContainerFilter(filter);
    }
 
    @Override
    public Collection<Filter> getContainerFilters() {
        return getDelegate().getContainerFilters();
    }
 
    @Override
    public void removeAllContainerFilters() {
        getDelegate().removeAllContainerFilters();
    }
 
    @Override
    public void removeContainerFilter(Filter filter) {
        getDelegate().removeContainerFilter(filter);
    }
 
    @Override
    public Collection<?> getSortableContainerPropertyIds() {
        return getDelegate().getSortableContainerPropertyIds();
    }
 
    @Override
    public void sort(Object[] objects, boolean[] booleans) {
        getDelegate().sort(objects, booleans);
    }
 
    @Override
    public void addItemSetChangeListener(ItemSetChangeListener itemSetChangeListener) {
        getDelegate().addItemSetChangeListener(itemSetChangeListener);
    }
 
    @Override
    @Deprecated
    public void addListener(ItemSetChangeListener itemSetChangeListener) {
        getDelegate().addListener(itemSetChangeListener);
    }
 
    @Override
    public void removeItemSetChangeListener(ItemSetChangeListener itemSetChangeListener) {
        getDelegate().removeItemSetChangeListener(itemSetChangeListener);
    }
 
    @Override
    @Deprecated
    public void removeListener(ItemSetChangeListener itemSetChangeListener) {
        getDelegate().removeListener(itemSetChangeListener);
    }
}