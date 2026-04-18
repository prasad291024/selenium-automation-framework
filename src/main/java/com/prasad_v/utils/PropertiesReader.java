package com.prasad_v.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class PropertiesReader {

    // File - data.properties -> key -> value

    /**
     * Reads a value from data.properties.
     * Includes a null-check to prevent NullPointerException if the file is missing.
     *
     * @param key The key to read
     * @return The value from the properties file, or null if not found
     */
    public static String readKey(String key) {
        Properties p = new Properties(); // Initialize to avoid NPE
        String path = System.getProperty("user.dir") + "/src/main/resources/data.properties";
        
        try (FileInputStream fileInputStream = new FileInputStream(path)) {
            p.load(fileInputStream);
        } catch (FileNotFoundException e) {
            System.err.println("[ERROR] data.properties not found at: " + path);
        } catch (IOException e) {
            System.err.println("[ERROR] Failed to load data.properties: " + e.getMessage());
        }
        
        return p.getProperty(key);
    }
}
