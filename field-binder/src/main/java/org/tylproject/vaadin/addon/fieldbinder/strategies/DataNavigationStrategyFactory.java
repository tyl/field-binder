package org.tylproject.vaadin.addon.fieldbinder.strategies;

import com.vaadin.data.Container;
import org.tylproject.vaadin.addon.fieldbinder.FieldBinder;
import org.tylproject.vaadin.addon.fieldbinder.strategies.DataNavigationStrategy;

/**
 * Created by evacchi on 15/12/14.
 */
public interface DataNavigationStrategyFactory<T> {

    <T extends DataNavigationStrategy> T forContainer(Container container);
}
