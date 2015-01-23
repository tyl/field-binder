package org.tylproject.vaadin.addon;

import org.tylproject.vaadin.addon.fields.FilterExpressionField;
import org.tylproject.vaadin.addon.fields.SearchPattern;

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
