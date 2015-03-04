package org.tylproject.demos.fieldbinder;

import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Label;
import org.tylproject.demos.fieldbinder.model.Person;
import org.tylproject.vaadin.addon.fields.zoom.GridZoomDialog;
import org.tylproject.vaadin.addon.fields.zoom.TableZoomDialog;
import org.tylproject.vaadin.addon.fields.zoom.TextZoomField;
import org.tylproject.vaadin.addon.fields.zoom.ZoomField;

import org.vaadin.spring.annotation.VaadinUIScope;
import org.vaadin.spring.navigator.annotation.VaadinView;
import org.vaadin.viritin.FilterableListContainer;
import org.vaadin.viritin.layouts.MFormLayout;

@VaadinView(name = "/zoom")
@VaadinUIScope
public class TutorialZoom extends MFormLayout implements View {

    private static final String TARGET_PROPERTY_ID = "firstName";

    {
        final Container.Indexed container = MyDataSourceGenerator.makeDummyDataset();

        final TextZoomField zoomField =
                new TextZoomField("Simple Zoom Field")
                    .withZoomDialog(new TableZoomDialog(TARGET_PROPERTY_ID, container));
        final Label zoomFieldSelection = new Label();
        zoomField.addValueChangeListener(new LabelUpdater(zoomFieldSelection));


        final TextZoomField zoomFieldNoNull =
                new TextZoomField("Zoom Field with Null Disabled")
                        .withNullSelectionDisabled()
                        .withZoomDialog(new TableZoomDialog(TARGET_PROPERTY_ID,
                        container));
        final Label zoomFieldNoNullSelection = new Label();

        zoomFieldNoNull.addValueChangeListener(new LabelUpdater(zoomFieldNoNullSelection));



        final TextZoomField zoomFieldModeProperty =
                new TextZoomField("Zoom Field with Property-Only")
                        .withMode(ZoomField.Mode.PropertyId)
                        .withZoomDialog(new TableZoomDialog(TARGET_PROPERTY_ID, container));

        final Label zoomFieldModePropertySelection = new Label();
        zoomFieldModeProperty.addValueChangeListener(new LabelUpdater(zoomFieldModePropertySelection));



        final TextZoomField drillDownField =
                new TextZoomField("DrillDown Only, with Grid")
                    .drillDownOnly()
                    .withMode(ZoomField.Mode.PropertyId)
                    .withZoomDialog(new GridZoomDialog(TARGET_PROPERTY_ID, container));
        drillDownField.setValue("Paul");

        addComponents(
                        zoomField, zoomFieldSelection,
                        zoomFieldNoNull, zoomFieldNoNullSelection,
                        zoomFieldModeProperty, zoomFieldModePropertySelection,
                        drillDownField
        );

        setMargin(true);
    }

    static class LabelUpdater implements Property.ValueChangeListener {
        final Label label;

        public LabelUpdater(Label label) {
            this.label = label;
        }

        @Override
        public void valueChange(Property.ValueChangeEvent event) {
            label.setValue(
                    "Selection internal value: " + event.getProperty().getValue());
        }

    };


    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }


}
