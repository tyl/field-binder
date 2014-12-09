package org.tylproject.vaadin.addon.crudnav.resources;

import java.util.ListResourceBundle;

/**
 * Created by evacchi on 09/12/14.
 */
public class ButtonCaptions_it extends ListResourceBundle {

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
            {"find", "Cerca"}

    };

    @Override
    protected Object[][] getContents() {
        return STRINGS;
    }
}
