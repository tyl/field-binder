package org.tylproject.vaadin.addon.masterdetail.connectors;

import com.vaadin.data.Container;
import org.tylproject.vaadin.addon.masterdetail.builder.Detail;
import org.vaadin.maddon.ListContainer;

import java.util.Collection;
import java.util.List;

/**
 * Created by evacchi on 27/11/14.
 */
public class ListContainerConnector implements MasterDetailConnector {
    @Override
    public <M, D> Container.Indexed toContainer(Detail<D> detail, Collection<D> detailCollection) {
        if (!(detailCollection instanceof List))
            throw new UnsupportedOperationException("detailCollection type should be java.util.List");

        return new ListContainer<D>(detail.getType(), detailCollection);
    }

    @Override
    public Class<?> getContainerType() {
        return ListContainer.class;
    }
}
