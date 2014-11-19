package org.tylproject.vaadin.addon.crudnav.events;

import org.tylproject.vaadin.addon.crudnav.CrudNavigation;

import java.util.EventObject;

/**
 * Created by evacchi on 19/11/14.
 */
public class CrudEvent extends EventObject {
    public CrudEvent(CrudNavigation source) {
        super(source);
    }

    @Override
    public CrudNavigation getSource() {
        return (CrudNavigation) super.getSource();
    }
}
