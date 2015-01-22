package org.tylproject.vaadin.addon;

import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.tylproject.vaadin.addon.fieldbinder.FieldBinder;
import org.tylproject.vaadin.addon.fields.FilterExpressionField;

/**
 * Created by evacchi on 22/01/15.
 */
public class SearchWindow extends Window {
    final SearchForm searchForm;

    final VerticalLayout rootLayout = new VerticalLayout();

    final Label spacer = new Label();
    final Button btnApply = new Button("Apply");
    final Button btnClear = new Button("Clear");
    final Button btnCancel = new Button("Cancel");

    final HorizontalLayout buttonLayout = new HorizontalLayout(spacer, btnApply, btnClear, btnCancel);

    public SearchWindow(final SearchForm searchForm) {
        this.searchForm = searchForm;
        searchForm.setSizeUndefined();

        this.setContent(rootLayout);

        setClosable(false);
        setModal(true);
        setDraggable(false);
        setResizable(false);
        rootLayout.setMargin(true);

        btnApply.addStyleName(ValoTheme.BUTTON_PRIMARY);
        buttonLayout.addStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);
        buttonLayout.setSizeFull();
        buttonLayout.setExpandRatio(spacer, 1f);

        rootLayout.addComponents(searchForm, buttonLayout);

        btnApply.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                close();
            }
        });

        btnClear.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                searchForm.clear();
            }});

        btnCancel.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                close();
            }
        });
    }

    public SearchWindow(FieldBinder<?> fieldBinder) {
        this(new SearchForm(fieldBinder));
    }

    public SearchForm getSearchForm() {
        return searchForm;
    }
}
