package org.tylproject.vaadin.addon.fields.zoom;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;
import org.tylproject.vaadin.addon.utils.BeanExtractor;
import org.tylproject.vaadin.addon.utils.DefaultBeanExtractor;

/**
 * Base class for ZoomDialogs.
 *
 * The selected "value" is extracted from an Item using a
 * {@link org.tylproject.vaadin.addon.utils.BeanExtractor}.
 *
 *
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
    public Object getContainerPropertyId() {
        return propertyId;
    }

    @Override
    public Component getDialogContents() {
        return this;
    }

    @Override
    public AbstractZoomDialog withContainerPropertyId(Object propertyId, Class<?> propertyType) {
        this.propertyId = propertyId;
        this.propertyType = propertyType;
        return this;
    }

    public Class<?> getContainerPropertyType() {
        return propertyType;
    }

    @Override
    public void dismiss() {}


    public Item getSelectedItem() {
        return getContainer().getItem(getSelectedItemId());
    }
    @Override
    public Property<?> getContainerProperty() {
        Item item = getSelectedItem();
        return item.getItemProperty(getContainerPropertyId());
    }

    @Override
    public Object getSelectedBean() throws UnsupportedOperationException {
        return beanExtractor.extract(getSelectedItemId(), getSelectedItem());
    }

    @Override
    public Object getSelectedValue() {
        Item item = getSelectedItem();
        if (item == null) return null;
        return item.getItemProperty(getContainerPropertyId()).getValue();
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
