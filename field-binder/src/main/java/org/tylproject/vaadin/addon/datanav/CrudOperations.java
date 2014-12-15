package org.tylproject.vaadin.addon.datanav;

/**
 * Created by evacchi on 19/11/14.
 */
public interface CrudOperations {
    // CRUD

    /**
     *
     * @return
     */
    public void create();
    public void commit();
    public void discard();
    public void remove(Object itemId);
}
