package ru.training.apitesting.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyManager {
    private static final String PROPERTIES_FILE_PATH =
            "src/main/resources/properties/testEnvironment.properties";
    private static final String API_KEY_PROPERTY = "api.key";
    private static final String API_TOKEN_PROPERTY = "api.token";
    private static final String API_BASE_URL = "api.baseUrl";

    private static Properties properties;

    static {
        try (InputStream inputStream = new FileInputStream(PROPERTIES_FILE_PATH)) {
            properties = new Properties();
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Used if we want to reload changed property file in runtime
    public void load() {
        try (InputStream inputStream = new FileInputStream(PROPERTIES_FILE_PATH)) {
            properties = new Properties();
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getApiKey() {
        return properties.getProperty(API_KEY_PROPERTY);
    }

    public static String getApiToken() {
        return properties.getProperty(API_TOKEN_PROPERTY);
    }

    public static String getApiBaseUrl() {
        return properties.getProperty(API_BASE_URL);
    }
}
