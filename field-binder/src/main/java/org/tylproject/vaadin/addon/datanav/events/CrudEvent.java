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

import org.tylproject.vaadin.addon.datanav.DataNavigation;

import java.util.EventObject;

/**
 * Created by evacchi on 19/11/14.
 */
public class CrudEvent extends EventObject {
    public CrudEvent(DataNavigation source) {
        super(source);
    }

    @Override
    public DataNavigation getSource() {
        return (DataNavigation) super.getSource();
    }
}
