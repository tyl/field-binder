package org.tylproject.vaadin.addon.fields;

import com.vaadin.data.Container;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import org.tylproject.vaadin.addon.autoform.TableZoomDialog;
import org.tylproject.vaadin.addon.autoform.ZoomDialog;
import org.tylproject.vaadin.addon.fieldbinder.BeanTable;

/**
 * Created by evacchi on 16/01/15.
 */
public class ZoomField<T> extends CombinedField<T> {

    private ZoomDialog dialog;

    public ZoomField(TextField textField, Class<T> type) {
        super(textField, new Button(FontAwesome.SEARCH), type);
        getButton().addClickListener(new ClickListener());
    }
    public ZoomField(Class<T> type) {
        this(new TextField(), type);
        getBackingField().setNullRepresentation("");
    }
    public void setZoomDialog(ZoomDialog dialog) {
        this.dialog = dialog;
    }

    public ZoomDialog getZoomDialog() {
        return dialog;
    }

    public void setZoomContainer(Container.Ordered container) {
        this.dialog = new TableZoomDialog<T>(
                new BeanTable<T>((Class<T>)this.getType(), container));
    }


    class ClickListener implements Button.ClickListener {
        @Override
        public void buttonClick(Button.ClickEvent event) {
            ZoomDialog dialog = getZoomDialog();
            Window w = new Window(dialog);
            w.show();
        }
    }

    class Window extends com.vaadin.ui.Window {
        private final ZoomDialog dialog;

        public Window(ZoomDialog dialog) {
            super(dialog.getCaption(), dialog);
            this.dialog = dialog;
            this.addCloseListener(new ZoomCallback());
        }

        public void show() {
            dialog.show(getValue());
            UI.getCurrent().addWindow(this);
        }
    }


    class ZoomCallback implements com.vaadin.ui.Window.CloseListener {
        @Override
        public void windowClose(com.vaadin.ui.Window.CloseEvent e) {
            Object itemId = dialog.dismiss();
            setValue((T) itemId);
        }
    }

}
