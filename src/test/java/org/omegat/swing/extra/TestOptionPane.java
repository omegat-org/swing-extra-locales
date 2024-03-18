package org.omegat.swing.extra;

import static org.junit.Assert.assertEquals;

import java.awt.*;
import java.util.Locale;
import javax.swing.*;
import org.assertj.swing.core.GenericTypeMatcher;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.Test;

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
        window.dialog().button(new JButtonMatcher(0)).click();
    }

    @Test
    public void testOptionPaneMessageDialog() {
        SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(parent, "Message"));
        window.dialog().requireVisible();
        window.dialog().requireModal();
        assertEquals("Сообщение", window.dialog().target().getTitle());
        window.dialog().button(new JButtonMatcher(0)).requireText("OK");
        window.dialog().label("OptionPane.label").requireText("Message");
        window.dialog().button(new JButtonMatcher(0)).click();
    }

    @Test
    public void testOptionPaneInputDialog() {
        SwingUtilities.invokeLater(() -> JOptionPane.showInputDialog(parent, "Message"));
        window.dialog().requireVisible();
        window.dialog().requireModal();
        assertEquals("ввод", window.dialog().target().getTitle());
        window.dialog().button(new JButtonMatcher(0)).requireText("OK");
        window.dialog().button(new JButtonMatcher(1)).requireText("Отмена");
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
