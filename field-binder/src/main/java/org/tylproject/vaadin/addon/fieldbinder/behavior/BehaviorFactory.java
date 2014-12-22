package org.tylproject.vaadin.addon.fieldbinder.behavior;

import com.vaadin.data.Container;

/**
 * Created by evacchi on 15/12/14.
 */
public interface BehaviorFactory<T> {

    <T extends Behavior> T forContainer(Container container);
}
