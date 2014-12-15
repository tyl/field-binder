package org.tylproject.vaadin.addon.datanav;

import com.vaadin.data.Container;
import com.vaadin.ui.Button;
import com.vaadin.ui.Layout;
import org.tylproject.vaadin.addon.datanav.events.NavigationEnabled;

import javax.annotation.Nonnull;

/**
 * Created by evacchi on 04/12/14.
 */
public class NavButtonBar extends AbstractButtonBar {


    private final Button btnFirst = button("first");
    private final Button btnPrev  = button("prev");
    private final Button btnNext  = button("next");
    private final Button btnLast  = button("last");

    private final Button[] navButtons = {
            btnFirst,
            btnPrev,
            btnNext,
            btnLast
    };

    public NavButtonBar(final DataNavigation nav) {
        super(nav);
        Layout buttonLayout = getLayout();

        buttonLayout.addComponent(btnFirst);
        buttonLayout.addComponent(btnPrev);
        buttonLayout.addComponent(btnNext);
        buttonLayout.addComponent(btnLast);




        btnFirst.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                nav().first();
            }
        });

        btnNext.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                nav().next();
            }
        });

        btnPrev.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                nav().prev();
            }
        });

        btnLast.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                nav().last();
            }
        });

        setNavigation(nav);
    }

    @Override
    protected void attachNavigation(@Nonnull DataNavigation nav) {
        super.attachNavigation(nav);
        nav.addNavigationEnabledListener(buttonEnabler);
    }

    @Override
    protected void detachNavigation(@Nonnull DataNavigation nav) {
        nav.removeNavigationEnabledListener(buttonEnabler);
        super.detachNavigation(nav);
    }

    NavigationEnabled.Listener buttonEnabler = new NavigationEnabled.Listener() {
        @Override
        public void navigationEnabled(NavigationEnabled.Event event) {
            updateButtonStatus();
        }
    };

    @Override
    protected  void updateButtonStatus() {
        Container.Ordered ctr = nav().getContainer();
        if (ctr == null || !nav().isNavigationEnabled()) {
            disable(navButtons);
            return;
        } else {
            enable(navButtons);
        }

        Object currentId = nav().getCurrentItemId();
        if (currentId == null) {
            disable(navButtons);
            return;
        }

        boolean hasNext = false;
        boolean hasPrev = false;

        if(currentId != null){
            hasNext = null != ctr.nextItemId(currentId);
            hasPrev = null != ctr.prevItemId(currentId);
        }

        btnNext.setEnabled(hasNext);
        btnPrev.setEnabled(hasPrev);
        btnFirst.setEnabled(hasPrev);
        btnLast.setEnabled(hasNext);
    }
}
