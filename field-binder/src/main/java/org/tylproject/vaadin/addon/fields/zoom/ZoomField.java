package org.tylproject.vaadin.addon.fields.zoom;

import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.tylproject.vaadin.addon.datanav.resources.Strings;
import org.tylproject.vaadin.addon.fieldbinder.BeanTable;
import org.tylproject.vaadin.addon.fields.CombinedField;

import java.util.ResourceBundle;

/**
 * Decorates a Field with a "zoom" button.
 *
 * The zoom button opens a customizable popup from where the user may select an element.
 * The selected element will be set as the value of the field.
 *
 */
public class ZoomField<T> extends CombinedField<T, String, TextField> {

    private ZoomDialog<T> dialog;

    public ZoomField(TextField field, Class<T> type) {
        super(field, new Button(FontAwesome.SEARCH), type);
        getButton().addClickListener(new ButtonClickListener<>(this));
    }

    public ZoomField(Class<T> type) {
        this(new TextField(), type);
    }

    public ZoomDialog<T> getZoomDialog() {
        return dialog;
    }

    public void setZoomDialog(ZoomDialog<T> dialog) {
        this.dialog = dialog;
    }

    /**
     * "fluent" alias to {@link #setZoomDialog(ZoomDialog)}
     */
    public ZoomField<T> withZoomDialog(ZoomDialog<T> dialog) {
        this.setZoomDialog(dialog);
        return this;
    }

    /**
     * "fluent" shorthand to <code>setZoomDialog(new TableZoomDialog<T>(beanTable))</code>
     * @param beanTable
     * @return
     */
    public ZoomField<T> withZoomOnTable(BeanTable<?> beanTable) {
        this.setZoomDialog(new TableZoomDialog<T>(beanTable));
        return this;
    }

    static class ButtonClickListener<T> implements Button.ClickListener {
        final ZoomField<T> field;
        public ButtonClickListener(ZoomField<T> field) {
            this.field = field;
        }

        @Override
        public void buttonClick(Button.ClickEvent event) {
            new ZoomWindow<>(field).show();
        }
    }

    static class ZoomWindow<T> extends com.vaadin.ui.Window implements Button.ClickListener {
        private final ZoomField<T> field;

        protected static final ResourceBundle resourceBundle =
                ResourceBundle.getBundle(Strings.class.getCanonicalName());


        private final Label spacer = new Label();
        private final Button btnSelect = new Button(resourceBundle.getString("select"));
        private final Button btnSelectNone = new Button(resourceBundle.getString("selectNone"));
        private final Button btnCancel = new Button(resourceBundle.getString("cancel"));

        private final Panel content = new Panel();

        private final HorizontalLayout buttonBar = new HorizontalLayout(spacer, btnSelect, btnSelectNone, btnCancel);
        private final VerticalLayout rootLayout = new VerticalLayout(content, buttonBar);


        public ZoomWindow(ZoomField<T> field) {
            super(field.getCaption());

            makeLayout(field);

            setContent(rootLayout);
            setWidth("400px");
            setHeight("400px");

            this.field = field;

            btnSelect.addClickListener(this);
            btnSelectNone.addClickListener(this);
            btnCancel.addClickListener(this);

            setCloseShortcut(ShortcutAction.KeyCode.ESCAPE, null);


        }

        public Layout makeLayout(ZoomField<T> field) {

            btnSelect.addStyleName(ValoTheme.BUTTON_PRIMARY);
            content.addStyleName(ValoTheme.PANEL_BORDERLESS);
            Component dialogContents = field.getZoomDialog();
            dialogContents.setWidth("100%");
            dialogContents.setHeightUndefined();
            content.setContent(dialogContents);
            content.setSizeFull();

            buttonBar.setStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);
            buttonBar.setWidth("100%");
            buttonBar.setSpacing(true);
            buttonBar.setExpandRatio(spacer, 1);

            rootLayout.setSizeFull();
            rootLayout.setExpandRatio(content, 1);

            return rootLayout;
        }

        public void show() {
            field.getZoomDialog().show(this.field.getValue());
            UI.getCurrent().addWindow(this);
            center();
        }

        @Override
        public void buttonClick(Button.ClickEvent event) {
            if (event.getSource() == btnSelect) {
                field.setValue(field.getZoomDialog().dismiss());
            } else
            if (event.getSource() == btnSelectNone) {
                field.setValue(null);
            }
            this.close();
        }
    }

}
