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

package org.tylproject.vaadin.addon.datanav.events;

import com.vaadin.util.ReflectTools;
import org.tylproject.vaadin.addon.datanav.DataNavigation;

import java.lang.reflect.Method;
import java.util.EventObject;

/**
 * Created by evacchi on 19/11/14.
 */
public class NavigationEnabled {

    public static class Event extends EventObject {
        private final boolean navigationEnabled;

        public Event(DataNavigation source, boolean navigationEnabled) {
            super(source);
            this.navigationEnabled = navigationEnabled;
        }

        public boolean isNavigationEnabled() {
            return navigationEnabled;
        }

        @Override
        public DataNavigation getSource() {
            return (DataNavigation) super.getSource();
        }

    }
    public static interface Listener extends java.io.Serializable {
        public static final Method METHOD =
                ReflectTools.findMethod(Listener.class, "navigationEnabled", Event.class);
        public void navigationEnabled(Event event);
    }

    public static interface Notifier extends java.io.Serializable {
        void addNavigationEnabledListener(Listener listener);
        void removeNavigationEnabledListener(Listener listener);
    }
}
