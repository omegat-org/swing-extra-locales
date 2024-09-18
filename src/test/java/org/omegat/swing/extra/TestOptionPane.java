package org.omegat.swing.extra;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.omegat.swing.extra.ExtraLocales.EXTRA_BASIC;
import static org.omegat.swing.extra.ExtraLocales.SUPPORTED;
import static org.omegat.swing.utils.TestUtils.buildExpectations;

import java.awt.*;
import java.util.Arrays;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.*;
import org.assertj.swing.core.GenericTypeMatcher;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.Assume;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestOptionPane extends AssertJSwingJUnitTestCase {

    protected FrameFixture window;
    protected JFrame parent;

    // expectations
    private String[] buttonLabels;
    private String[] titles;

    @Override
    public void onSetUp() {
        Assume.assumeTrue(Arrays.stream(SUPPORTED)
                .anyMatch(s -> Locale.getDefault().getLanguage().equals(s)));
        ResourceBundle bundle = ResourceBundle.getBundle(EXTRA_BASIC);
        buttonLabels = buildExpectations(
                bundle,
                "OptionPane.yesButton.textAndMnemonic",
                "OptionPane.noButton.textAndMnemonic",
                "OptionPane.okButton.textAndMnemonic",
                "OptionPane.cancelButton.textAndMnemonic");
        titles = buildExpectations(
                bundle,
                "OptionPane.title.textAndMnemonic",
                "OptionPane.inputDialog.titleAndMnemonic",
                "OptionPane.messageDialog.titleAndMnemonic");
        parent = GuiActionRunner.execute(() -> {
            ExtraLocales.initialize();
            JFrame frame = new JFrame();
            frame.setPreferredSize(new Dimension(800, 600));
            return frame;
        });
        assertNotNull(parent);
        window = new FrameFixture(robot(), parent);
        window.show();
    }

    @Test
    public void testOptionPaneConfirmDialog() {
        SwingUtilities.invokeLater(() -> JOptionPane.showConfirmDialog(parent, "Confirm"));
        window.dialog().requireVisible();
        window.dialog().requireModal();
        assertEquals(titles[0], window.dialog().target().getTitle());
        window.dialog().button(new JButtonMatcher(0)).requireText(buttonLabels[0]);
        window.dialog().button(new JButtonMatcher(1)).requireText(buttonLabels[1]);
        window.dialog().button(new JButtonMatcher(2)).requireText(buttonLabels[3]);
        window.dialog().label("OptionPane.label").requireText("Confirm");
        window.dialog().button(new JButtonMatcher(0)).click();
    }

    @Test
    public void testOptionPaneMessageDialog() {
        SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(parent, "Message"));
        window.dialog().requireVisible();
        window.dialog().requireModal();
        assertEquals(titles[2], window.dialog().target().getTitle());
        window.dialog().button(new JButtonMatcher(0)).requireText(buttonLabels[2]);
        window.dialog().label("OptionPane.label").requireText("Message");
        window.dialog().button(new JButtonMatcher(0)).click();
    }

    @Test
    public void testOptionPaneInputDialog() {
        SwingUtilities.invokeLater(() -> JOptionPane.showInputDialog(parent, "Message"));
        window.dialog().requireVisible();
        window.dialog().requireModal();
        assertEquals(titles[1], window.dialog().target().getTitle());
        window.dialog().button(new JButtonMatcher(0)).requireText(buttonLabels[2]);
        window.dialog().button(new JButtonMatcher(1)).requireText(buttonLabels[3]);
        window.dialog().label("OptionPane.label").requireText("Message");
        window.dialog().textBox().enterText("sample");
        window.dialog().button(new JButtonMatcher(0)).click();
    }

    private static class JButtonMatcher extends GenericTypeMatcher<JButton> {
        private int count = 0;
        private final int index;

        public JButtonMatcher(int index) {
            super(JButton.class);
            this.index = index;
        }

        @Override
        protected boolean isMatching(JButton jButton) {
            // OptionPane buttons share a "OptionPane.button" name and are not unique.
            return "OptionPane.button".equals(jButton.getName()) && count++ == index;
        }
    }
}
