package org.tylproject.vaadin.addon.fields.search;

import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.tylproject.vaadin.addon.datanav.DataNavigation;
import org.tylproject.vaadin.addon.fieldbinder.FieldBinder;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A window that wraps a SearchForm
 */
public class SearchWindow extends Window implements SearchDialog {
    final SearchForm searchForm;
    final Map<Object, SearchPattern> propertyIdToPatterns = new LinkedHashMap<>();


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
                apply();
            }
        });

        btnClear.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                clear();
            }});

        btnCancel.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                cancel();
            }
        });
    }

    public SearchWindow(FieldBinder<?> fieldBinder) {
        this(new SearchForm(fieldBinder));
        callFindOnClose(fieldBinder.getNavigation());
    }

    /**
     * Set to automatically invoke navigation.find() when the apply() button is clicked
     */
    public SearchWindow callFindOnClose(final DataNavigation navigation) {
        this.addCloseListener(new SearchWindow.CloseListener() {
            @Override
            public void windowClose(Window.CloseEvent e) {
                navigation.find();
            }
        });
        return this;
    }

    public SearchForm getSearchForm() {
        return searchForm;
    }

    @Override
    public void show() {
        restorePatternsIntoFields();
        UI.getCurrent().addWindow(this);
    }

    @Override
    public void cancel() {
        restorePatternsIntoFields();
        close();
    }

    private void restorePatternsIntoFields() {
        for (Map.Entry<Object, FilterPatternField> e:
                searchForm.getSearchFieldManager().getPropertyIdToFilterExpressionField().entrySet()) {
            final SearchPattern searchPattern = propertyIdToPatterns.get(e.getKey());
            if (searchPattern == null) {
                e.getValue().setValue(null);
            } else {
                e.getValue().setValue(searchPattern.getObjectPattern());

            }
        }
    }

    @Override
    public void apply() {
        backupPatternsFromFields();
        close();
    }
    private void backupPatternsFromFields() {
        propertyIdToPatterns.clear();
        for (Map.Entry<Object, FilterPatternField> e:
                searchForm.getSearchFieldManager().getPropertyIdToFilterExpressionField().entrySet()) {
            propertyIdToPatterns.put(e.getKey(), e.getValue().getPatternFromValue());
        }
    }

    @Override
    public void clear() {
        getSearchForm().getSearchFieldManager().clear();
    }



    @Override
    public Collection<SearchPattern> getSearchPatterns() {
        return Collections.unmodifiableCollection(getSearchForm().getPatternsFromValues()
        .values());
    }


    private static final Method SEARCH_WINDOW_APPLY_METHOD;
    static {
        try {
            SEARCH_WINDOW_APPLY_METHOD = ApplyListener.class.getDeclaredMethod(
                    "searchWindowApply", new Class[] { ApplyEvent.class });
        } catch (final java.lang.NoSuchMethodException e) {
            // This should never happen
            throw new java.lang.RuntimeException(
                    "Internal error, window close method not found");
        }
    }

    protected void fireApply() {
        fireEvent(new SearchWindow.ApplyEvent(this));
    }


    public static class ApplyEvent extends Component.Event {

        /**
         *
         * @param source
         */
        public ApplyEvent(Component source) {
            super(source);
        }

        /**
         * Gets the Window.
         *
         * @return the window.
         */
        public SearchWindow getSearchWindow() {
            return (SearchWindow) getSource();
        }
    }

    /**
     *
     */
    public interface ApplyListener extends Serializable {
        /**
         *
         */
        public void searchWindowApply(ApplyEvent e);
    }

    /**
     * Adds a CloseListener to the window.
     */
    public void addApplyListener(ApplyListener listener) {
        addListener(ApplyEvent.class, listener, SEARCH_WINDOW_APPLY_METHOD);
    }


    /**
     * Removes the CloseListener from the window.
     *
     * <p>
     * For more information on CloseListeners see {@link CloseListener}.
     * </p>
     *
     * @param listener
     *            the CloseListener to remove.
     */
    public void removeApplyListener(ApplyListener listener) {
        removeListener(ApplyEvent.class, listener, SEARCH_WINDOW_APPLY_METHOD);
    }

}
