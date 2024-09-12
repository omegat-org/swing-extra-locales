package org.omegat.swing.extra;

import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.*;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.synth.SynthLookAndFeel;

import org.openide.awt.Mnemonics;

/**
 * @author Hiroshi Miura
 */
public final class ExtraLocales {
    private static final String EXTRA_BASIC = "org/omegat/swing/extra/basic";
    // LAF specific
    private static final String AQUA_LAF = "Aqua";
    private static final String EXTRA_AQUA = "org/omegat/swing/extra/aqua";
    private static final String GTK_LAF = "GTK";
    private static final String EXTRA_GTK = "org/omegat/swing/extra/gtk";
    private static final String WINDOWS_LAF = "Windows";
    private static final String EXTRA_WINDOWS = "org/omegat/swing/extra/windows";
    private static final String MOTIF_LAF = "Motif";
    private static final String EXTRA_MOTIF = "org/omegat/swing/extra/motif";
    private static final String EXTRA_SYNTH = "org/omegat/swing/extra/synth";
    private static final String EXTRA_METAL = "org/omegat/swing/extra/metal";

    // JRE supports {"zh_CN", "zh_TW", "en", "de", "fr", "it", "es", "pt_BR", "ko", "ja", "sv"};
    private static final Locale locale;
    private static final String[] supported = {"ru"};

    private static Boolean initialized = false;

    static {
        locale = new Locale(System.getProperty("user.language"), System.getProperty("user.country"));
    }

    private ExtraLocales() {}

    @SuppressWarnings("unused")
    public static void initialize() {
        if (initialized) {
            return;
        }
        synchronized (locale) {
            if (initialized) {
                return;
            }
            for (String s : supported) {
                if (locale.equals(new Locale(s))) {
                    initialize(locale);
                    break;
                }
            }
            initialized = true;
        }
    }

    /**
     * Initializer.
     * @param locale locale for test (russian).
     */
    static void initialize(Locale locale) {
        ResourceBundle basicResource = ResourceBundle.getBundle(EXTRA_BASIC, locale);
        loadLocalizeOverrides(basicResource);
        lafInitialize(locale);
    }

    private static void lafInitialize(Locale locale) {
        LookAndFeel laf = UIManager.getLookAndFeel();
        String id = laf.getID();
        if (AQUA_LAF.endsWith(id)) {
            ResourceBundle aquaResource = ResourceBundle.getBundle(EXTRA_AQUA, locale);
            loadLocalizeOverrides(aquaResource);
        } else if (GTK_LAF.equals(id)) {
            ResourceBundle gtkResource = ResourceBundle.getBundle(EXTRA_GTK, locale);
            loadLocalizeOverrides(gtkResource);
        } else if (WINDOWS_LAF.equals(id)) {
            ResourceBundle windowsResource = ResourceBundle.getBundle(EXTRA_WINDOWS, locale);
            loadLocalizeOverrides(windowsResource);
        } else if (MOTIF_LAF.equals(id)) {
            ResourceBundle motifResource = ResourceBundle.getBundle(EXTRA_MOTIF, locale);
            loadLocalizeOverrides(motifResource);
        } else if (laf instanceof SynthLookAndFeel) {
            ResourceBundle bundle = ResourceBundle.getBundle(EXTRA_SYNTH, locale);
            loadLocalizeOverrides(bundle);
        } else if (laf instanceof MetalLookAndFeel) {
            ResourceBundle bundle = ResourceBundle.getBundle(EXTRA_METAL, locale);
            loadLocalizeOverrides(bundle);
        }
    }

    private static void loadLocalizeOverrides(ResourceBundle basicResource) {
        for (String key : basicResource.keySet()) {
            String val = basicResource.getString(key);
            if (!val.isEmpty()) {
                if (key.endsWith(".textAndMnemonic")) {
                    processMnemonics(key, val);
                } else {
                    UIManager.put(key, val);
                }
            }
        }
    }

    private static final String[][] postfixes = {
        {".nameText", ".mnemonic", ".displayedMnemonicIndex"},
        {"Text", "Mnemonic", "MnemonicIndex"}
    };

    private static void processMnemonics(String key, String val) {
        String prefix = key.substring(0, key.length() - ".textAndMnemonic".length() - 1);
        int i = prefix.lastIndexOf('.') < 0 ? 0 : 1;
        int n = Mnemonics.findMnemonicAmpersand(val);
        if (n < 0) {
            // no mnemonic config
            UIManager.put(prefix + postfixes[i][0], val);
        } else {
            UIManager.put(prefix + postfixes[i][0], val.substring(0, n) + val.substring(n + 1));
            int ch = getMnemonicChr(val.charAt(n));
            if (ch > 0) {
                UIManager.put(prefix + postfixes[i][1], Integer.toString(ch, 10));
                UIManager.put(prefix + postfixes[i][2], n);
            }
        }
    }

    private static int getMnemonicChr(char ch) {
        if ((ch >= 'A' && ch <= 'Z') || (ch >= '0' && ch <= '9')) {
            return ch;
        } else if (ch >= 'a' && ch <= 'z') {
            return ch + ('A' - 'a');
        } else {
            // it's non-latin, getting the latin correspondence
            return Mnemonics.getLatinKeycode(ch, locale);
        }
    }
}
