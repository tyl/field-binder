package org.tylproject.vaadin.addon.fields.search;

import java.util.Collection;

/**
 * Created by evacchi on 23/01/15.
 */
public interface SearchDialog {
    void show();

    void apply();

    Collection<SearchPattern> getSearchPatterns();
    void clear();
    void cancel();

}
