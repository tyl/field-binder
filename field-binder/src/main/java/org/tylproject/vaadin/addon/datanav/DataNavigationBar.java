package org.tylproject.vaadin.addon.datanav;

import com.vaadin.ui.Layout;

import javax.annotation.Nonnull;
import java.io.Serializable;

/**
 * Created by evacchi on 04/12/14.
 */
public interface DataNavigationBar extends Serializable {
    Layout getLayout();

    void setNavigation(@Nonnull DataNavigation nav);
    DataNavigation getNavigation();
}
