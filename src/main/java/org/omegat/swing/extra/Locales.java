package org.omegat.swing.extra;

import javax.swing.UIManager;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @author Hiroshi Miura
 */
public final class Locales {
    // JRE supports {"zh_CN", "zh_TW", "en", "de", "fr", "it", "es", "pt_BR", "ko", "ja", "sv"};
    private static Boolean initialized = false;
    private static final Locale locale;
    private static final String[] supported = {"ru", "ca"};

    static {
        locale = new Locale(System.getProperty("user.language"), System.getProperty("user.country"));
    }

    private Locales() {
    }

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
                    loadLocalizeOverrides();
                }
            }
            initialized = true;
        }
    }

    private static void loadLocalizeOverrides() {
        ResourceBundle basicResource = ResourceBundle.getBundle("org/omegat/swing/extra/basic");
        for (String key: basicResource.keySet()) {
            String val = basicResource.getString(key);
            if (!val.isEmpty()) {
                UIManager.put(key, val);
            }
        }
    }

}
