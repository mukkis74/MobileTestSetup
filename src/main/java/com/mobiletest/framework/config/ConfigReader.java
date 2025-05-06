package com.mobiletest.framework.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * ConfigReader is responsible for reading configuration properties from a properties file.
 */
public class ConfigReader {
    
    private static final Properties properties = new Properties();
    private static final String DEFAULT_CONFIG_PATH = "src/main/resources/config/config.properties";
    private static boolean isInitialized = false;
    
    /**
     * Initialize the ConfigReader with the default configuration file.
     */
    public static void initialize() {
        initialize(DEFAULT_CONFIG_PATH);
    }
    
    /**
     * Initialize the ConfigReader with a specific configuration file.
     * 
     * @param configPath The path to the configuration file
     */
    public static void initialize(String configPath) {
        try (InputStream input = new FileInputStream(configPath)) {
            properties.load(input);
            isInitialized = true;
        } catch (IOException e) {
            throw new RuntimeException("Failed to load configuration file: " + configPath, e);
        }
    }
    
    /**
     * Get a property value from the configuration.
     * 
     * @param key The property key
     * @return The property value
     */
    public static String getProperty(String key) {
        if (!isInitialized) {
            initialize();
        }
        return properties.getProperty(key);
    }
    
    /**
     * Get a property value from the configuration with a default value.
     * 
     * @param key The property key
     * @param defaultValue The default value to return if the key is not found
     * @return The property value or the default value
     */
    public static String getProperty(String key, String defaultValue) {
        if (!isInitialized) {
            initialize();
        }
        return properties.getProperty(key, defaultValue);
    }
    
    /**
     * Check if a property exists in the configuration.
     * 
     * @param key The property key
     * @return True if the property exists, false otherwise
     */
    public static boolean hasProperty(String key) {
        if (!isInitialized) {
            initialize();
        }
        return properties.containsKey(key);
    }
}