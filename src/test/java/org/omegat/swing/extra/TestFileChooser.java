package org.omegat.swing.extra;

import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.util.Locale;

import static org.junit.Assert.assertEquals;

public class TestFileChooser extends AssertJSwingJUnitTestCase {

    protected FrameFixture window;
    protected JFrame parent;
    protected JFileChooser fc;

    @Override
    protected void onSetUp() {
        Locales.initialize(new Locale("ru"));
        parent = GuiActionRunner.execute(() -> {
            JFrame frame = new JFrame();
            frame.setPreferredSize(new Dimension(800, 600));
            fc = new JFileChooser();
            return frame;
        });
        window = new FrameFixture(robot(), parent);
        window.show();
    }

    @Test
    public void testFileChooserOpenDialog() {
        SwingUtilities.invokeLater(() -> fc.showOpenDialog(parent));
        window.fileChooser().requireVisible();
        window.fileChooser().approveButton().requireText("Открыть");
        window.fileChooser().cancelButton().requireText("Отмена");
        assertEquals("Открытие", window.dialog().target().getTitle());
        window.fileChooser().cancel();
    }

    @Test
    public void testFileChooserSaveDialog() {
        SwingUtilities.invokeLater(() -> fc.showSaveDialog(parent));
        window.fileChooser().requireVisible();
        window.fileChooser().approveButton().requireText("Сохранить");
        window.fileChooser().cancelButton().requireText("Отмена");
        assertEquals("Сохранение", window.dialog().target().getTitle());
        window.fileChooser().cancel();
    }
}
