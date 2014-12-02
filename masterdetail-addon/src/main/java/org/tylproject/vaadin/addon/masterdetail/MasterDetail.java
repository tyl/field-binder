package org.tylproject.vaadin.addon.masterdetail;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.ui.Table;
import org.tylproject.vaadin.addon.masterdetail.connectors.ListContainerConnector;
import org.tylproject.vaadin.addon.masterdetail.connectors.MasterDetailConnector;
import org.tylproject.vaadin.addon.crudnav.events.CurrentItemChange;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by evacchi on 25/11/14.
 */
public class MasterDetail<T,U> {

    private final MasterDetailController controller;
    private final Master<T> master;
    private final Detail<U> detail;

    public MasterDetail(MasterDetailController controller,
                        Master<T> master,
                        Detail<U> detail) {
        this.controller = controller;
        this.master = master; //new NavigableFieldGroup<FieldGroup>(controller.getMaster(), new BasicCrudNavigation());
        this.detail = detail; //new NavigableTable((Table)controller.getDetail(), new BasicCrudNavigation());
    }


    public MasterDetailController getController() {
        return controller;
    }

    public Master<T> getMaster() {
        return master;
    }

    public Detail<U> getDetail() {
        return detail;
    }

    public static <X,Y> Builder<X,Y> with(Master.Builder<X> masterBuilder, Detail.Builder<Y> detailBuilder) {
        return new Builder<X,Y>(masterBuilder, detailBuilder);
    }

    public static class Builder<TT,UU> {



        private final Master.Builder<TT> masterBuilder;
        private final Detail.Builder<UU> detailBuilder;
        private MasterDetailConnector connector = new ListContainerConnector();

        public Builder(Master.Builder<TT> masterBuilder, Detail.Builder<UU> detailBuilder) {
            this.masterBuilder = masterBuilder;
            this.detailBuilder = detailBuilder;
        }

        public Builder<TT,UU> withConnector(MasterDetailConnector connector) {
            this.connector = connector;
            return this;
        }

        public MasterDetail<TT,UU> build() {
            Master<TT> master = masterBuilder.build();
            Detail<UU> detail = detailBuilder.build();

            MasterDetailController controller = MasterDetailController.Builder
                    .forMaster(master.getFieldBinder())
                    .withDetail(detail.getTable())
                    .build();

            MasterDetail<TT,UU> masterDetail = new MasterDetail<TT,UU>(controller, master, detail);



            if (! detail.getContainerType().isAssignableFrom(connector.getContainerType())) {
                throw new UnsupportedOperationException(
                        String.format(
                                "expected type for detail container is not compatible with connector type: %s vs. %s",
                                detail.getContainerType(),
                                connector.getContainerType()
                        ));
            }

            masterDetail.getMaster().getNavigation().addCurrentItemChangeListener(
                    new MasterListener<UU>(detail, connector));


            master.getNavigation().first();

            return masterDetail;

        }

    }

    private static class MasterListener<U> implements CurrentItemChange.Listener {

        private final Detail<U> detail;
        private final MasterDetailConnector masterDetailConnector;
        public MasterListener(Detail<U> detail, MasterDetailConnector connector) {
            this.detail = detail;
            this.masterDetailConnector = connector;

        }

        @Override
        public void currentItemChange(CurrentItemChange.Event event) {
            Item newItem = event.getNewItem();

            Table detailTable = detail.getTable();
            Collection<U> values;
            if (newItem == null) {
                values = new ArrayList<U>();
            } else {
                Property<?> detailProperty = newItem.getItemProperty(detail.getMasterPropertyId());
                values = (Collection<U>) detailProperty.getValue();
            }

            Container.Indexed detailContainer = masterDetailConnector.toContainer(detail, values);
            detail.getNavigation().setContainer(detailContainer);

            detailTable.setContainerDataSource(detailContainer);
        }
    }
}
