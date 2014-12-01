package org.tylproject.vaadin.addon.masterdetail.connectors;

import com.vaadin.data.Container;
import org.tylproject.vaadin.addon.masterdetail.Detail;

import java.util.Collection;

/**
 * Created by evacchi on 27/11/14.
 */
public interface MasterDetailConnector {
    /**
     * Returns the container from the given masterItem
     *
     * @param detailCollection collection of the values that should go into the detail
     */
    public <M,D> Container.Indexed toContainer(Detail<D> detail, Collection<D> detailCollection);
    public Class<?> getContainerType();

}
