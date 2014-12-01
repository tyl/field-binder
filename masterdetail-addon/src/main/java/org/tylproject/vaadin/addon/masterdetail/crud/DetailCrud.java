package org.tylproject.vaadin.addon.masterdetail.crud;

import org.tylproject.vaadin.addon.masterdetail.Detail;

/**
 * Created by evacchi on 27/11/14.
 */
public interface DetailCrud extends CrudStrategy {
    DetailCrud withDetail(Detail<?> detail);
}
