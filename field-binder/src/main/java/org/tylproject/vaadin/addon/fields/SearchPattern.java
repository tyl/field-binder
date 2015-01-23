package org.tylproject.vaadin.addon.fields;

import com.vaadin.data.Container;

public class SearchPattern {
    public static final SearchPattern Empty = new SearchPattern();

    private final Object objectPattern;
    private final Container.Filter filter;
    private final boolean empty;

    public static SearchPattern of(Object stringPattern, Container.Filter filter) {
        return new SearchPattern(stringPattern, filter);
    }

    private SearchPattern() {
        this.filter = null; this.objectPattern = null; this.empty = true;
    }

    private SearchPattern(Object objectPattern, Container.Filter filter) {
        this.filter = filter;
        this.objectPattern = objectPattern;
        this.empty = false;
    }

    public Object getObjectPattern() {
        return objectPattern;
    }
    public Container.Filter getFilter() {
        return filter;
    }

    public boolean isEmpty() {
        return empty;
    }
}
