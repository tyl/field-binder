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
import com.vaadin.ui.*;
import org.tylproject.vaadin.addon.datanav.DataNavigation;
import org.tylproject.vaadin.addon.datanav.FindButtonBar;
import org.tylproject.vaadin.addon.datanav.events.ClearToFind;
import org.tylproject.vaadin.addon.datanav.events.OnFind;
import org.tylproject.vaadin.addon.fieldbinder.FieldBinder;

public class TableFindBehavior<T> implements FindListeners {

	private final Class<T> beanClass;
	private FindWindow window;
	private final FilterApplier filterApplier = new FilterApplier();


	public TableFindBehavior(Class<T> beanClass) {
		this.beanClass = beanClass;
	}
	@Deprecated
	public TableFindBehavior(Class<T> beanClass, Table table) {
		this(beanClass);
	}


	public void clearToFind(ClearToFind.Event event) {

		FieldBinder<T> fieldBinder = new FieldBinder<T>(beanClass);
//		this.window.fieldBinder.clear();
		this.window = new FindWindow(fieldBinder, event.getSource());
		this.window.center();

		UI.getCurrent().addWindow(window);
		event.getSource().setCurrentItemId(null);

		fieldBinder.unbindAll();
		fieldBinder.setReadOnly(false);
		event.getSource().setCurrentItemId(null);

		if (filterApplier.hasAppliedFilters()) {
			filterApplier.restorePatterns(fieldBinder.getPropertyIdToFieldBindings());
			filterApplier.clearPropertyIdToFilterPatterns();
		}


		fieldBinder.focus();
	}

	@Override
	public void onFind(OnFind.Event event) {
		if (window == null) {
			throw new IllegalStateException("Find Window is null: illegal state transition occurred");
		}

		FieldBinder<T> fieldBinder = window.getFieldBinder();

		filterApplier.applyFilters(fieldBinder.getPropertyIdToFieldBindings(), (Container.Filterable) event.getSource().getContainer());

		window.close();
		window = null;

		event.getSource().first();
	}


    class FindWindow extends Window {
    	
    	protected final FieldBinder<T> fieldBinder;

		public FindWindow(FieldBinder<T> fieldBinder, DataNavigation navigation) {
    		this.fieldBinder = fieldBinder;
    		
    		setClosable(false);
    		setModal(true);
    		setDraggable(false);
    		setResizable(false);
			VerticalLayout layout = new VerticalLayout();
			layout.setMargin(true);

			Button find = new FindButtonBar(navigation).getFindButton();

			layout.addComponents(fieldBinder.buildAll().toArray(new Component[0]));
			layout.addComponent(find);

			setContent(layout);

    	}

		@Override
		public void setParent(HasComponents parent) {
			super.setParent(parent);
		}

		public FieldBinder<T> getFieldBinder() {
			return fieldBinder;
		}
    }

}
