package org.omegat.swing.extra;

import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.synth.SynthLookAndFeel;
import org.openide.awt.Mnemonics;

/**
 * @author Hiroshi Miura
 */
public final class ExtraLocales {
    // Bundle paths.
    static final String EXTRA_BASIC = "org/omegat/swing/extra/basic";
    static final String EXTRA_AQUA = "org/omegat/swing/extra/aqua";
    static final String EXTRA_GTK = "org/omegat/swing/extra/gtk";
    static final String EXTRA_WINDOWS = "org/omegat/swing/extra/windows";

    // LaF IDs.
    private static final String AQUA_LAF = "Aqua";
    private static final String GTK_LAF = "GTK";
    private static final String WINDOWS_LAF = "Windows";
    private static final String MOTIF_LAF = "Motif";

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

    // Force run initialization only once.
    private static Boolean initialized = false;

    /**
     * Initialize localized SWING UI messages.
     * <p>
     *     ExtraLocales#initialize loads localized messages
     *     when the library support the JVM locale.
     *     It also override messages for LaF specific UI texts
     *     when the library support it.
     * </p>
     * <p>
     *     The library support the following languages;
     *     <ul>
     *         <li>Arabic</li>
     *         <li>Catalan</li>
     *         <li>Dansk</li>
     *         <li>Russian</li>
     *         <li>Ukrainian</li>
     *     </ul>
     *
     *     It support the following Look-and-Feels;
     *     <ul>
     *         <li>Aqua (macOS)</li>
     *         <li>Gtk (Linux)</li>
     *         <li>Windows (MS Windows)</li>
     *     </ul>
     * </p>
     */
    public static void initialize() {
        if (initialized) {
            return;
        }
        synchronized (SUPPORTED) {
            if (initialized) {
                return;
            }
            final String lang = System.getProperty("user.language");
            for (String s : SUPPORTED) {
                if (s.equals(lang)) {
                    initialize(new Locale(s, System.getProperty("user.country")));
                    break;
                }
            }
            // got default or unsupported locale
            initialized = true;
        }
    }

    /**
     * Initializer with locale.
     * <p>
     *     It is package-private for test purpose.
     *
     * @param locale to load.
     */
    static void initialize(Locale locale) {
        loadLocalizeOverrides(EXTRA_BASIC, locale);

        // Laf specific initialization.
        LookAndFeel laf = UIManager.getLookAndFeel();
        String id = laf.getID();
        if (AQUA_LAF.endsWith(id)) {
            loadLocalizeOverrides(EXTRA_AQUA, locale);
        } else if (GTK_LAF.equals(id)) {
            loadLocalizeOverrides(EXTRA_GTK, locale);
        } else if (WINDOWS_LAF.equals(id)) {
            loadLocalizeOverrides(EXTRA_WINDOWS, locale);
        } else if (MOTIF_LAF.equals(id)) {
            nop(EXTRA_MOTIF, locale);
        } else if (laf instanceof SynthLookAndFeel) {
            nop(EXTRA_SYNTH, locale);
        } else if (laf instanceof MetalLookAndFeel) {
            nop(EXTRA_METAL, locale);
        }
    }

    // When we don't have translations.
    @SuppressWarnings("unused")
    private static void nop(String bundleName, Locale locale) {}

    // override by localized texts.
    private static void loadLocalizeOverrides(String bundleName, Locale locale) {
        ResourceBundle bundle = ResourceBundle.getBundle(bundleName, locale);
        for (String key : bundle.keySet()) {
            String val = bundle.getString(key);
            if (!val.isEmpty()) {
                if (key.endsWith(".textAndMnemonic")) {
                    processTextMnemonics(key, val, locale);
                } else if (key.endsWith(".titleAndMnemonic")) {
                    processTitleMnemonics(key, val);
                } else {
                    UIManager.put(key, val);
                }
            }
        }
    }

    private static final String[][] titlePostfixes = {
        {"Title", "NameTitle", "Mnemonic", "MnemonicIndex", "DisplayedMnemonicIndex"}
    };

    private static void processTitleMnemonics(String key, String val) {
        String prefix = key.substring(0, key.length() - ".titleAndMnemonic".length());
        int n = Mnemonics.findMnemonicAmpersand(val);
        if (n < 0) {
            // no mnemonic config
            UIManager.put(prefix + titlePostfixes[0][0], val);
            UIManager.put(prefix + textPostfixes[0][1], val);
            // reset mnemonic
            UIManager.put(prefix + textPostfixes[0][2], "");
            UIManager.put(prefix + textPostfixes[0][3], -1);
            UIManager.put(prefix + textPostfixes[0][4], -1);
        }
    }

    private static final String[][] textPostfixes = {
        {".text", ".nameText", ".mnemonic", ".mnemonicIndex", ".displayedMnemonicIndex"},
        {"Text", "NameText", "Mnemonic", "MnemonicIndex", "DisplayedMnemonicIndex"}
    };

    private static void processTextMnemonics(String key, String val, Locale locale) {
        String prefix = key.substring(0, key.length() - ".textAndMnemonic".length());
        int i = prefix.lastIndexOf('.') < 0 ? 0 : 1;
        int n = Mnemonics.findMnemonicAmpersand(val);
        if (n < 0) {
            // no mnemonic config
            UIManager.put(prefix + textPostfixes[i][0], val);
            UIManager.put(prefix + textPostfixes[i][1], val);
            // reset mnemonic
            UIManager.put(prefix + textPostfixes[i][2], "");
            UIManager.put(prefix + textPostfixes[i][3], -1);
            UIManager.put(prefix + textPostfixes[i][4], -1);
        } else {
            UIManager.put(prefix + textPostfixes[i][0], val.substring(0, n) + val.substring(n + 1));
            UIManager.put(prefix + textPostfixes[i][1], val.substring(0, n) + val.substring(n + 1));
            int ch = getMnemonicChr(val.charAt(n), locale);
            if (ch > 0) {
                UIManager.put(prefix + textPostfixes[i][2], Integer.toString(ch, 10));
                UIManager.put(prefix + textPostfixes[i][3], n);
                UIManager.put(prefix + textPostfixes[i][4], n);
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
