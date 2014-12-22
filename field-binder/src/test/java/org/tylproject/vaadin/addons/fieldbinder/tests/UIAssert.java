package org.tylproject.vaadin.addons.fieldbinder.tests;

import com.vaadin.ui.Button;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by evacchi on 19/12/14.
 */
public class UIAssert {

    public static void assertButtonsEnabled(Button... buttons) {
        List<Button> disabledButtons = new ArrayList<Button>();
        for (Button b: buttons) {
            if (!b.isEnabled()) {
                disabledButtons.add(b);
            }
        }
        if (!disabledButtons.isEmpty()) {
            throw new AssertionError("Buttons were disabled: " + printButtons(disabledButtons));
        }
    }

    public static void assertButtonsDisabled(Button... buttons) {
        List<Button> enabledButtons = new ArrayList<Button>();
        for (Button b: buttons) {
            if (b.isEnabled()) {
                enabledButtons.add(b);
            }
        }
        if (!enabledButtons.isEmpty()) {
            throw new AssertionError("Buttons were enabled: " + printButtons(enabledButtons));
        }
    }

    public static String printButtons(Collection<Button> buttons) {
        StringBuffer sb = new StringBuffer("[ ");
        for (Button b: buttons) {
            sb.append(b.getCaption());
            sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }

}
