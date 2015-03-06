package org.tylproject.vaadin.addon.utils;

import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItem;
import org.vaadin.viritin.DynaBeanItem;
import org.vaadin.viritin.ListContainer;

/**
 * Extracts bean from an Item, depending on the Container implementation
 */
public class DefaultBeanExtractor implements BeanExtractor {
    @Override
    public Object extract(Object itemId, Item item) throws IllegalArgumentException {

        final Class<?> itemClass = item.getClass();
        Object bean;

        if (ListContainer.DynaBeanItem.class.isAssignableFrom(itemClass)
            || DynaBeanItem.class.isAssignableFrom(itemClass)) {
            bean = itemId;
        } else
        if (isJPAContainer(itemClass)) {
            bean = ((EntityItem<?>)item).getEntity();
        } else
        if (BeanItem.class.isAssignableFrom(item.getClass())) {
            bean = ((BeanItem<?>) item).getBean();
        } else{
            throw new UnsupportedOperationException(
                    "Unknown item type: "+itemClass.getCanonicalName());
        }

        return bean;
    }


    protected boolean isJPAContainer(Class<?> itemClass) {
        try {
            return EntityItem.class.isAssignableFrom(itemClass);
        } catch (NoClassDefFoundError ex) { return false; }
    }




}
