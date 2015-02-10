package org.tylproject.vaadin.addons.fieldbinder.tests;

import com.vaadin.data.util.BeanItem;
import org.junit.Assert;
import org.junit.Test;
import org.tylproject.vaadin.addon.utils.BeanExtractor;
import org.tylproject.vaadin.addon.utils.DefaultBeanExtractor;
import org.vaadin.viritin.DynaBeanItem;

/**
 * Created by evacchi on 10/02/15.
 */
public class DefaultBeanExtractorText {

    BeanExtractor beanExtractor = new DefaultBeanExtractor();

    @Test
    public void testListContainer() {

        Object itemId = new Object();
        DynaBeanItem<Object> item = new DynaBeanItem<>(itemId);

        Object object = beanExtractor.extract(itemId, item);

        Assert.assertSame(itemId, object);

    }


    @Test
    public void testBeanItem() {
        Object itemId = new Object();
        BeanItem<Object> beanItem = new BeanItem<>(itemId);

        Object object = beanExtractor.extract(itemId, beanItem);

        Assert.assertSame(itemId, object);

    }


}
