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

    private static final FilterFactory filterFactory = new DefaultFilterFactory();

    private Container.Filter lastAppliedFilter;
    private Container.Filterable targetContainer;
    private final Object targetPropertyId;
    private final Class<?> targetPropertyType;


    public FilterExpressionField(final Object propertyId, final Class<?> propertyType) {
        super(new TextField(), new Button(FontAwesome.TIMES_CIRCLE), String.class);
        final Button clearBtn = getButton();
        final TextField textField = getBackingField();

        this.targetPropertyId = propertyId;
        this.targetPropertyType = propertyType;

        textField.setNullRepresentation("");
        textField.setImmediate(true);

        clearBtn.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                clear();
            }
        });
    }


    public FilterExpressionField(final Object propertyId, final Class<?> propertyType,
                                 final Container.Filterable targetContainer) {
        this(propertyId, propertyType);
        setTargetContainer(targetContainer);
    }

    public void setTargetContainer(Container.Filterable targetContainer) {
        if (!getBackingField().getListeners(
                FieldEvents.TextChangeEvent.class).contains(textChangeListener)) {
            getBackingField().addTextChangeListener(textChangeListener);
        }
        this.targetContainer = targetContainer;
    }

    public Container.Filterable getTargetContainer() {
        return targetContainer;
    }


    public void setLastAppliedFilter(Container.Filter filter) {
        this.lastAppliedFilter = filter;
    }

    public Container.Filter getFilterFromValue() {
        return filterFactory.createFilter(targetPropertyType, targetPropertyId, getValue());
    }


    protected Container.Filter getLastAppliedFilter() {
        return this.lastAppliedFilter;
    }

    protected Container.Filter
            applyFilterPattern(Class<?> propertyType,
                                Object propertyId,
                                String pattern,
                                Container.Filterable filterableContainer) {

        Container.Filter oldFilter = getLastAppliedFilter();
        filterableContainer.removeContainerFilter(oldFilter);

        if (pattern != null && !pattern.isEmpty()) {
            Container.Filter newFilter = getFilterFromValue();
            setLastAppliedFilter(newFilter);
            filterableContainer.addContainerFilter(newFilter);
        }

        return oldFilter;
    }

    public void clear() {
        setValue(null);
        Container.Filter oldFilter = getLastAppliedFilter();
        setLastAppliedFilter(null);
        Container.Filterable c = getTargetContainer();
        if (c != null) c.removeContainerFilter(oldFilter);
    }

    private final FieldEvents.TextChangeListener textChangeListener = new FieldEvents.TextChangeListener() {
        @Override
        public void textChange(FieldEvents.TextChangeEvent event) {
            applyFilterPattern(
                    targetPropertyType,
                    targetPropertyId,
                    event.getText(),
                    getTargetContainer());
        }
    };


}
