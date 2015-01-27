package org.tylproject.vaadin.addon.fields.drilldown;

import com.vaadin.event.ShortcutAction;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.tylproject.vaadin.addon.datanav.resources.Strings;

import java.util.ResourceBundle;

/**
 * Created by evacchi on 27/01/15.
 */
public class DrillDownWindow<T> extends Window implements Button.ClickListener {
    private final DrillDownField<T> field;

    protected static final ResourceBundle resourceBundle =
            ResourceBundle.getBundle(Strings.class.getCanonicalName());


    private final Label spacer = new Label();
    private final Button btnClose = new Button(resourceBundle.getString("close"));

    private final Panel content = new Panel();

    private final HorizontalLayout buttonBar = new HorizontalLayout(spacer, btnClose);
    private final VerticalLayout rootLayout = new VerticalLayout(content, buttonBar);


    public DrillDownWindow(DrillDownField<T> field) {
        super(field.getCaption());

        makeLayout(field);

        setContent(rootLayout);
        setWidth("1200px");
        setHeight("400px");

        this.field = field;

        btnClose.addClickListener(this);
        btnClose.setClickShortcut(ShortcutAction.KeyCode.ESCAPE);
        btnClose.setClickShortcut(ShortcutAction.KeyCode.ENTER);
    }

    public Layout makeLayout(DrillDownField<T> field) {

        btnClose.setStyleName(ValoTheme.BUTTON_PRIMARY);
        content.addStyleName(ValoTheme.PANEL_BORDERLESS);

        Component dialogContents = field.getDrillDownDialog();

        content.setContent(dialogContents);
        content.setSizeFull();

        buttonBar.setStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);
        buttonBar.setWidth("100%");
        buttonBar.setSpacing(true);
        buttonBar.setExpandRatio(spacer, 1);

        rootLayout.setSizeFull();
        rootLayout.setExpandRatio(content, 1);
        rootLayout.setMargin(new MarginInfo(true, false, true, false));

        return rootLayout;
    }

    public void show() {
        UI.getCurrent().addWindow(this);
        center();
        focus();
    }

    @Override
    public void buttonClick(Button.ClickEvent event) {
        this.close();
    }

}
