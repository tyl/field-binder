package org.tylproject.vaadin.addon.fields;

import com.vaadin.data.Property;
import com.vaadin.data.Validator;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.server.Resource;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.CustomField;

import java.util.Collection;

/**
 * Created by evacchi on 19/01/15.
 */
public abstract class FieldDecorator<T, FT, F extends AbstractField<FT>> extends CustomField<T> {

    private final F backingField;
    public FieldDecorator(F backingField) {
        super();
        this.backingField = backingField;
    }

    public F getBackingField() {
        return backingField;
    }


    @Override
    public abstract Class<? extends T> getType();

    @Override
    public abstract T getValue();

    @Override
    public abstract void setValue(T newValue) throws ReadOnlyException ;


    @Override
    public String getRequiredError() {
        return getBackingField().getRequiredError();
    }

    @Override
    public boolean isRequired() {
        return getBackingField().isRequired();
    }

    @Override
    public void setRequired(boolean required) {
        getBackingField().setRequired(required);
    }

    @Override
    public void setRequiredError(String requiredMessage) {
        getBackingField().setRequiredError(requiredMessage);
    }

    @Override
    public void addListener(Listener listener) {
        getBackingField().addListener(listener);
    }

    @Override
    public void addStyleName(String style) {
        getBackingField().addStyleName(style);
    }
//
//    @Override
//    public void attach() {
//        getBackingField().attach();
//    }

    @Override
    public String getCaption() {
        return getBackingField().getCaption();
    }

    @Override
    public String getDescription() {
        return getBackingField().getDescription();
    }

    @Override
    public Resource getIcon() {
        return getBackingField().getIcon();
    }
//
//    @Override
//    public String getId() {
//        return getBackingField().getId();
//    }
//
//    @Override
//    public Locale getLocale() {
//        return getBackingField().getLocale();
//    }
//
//    @Override
//    public HasComponents getParent() {
//        return getBackingField().getParent();
//    }

    @Override
    public String getPrimaryStyleName() {
        return getBackingField().getPrimaryStyleName();
    }

    @Override
    public String getStyleName() {
        return getBackingField().getStyleName();
    }
//
//    @Override
//    public UI getUI() {
//        return getBackingField().getUI();
//    }

    @Override
    public boolean isEnabled() {
        return getBackingField().isEnabled();
    }

    @Override
    public boolean isReadOnly() {
        return getBackingField().isReadOnly();
    }

    @Override
    public boolean isVisible() {
        return getBackingField().isVisible();
    }

    @Override
    public void removeListener(Listener listener) {
        getBackingField().removeListener(listener);
    }

    @Override
    public void removeStyleName(String style) {
        getBackingField().removeStyleName(style);
    }

    @Override
    public void setCaption(String caption) {
        getBackingField().setCaption(caption);
    }

    @Override
    public void setEnabled(boolean enabled) {
        getBackingField().setEnabled(enabled);
    }

    @Override
    public void setIcon(Resource icon) {
        getBackingField().setIcon(icon);
    }

    @Override
    public void setId(String id) {
        getBackingField().setId(id);
    }

//    @Override
//    public void setParent(HasComponents parent) {
//        getBackingField().setParent(parent);
//    }

    @Override
    public void setPrimaryStyleName(String style) {
        getBackingField().setPrimaryStyleName(style);
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        getBackingField().setReadOnly(readOnly);
    }

    @Override
    public void setStyleName(String style) {
        getBackingField().setStyleName(style);
    }

    @Override
    public void setVisible(boolean visible) {
        getBackingField().setVisible(visible);
    }

//    @Override
//    public void addAttachListener(AttachListener listener) {
//        getBackingField().addAttachListener(listener);
//    }
//
//    @Override
//    public void addDetachListener(DetachListener listener) {
//        getBackingField().addDetachListener(listener);
//    }
//
//    @Override
//    public void beforeClientResponse(boolean initial) {
//        getBackingField().beforeClientResponse(initial);
//    }
//
//    @Override
//    public void detach() {
//        getBackingField().detach();
//    }
//
//    @Override
//    public JSONObject encodeState() throws JSONException {
//        return getBackingField().encodeState();
//    }
//
//    @Override
//    public ErrorHandler getErrorHandler() {
//        return getBackingField().getErrorHandler();
//    }
//
//    @Override
//    public Collection<Extension> getExtensions() {
//        return getBackingField().getExtensions();
//    }
//
//    @Override
//    public ServerRpcManager<?> getRpcManager(String rpcInterfaceName) {
//        return getBackingField().getRpcManager(rpcInterfaceName);
//    }
//
//    @Override
//    public Class<? extends SharedState> getStateType() {
//        return getBackingField().getStateType();
//    }
//
//    @Override
//    public boolean handleConnectorRequest(VaadinRequest request, VaadinResponse
//            response, String path) throws IOException {
//        return getBackingField().handleConnectorRequest(request, response, path);
//    }
//
//    @Override
//    public boolean isAttached() {
//        return getBackingField().isAttached();
//    }
//
//    @Override
//    public boolean isConnectorEnabled() {
//        return getBackingField().isConnectorEnabled();
//    }
//
//    @Override
//    public void markAsDirty() {
//        getBackingField().markAsDirty();
//    }
//
//    @Override
//    public void markAsDirtyRecursive() {
//        getBackingField().markAsDirtyRecursive();
//    }
//
//    @Override
//    public void removeAttachListener(AttachListener listener) {
//        getBackingField().removeAttachListener(listener);
//    }
//
//    @Override
//    public void removeDetachListener(DetachListener listener) {
//        getBackingField().removeDetachListener(listener);
//    }
//
//    @Override
//    public void removeExtension(Extension extension) {
//        getBackingField().removeExtension(extension);
//    }
//
//    @Override
//    @Deprecated
//    public void requestRepaint() {
//        getBackingField().requestRepaint();
//    }
//
//    @Override
//    @Deprecated
//    public void requestRepaintAll() {
//        getBackingField().requestRepaintAll();
//    }
//
//    @Override
//    public List<ClientMethodInvocation> retrievePendingRpcCalls() {
//        return getBackingField().retrievePendingRpcCalls();
//    }
//
//    @Override
//    public void setErrorHandler(ErrorHandler errorHandler) {
//        getBackingField().setErrorHandler(errorHandler);
//    }
//
//    @Override
//    public String getConnectorId() {
//        return getBackingField().getConnectorId();
//    }
//
//    @Override
//    public float getHeight() {
//        return getBackingField().getHeight();
//    }
//
//    @Override
//    public Unit getHeightUnits() {
//        return getBackingField().getHeightUnits();
//    }
//
//    @Override
//    public float getWidth() {
//        return getBackingField().getWidth();
//    }
//
//    @Override
//    public Unit getWidthUnits() {
//        return getBackingField().getWidthUnits();
//    }
//
//    @Override
//    public void setHeight(float height, Unit unit) {
//        getBackingField().setHeight(height, unit);
//    }
//
//    @Override
//    public void setHeight(String height) {
//        getBackingField().setHeight(height);
//    }
//
//    @Override
//    public void setHeightUndefined() {
//        getBackingField().setHeightUndefined();
//    }
//
//    @Override
//    public void setSizeFull() {
//        getBackingField().setSizeFull();
//    }
//
//    @Override
//    public void setSizeUndefined() {
//        getBackingField().setSizeUndefined();
//    }
//
//    @Override
//    public void setWidth(float width, Unit unit) {
//        getBackingField().setWidth(width, unit);
//    }
//
//    @Override
//    public void setWidth(String width) {
//        getBackingField().setWidth(width);
//    }
//
//    @Override
//    public void setWidthUndefined() {
//        getBackingField().setWidthUndefined();
//    }

    @Override
    public boolean isInvalidCommitted() {
        return getBackingField().isInvalidCommitted();
    }

    @Override
    public void setInvalidCommitted(boolean isCommitted) {
        getBackingField().setInvalidCommitted(isCommitted);
    }

    @Override
    public void commit() throws SourceException, Validator.InvalidValueException {
        getBackingField().commit();
    }

    @Override
    public void discard() throws SourceException {
        getBackingField().discard();
    }

    @Override
    public boolean isBuffered() {
        return getBackingField().isBuffered();
    }

    @Override
    public boolean isModified() {
        return getBackingField().isModified();
    }

    @Override
    public void setBuffered(boolean buffered) {
        getBackingField().setBuffered(buffered);
    }

    @Override
    public void addValidator(Validator validator) {
        getBackingField().addValidator(validator);
    }

    @Override
    public Collection<Validator> getValidators() {
        return getBackingField().getValidators();
    }

    @Override
    public boolean isInvalidAllowed() {
        return getBackingField().isInvalidAllowed();
    }

    @Override
    public boolean isValid() {
        return getBackingField().isValid();
    }

    @Override
    public void removeAllValidators() {
        getBackingField().removeAllValidators();
    }

    @Override
    public void removeValidator(Validator validator) {
        getBackingField().removeValidator(validator);
    }

    @Override
    public void setInvalidAllowed(boolean invalidValueAllowed) throws UnsupportedOperationException {
        getBackingField().setInvalidAllowed(invalidValueAllowed);
    }

    @Override
    public void validate() throws Validator.InvalidValueException {
        getBackingField().validate();
    }

    @Override
    @Deprecated
    public void addListener(ValueChangeListener listener) {
        getBackingField().addListener(listener);
    }

    @Override
    public void addValueChangeListener(ValueChangeListener listener) {
        getBackingField().addValueChangeListener(listener);
    }

    @Override
    @Deprecated
    public void removeListener(ValueChangeListener listener) {
        getBackingField().removeListener(listener);
    }

    @Override
    public void removeValueChangeListener(ValueChangeListener listener) {
        getBackingField().removeValueChangeListener(listener);
    }

    @Override
    public void valueChange(Property.ValueChangeEvent event) {
        getBackingField().valueChange(event);
    }

    @Override
    public Property getPropertyDataSource() {
        return getBackingField().getPropertyDataSource();
    }

    @Override
    public void setPropertyDataSource(Property newDataSource) {
        getBackingField().setPropertyDataSource(newDataSource);
    }

    @Override
    public void focus() {
        getBackingField().focus();
    }

    @Override
    public int getTabIndex() {
        return getBackingField().getTabIndex();
    }

    @Override
    public void setTabIndex(int tabIndex) {
        getBackingField().setTabIndex(tabIndex);
    }

    @Override
    public void setImmediate(boolean immediate) {
        super.setImmediate(immediate);
        getBackingField().setImmediate(immediate);
    }
}
