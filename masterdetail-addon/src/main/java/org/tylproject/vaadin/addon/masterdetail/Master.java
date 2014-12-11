package org.tylproject.vaadin.addon.masterdetail;

import com.vaadin.data.Container;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.BeanItemContainer;
import org.tylproject.vaadin.addon.fieldbinder.FieldBinder;
import org.tylproject.vaadin.addon.masterdetail.crud.BeanNavigationStrategy;
import org.tylproject.vaadin.addon.masterdetail.crud.MongoNavigationStrategy;
import org.tylproject.vaadin.addon.MongoContainer;
import org.tylproject.vaadin.addon.crudnav.BasicCrudNavigation;
import org.tylproject.vaadin.addon.crudnav.CrudNavigation;
import org.tylproject.vaadin.addon.crudnav.events.*;
import org.vaadin.maddon.ListContainer;

/**
 * Created by evacchi on 27/11/14.
 */
public class Master<T> extends NavigableFieldBinder<T> {

    private final Class<T> type;

    private Master(FieldBinder<T> fieldGroup, CrudNavigation navigation, Class<T> masterClass) {
        super(fieldGroup, navigation);
        this.type = masterClass;
    }

    public Class<T> getType() {
        return type;
    }

    public static <T> Master.Builder<T> of(Class<T> masterClass) {
        return new Master.Builder<T>(masterClass);
    }

    public static class Builder<M> {

        private final Class<M> masterClass;
        private Container.Indexed masterContainer;
        private FieldBinder<M> fieldBinder;
        private Master<M> masterInstance;

        private Builder(Class<M> masterClass) {
            this.masterClass = masterClass;
            this.fieldBinder = new FieldBinder<M>(masterClass);
        }

        public Master.Builder<M> fromContainer(Container.Indexed containerDataSource) {
            this.masterContainer = containerDataSource;
            return this;
        }

        public Master.Builder<M> boundTo(FieldBinder<M> fieldBinder) {
            this.fieldBinder = fieldBinder;
            return this;
        }

        public Master<M> build() {
            if (masterInstance == null) {
                this.masterInstance = new Master<M>(
                        fieldBinder,
                        new BasicCrudNavigation(),
                        masterClass
                );
                this.masterInstance.getNavigation().setContainer(masterContainer);
            }
            return this.masterInstance ;
        }

        // optionally
        public <X extends OnDiscard.Listener
                & OnCommit.Listener
                & ItemRemove.Listener
                & ItemEdit.Listener
                & ItemCreate.Listener
                & CurrentItemChange.Listener> Master.Builder<M> withCrud(X crudObject) {


            CrudNavigation masterNav = this.build().getNavigation();

            masterNav.addItemRemoveListener(crudObject);
            masterNav.addOnCommitListener(crudObject);
            masterNav.addOnDiscardListener(crudObject);
            masterNav.addItemEditListener(crudObject);
            masterNav.addItemCreateListener(crudObject);
            masterNav.addCurrentItemChangeListener(crudObject);

            return this;
        }


        public Master.Builder<M> withDefaultCrud() {

            if (masterContainer instanceof BeanContainer
                    || masterContainer instanceof BeanItemContainer
                    || masterContainer instanceof ListContainer) {
                withCrud(new BeanNavigationStrategy<M>(masterClass).withMaster(this.build()));
            } else if (masterContainer instanceof MongoContainer) {
                withCrud(new MongoNavigationStrategy<M>(masterClass).withMaster(this.build()));
            }
            else throw new UnsupportedOperationException("no known CrudStrategy for this container type: "+masterContainer.getClass());

            return this;
        }



    }
}
