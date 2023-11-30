package com.fersko.info.config;

import com.fersko.info.exceptions.LoadPropertiesException;

import java.util.Properties;

public final class PropertiesUtils {

    private static final Properties PROPERTIES = new Properties();

    static {
        try {
            loadProperties();
        } catch (LoadPropertiesException e) {
            throw new LoadPropertiesException(e);
        }
    }

    public static void loadProperties() throws LoadPropertiesException {
        try (var stream = PropertiesUtils.class.getClassLoader().getResourceAsStream("application.properties")) {
            PROPERTIES.load(stream);
        } catch (Exception e) {
            throw new LoadPropertiesException(e);
        }
    }

    public static String get(String key) {
        return  PROPERTIES.getProperty(key);
    }

}
