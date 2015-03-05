/*
 * Copyright (c) 2015 - Tyl Consulting s.a.s.
 *
 *   Authors: Edoardo Vacchi
 *   Contributors: Marco Pancotti, Daniele Zonca
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.tylproject.vaadin.addon.fields.search;

import com.vaadin.data.Container;

/**
 * An immutable pair of (Object, Container.Filter)
 */
public class SearchPattern {
    public static final SearchPattern Empty = new SearchPattern() {
        @Override
        public boolean isEmpty() {
            return true;
        }
    };

    /**
     * an object representing a search pattern (usually a string value)
     */
    private final Object objectPattern;
    /**
     * the filter that corresponds to the objectPattern
     */
    private final Container.Filter filter;

    public static SearchPattern of(Object stringPattern, Container.Filter filter) {
        return new SearchPattern(stringPattern, filter);
    }

    private SearchPattern() {
        this.filter = null; this.objectPattern = null;
    }

    private SearchPattern(Object objectPattern, Container.Filter filter) {
        this.filter = filter;
        this.objectPattern = objectPattern;
    }

    public Object getObjectPattern() {
        return objectPattern;
    }
    public Container.Filter getFilter() {
        return filter;
    }

    public boolean isEmpty() {
        return false;
    }
}
