package com.xyzbank.utils;

public final class ConfigUtils {

    private ConfigUtils() {
        // Utility class
    }

    public static String getPropOrEnv(String key, String defaultValue) {
        String value = System.getProperty(key);
        if (value == null || value.isEmpty()) {
            // Check for both DOT and UNDERSCORE format (BASE.URL and BASE_URL)
            value = System.getenv(key.toUpperCase().replace(".", "_"));
        }
        return (value != null && !value.isEmpty()) ? value : defaultValue;
    }
}
