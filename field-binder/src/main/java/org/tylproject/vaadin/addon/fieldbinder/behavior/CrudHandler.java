package org.tylproject.vaadin.addon.fieldbinder.behavior;


/**
 * A generic interface for an object that declares if it handles
 * a particular Class instance
 *
 * @see org.tylproject.vaadin.addon.fieldbinder.behavior.commons.FieldBinders.BaseCrud
 * @see org.tylproject.vaadin.addon.fieldbinder.behavior.commons.Tables.BaseCrud
 */
public interface CrudHandler extends CrudListeners {
    /**
     *
     * Returns true if the given class matches the one handled by the implementor of the interface
     * @param clazz
     */
    public boolean matches(Class<?> clazz);
}