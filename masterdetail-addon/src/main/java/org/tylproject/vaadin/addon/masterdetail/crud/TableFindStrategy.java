package org.tylproject.vaadin.addon.masterdetail.crud;

import java.util.Map;

import com.vaadin.ui.*;
import org.tylproject.vaadin.addon.crudnav.CrudNavigation;
import org.tylproject.vaadin.addon.crudnav.FindButtonBar;
import org.tylproject.vaadin.addon.crudnav.KeyBinder;
import org.tylproject.vaadin.addon.crudnav.events.*;
import org.tylproject.vaadin.addon.fieldbinder.FieldBinder;

import com.vaadin.data.Container;
import com.vaadin.data.util.converter.Converter;
import org.tylproject.vaadin.addon.masterdetail.FocusManager;

public class TableFindStrategy<T> implements FindStrategy {
	
	private final Table table;
	private final CrudNavigation navigation;
	private final Class<T> beanClass;
	private final TableFindStrategy<T>.FindWindow window;

	FilterFactory filterFactory = new DefaultFilterFactory();
	
	public TableFindStrategy(Class<T> beanClass, CrudNavigation navigation, Table table) {
		this.beanClass = beanClass;
		this.table = table;
		this.navigation = navigation;
		this.window = new FindWindow(new FieldBinder<T>(beanClass));
	}

	@Override
	public void clearToFind(ClearToFind.Event event) {
		window.fieldBinder.clear();
		window.center();
		UI.getCurrent().addWindow(window);
		navigation.setCurrentItemId(null);
		
	}
	
	@Override
	public void onFind(OnFind.Event event) {
		applyFilters(window.getFieldBinder(), (Container.Filterable) table.getContainerDataSource());
		window.close();
		navigation.first();
	}
	
	

    private void applyFilters(FieldBinder<T> fieldBinder, Container.Filterable container) {
        container.removeAllContainerFilters();
        for (Map.Entry<Field<?>,Object> e : fieldBinder.getFieldToPropertyIdBindings().entrySet()) {
            Field<?> prop = e.getKey();
            Object propertyId = e.getValue();
            Object value = prop.getValue();
            Class<?> modelType = getModelType(prop);
            if (value != null) {
                container.addContainerFilter(filterFactory.createFilter(modelType,
                        propertyId, value));
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
		FocusManager focusManager = new FocusManager();


		public FindWindow(FieldBinder<T> fieldBinder) {
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

			focusManager.configure().constrainTab(fields);

			addActionHandler(focusManager);
			KeyBinder keyBinder = focusManager.getKeyBinder();
			keyBinder.setNavigation(navigation);
			addActionHandler(keyBinder);
    	}

		@Override
		public void setParent(HasComponents parent) {
			super.setParent(parent);
			focusManager.focusCurrentGroup();
		}

		public FieldBinder<T> getFieldBinder() {
			return fieldBinder;
		}
    }

}
