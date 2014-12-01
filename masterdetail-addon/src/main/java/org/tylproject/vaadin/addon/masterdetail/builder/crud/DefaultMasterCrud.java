package org.tylproject.vaadin.addon.masterdetail.builder.crud;

import com.vaadin.data.fieldgroup.FieldGroup;
import org.tylproject.vaadin.addon.masterdetail.builder.Master;
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
    protected FieldGroup fieldGroup;

    public DefaultMasterCrud() {}

    @Override
    public MasterCrud withMaster(Master<?> target) {
        this.navigation = target.getNavigation();
        this.fieldGroup = target.getFieldGroup();
        fieldGroup.setReadOnly(true);
        return this;
    }

    @Override
    public void onCommit(OnCommit.Event event) {
        try {
            fieldGroup.commit();
            fieldGroup.setReadOnly(true);
        } catch (FieldGroup.CommitException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onDiscard(OnDiscard.Event event) {
        fieldGroup.discard();
        fieldGroup.setReadOnly(true);
    }

    @Override
    public void itemRemove(ItemRemove.Event event) {
        navigation.getContainer().removeItem(navigation.getCurrentItemId());
    }

    @Override
    public void itemEdit(ItemEdit.Event event) {
        fieldGroup.setReadOnly(!fieldGroup.isReadOnly());
    }
}
