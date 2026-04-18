package com.prasad_v.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigManager {

    private static Properties properties;
    private static final String ENV = System.getProperty("env", "qa");
    private static final String APP = System.getProperty("app", "");

    static {
        loadProperties();
    }

    private static void loadProperties() {
        properties = new Properties();

        // Per-app config: src/main/resources/config/{app}/{env}.properties
        // Generic config: src/main/resources/config/{env}.properties
        String configPath = APP.isEmpty()
                ? "src/main/resources/config/" + ENV + ".properties"
                : "src/main/resources/config/" + APP + "/" + ENV + ".properties";

        try (FileInputStream fis = new FileInputStream(configPath)) {
            properties.load(fis);
            LoggerUtil.info("Loaded config: " + configPath);
        } catch (IOException e) {
            LoggerUtil.warn("Config not found at: " + configPath + " - falling back to data.properties");
            try (FileInputStream fis = new FileInputStream("src/main/resources/data.properties")) {
                properties.load(fis);
            } catch (IOException ex) {
                LoggerUtil.error("Failed to load any properties file", ex);
            }
        }
    }

    /**
     * Retrieves the configuration value for the given key.
     * Supports environment variable placeholders in the format "${ENV_VAR_NAME}".
     * If the value is a placeholder, it will be resolved against system environment variables.
     *
     * @param key The configuration key
     * @return The resolved configuration value, or null if not found
     */
    public static String get(String key) {
        String value = properties.getProperty(key);
        if (value != null && value.startsWith("${") && value.endsWith("}")) {
            String envVar = value.substring(2, value.length() - 1);
            String envValue = System.getenv(envVar);
            if (envValue != null) {
                return envValue;
            }
            LoggerUtil.warn("Environment variable " + envVar + " is not set. Using original placeholder.");
        }
        return value;
    }

    public static String getEnvironment() {
        return ENV;
    }

    public static String getApp() {
        return APP;
    }
}
