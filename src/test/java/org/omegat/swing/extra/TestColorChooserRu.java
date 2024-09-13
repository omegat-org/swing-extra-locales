package org.omegat.swing.extra;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.awt.*;
import java.util.Locale;
import javax.swing.*;
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
        String rgbText = UIManager.getString("ColorChooser.rgbText");
        assertEquals("RGB", rgbText);
        SwingUtilities.invokeLater(() -> JColorChooser.showDialog(parent, "Color", Color.RED));
        window.dialog().requireVisible();
        window.dialog().requireModal();
        assertEquals("Color", window.dialog().target().getTitle());
        assertEquals("ru", window.dialog().target().getLocale().getLanguage());
        window.dialog().tabbedPane().requireTabTitles(titles);
    }
}
