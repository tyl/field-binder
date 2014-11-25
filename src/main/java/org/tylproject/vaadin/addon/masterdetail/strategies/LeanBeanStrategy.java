package org.tylproject.vaadin.addon.masterdetail.strategies;

import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import org.tylproject.vaadin.addon.masterdetail.MasterItemChange;
import org.tylproject.vaadin.addon.masterdetail.strategies.BeanItemStrategy;
import org.tylproject.vaadin.addon.masterdetail.strategies.MasterStrategyBuilder;
import org.vaadin.maddon.ListContainer;

import java.util.Collection;

/**
 * Created by evacchi on 24/11/14.
 */
public abstract class LeanBeanStrategy {


    public static Builder masterProperty(String masterPropertyId) {
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

            ListContainer<U> detailContainer = new ListContainer<U>(values);

            event.getSource().setDetailContainerDataSource(detailContainer);
        }
    }

}
