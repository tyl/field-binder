package org.tylproject.vaadin.addon.utils;

import com.vaadin.data.Container;
import com.vaadin.data.Item;

/**
 * Created by evacchi on 10/02/15.
 */
public interface BeanExtractor {
    public Object extract(Object itemId, Item item) throws IllegalArgumentException;
}
