package org.tylproject.vaadin.addon.masterdetail.strategies;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.query.QueryDelegate;
import org.tylproject.vaadin.addon.masterdetail.MasterItemChange;

import java.sql.SQLException;

/**
 * Created by evacchi on 24/11/14.
 */
public class SqlStrategy {


    public static SqlStrategy.Builder masterProperty(String masterPropertyId) {
        return new Builder(masterPropertyId);
    }

    public static class Builder {
        String masterPropertyId;
        String detailPropertyId;

        Builder(String masterPropertyId) {
            this.masterPropertyId = masterPropertyId;
        }

        public Builder equalsDetailProperty(String detailPropertyId) {
            this.detailPropertyId = detailPropertyId;
            return this;
        }

        public MasterListener withQueryDelegate(QueryDelegate queryDelegate) {
            return new MasterListener(masterPropertyId, detailPropertyId, queryDelegate);
        }
    }

    public static class MasterListener implements MasterItemChange.Listener {

        final String masterPropertyId;
        final String detailPropertyId;
        final QueryDelegate queryDelegate;

        public MasterListener(String masterPropertyId,
                              String detailPropertyId,
                              QueryDelegate queryDelegate) {
            this.masterPropertyId = masterPropertyId;
            this.detailPropertyId = detailPropertyId;
            this.queryDelegate = queryDelegate;
        }

        @Override
        public void masterItemChange(MasterItemChange.Event event) {
            try {
                Item newItem = event.getNewItem();
                Property<?> masterProperty = newItem.getItemProperty(masterPropertyId);

                SQLContainer detailContainer = new SQLContainer(queryDelegate);
                detailContainer.addContainerFilter(new Compare.Equal(detailPropertyId, masterProperty.getValue()));

                event.getSource().setDetailContainerDataSource(detailContainer);

            } catch (SQLException ex) {
                throw new MasterDetailStrategyInstantiationException(ex);
            }
        }
    }

}
