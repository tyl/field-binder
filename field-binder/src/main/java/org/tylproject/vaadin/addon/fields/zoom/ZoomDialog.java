package org.tylproject.vaadin.addon.fields.zoom;

import com.vaadin.ui.Component;

/**
 * Created by evacchi on 19/01/15.
 */
public interface ZoomDialog<T> extends Component {
    public T dismiss();
    public void show(T value);
    public ZoomDialog<T> withPropertyId(Object propertyId);
}
