package com.prasad_v.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class ConfigManager {

    private static Properties properties;
    private static final String ENV = System.getProperty("env", "qa");
    private static final String APP = System.getProperty("app", "");
    private static final Set<String> SUPPORTED_APPS = Set.of("", "vwo", "orangehrm", "katalon");
    private static final Set<String> SUPPORTED_ENVS = Set.of("qa", "prod");

    static {
        loadProperties();
    }

    private static void loadProperties() {
        properties = new Properties();
        validateSelection();

        // Step 1: Always load data.properties as base (shared keys: grid, cloud, db)
        if (loadFromClasspathOrFile("data.properties")) {
            LoggerUtil.info("Loaded base config: data.properties");
        } else {
            LoggerUtil.warn("data.properties not found - skipping base config");
        }

        // Step 2: Overlay app-specific config (overrides base keys where defined)
        if (!APP.isEmpty()) {
            String appConfigPath = "config/" + APP + "/" + ENV + ".properties";
            if (loadFromClasspathOrFile(appConfigPath)) {
                LoggerUtil.info("Loaded app config: " + appConfigPath);
            } else {
                LoggerUtil.warn("App config not found at: " + appConfigPath + " - using base config only");
            }
        }

        // Validate required environment variables
        validateRequiredEnvironmentVariables();
    }

    private static void validateSelection() {
        if (!SUPPORTED_APPS.contains(APP)) {
            throw new IllegalArgumentException("Unsupported app: " + APP);
        }
        if (!SUPPORTED_ENVS.contains(ENV)) {
            throw new IllegalArgumentException("Unsupported env: " + ENV);
        }
    }

    // Default values for common test credentials (used when environment variables are not set)
    private static final Map<String, String> DEFAULT_CREDENTIAL_VALUES = new HashMap<>();

    static {
        // Initialize default credential values
        DEFAULT_CREDENTIAL_VALUES.put("VWO_USERNAME", "Admin");
        DEFAULT_CREDENTIAL_VALUES.put("VWO_PASSWORD", "admin");
        DEFAULT_CREDENTIAL_VALUES.put("VWO_INVALID_USERNAME", "InvalidUser");
        DEFAULT_CREDENTIAL_VALUES.put("VWO_INVALID_PASSWORD", "InvalidPass");
        DEFAULT_CREDENTIAL_VALUES.put("OHR_USERNAME", "Admin");
        DEFAULT_CREDENTIAL_VALUES.put("OHR_PASSWORD", "admin123");
    }

    private static void validateRequiredEnvironmentVariables() {
        // Define required environment variables for each app needs
        Map<String, List<String>> requiredEnvVars = new HashMap<>();
        requiredEnvVars.put("vwo", Arrays.asList("VWO_USERNAME", "VWO_PASSWORD", "VWO_INVALID_USERNAME", "VWO_INVALID_PASSWORD"));
        requiredEnvVars.put("orangehrm", Arrays.asList()); // OrangeHRM uses properties file (public demo instance)
        requiredEnvVars.put("katalon", Arrays.asList()); // Katalon uses properties file, not env vars

        List<String> requiredVars = requiredEnvVars.getOrDefault(APP, Collections.emptyList());
        for (String envVar : requiredVars) {
            String value = System.getenv(envVar);
            if (value == null || value.isBlank()) {
                LoggerUtil.warn("Required environment variable " + envVar + " is not set for " + APP + " application");
            }
        }
    }

    private static boolean loadFromClasspathOrFile(String resourcePath) {
        try (InputStream inputStream = ConfigManager.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (inputStream != null) {
                properties.load(inputStream);
                return true;
            }
        } catch (IOException e) {
            LoggerUtil.warn("Failed to load classpath config: " + resourcePath);
        }

        String filePath = "src/main/resources/" + resourcePath;
        try (FileInputStream fis = new FileInputStream(filePath)) {
            properties.load(fis);
            return true;
        } catch (IOException e) {
            return false;
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
        String systemValue = System.getProperty(key);
        if (systemValue != null && !systemValue.isBlank()) {
            return systemValue;
        }

        String value = properties.getProperty(key);
        if (value != null && value.startsWith("${") && value.endsWith("}")) {
            String envVar = value.substring(2, value.length() - 1);
            String envValue = System.getenv(envVar);
            if (envValue != null && !envValue.isBlank()) {
                return envValue;
            }
            // Check for default value
            String defaultValue = DEFAULT_CREDENTIAL_VALUES.get(envVar);
            if (defaultValue != null) {
                LoggerUtil.info("Environment variable " + envVar + " is not set. Using default value: " + defaultValue);
                return defaultValue;
            }
            LoggerUtil.warn("Environment variable " + envVar + " is not set.");
            return "";
        }
        return value;
    }

    public static String getRequired(String key) {
        String value = get(key);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Required configuration key '" + key + "' is missing or empty");
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
