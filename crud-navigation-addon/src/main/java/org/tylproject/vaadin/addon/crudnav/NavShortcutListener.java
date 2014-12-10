package org.tylproject.vaadin.addon.crudnav;

import com.vaadin.event.ShortcutListener;

/**
 * Created by evacchi on 25/11/14.
 */
public abstract class NavShortcutListener extends ShortcutListener {
    private boolean enabled = true;

    public NavShortcutListener(String caption, int keyCode, int... modifiers) {
        super(caption, keyCode, modifiers);
    }

    public void enable() {
        this.enabled = true;
    }

    public void disable() {
        this.enabled = false;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public final void handleAction(Object sender, Object target) {
        if (isEnabled()) handle(sender, target);
    }

    protected abstract void handle(Object sender, Object target);


}
