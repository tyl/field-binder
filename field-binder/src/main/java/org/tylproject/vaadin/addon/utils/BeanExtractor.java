package org.tylproject.vaadin.addon.utils;

import com.vaadin.data.Container;
import com.vaadin.data.Item;

/**
 * Extract a Bean for a given (Item, itemId pair)
 */
public interface BeanExtractor {
    public Object extract(Object itemId, Item item) throws IllegalArgumentException;
}
