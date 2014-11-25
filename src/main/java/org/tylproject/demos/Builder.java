package org.tylproject.demos;

import com.vaadin.data.Container;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.ui.Panel;
import org.tylproject.vaadin.addon.crudnav.BasicCrudNavigation;
import org.tylproject.vaadin.addon.crudnav.CrudNavigation;
import org.tylproject.vaadin.addon.masterdetail.MasterDetail;
import org.tylproject.vaadin.addon.masterdetail.strategies.MasterStrategyBuilder;

/**
 * Created by evacchi on 25/11/14.
 */
public class Builder {

    final CrudNavigation masterNav;
    final FieldGroup master;
    final Panel masterPanel;

    CrudNavigation detailNav;
    Container.Viewer detail;
    Panel detailPanel;

    MasterDetail masterDetail;

    public static Builder forMaster(FieldGroup master, Panel panel) {
        return new Builder(master, panel);
    }

    public Builder(FieldGroup master, Panel masterPanel) {
        this.master = master;
        this.masterPanel = masterPanel;
        this.masterNav = new BasicCrudNavigation();
    }

    public Builder withDetail(Container.Viewer detail, Panel detailPanel) {
        this.detail = detail;
        this.detailNav = new BasicCrudNavigation();
        this.detailPanel = detailPanel;
        return this;
    }

    public Builder where(MasterStrategyBuilder strategyBuilder) {
        this.masterDetail = MasterDetail.Builder.forMaster(master)
                .withDetail(detail)
                .where(strategyBuilder).build();
        this.masterNav.addCurrentItemChangeListener(
                new MyVaadinUI.MasterUpdater(this.masterDetail));
        return this;
    }

    public Builder withDefaultKeyBindings() {
        return this;
    }



}
