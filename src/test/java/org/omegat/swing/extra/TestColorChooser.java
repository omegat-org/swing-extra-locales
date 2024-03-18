package org.omegat.swing.extra;

import static org.junit.Assert.assertEquals;

import java.awt.*;
import java.util.Locale;
import javax.swing.*;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.Test;

public class TestColorChooser extends AssertJSwingJUnitTestCase {

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
    public void testColorChooserDialog() {
        String[] titles = {"Образцы" /* Samples */, "HSV", "HSL", "RGB", "CMYK"};
        SwingUtilities.invokeLater(() -> JColorChooser.showDialog(parent, "Color", Color.RED));
        window.dialog().requireVisible();
        window.dialog().requireModal();
        assertEquals("Color", window.dialog().target().getTitle());
        window.dialog().tabbedPane().requireTabTitles(titles);
    }
}
