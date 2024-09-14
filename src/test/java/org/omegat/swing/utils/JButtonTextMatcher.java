package org.omegat.swing.utils;

import javax.swing.*;
import org.assertj.swing.core.GenericTypeMatcher;

public class JButtonTextMatcher extends GenericTypeMatcher<JButton> {
    private final String text;

    public JButtonTextMatcher(String text) {
        super(JButton.class);
        this.text = text;
    }

    @Override
    protected boolean isMatching(JButton button) {
        // Check if the button has no name
        return button.getText().equals(text) && button.isShowing();
    }
}
