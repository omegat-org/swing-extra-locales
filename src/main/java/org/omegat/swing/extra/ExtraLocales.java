package org.omegat.swing.extra;

import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.*;
import org.openide.awt.Mnemonics;

/**
 * @author Hiroshi Miura
 */
@SuppressWarnings("unused")
public final class ExtraLocales {
    // Bundle paths.
    static final String EXTRA_BASIC = "org/omegat/swing/extra/basic";
    static final String EXTRA_AQUA = "org/omegat/swing/extra/aqua";
    static final String EXTRA_GTK = "org/omegat/swing/extra/gtk";
    static final String EXTRA_WINDOWS = "org/omegat/swing/extra/windows";

    // LaF IDs.
    private static final String AQUA_LAF = "AquaLookAndFeel";
    private static final String GTK_LAF = "GTKLookAndFeel";
    private static final String WINDOWS_LAF = "WindowsLookAndFeel";

    // other bundles.
    private static final String EXTRA_MOTIF = "org/omegat/swing/extra/motif";
    private static final String EXTRA_SYNTH = "org/omegat/swing/extra/synth";
    private static final String EXTRA_METAL = "org/omegat/swing/extra/metal";

    // Supported extra languages.
    // FYI: JRE supports {"zh_CN", "zh_TW", "en", "de", "fr", "it", "es", "pt_BR", "ko", "ja", "sv"};
    static final String[] SUPPORTED = {"ar", "ca", "ru", "uk"};

    /**
     * We don't allow instantiates the utility class.
     */
    private ExtraLocales() {}

    /**
     * Initialize localized SWING UI messages.
     * <p>
     *     ExtraLocales#initialize loads localized messages
     *     when the library supports the JVM locale.
     *     It also overrides messages for LaF specific UI texts
     *     when the library supports it.
     *
     *     The library supports the following languages;
     *     <ul>
     *         <li>Arabic</li>
     *         <li>Catalan</li>
     *         <li>Danish</li>
     *         <li>Russian</li>
     *         <li>Ukrainian</li>
     *     </ul>
     *
     *     It supports the following Look-and-Feels;
     *     <ul>
     *         <li>Aqua (macOS)</li>
     *         <li>Gtk (Linux)</li>
     *         <li>Windows (MS Windows)</li>
     *     </ul>
     */
    public static void initialize() throws Exception {
        LookAndFeel originalLaf = UIManager.getLookAndFeel();
        UIManager.setLookAndFeel(new ExtraLocalesLookAndFeel(originalLaf));
    }

    /**
     * Override UIDefaults dialog title/messages with Localized strings.
     * @param uiDefaults Application UIDefaults table to be overridden.
     * @return UIDefaults with L10N messages when the locale is supported.
     */
    public static UIDefaults setDefaults(UIDefaults uiDefaults) {
        final String lang = System.getProperty("user.language");
        for (String s : SUPPORTED) {
            if (s.equals(lang)) {
                Locale locale = new Locale(s, System.getProperty("user.country"));
                return setDefaults(uiDefaults, locale);
            }
        }
        return uiDefaults;
    }

    /**
     * Initializer with locale.
     *
     * @param locale to load.
     */
    private static UIDefaults setDefaults(UIDefaults uiDefaults, Locale locale) {
        loadLocalizeOverrides(uiDefaults, EXTRA_BASIC, locale);

        // Laf specific initialization.
        String clazz = UIManager.getSystemLookAndFeelClassName();
        if (clazz.endsWith(AQUA_LAF)) {
            loadLocalizeOverrides(uiDefaults, EXTRA_AQUA, locale);
        } else if (clazz.endsWith(GTK_LAF)) {
            loadLocalizeOverrides(uiDefaults, EXTRA_GTK, locale);
        } else if (clazz.endsWith(WINDOWS_LAF)) {
            loadLocalizeOverrides(uiDefaults, EXTRA_WINDOWS, locale);
        }

        return uiDefaults;
    }

    // override by localized texts.
    private static void loadLocalizeOverrides(UIDefaults uiDefaults, String bundleName, Locale locale) {
        ResourceBundle bundle = ResourceBundle.getBundle(bundleName, locale);
        for (String key : bundle.keySet()) {
            String val = bundle.getString(key);
            if (!val.isEmpty()) {
                if (key.endsWith(".textAndMnemonic")) {
                    processTextMnemonics(uiDefaults, key, val, locale);
                } else if (key.endsWith(".titleAndMnemonic")) {
                    processTitleMnemonics(uiDefaults, key, val);
                } else {
                    uiDefaults.put(key, val);
                }
            }
        }
    }

    private static final String[][] titlePostfixes = {
        {"Title", "NameTitle", "Mnemonic", "MnemonicIndex", "DisplayedMnemonicIndex"}
    };

    private static void removeIfExists(UIDefaults uiDefaults, String key) {
        if (uiDefaults.get(key) != null) {
            uiDefaults.remove(key);
        }
    }

    private static void processTitleMnemonics(UIDefaults uiDefaults, String key, String val) {
        String prefix = key.substring(0, key.length() - ".titleAndMnemonic".length());
        int n = Mnemonics.findMnemonicAmpersand(val);
        if (n < 0) {
            // no mnemonic config
            uiDefaults.put(prefix + titlePostfixes[0][0], val);
            uiDefaults.put(prefix + textPostfixes[0][1], val);
            // reset mnemonic
            removeIfExists(uiDefaults, prefix + textPostfixes[0][2]);
            removeIfExists(uiDefaults, prefix + textPostfixes[0][3]);
            removeIfExists(uiDefaults, prefix + textPostfixes[0][4]);
        }
    }

    private static final String[][] textPostfixes = {
        {".text", ".nameText", ".mnemonic", ".mnemonicIndex", ".displayedMnemonicIndex"},
        {"Text", "NameText", "Mnemonic", "MnemonicIndex", "DisplayedMnemonicIndex"}
    };

    private static void processTextMnemonics(UIDefaults uiDefaults, String key, String val, Locale locale) {
        String prefix = key.substring(0, key.length() - ".textAndMnemonic".length());
        int i = prefix.lastIndexOf('.') < 0 ? 0 : 1;
        int n = Mnemonics.findMnemonicAmpersand(val);
        if (n < 0) {
            // no mnemonic config
            uiDefaults.put(prefix + textPostfixes[i][0], val);
            uiDefaults.put(prefix + textPostfixes[i][1], val);
            // reset mnemonic
            removeIfExists(uiDefaults, prefix + textPostfixes[i][2]);
            removeIfExists(uiDefaults, prefix + textPostfixes[i][3]);
            removeIfExists(uiDefaults, prefix + textPostfixes[i][4]);
        } else {
            uiDefaults.put(prefix + textPostfixes[i][0], val.substring(0, n) + val.substring(n + 1));
            uiDefaults.put(prefix + textPostfixes[i][1], val.substring(0, n) + val.substring(n + 1));
            int ch = getMnemonicChr(val.charAt(n), locale);
            if (ch > 0) {
                uiDefaults.put(prefix + textPostfixes[i][2], Integer.toString(ch, 10));
                uiDefaults.put(prefix + textPostfixes[i][3], n);
                uiDefaults.put(prefix + textPostfixes[i][4], n);
            }
        }
    }

    private static int getMnemonicChr(char ch, Locale locale) {
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
