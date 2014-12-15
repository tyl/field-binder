package org.tylproject.vaadin.addon.datanav.resources;

import java.util.ListResourceBundle;

/**
 * Created by evacchi on 09/12/14.
 */
public class Strings_it extends ListResourceBundle {

    private final Object[][] STRINGS = {

            // navigation
            {"next", "Successivo"},
            {"prev", "Precedente"},
            {"first", "Primo"},
            {"last", "Ultimo"},

            // CRUD
            {"create", "Crea"},
            {"edit", "Modifica"},
            {"remove", "Elimina"},
            {"commit", "Salva"},
            {"discard", "Annulla"},

            // find
            {"clearToFind", "Pulisci e Cerca"},
            {"find", "Cerca"},

            {"recordCounter", "{0} di {1}"},
            {"recordCounterFiltered", "{0} di {1} (*)"}


    };

    @Override
    protected Object[][] getContents() {
        return STRINGS;
    }
}
