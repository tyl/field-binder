package org.tylproject.vaadin.addon.datanav.events;

import org.tylproject.vaadin.addon.datanav.DataNavigation;

import java.util.EventObject;

/**
 * Created by evacchi on 19/11/14.
 */
public class CrudEvent extends EventObject {
    public CrudEvent(DataNavigation source) {
        super(source);
    }

    @Override
    public DataNavigation getSource() {
        return (DataNavigation) super.getSource();
    }
}
