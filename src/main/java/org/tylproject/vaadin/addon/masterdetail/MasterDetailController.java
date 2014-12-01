package org.tylproject.vaadin.addon.masterdetail;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.event.EventRouter;

import java.io.Serializable;

/**
 * Created by evacchi on 20/11/14.
 */
public class MasterDetailController implements Serializable,
        MasterItemChange.Notifier, DetailContainerChange.Notifier {

    private final EventRouter eventRouter = new EventRouter();
    private final FieldGroup master;
    private final Container.Viewer detail;

    private Item masterItemDataSource;
    private Container detailContainerDataSource;

    MasterDetailController(Builder builder) {
        this.master = builder.master;
        this.detail = builder.detail;
        if (builder.masterListener != null) {
            addMasterItemChangeListener(builder.masterListener);
        }
    }



    public FieldGroup getMaster() {
        return master;
    }


    public Container.Viewer getDetail() {
        return detail;
    }

    public void setMasterItemDataSource(Item dataSource) {
        Item oldItem = this.masterItemDataSource;

        this.masterItemDataSource = dataSource;

        // substitute master item
        master.setItemDataSource(dataSource);

        fireMasterItemChangeEvent(oldItem, dataSource);
    }

    public void setDetailContainerDataSource(Container dataSource) {
        Container oldContainer = this.detailContainerDataSource;

        this.detailContainerDataSource = dataSource;

        // substitute detail container
        detail.setContainerDataSource(dataSource);

        fireDetailContainerChangeEvent(oldContainer, dataSource);
    }



    protected void fireMasterItemChangeEvent(Item oldItem, Item newItem) {
        this.eventRouter.fireEvent(new MasterItemChange.Event(this, oldItem, newItem));
    }

    protected void fireDetailContainerChangeEvent(Container oldContainer, Container newContainer) {
        this.eventRouter.fireEvent(new DetailContainerChange.Event(this, oldContainer, newContainer));
    }

    @Override
    public void addMasterItemChangeListener(MasterItemChange.Listener listener) {
        this.eventRouter.addListener(MasterItemChange.Event.class, listener, MasterItemChange.Listener.method);
    }

    @Override
    public void removeMasterItemChangeListener(MasterItemChange.Listener listener) {
        this.eventRouter.removeListener(MasterItemChange.Event.class, listener, MasterItemChange.Listener.method);
    }

    @Override
    public void addDetailContainerChangeListener(DetailContainerChange.Listener listener) {
        this.eventRouter.addListener(DetailContainerChange.Event.class, listener, DetailContainerChange.Listener.method);
    }

    @Override
    public void removeDetailContainerChangeListener(DetailContainerChange.Listener listener) {
        this.eventRouter.removeListener(DetailContainerChange.Event.class, listener, DetailContainerChange.Listener.method);
    }


    public static class Builder {

        public static Builder forMaster(FieldGroup fieldGroup) {
            return new Builder(fieldGroup);
        }

        final FieldGroup master;
        Container.Viewer detail;
        MasterItemChange.Listener masterListener;

        public Builder(FieldGroup fieldGroup) {
            this.master = fieldGroup;
        }
        public Builder withDetail(Container.Viewer detail) {
            this.detail = detail;
            return this;
        }
//        public Builder where(MasterStrategyBuilder masterStrategyBuilder) {
//            return this.connectedBy(masterStrategyBuilder.build());
//        }


        public Builder connectedBy(MasterItemChange.Listener listener) {
            this.masterListener = listener;
            return this;
        }
        public MasterDetailController build() {
            return new MasterDetailController(this);
        }



    }

}
