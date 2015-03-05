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

import java.util.Collection;

/**
 * Interface for an abstract Search Dialog
 */
public interface SearchDialog {
    /**
     * Shows the dialog
     */
    void show();

    /**
     * Closes the dialog and <em>signals</em> user's intention to actually
     * apply the filters.
     *
     * In practice, this is the method that would get called when users click an "apply" button.
     * Implementation is NOT required to actuallt apply the filters immediately
     */
    void apply();

    /**
     * clear the filters; does not dismiss the dialog
     */
    void clear();

    /**
     * dismiss the dialog without applying new filters;
     * restores the previous search criteria in the dialog
     */
    void cancel();

    /**
     * return the search patterns that the user have described by interacting with the dialog
     */
    Collection<SearchPattern> getSearchPatterns();

}
