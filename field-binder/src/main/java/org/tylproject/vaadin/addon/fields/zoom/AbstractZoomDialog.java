package org.tylproject.vaadin.addon.fields.zoom;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;
import org.tylproject.vaadin.addon.utils.BeanExtractor;
import org.tylproject.vaadin.addon.utils.DefaultBeanExtractor;

/**
 * Created by evacchi on 10/02/15.
 */
public abstract class AbstractZoomDialog extends VerticalLayout implements ZoomDialog {

    private Object propertyId;
    private BeanExtractor beanExtractor = new DefaultBeanExtractor();
    private boolean nullSelectionAllowed;
    private Class<?> propertyType;

    public void setBeanExtractor(BeanExtractor beanExtractor) {
        this.beanExtractor = beanExtractor;
    }

    public BeanExtractor getBeanExtractor() {
        return beanExtractor;
    }

    @Override
    public Object getNestedPropertyId() {
        return propertyId;
    }

    @Override
    public Component getDialogContents() {
        return this;
    }

    @Override
    public AbstractZoomDialog withNestedPropertyId(Object propertyId, Class<?> propertyType) {
        this.propertyId = propertyId;
        this.propertyType = propertyType;
        return this;
    }

    public Class<?> getNestedPropertyType() {
        return propertyType;
    }

    @Override
    public void dismiss() {}

    @Override
    public boolean hasNestedPropertyId() {
        return getNestedPropertyId() != null;
    }

    public Item getSelectedItem() {
        return getContainer().getItem(getSelectedItemId());
    }
    @Override
    public Property<?> getNestedProperty() {
        Item item = getSelectedItem();
        return item.getItemProperty(getNestedPropertyId());
    }

    @Override
    public Object getSelectedBean() throws UnsupportedOperationException {
        return beanExtractor.extract(getSelectedItemId(), getSelectedItem());
    }

    @Override
    public Object getSelectedValue() {
        Item item = getSelectedItem();
        if (item == null) return null;
        return item.getItemProperty(getNestedPropertyId()).getValue();
    }

    @Override
    public void setNullSelectionAllowed(boolean allowed) {
        this.nullSelectionAllowed = allowed;
    }

    @Override
    public boolean isNullSelectionAllowed() {
        return nullSelectionAllowed;
    }
}
