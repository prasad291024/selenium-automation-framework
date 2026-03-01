package com.prasad_v.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigManager {

    private static Properties properties;
    private static final String ENV = System.getProperty("env", "qa");

    static {
        loadProperties();
    }

    private static void loadProperties() {
        properties = new Properties();
        String configPath = "src/main/resources/config/" + ENV + ".properties";
        
        try (FileInputStream fis = new FileInputStream(configPath)) {
            properties.load(fis);
            LoggerUtil.info("Loaded configuration for environment: " + ENV);
        } catch (IOException e) {
            LoggerUtil.warn("Environment config not found, loading default data.properties");
            try (FileInputStream fis = new FileInputStream("src/main/resources/data.properties")) {
                properties.load(fis);
            } catch (IOException ex) {
                LoggerUtil.error("Failed to load properties", ex);
            }
        }
    }

    public static String get(String key) {
        return properties.getProperty(key);
    }

    public static String getEnvironment() {
        return ENV;
    }
}
