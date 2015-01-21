package org.tylproject.vaadin.addon.fields;

import com.vaadin.data.Container;
import com.vaadin.event.FieldEvents;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.TextField;
import org.tylproject.vaadin.addon.fieldbinder.behavior.DefaultFilterFactory;
import org.tylproject.vaadin.addon.fieldbinder.behavior.FilterFactory;

/**
 * Created by evacchi on 21/01/15.
 */
public class FilterExpressionField extends CombinedField<String, String, TextField> {

    private Container.Filter lastAppliedFilter;
    private final Container.Filterable targetContainer;
    private static final FilterFactory filterFactory = new DefaultFilterFactory();



    public FilterExpressionField(final Object propertyId, final Class<?> propertyType, final Container.Filterable targetContainer) {
        super(new TextField(), new Button(FontAwesome.TIMES_CIRCLE), String.class);
        this.targetContainer = targetContainer;
        final Button clearBtn = getButton();
        final TextField textField = getBackingField();


        textField.setNullRepresentation("");
        textField.setImmediate(true);
        textField.addTextChangeListener(new FieldEvents.TextChangeListener() {
            @Override
            public void textChange(FieldEvents.TextChangeEvent event) {
                applyFilterPattern(propertyType, propertyId, event.getText(), targetContainer);
            }
        });

        clearBtn.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                clear();
            }
        });
    }

    public void setLastAppliedFilter(Container.Filter filter) {
        this.lastAppliedFilter = filter;
    }


    public Container.Filter getLastAppliedFilter() {
        return this.lastAppliedFilter;
    }

    public Container.Filter
            applyFilterPattern(Class<?> propertyType,
                                Object propertyId,
                                String pattern,
                                Container.Filterable filterableContainer) {

        Container.Filter oldFilter = getLastAppliedFilter();
        filterableContainer.removeContainerFilter(oldFilter);

        if (pattern != null && !pattern.isEmpty()) {
            Container.Filter newFilter = filterFactory.createFilter(propertyType, propertyId, pattern);
            setLastAppliedFilter(newFilter);
            filterableContainer.addContainerFilter(newFilter);
        }

        return oldFilter;
    }

    public void clear() {
        setValue(null);
        Container.Filter oldFilter = getLastAppliedFilter();
        setLastAppliedFilter(null);
        targetContainer.removeContainerFilter(oldFilter);

    }


}
