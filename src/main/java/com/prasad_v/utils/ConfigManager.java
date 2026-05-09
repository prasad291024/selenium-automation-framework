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

        // Step 1: Always load data.properties as base (shared keys: grid, cloud, db)
        try (FileInputStream fis = new FileInputStream("src/main/resources/data.properties")) {
            properties.load(fis);
            LoggerUtil.info("Loaded base config: data.properties");
        } catch (IOException e) {
            LoggerUtil.warn("data.properties not found - skipping base config");
        }

        // Step 2: Overlay app-specific config (overrides base keys where defined)
        if (!APP.isEmpty()) {
            String appConfigPath = "src/main/resources/config/" + APP + "/" + ENV + ".properties";
            try (FileInputStream fis = new FileInputStream(appConfigPath)) {
                properties.load(fis);
                LoggerUtil.info("Loaded app config: " + appConfigPath);
            } catch (IOException e) {
                LoggerUtil.warn("App config not found at: " + appConfigPath + " - using base config only");
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
