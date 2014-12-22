/*
 * Copyright (c) 2014 - Tyl Consulting s.a.s.
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

package org.tylproject.vaadin.addon.fieldbinder.behavior;

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
