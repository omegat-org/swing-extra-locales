package org.omegat.swing.extra;

import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Locale;
import java.util.ResourceBundle;

import static org.junit.Assert.*;
import static org.omegat.swing.extra.ExtraLocales.EXTRA_AQUA;
import static org.omegat.swing.extra.ExtraLocales.EXTRA_BASIC;
import static org.omegat.swing.extra.ExtraLocales.EXTRA_GTK;
import static org.omegat.swing.extra.ExtraLocales.EXTRA_WINDOWS;
import static org.omegat.swing.extra.ExtraLocales.supported;

public class TestLoadingProperties {

    private String[] supportedBundles;

    @Before
    public void onSetUp() {
        Assume.assumeTrue(Arrays.stream(supported).anyMatch(s -> Locale.getDefault().getLanguage().equals(s)));
        supportedBundles = new String[] { EXTRA_BASIC, EXTRA_AQUA, EXTRA_GTK, EXTRA_WINDOWS };
    }

    @Test
    public void loadBundleTest() {
        for (String baseName: supportedBundles) {
            ResourceBundle bundle = ResourceBundle.getBundle(baseName);
            assertNotNull(bundle);
            // check the existence of translation.
            switch (baseName) {
                case EXTRA_GTK:
                    assertNotEquals("All Files", bundle.getString("FileChooser.acceptAllFileFilter.textAndMnemonic"));
                    break;
                case EXTRA_WINDOWS:
                    assertNotEquals("Create New Folder", bundle.getString("FileChooser.newFolderToolTip.textAndMnemonic"));
                    break;
                case EXTRA_BASIC:
                case EXTRA_AQUA:
                    assertNotEquals("Generic File", bundle.getString("FileChooser.fileDescription.textAndMnemonic"));
                    break;
            }
        }
    }
}
