package org.tylproject.vaadin.addon.masterdetail;

import com.vaadin.ui.Table;
import org.tylproject.vaadin.addon.masterdetail.crud.BeanDetailCrud;
import org.tylproject.vaadin.addon.crudnav.BasicCrudNavigation;
import org.tylproject.vaadin.addon.crudnav.CrudNavigation;
import org.tylproject.vaadin.addon.crudnav.events.*;
import org.vaadin.maddon.ListContainer;

/**
 * Created by evacchi on 27/11/14.
 */
public class Detail<D> extends NavigableTable {

    private final Class<D> type;
    private final String masterPropertyId;
    private final Class<?> containerType;


    private Detail(Table table, BasicCrudNavigation navigation,
                   Class<D> type, String masterPropertyId,
                   Class<?> containerType) {
        super(table, navigation);
        this.type = type;
        this.masterPropertyId = masterPropertyId;
        this.containerType = containerType;
    }

    public Class<D> getType() {
        return type;
    }

    public String getMasterPropertyId() {
        return masterPropertyId;
    }

    public static <T> Detail.Builder<T> collectionOf(Class<T> detailClass) {
        return new Detail.Builder<T>(detailClass);
    }

    public Class<?> getContainerType() {
        return containerType;
    }

    public static class Builder<D> {
        private Class<D> detailClass;
        private String masterPropertyId;
        private Table table;
        private Detail<D> detailInstance;
        private Class<?> containerType = ListContainer.class;

        public Builder(Class<D> detailClass) {
            this.detailClass = detailClass;
        }

        public Builder<D> fromMasterProperty(String masterPropertyId) {
            this.masterPropertyId = masterPropertyId;
            return this;
        }

        public Builder<D> boundTo(Table table) {
            this.table = table;
            return this;
        }


        // optionally
        public <X extends OnDiscard.Listener
                & OnCommit.Listener
                & ItemRemove.Listener
                & ItemEdit.Listener
                & ItemCreate.Listener> Builder<D> withCrud(X crudObject) {


            CrudNavigation detailNav = this.build().getNavigation();

            detailNav.addItemRemoveListener(crudObject);
            detailNav.addOnCommitListener(crudObject);
            detailNav.addOnDiscardListener(crudObject);
            detailNav.addItemEditListener(crudObject);
            detailNav.addItemCreateListener(crudObject);

            return this;
        }


        public Builder<D> withDefaultCrud() {
            if (ListContainer.class.isAssignableFrom(containerType)) {
                withCrud(new BeanDetailCrud<D>(detailClass).withDetail(this.build()));
            }
            else throw new UnsupportedOperationException("no known CrudStrategy for this container type: " + containerType);

            return this;


        }

        public Detail<D> build() {
            if (detailInstance == null) {
                this.detailInstance = new Detail<D>(
                        table,
                        new BasicCrudNavigation(),
                        detailClass,
                        masterPropertyId,
                        containerType
                );
            }
            return this.detailInstance;
        }
        public Builder<D> withContainerType(Class<?> containerType) {
            this.containerType = containerType;
            return this;
        }


    }


}
