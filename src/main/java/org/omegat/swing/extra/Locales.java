package org.omegat.swing.extra;

import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.UIManager;
import org.openide.awt.Mnemonics;

/**
 * @author Hiroshi Miura
 */
public final class Locales {
    private static final String BUNDLE = "org/omegat/swing/extra/basic";
    // JRE supports {"zh_CN", "zh_TW", "en", "de", "fr", "it", "es", "pt_BR", "ko", "ja", "sv"};
    private static Boolean initialized = false;
    private static final Locale locale;
    private static final String[] supported = {"ru"};

    static {
        locale = new Locale(System.getProperty("user.language"), System.getProperty("user.country"));
    }

    private Locales() {}

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
                    ResourceBundle basicResource = ResourceBundle.getBundle(BUNDLE);
                    loadLocalizeOverrides(basicResource);
                }
            }
            initialized = true;
        }
    }

    /**
     * Initializer for test.
     * @param locale locale for test (russian).
     */
    static void initialize(Locale locale) {
        ResourceBundle basicResource = ResourceBundle.getBundle(BUNDLE, locale);
        loadLocalizeOverrides(basicResource);
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
