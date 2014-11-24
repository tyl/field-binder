package org.tylproject.vaadin.addon.masterdetail.strategies;

import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.sqlcontainer.query.QueryDelegate;
import com.vaadin.ui.Table;
import org.tylproject.vaadin.addon.masterdetail.DetailContainerChange;
import org.tylproject.vaadin.addon.masterdetail.MasterItemChange;

import java.util.Collection;

/**
 * Created by evacchi on 24/11/14.
 */
public abstract class BeanItemStrategy {


    public static BeanItemStrategy.Builder masterProperty(String masterPropertyId) {
        return new Builder(masterPropertyId);
    }

    public static class Builder implements MasterStrategyBuilder {
        String masterPropertyId;
        Class<?> beanType;

        Builder(String masterPropertyId) {
            this.masterPropertyId = masterPropertyId;
        }

        public <U> Builder isCollectionOf(Class<U> beanType) {
            this.beanType = beanType;
            return this;
        }


        @Override
        public MasterItemChange.Listener build() {
            return new MasterListener(masterPropertyId, beanType);
        }
    }


    public static class MasterListener<U> implements MasterItemChange.Listener {

        final String detailPropertyId;
        final Class<U> detailPropertyType;


        public MasterListener(String detailPropertyId, Class<U> detailPropertyType) {
            this.detailPropertyId = detailPropertyId;
            this.detailPropertyType = detailPropertyType;
        }

        @Override
        public void masterItemChange(MasterItemChange.Event event) {
            BeanItem<?> beanItem = (BeanItem<?>) event.getNewItem();
            if (beanItem == null) return;

            Property<?> detailProperty = beanItem.getItemProperty(detailPropertyId);
            Collection<U> values = (Collection<U>) detailProperty.getValue();

            BeanItemContainer<U> detailContainer = new BeanItemContainer<U>(detailPropertyType);
            detailContainer.addAll(values);

            event.getSource().setDetailContainerDataSource(detailContainer);
        }
    }

}
