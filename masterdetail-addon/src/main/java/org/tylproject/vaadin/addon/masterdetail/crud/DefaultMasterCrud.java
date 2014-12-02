package org.tylproject.vaadin.addon.masterdetail.crud;

import org.tylproject.vaadin.addon.fieldbinder.BeanFieldBinder;
import org.tylproject.vaadin.addon.masterdetail.Master;
import org.tylproject.vaadin.addon.crudnav.CrudNavigation;
import org.tylproject.vaadin.addon.crudnav.events.ItemEdit;
import org.tylproject.vaadin.addon.crudnav.events.ItemRemove;
import org.tylproject.vaadin.addon.crudnav.events.OnCommit;
import org.tylproject.vaadin.addon.crudnav.events.OnDiscard;

/**
 * Created by evacchi on 26/11/14.
 */
public abstract class DefaultMasterCrud implements MasterCrud {

    protected CrudNavigation navigation;
    protected BeanFieldBinder<?> fieldBinder;

    public DefaultMasterCrud() {}

    @Override
    public MasterCrud withMaster(Master<?> target) {
        this.navigation = target.getNavigation();
        this.fieldBinder = target.getFieldGroup();
        fieldBinder.setReadOnly(true);
        return this;
    }

    @Override
    public void onCommit(OnCommit.Event event) {
        fieldBinder.commit();
        fieldBinder.setReadOnly(true);
    }

    @Override
    public void onDiscard(OnDiscard.Event event) {
        fieldBinder.discard();
        fieldBinder.setReadOnly(true);
    }

    @Override
    public void itemRemove(ItemRemove.Event event) {
        navigation.getContainer().removeItem(navigation.getCurrentItemId());
    }

    @Override
    public void itemEdit(ItemEdit.Event event) {
        fieldBinder.setReadOnly(!fieldBinder.isReadOnly());
    }
}
