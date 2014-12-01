package org.tylproject.vaadin.addon.masterdetail.builder.crud;

import com.vaadin.ui.Table;
import org.tylproject.vaadin.addon.masterdetail.builder.Detail;
import org.tylproject.vaadin.addon.crudnav.CrudNavigation;
import org.tylproject.vaadin.addon.crudnav.events.*;

/**
 * Created by evacchi on 26/11/14.
 */
public abstract class DefaultDetailCrud implements DetailCrud {
    protected CrudNavigation navigation;
    protected Table table;

    @Override
    public DetailCrud withDetail(Detail<?> detail) {
        this.navigation = detail.getNavigation();
        this.table = detail.getTable();
        return this;
    }
    @Override
    public void itemEdit(ItemEdit.Event event) {
        table.setEditable(!table.isEditable());
    }

    @Override
    public void onDiscard(OnDiscard.Event event) {
        table.discard();
        table.setEditable(false);
    }

    @Override
    public void onCommit(OnCommit.Event event) {
        table.commit();
        table.setEditable(false);
    }

    @Override
    public void itemRemove(ItemRemove.Event event) {
        table.removeItem(navigation.getCurrentItemId());
    }

}
