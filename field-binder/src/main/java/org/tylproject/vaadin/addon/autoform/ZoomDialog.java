package org.tylproject.vaadin.addon.autoform;

import com.vaadin.ui.Component;

/**
 * Created by evacchi on 19/01/15.
 */
public interface ZoomDialog extends Component {
    public Object dismiss();
    public void show(Object itemId);
}
