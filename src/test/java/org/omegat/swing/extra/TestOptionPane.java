package org.omegat.swing.extra;

import org.assertj.swing.core.GenericTypeMatcher;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.util.Locale;

import static org.junit.Assert.assertEquals;

public class TestOptionPane extends AssertJSwingJUnitTestCase {

    protected FrameFixture window;
    protected JFrame parent;

    @Override
    protected void onSetUp() {
        Locales.initialize(new Locale("ru"));
        parent = GuiActionRunner.execute(() -> {
            JFrame frame = new JFrame();
            frame.setPreferredSize(new Dimension(800, 600));
            return frame;
        });
        window = new FrameFixture(robot(), parent);
        window.show();
    }

    @Test
    public void testOptionPaneConfirmDialog() {
        SwingUtilities.invokeLater(() -> JOptionPane.showConfirmDialog(parent, "Confirm"));
        window.dialog().requireVisible();
        window.dialog().requireModal();
        assertEquals("Выберите опцию", window.dialog().target().getTitle());
        window.dialog().button(new JButtonMatcher(0)).requireText("Да");
        window.dialog().button(new JButtonMatcher(1)).requireText("Нет");
        window.dialog().button(new JButtonMatcher(2)).requireText("Отмена");
        window.dialog().label("OptionPane.label").requireText("Confirm");
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
