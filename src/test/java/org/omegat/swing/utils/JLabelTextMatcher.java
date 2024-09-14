package org.omegat.swing.utils;

import javax.swing.*;
import org.assertj.swing.core.GenericTypeMatcher;

public class JLabelTextMatcher extends GenericTypeMatcher<JLabel> {
    private final String text;

    public JLabelTextMatcher(String text) {
        super(JLabel.class);
        this.text = text;
    }

    @Override
    protected boolean isMatching(JLabel label) {
        return text.equals(label.getText()) && label.isShowing();
    }
}
