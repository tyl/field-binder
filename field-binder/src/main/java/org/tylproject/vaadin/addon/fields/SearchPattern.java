package org.tylproject.vaadin.addon.fields;

import com.vaadin.data.Container;
import org.tylproject.vaadin.addon.fieldbinder.behavior.DefaultFilterFactory;
import org.tylproject.vaadin.addon.fieldbinder.behavior.FilterFactory;

public class SearchPattern {
    public static final SearchPattern Empty = new SearchPattern();

    private final String stringPattern;
    private final Container.Filter filter;
    private final boolean empty;

    public static SearchPattern of(String stringPattern, Container.Filter filter) {
        return new SearchPattern(stringPattern, filter);
    }

    private SearchPattern() {
        this.filter = null; this.stringPattern = null; this.empty = true;
    }

    private SearchPattern(String stringPattern, Container.Filter filter) {
        this.filter = filter;
        this.stringPattern = stringPattern;
        this.empty = false;
    }

    public String getStringPattern() {
        return stringPattern;
    }
    public Container.Filter getFilter() {
        return filter;
    }

    public boolean isEmpty() {
        return empty;
    }
}
