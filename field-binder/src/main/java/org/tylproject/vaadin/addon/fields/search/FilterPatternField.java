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
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Button;
import org.tylproject.vaadin.addon.fieldbinder.behavior.DefaultFilterFactory;
import org.tylproject.vaadin.addon.fieldbinder.behavior.FilterFactory;
import org.tylproject.vaadin.addon.fields.CombinedField;

/**
 * A Field that returns a filter for a given text pattern; it also immediately applies the
 * filter to a Container instance, if it is given
 *
 */
public abstract class FilterPatternField<T,FT,F extends AbstractField<FT>> extends CombinedField<T, FT, F> {

    private static final FilterFactory filterFactory = new DefaultFilterFactory();

    private SearchPattern lastAppliedSearchPattern;
    private Container.Filterable targetContainer;
    private final Object targetPropertyId;
    private final Class<?> targetPropertyType;


    public FilterPatternField(final F backingField,    final Class<T> fieldType,
                              final Object propertyId, final Class<?> propertyType) {
        super(backingField, new Button(FontAwesome.TIMES_CIRCLE), fieldType);
        final Button clearBtn = getButton();

        this.targetPropertyId = propertyId;
        this.targetPropertyType = propertyType;

        backingField.setImmediate(true);

        clearBtn.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                clear();
            }
        });
    }


    /**
     *
     * @param propertyId the propertyId of the Filter
     * @param propertyType the type of the property in the Filter
     * @param targetContainer the container the Filter should be applied to
     */
    public FilterPatternField(final F backingField,    final Class<T> fieldType,
                              final Object propertyId, final Class<?> propertyType,
                              final Container.Filterable targetContainer) {
        this(backingField, fieldType, propertyId, propertyType);
        setTargetContainer(targetContainer);
    }

    /**
     * set a container instance onto which the filter should be applied
     */
    public final void setTargetContainer(Container.Filterable targetContainer) {
        this.targetContainer = targetContainer;
    }

    public Container.Filterable getTargetContainer() {
        return targetContainer;
    }


    public Class<?> getTargetPropertyType() {
        return targetPropertyType;
    }

    public Object getTargetPropertyId() {
        return targetPropertyId;
    }

    protected static FilterFactory getFilterFactory() {
        return filterFactory;
    }


    protected void setLastAppliedSearchPattern(SearchPattern searchPattern) {
        this.lastAppliedSearchPattern = searchPattern;
    }

    protected SearchPattern getLastAppliedSearchPattern() {
        return this.lastAppliedSearchPattern;
    }

    protected SearchPattern
            applyFilterPattern(Class<?> propertyType,
                                Object propertyId,
                                Object objectPattern,
                                Container.Filterable filterableContainer) {

        // remove last applied filter from the container
        SearchPattern lastPattern = getLastAppliedSearchPattern();
        if (lastPattern != null)
            filterableContainer.removeContainerFilter(lastPattern.getFilter());

        // if the objectPattern is non-empty
        if (objectPattern != null
            && (
              (!(objectPattern instanceof String))
              || !((String)objectPattern).isEmpty())
            ) {

            SearchPattern newPattern = getPattern(objectPattern);

            filterableContainer.addContainerFilter(newPattern.getFilter());
            setLastAppliedSearchPattern(newPattern);
        }

        return lastPattern;
    }

    public SearchPattern getPatternFromValue() {
        return getPattern(getValue());
    }

    private SearchPattern getPattern(Object objectPattern) {
        if (objectPattern == null) return SearchPattern.Empty;
        if (objectPattern instanceof String && ((String)objectPattern).isEmpty()) return SearchPattern.Empty;

        else return SearchPattern.of(objectPattern, filterFactory.createFilter(targetPropertyType, targetPropertyId, objectPattern));
    }


    /**
     * Sets the new value to field, and applies the filter on the target container (if any).
     *
     * If the given value is null, then it only clears the filter on the container.
     */
    @Override
    public void setValue(T newValue) throws ReadOnlyException {
        super.setValue(newValue);
        if (newValue != null) return;

        SearchPattern lastPattern = getLastAppliedSearchPattern();
        setLastAppliedSearchPattern(null);
        if (lastPattern == null) return;
        Container.Filterable c = getTargetContainer();
        if (c != null) c.removeContainerFilter(lastPattern.getFilter());

    }

    /**
     * equivalent to setValue(null)
     */
    public void clear() {
        this.setValue(null);
    }



}
