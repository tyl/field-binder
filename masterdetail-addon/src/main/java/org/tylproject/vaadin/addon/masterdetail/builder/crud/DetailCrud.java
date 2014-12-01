package org.tylproject.vaadin.addon.masterdetail.builder.crud;

import org.tylproject.vaadin.addon.masterdetail.builder.Detail;

/**
 * Created by evacchi on 27/11/14.
 */
public interface DetailCrud extends CrudStrategy {
    DetailCrud withDetail(Detail<?> detail);
}
