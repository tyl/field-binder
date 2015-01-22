package org.tylproject.vaadin.addon.fields;

import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.event.FieldEvents;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.TextField;
import org.tylproject.vaadin.addon.fieldbinder.behavior.DefaultFilterFactory;
import org.tylproject.vaadin.addon.fieldbinder.behavior.FilterFactory;

/**
 * A Field that returns a filter for a given text pattern; it also immediately applies the
 * filter to a Container instance, if it is given
 *
 */
public class FilterExpressionField extends CombinedField<String, String, TextField> {

    private static final FilterFactory filterFactory = new DefaultFilterFactory();

    private Container.Filter lastAppliedFilter;
    private Container.Filterable targetContainer;
    private final Object targetPropertyId;
    private final Class<?> targetPropertyType;


    /**
     *
     * @param propertyId the propertyId of the Filter
     * @param propertyType the type of the property in the Filter
     */
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


    /**
     *
     * @param propertyId the propertyId of the Filter
     * @param propertyType the type of the property in the Filter
     * @param targetContainer the container the Filter should be applied to
     */
    public FilterExpressionField(final Object propertyId, final Class<?> propertyType,
                                 final Container.Filterable targetContainer) {
        this(propertyId, propertyType);
        setTargetContainer(targetContainer);
    }

    /**
     * set a container instance onto which the filter should be applied
     */
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

    protected void setLastAppliedFilter(Container.Filter filter) {
        this.lastAppliedFilter = filter;
    }

    /**
     * Get the filter for the current value of the field
     */
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

        // remove last applied filter from the container
        Container.Filter oldFilter = getLastAppliedFilter();
        filterableContainer.removeContainerFilter(oldFilter);

        // if the pattern is non-empty
        if (pattern != null && !pattern.isEmpty()) {
            Container.Filter newFilter = this.getFilterFromValue();
            filterableContainer.addContainerFilter(newFilter);
            setLastAppliedFilter(newFilter);
        }

        return oldFilter;
    }


    /**
     * Sets the new value to field, and applies the filter on the target container (if any).
     *
     * If the given value is null, then it only clears the filter on the container.
     */
    @Override
    public void setValue(String newValue) throws ReadOnlyException {
        super.setValue(newValue);
        if (newValue != null) return;

        Container.Filter oldFilter = getLastAppliedFilter();
        setLastAppliedFilter(null);
        Container.Filterable c = getTargetContainer();
        if (c != null) c.removeContainerFilter(oldFilter);

    }

    /**
     * equivalent to setValue(null)
     */
    public void clear() {
        this.setValue(null);
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
