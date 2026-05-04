package com.prasad_v.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Utility class to load configuration from properties files with environment variable fallback.
 * Supports placeholders in format ${ENV_VAR_NAME} that are resolved from system environment.
 */
public class EnvConfig {
    private static final Properties PROPERTIES = new Properties();
    private static final String DEFAULT_PROPERTIES_FILE = "src/main/resources/data.properties";
    private static boolean initialized = false;

    /**
     * Initializes the configuration by loading the default properties file.
     * This method is thread-safe and idempotent.
     */
    private static synchronized void initialize() {
        if (initialized) {
            return;
        }

        String propertiesFilePath = System.getProperty("config.file", DEFAULT_PROPERTIES_FILE);

        try (InputStream input = new FileInputStream(propertiesFilePath)) {
            PROPERTIES.load(input);
        } catch (IOException e) {
            // If properties file doesn't exist, continue with empty properties
            // Environment variables will still be used
        }

        initialized = true;
    }

    /**
     * Gets a configuration value by key, resolving environment variable placeholders.
     *
     * @param key the configuration key
     * @return the configuration value, or null if not found
     */
    public static String get(String key) {
        initialize();
        String value = PROPERTIES.getProperty(key);
        return resolvePlaceholders(value);
    }

    /**
     * Gets a required configuration value by key, resolving environment variable placeholders.
     *
     * @param key the configuration key
     * @return the configuration value
     * @throws RuntimeException if the configuration value is missing or empty
     */
    public static String getRequired(String key) {
        String value = get(key);
        if (value == null || value.isEmpty()) {
            throw new RuntimeException("Required configuration key '" + key + "' is missing or empty");
        }
        return value;
    }

    /**
     * Resolves environment variable placeholders in the format ${ENV_VAR_NAME}.
     *
     * @param value the value that may contain placeholders
     * @return the value with placeholders resolved, or original value if no placeholders
     */
    private static String resolvePlaceholders(String value) {
        if (value == null) {
            return null;
        }

        // Handle placeholder format: ${ENV_VAR_NAME}
        if (value.startsWith("${") && value.endsWith("}")) {
            String envVarName = value.substring(2, value.length() - 1);
            String envValue = System.getenv(envVarName);
            return envValue != null ? envValue : "";
        }

        return value;
    }
}