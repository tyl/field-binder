package org.tylproject.vaadin.addon.utils;

import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItem;

/**
 * Created by evacchi on 10/02/15.
 */
public class DefaultBeanExtractor implements BeanExtractor {
    @Override
    public Object extract(Object itemId, Item item) throws IllegalArgumentException {

        final Class<?> itemClass = item.getClass();
        final String itemClassName = itemClass.getCanonicalName();
        Object bean;

        // we compare strings so that Java linked does not complain
        // if any of these classes is missing
        if (itemClassName.startsWith("org.vaadin.viritin.")) {
            bean = itemId;
        } else
        if (itemClassName.startsWith("com.vaadin.addon.jpacontainer.")) {
            bean = ((EntityItem<?>)item).getEntity();
        } else
        if (BeanItem.class.isAssignableFrom(item.getClass())) {
            bean = ((BeanItem<?>) item).getBean();
        } else{
            throw new UnsupportedOperationException(
                    "Unknown item type: "+itemClassName);
        }

        return bean;
    }
}
