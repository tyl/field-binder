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

package org.tylproject.vaadin.addon.fieldbinder.behavior;

import com.vaadin.data.Container;

import javax.annotation.Nonnull;

/**
 * Creates a suitable {@link org.tylproject.vaadin.addon.fieldbinder.behavior.Behavior} object
 * for the given container class.
 *
 * Currently two default implementations are available:
 *
 *  <dl>
 *      <dt>{@link org.tylproject.vaadin.addon.fieldbinder.behavior.DefaultBehaviorFactory}</dt>
 *      <dd>When a FieldBinder is being used</dd>
 *
 *      <dt>{@link org.tylproject.vaadin.addon.fieldbinder.behavior.DefaultTableBehaviorFactory}</dt>
 *      <dd>When a Table-like field is being used</dd>
 *  </dl>
 *
 * Default implementations check the given containerClass <b>by name</b> to avoid
 * a NoClassDefFoundError if any of the supported Container implementations
 * is an unsatisfied dependency.
 *
 * Pre-defined Behavior implementations are available for JPAContainer, MongoContainer
 * and Viritin's ListContainer. If any of these are not available on the classpath
 * the Factory should not throw an error.
 *
 * Caveat: because class names are checked by string equality, third-party Container
 * subclasses may not work with the pre-defined Factories.
 *
 */
public interface BehaviorFactory<T> {
    Behavior forContainerType(@Nonnull Class<? extends Container> containerClass);
}
