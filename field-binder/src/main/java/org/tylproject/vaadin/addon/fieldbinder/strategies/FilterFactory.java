package org.tylproject.vaadin.addon.fieldbinder.strategies;

import com.vaadin.data.Container;

/**
 * Created by evacchi on 05/12/14.
 */
public interface FilterFactory {

    /**
     *
     * Returns a {@link com.vaadin.data.Container.Filter} instance that suits the given parameters
     *
     * @param targetType type of the value that we are filtering
     * @param targetPropertyId name of the target property on which the filter will be applied
     * @param pattern pattern from which the filter is generated
     * @return the requested filter instance
     * @throws com.vaadin.data.Validator.InvalidValueException
     *              when the target type and the pattern cannot identify a valid filter
     */
    Container.Filter createFilter(Class<?> targetType, Object targetPropertyId, Object
            pattern);
}
