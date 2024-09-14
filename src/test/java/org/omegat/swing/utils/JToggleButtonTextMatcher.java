package org.omegat.swing.utils;

import javax.swing.*;
import org.assertj.swing.core.GenericTypeMatcher;

public class JToggleButtonTextMatcher extends GenericTypeMatcher<JToggleButton> {
    private final String text;

    public JToggleButtonTextMatcher(String text) {
        super(JToggleButton.class);
        this.text = text;
    }

    @Override
    protected boolean isMatching(JToggleButton jToggleButton) {
        return jToggleButton.getText().equals(text) && jToggleButton.isShowing();
    }
}
