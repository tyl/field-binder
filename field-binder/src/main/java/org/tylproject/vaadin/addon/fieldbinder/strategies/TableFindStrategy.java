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
//	private final DataNavigation navigation;
	private final Class<T> beanClass;
	private FindWindow window;

	private static final Logger logger = Logger.getAnonymousLogger();

	FilterFactory filterFactory = new DefaultFilterFactory();

	/**
	 * maps fieldId to filter
	 */
	Map<Object, Object> propertyIdToFilterPattern = new HashMap<Object, Object>();

	public Map<Object, Object> getPropertyIdToFilterPattern () {
		return propertyIdToFilterPattern;
	}


	private void clearPropertyIdToFilterPattern() {
		propertyIdToFilterPattern.clear();
	}


	private void restorePatterns(FieldBinder<?> fieldBinder,  Map<Object, Object> propertyIdToFilterPattern) {
		for (Map.Entry<Field<?>, Object> e : fieldBinder.getFieldToPropertyIdBindings().entrySet()) {

			Field field = e.getKey(); // raw type
			Object propertyId = e.getValue();
			Object pattern = propertyIdToFilterPattern.get(propertyId);

			field.setValue(pattern);

		}
	}

	public TableFindStrategy(Class<T> beanClass, Table table) {
		this.beanClass = beanClass;
		this.table = table;
//		this.navigation = navigation;
//		this.window = new FindWindow(new FieldBinder<T>(beanClass));
	}

	@Override
	public void clearToFind(ClearToFind.Event event) {
		this.window = new FindWindow(new FieldBinder<T>(beanClass), event.getSource());
		window.fieldBinder.clear();
		window.center();
		UI.getCurrent().addWindow(window);
		event.getSource().setCurrentItemId(null);


		if (!getPropertyIdToFilterPattern().isEmpty()) {
			restorePatterns(window.fieldBinder, getPropertyIdToFilterPattern());
			clearPropertyIdToFilterPattern();
		}


	}


	
	@Override
	public void onFind(OnFind.Event event) {
		if (window == null) throw new IllegalStateException("Find Window is null: illegal state transition occurred");

		applyFilters(window.getFieldBinder(), (Container.Filterable) table.getContainerDataSource());
		window.close();

		event.getSource().first();
	}
	
	

    private void applyFilters(FieldBinder<T> fieldBinder, Container.Filterable container) {
		clearPropertyIdToFilterPattern();

		container.removeAllContainerFilters();
        for (Map.Entry<Field<?>,Object> e : fieldBinder.getFieldToPropertyIdBindings().entrySet()) {
            Field<?> prop = e.getKey();
            Object propertyId = e.getValue();
            Object pattern = prop.getValue();
            Class<?> modelType = getModelType(prop);
            if (pattern != null) {

				propertyIdToFilterPattern.put(propertyId, pattern);

				Container.Filter f = filterFactory.createFilter(modelType,
						propertyId, pattern);

				if (f != null) {
					logger.info("Applying filter: " + f);
					container.addContainerFilter(f);
				}
			}
        }
    }

    private Class<?> getModelType(Field<?> prop) {
        if (prop instanceof AbstractField) {
            AbstractField<?> abstractField = (AbstractField<?>) prop;
            Converter<?, Object> converter = abstractField.getConverter();
            if (converter != null) {
                return converter.getModelType();
            }
        }

        // otherwise, fallback to the property type
        return prop.getType();

    }

    class FindWindow extends Window {
    	
    	protected final FieldBinder<T> fieldBinder;
//		FocusManager focusManager = new FocusManager();


		public FindWindow(FieldBinder<T> fieldBinder, DataNavigation navigation) {
    		this.fieldBinder = fieldBinder;
    		
    		setClosable(false);
    		setModal(true);
    		setDraggable(false);
    		setResizable(false);
			VerticalLayout layout = new VerticalLayout();
			layout.setMargin(true);

			Field<?>[] fields = {
					fieldBinder.build("city"),
					fieldBinder.build("state"),
					fieldBinder.build("street"),
					fieldBinder.build("zipCode")
			};


			Button find = new FindButtonBar(navigation).getFindButton();

			layout.addComponents(fields);
			layout.addComponent(find);

			setContent(layout);

//			focusManager.configure().constrainTab(fields);

//			addActionHandler(focusManager);
//			KeyBinder keyBinder = focusManager.getKeyBinder();
//			keyBinder.setNavigation(navigation);
//			addActionHandler(keyBinder);
    	}

		@Override
		public void setParent(HasComponents parent) {
			super.setParent(parent);
//			focusManager.focusCurrentGroup();
		}

		public FieldBinder<T> getFieldBinder() {
			return fieldBinder;
		}
    }

}
