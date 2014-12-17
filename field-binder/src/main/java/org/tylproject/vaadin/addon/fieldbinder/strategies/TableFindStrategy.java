package org.tylproject.vaadin.addon.fieldbinder.strategies;

import com.vaadin.data.Container;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.ui.*;
import org.tylproject.vaadin.addon.datanav.DataNavigation;
import org.tylproject.vaadin.addon.datanav.FindButtonBar;
import org.tylproject.vaadin.addon.datanav.events.ClearToFind;
import org.tylproject.vaadin.addon.datanav.events.OnFind;
import org.tylproject.vaadin.addon.fieldbinder.FieldBinder;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class TableFindStrategy<T> implements FindStrategy {
	
	private final Table table;
	private final Class<T> beanClass;
	private FindWindow window;
	private final FilterApplier filterApplier = new FilterApplier();


	public TableFindStrategy(Class<T> beanClass, Table table) {
		this.beanClass = beanClass;
		this.table = table;
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
