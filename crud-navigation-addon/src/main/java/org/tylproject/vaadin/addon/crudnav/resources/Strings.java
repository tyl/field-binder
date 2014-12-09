package org.tylproject.vaadin.addon.crudnav.resources;

import java.util.ListResourceBundle;

/**
 * Created by evacchi on 09/12/14.
 */
public class Strings extends ListResourceBundle {

    private final Object[][] STRINGS = {

            // navigation
            {"next", "Next"},
            {"prev", "Prev"},
            {"first", "First"},
            {"last", "Last"},

            // CRUD
            {"create", "Create"},
            {"edit", "Edit"},
            {"remove", "Remove"},
            {"commit", "Commit"},
            {"discard", "Discard"},

            // find
            {"clearToFind", "Clear to Find"},
            {"find", "Find"},

            {"recordCounter", "{0} of {1}"},
            {"recordCounterFiltered", "{0} of {1} (*)"}

    };

    @Override
    protected Object[][] getContents() {
        return STRINGS;
    }
}
