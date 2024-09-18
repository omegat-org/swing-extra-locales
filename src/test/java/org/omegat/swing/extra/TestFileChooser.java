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
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.Assume;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestFileChooser extends AssertJSwingJUnitTestCase {

    protected FrameFixture window;
    protected JFrame parent;
    protected JFileChooser fc;

    // expactations
    String language;
    String[] titleLabels;
    String[] buttonLabels;

    @Override
    protected void onSetUp() throws Exception {
        language = Locale.getDefault().getLanguage();
        Assume.assumeTrue(Arrays.stream(SUPPORTED).anyMatch(s -> language.equals(s)));
        // expectations
        ResourceBundle bundle = ResourceBundle.getBundle(EXTRA_BASIC);
        titleLabels = buildExpectations(
                bundle, "FileChooser.openDialogTitle.textAndMnemonic", "FileChooser.saveDialogTitle.textAndMnemonic");
        buttonLabels = buildExpectations(
                bundle,
                "FileChooser.cancelButton.textAndMnemonic",
                "FileChooser.openButton.textAndMnemonic",
                "FileChooser.saveButton.textAndMnemonic");

        parent = GuiActionRunner.execute(() -> {
            ExtraLocales.initialize();
            JFrame frame = new JFrame();
            frame.setPreferredSize(new Dimension(800, 600));
            fc = new JFileChooser();
            return frame;
        });
        assertNotNull(parent);
        window = new FrameFixture(robot(), parent);
        window.show();
    }

    @Test
    public void testFileChooserOpenDialog() {
        SwingUtilities.invokeLater(() -> fc.showOpenDialog(parent));
        window.fileChooser().requireVisible();
        assertEquals(titleLabels[0], window.dialog().target().getTitle());
        window.fileChooser().cancelButton().requireText(buttonLabels[0]);
        window.fileChooser().approveButton().requireText(buttonLabels[1]);
        window.fileChooser().cancel();
    }

    @Test
    public void testFileChooserSaveDialog() {
        SwingUtilities.invokeLater(() -> fc.showSaveDialog(parent));
        window.fileChooser().requireVisible();
        assertEquals(titleLabels[1], window.dialog().target().getTitle());
        window.fileChooser().cancelButton().requireText(buttonLabels[0]);
        window.fileChooser().approveButton().requireText(buttonLabels[2]);
        window.fileChooser().cancel();
    }
}
