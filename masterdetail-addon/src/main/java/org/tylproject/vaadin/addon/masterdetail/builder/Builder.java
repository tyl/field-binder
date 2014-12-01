package org.tylproject.vaadin.addon.masterdetail.builder;

import com.vaadin.data.Container;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.ui.Table;
import org.tylproject.vaadin.addon.crudnav.BasicCrudNavigation;
import org.tylproject.vaadin.addon.masterdetail.MasterDetailController;


public class Builder {

    final NavigableFieldGroup master;
    NavigableTable detail;

    final MasterDetailController.Builder masterDetailBuilder;
    MasterDetailController masterDetailController;

    boolean withDefaultCrud = true;

    public static Builder forMaster(FieldGroup master) {
        return new Builder(master);
    }

    Builder(FieldGroup masterFieldGroup) {
        this.master = new NavigableFieldGroup<FieldGroup>(
                masterFieldGroup, new BasicCrudNavigation());

        this.masterDetailBuilder = MasterDetailController.Builder.forMaster(master.getFieldGroup());
    }

    public Builder withMasterContainer(Container.Indexed container) {
        this.master.getNavigation().setContainer(container);
        return this;
    }

    public Builder withDetail(Table detail) {
        this.detail = new NavigableTable(detail, new BasicCrudNavigation());
        masterDetailBuilder.withDetail(detail);
        return this;
    }





}
