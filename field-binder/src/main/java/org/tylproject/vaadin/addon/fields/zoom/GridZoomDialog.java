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

package org.tylproject.vaadin.addon.fields.zoom;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.ui.*;
import org.tylproject.vaadin.addon.fields.FilterableGrid;

/**
 * Created by evacchi on 20/01/15.
 */
public class GridZoomDialog extends AbstractZoomDialog {

    private final Grid grid;

    public GridZoomDialog(Grid grid) {
        this.grid = grid;
        setupDialog(grid);
    }

    public GridZoomDialog(Grid grid, Object propertyId, String caption) {
        this(grid, propertyId);
        this.setCaption(caption);
    }

    public GridZoomDialog(Grid grid, Object propertyId) {
        this(grid);
        withNestedPropertyId(propertyId);
        setCaption(DefaultFieldFactory.createCaptionByPropertyId(propertyId));
    }

    public GridZoomDialog(Object propertyId, Container.Indexed container) {
        withNestedPropertyId(propertyId);

        FilterableGrid grid = new FilterableGrid(container);

        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
        grid.setSizeFull();
        grid.setWidth("100%");
        grid.setHeight("100%");
        grid.setVisibileColumns("firstName", "lastName", "birthDate");

        this.grid = grid;

        setupDialog(grid);

    }

    private void setupDialog(Grid grid) {
        if (!(grid.getSelectionModel() instanceof Grid.SingleSelectionModel)) {
            throw new AssertionError(
                    "Selection mode must be "+Grid.SelectionMode.SINGLE.getClass()+", "+
                            grid.getSelectionModel().getClass() +" was given");
        }

        addComponent(grid);
        setExpandRatio(getGrid(), 1f);
        setSizeFull();
    }


    public GridZoomDialog withNestedPropertyId(Object propertyId) {
        super.withNestedPropertyId(propertyId);
        return this;
    }


    @Override
    public Container getContainer() {
        return grid.getContainerDataSource();
    }


    public final Grid getGrid() {
        return grid;
    }


    @Override
    public void show(Object value) {
        if (isReadOnly()) {
            grid.setSelectionMode(Grid.SelectionMode.NONE);
        } else {
            grid.setSelectionMode(Grid.SelectionMode.SINGLE);
        }
    }

    public Object getSelectedItemId() {
        return getGrid().getSelectedRow();
    }
}
