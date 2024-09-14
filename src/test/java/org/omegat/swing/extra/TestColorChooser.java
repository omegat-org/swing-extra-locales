package org.omegat.swing.extra;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.omegat.swing.extra.ExtraLocales.EXTRA_BASIC;
import static org.omegat.swing.extra.ExtraLocales.SUPPORTED;
import static org.omegat.swing.utils.TestUtils.buildExpectations;

import java.awt.Color;
import java.awt.Dimension;
import java.util.Arrays;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.Assume;
import org.junit.Test;
import org.omegat.swing.utils.JButtonTextMatcher;
import org.omegat.swing.utils.JLabelTextMatcher;
import org.omegat.swing.utils.JToggleButtonTextMatcher;

public class TestColorChooser extends AssertJSwingJUnitTestCase {

    protected FrameFixture window;
    protected JFrame parent;

    // expectations
    private String language;
    private String[] tabTitles;
    private String[] buttonLabels;
    private String[] hsvLabels;
    private String[] hslLabels;
    private String[] rgbLabels;
    private String[] cmykLabels;

    @Override
    protected void onSetUp() {
        language = Locale.getDefault().getLanguage();
        Assume.assumeTrue(Arrays.stream(SUPPORTED).anyMatch(s -> language.equals(s)));
        ResourceBundle bundle = ResourceBundle.getBundle(EXTRA_BASIC);
        tabTitles = buildExpectations(
                bundle,
                "ColorChooser.swatches.textAndMnemonic",
                "ColorChooser.hsv.textAndMnemonic",
                "ColorChooser.hsl.textAndMnemonic",
                "ColorChooser.rgb.textAndMnemonic",
                "ColorChooser.cmyk.textAndMnemonic");
        buttonLabels = buildExpectations(
                bundle,
                "ColorChooser.reset.textAndMnemonic",
                "ColorChooser.cancel.textAndMnemonic",
                "ColorChooser.ok.textAndMnemonic");
        hsvLabels = buildExpectations(
                bundle,
                "ColorChooser.hsvHue.textAndMnemonic",
                "ColorChooser.hsvSaturation.textAndMnemonic",
                "ColorChooser.hsvValue.textAndMnemonic",
                "ColorChooser.hsvTransparency.textAndMnemonic");
        hslLabels = buildExpectations(
                bundle,
                "ColorChooser.hslHue.textAndMnemonic",
                "ColorChooser.hslSaturation.textAndMnemonic",
                "ColorChooser.hslLightness.textAndMnemonic",
                "ColorChooser.hslTransparency.textAndMnemonic");
        rgbLabels = buildExpectations(
                bundle,
                "ColorChooser.rgbRed.textAndMnemonic",
                "ColorChooser.rgbGreen.textAndMnemonic",
                "ColorChooser.rgbBlue.textAndMnemonic",
                "ColorChooser.rgbAlpha.textAndMnemonic");
        cmykLabels = buildExpectations(
                bundle,
                "ColorChooser.cmykCyan.textAndMnemonic",
                "ColorChooser.cmykMagenta.textAndMnemonic",
                "ColorChooser.cmykYellow.textAndMnemonic",
                "ColorChooser.cmykBlack.textAndMnemonic",
                "ColorChooser.cmykAlpha.textAndMnemonic");
        // preparation
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
        SwingUtilities.invokeLater(() -> JColorChooser.showDialog(parent, "Color", Color.RED));
        window.dialog().requireVisible();
        window.dialog().requireModal();
        assertEquals("Color", window.dialog().target().getTitle());
        assertEquals(language, window.dialog().target().getLocale().getLanguage());
        // there are ok, reset, and cacnel buttons
        window.dialog().button((new JButtonTextMatcher(buttonLabels[0]))).requireEnabled();
        window.dialog().button((new JButtonTextMatcher(buttonLabels[1]))).requireEnabled();
        window.dialog().button((new JButtonTextMatcher(buttonLabels[2]))).requireEnabled();
        // check tab titles.
        window.dialog().tabbedPane().requireTabTitles(tabTitles);
        // select HSV
        window.dialog().tabbedPane().selectTab(tabTitles[1]);
        window.dialog().toggleButton(new JToggleButtonTextMatcher(hsvLabels[0]));
        window.dialog().toggleButton(new JToggleButtonTextMatcher(hsvLabels[1]));
        window.dialog().toggleButton(new JToggleButtonTextMatcher(hsvLabels[2]));
        // select HSL
        window.dialog().tabbedPane().selectTab(tabTitles[2]);
        window.dialog().toggleButton(new JToggleButtonTextMatcher(hslLabels[0]));
        window.dialog().toggleButton(new JToggleButtonTextMatcher(hslLabels[1]));
        window.dialog().toggleButton(new JToggleButtonTextMatcher(hslLabels[2]));
        window.dialog().label(new JLabelTextMatcher(hslLabels[3]));
        // select RGB
        window.dialog().tabbedPane().selectTab(tabTitles[3]);
        window.dialog().toggleButton(new JToggleButtonTextMatcher(rgbLabels[0]));
        window.dialog().toggleButton(new JToggleButtonTextMatcher(rgbLabels[1]));
        window.dialog().toggleButton(new JToggleButtonTextMatcher(rgbLabels[2]));
        window.dialog().label(new JLabelTextMatcher(rgbLabels[3]));
        // select CMYK
        window.dialog().tabbedPane().selectTab(tabTitles[4]);
        window.dialog().toggleButton(new JToggleButtonTextMatcher(cmykLabels[0]));
        window.dialog().toggleButton(new JToggleButtonTextMatcher(cmykLabels[1]));
        window.dialog().toggleButton(new JToggleButtonTextMatcher(cmykLabels[2]));
        window.dialog().label(new JLabelTextMatcher(cmykLabels[3]));
        window.dialog().label(new JLabelTextMatcher(cmykLabels[4]));
    }
}
