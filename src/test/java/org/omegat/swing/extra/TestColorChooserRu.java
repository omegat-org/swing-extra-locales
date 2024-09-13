package org.omegat.swing.extra;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.awt.*;
import java.util.Locale;
import javax.swing.*;

import org.assertj.swing.core.GenericTypeMatcher;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.Assume;
import org.junit.Test;

public class TestColorChooserRu extends AssertJSwingJUnitTestCase {

    protected FrameFixture window;
    protected JFrame parent;

    @Override
    protected void onSetUp() {
        Assume.assumeTrue(Locale.getDefault().getLanguage().equals("ru"));
        ExtraLocales.initialize();
        parent = GuiActionRunner.execute(() -> {
            JFrame frame = new JFrame();
            frame.setPreferredSize(new Dimension(800, 600));
            return frame;
        });
        assertNotNull(parent);
        window = new FrameFixture(robot(), parent);
        window.show();
    }

    @Test
    public void testColorChooserDialog() {
        String[] titles = {"Образцы" /* Samples */, "HSV", "HSL", "RGB", "CMYK"};
        SwingUtilities.invokeLater(() -> JColorChooser.showDialog(parent, "Color", Color.RED));
        window.dialog().requireVisible();
        window.dialog().requireModal();
        assertEquals("Color", window.dialog().target().getTitle());
        assertEquals("ru", window.dialog().target().getLocale().getLanguage());
        // there is a reset button
        window.dialog().button((new JButtonTextMatcher("Ресет"))).requireEnabled();
        // there is a cancel button
        window.dialog().button((new JButtonTextMatcher("Отмена"))).requireEnabled();
        //
        window.dialog().tabbedPane().requireTabTitles(titles);
        // select HSV
        window.dialog().tabbedPane().selectTab(titles[1]);
        window.dialog().toggleButton(new JToggleButtonTextMatcher("Hue"));
        window.dialog().toggleButton(new JToggleButtonTextMatcher("Сатурация"));
        window.dialog().toggleButton(new JToggleButtonTextMatcher("ценность"));
        // select HSL
        window.dialog().tabbedPane().selectTab(titles[2]);
        window.dialog().toggleButton(new JToggleButtonTextMatcher("Hue"));
        window.dialog().toggleButton(new JToggleButtonTextMatcher("Легкость"));
        window.dialog().toggleButton(new JToggleButtonTextMatcher("Сатурация"));
        window.dialog().label(new JLabelTextMatcher("Прозрачность"));
        // select RGB
        window.dialog().tabbedPane().selectTab(titles[3]);
        window.dialog().toggleButton(new JToggleButtonTextMatcher("Красный"));
        window.dialog().toggleButton(new JToggleButtonTextMatcher("Голубой"));
        window.dialog().toggleButton(new JToggleButtonTextMatcher("Зелёный"));
        window.dialog().label(new JLabelTextMatcher("Alpha"));
    }

    private static class JButtonTextMatcher extends GenericTypeMatcher<JButton> {
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

    private static class JToggleButtonTextMatcher extends GenericTypeMatcher<JToggleButton> {
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

    private static class JLabelTextMatcher extends GenericTypeMatcher<JLabel> {
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
}
