package com.mobiletest.framework.config;

import com.mobiletest.framework.utils.TestUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * CapabilityManager is responsible for reading and managing device capabilities
 * from the capabilities.json file.
 */
public class CapabilityManager {
    
    private static final String CAPABILITIES_PATH = "src/main/resources/config/capabilities.json";
    private static JSONObject capabilitiesJson;
    
    /**
     * Initialize the CapabilityManager by loading the capabilities.json file.
     */
    public static void initialize() {
        try {
            String content = new String(Files.readAllBytes(Paths.get(CAPABILITIES_PATH)));
            capabilitiesJson = new JSONObject(content);
        } catch (IOException e) {
            TestUtils.logError("Failed to load capabilities file: " + CAPABILITIES_PATH, e);
            throw new RuntimeException("Failed to load capabilities file", e);
        }
    }
    
    /**
     * Get the DesiredCapabilities for a specific platform and device.
     * 
     * @param platform The platform (android or ios)
     * @param deviceName The device name
     * @return The DesiredCapabilities for the specified platform and device
     */
    public static DesiredCapabilities getCapabilities(String platform, String deviceName) {
        if (capabilitiesJson == null) {
            initialize();
        }
        
        DesiredCapabilities capabilities = new DesiredCapabilities();
        
        // Get platform-specific capabilities
        JSONObject platformJson = capabilitiesJson.getJSONObject(platform.toLowerCase());
        JSONObject commonCapabilities = platformJson.getJSONObject("capabilities");
        
        // Add common capabilities
        addJsonObjectToCapabilities(commonCapabilities, capabilities);
        
        // Find the specific device
        JSONArray devices = platformJson.getJSONArray("devices");
        JSONObject deviceJson = null;
        
        for (int i = 0; i < devices.length(); i++) {
            JSONObject device = devices.getJSONObject(i);
            if (device.getString("name").equals(deviceName)) {
                deviceJson = device;
                break;
            }
        }
        
        if (deviceJson == null) {
            TestUtils.logError("Device not found in capabilities: " + deviceName);
            throw new RuntimeException("Device not found in capabilities: " + deviceName);
        }
        
        // Add device-specific capabilities
        capabilities.setCapability("deviceName", deviceJson.getString("name"));
        capabilities.setCapability("platformName", platform);
        capabilities.setCapability("platformVersion", deviceJson.getString("platformVersion"));
        
        // Add locale if available
        if (deviceJson.has("locale")) {
            capabilities.setCapability("locale", deviceJson.getString("locale"));
        }
        
        return capabilities;
    }
    
    /**
     * Get a list of all available devices for a specific platform.
     * 
     * @param platform The platform (android or ios)
     * @return A list of device names
     */
    public static List<String> getDeviceNames(String platform) {
        if (capabilitiesJson == null) {
            initialize();
        }
        
        List<String> deviceNames = new ArrayList<>();
        JSONObject platformJson = capabilitiesJson.getJSONObject(platform.toLowerCase());
        JSONArray devices = platformJson.getJSONArray("devices");
        
        for (int i = 0; i < devices.length(); i++) {
            JSONObject device = devices.getJSONObject(i);
            deviceNames.add(device.getString("name"));
        }
        
        return deviceNames;
    }
    
    /**
     * Get cloud capabilities for a specific provider.
     * 
     * @param provider The cloud provider (browserstack or saucelabs)
     * @return The cloud capabilities
     */
    public static Map<String, Object> getCloudCapabilities(String provider) {
        if (capabilitiesJson == null) {
            initialize();
        }
        
        Map<String, Object> cloudCapabilities = new HashMap<>();
        JSONObject cloudJson = capabilitiesJson.getJSONObject("cloud");
        JSONObject providerJson = cloudJson.getJSONObject(provider.toLowerCase());
        
        // Add provider-specific capabilities
        addJsonObjectToMap(providerJson, cloudCapabilities);
        
        return cloudCapabilities;
    }
    
    /**
     * Add all properties from a JSONObject to a DesiredCapabilities object.
     * 
     * @param jsonObject The JSONObject containing the properties
     * @param capabilities The DesiredCapabilities object to add the properties to
     */
    private static void addJsonObjectToCapabilities(JSONObject jsonObject, DesiredCapabilities capabilities) {
        Iterator<String> keys = jsonObject.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            Object value = jsonObject.get(key);
            capabilities.setCapability(key, value);
        }
    }
    
    /**
     * Add all properties from a JSONObject to a Map.
     * 
     * @param jsonObject The JSONObject containing the properties
     * @param map The Map to add the properties to
     */
    private static void addJsonObjectToMap(JSONObject jsonObject, Map<String, Object> map) {
        Iterator<String> keys = jsonObject.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            Object value = jsonObject.get(key);
            
            // Handle nested JSONObjects
            if (value instanceof JSONObject) {
                Map<String, Object> nestedMap = new HashMap<>();
                addJsonObjectToMap((JSONObject) value, nestedMap);
                map.put(key, nestedMap);
            } else if (value instanceof JSONArray) {
                // Handle JSONArrays
                JSONArray jsonArray = (JSONArray) value;
                List<Object> list = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    Object item = jsonArray.get(i);
                    if (item instanceof JSONObject) {
                        Map<String, Object> nestedMap = new HashMap<>();
                        addJsonObjectToMap((JSONObject) item, nestedMap);
                        list.add(nestedMap);
                    } else {
                        list.add(item);
                    }
                }
                map.put(key, list);
            } else {
                // Handle primitive values
                map.put(key, value);
            }
        }
    }
}