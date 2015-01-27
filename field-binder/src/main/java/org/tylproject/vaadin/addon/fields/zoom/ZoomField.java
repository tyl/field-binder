package org.tylproject.vaadin.addon.fields.zoom;

import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.tylproject.vaadin.addon.datanav.resources.Strings;
import org.tylproject.vaadin.addon.fieldbinder.BeanTable;
import org.tylproject.vaadin.addon.fields.CombinedField;

import java.util.ResourceBundle;

/**
 * Decorates a TextField with a "zoom" button.
 *
 * The zoom button opens a customizable popup from where the user may select an element.
 * The selected element will be set as the value of the field.
 *
 * The popup is customizable through the {@link #setZoomDialog(ZoomDialog)}
 * and {@link #withZoomDialog(ZoomDialog)} methods.
 *
 * The ZoomField can be seen as an "extended" ComboBox where a
 * popup window is shown instead of a small menu. Depending on the user's choice
 * the popup window may do different things. The popup window is automatically generated
 * from an instance of the {@link org.tylproject.vaadin.addon.fields.zoom.ZoomDialog} interface.
 *
 * The default implementations are {@link org.tylproject.vaadin.addon.fields.zoom.TableZoomDialog}.
 * and {@link org.tylproject.vaadin.addon.fields.zoom.GridZoomDialog}, which is backed
 * by Vaadin's Grid widget.
 *
 * <code>
 * <pre>
 * Grid myGrid = new Grid(someContainer);
 * ZoomField<Person> firstName =
 *       new ZoomField<>(Person.class)
 *           .withZoomDialog(new GridZoomDialog(myGrid, "firstName"));
 * </pre>
 * </code>
 *
 * Most often, you will probably get an instance of a ZoomField from a
 * {@link org.tylproject.vaadin.addon.fieldbinder.FieldBinder}. <i>E.g,:</i>
 *
 * <code>
 * <pre>
 *
 * ZoomField<Person> binder.zoomField("firstName")
 *           .withZoomDialog(new GridZoomDialog(myGrid)).build();
 * </pre>
 * </code>
 *
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
        getBackingField().setNullRepresentation("");
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

}
