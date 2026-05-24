package com.prasad_v.utils;

public class PropertiesReader {

    /**
     * Backward-compatible wrapper around ConfigManager.
     *
     * @param key The key to read
     * @return The resolved configuration value, or null if not found
     */
    public static String readKey(String key) {
        return ConfigManager.get(key);
    }
}
