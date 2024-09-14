package org.omegat.swing.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public final class TestUtils {

    private TestUtils() {}

    public static String[] buildExpectations(ResourceBundle bundle, String... keys) {
        List<String> expectations = new ArrayList<>();
        for (String key : keys) {
            expectations.add(bundle.getString(key).replace("&", ""));
        }
        return expectations.toArray(new String[0]);
    }
}
