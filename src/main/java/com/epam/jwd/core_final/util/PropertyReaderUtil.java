package com.epam.jwd.core_final.util;

import com.epam.jwd.core_final.Main;
import com.epam.jwd.core_final.domain.ApplicationProperties;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public final class PropertyReaderUtil {
    private static final Properties properties = new Properties();

    public PropertyReaderUtil() {
    }

    /**
     * try-with-resource using FileInputStream
     *
     * @see {https://www.netjstech.com/2017/09/how-to-read-properties-file-in-java.html for an example}
     * <p>
     * as a result - you should populate {@link ApplicationProperties} with corresponding
     * values from property file
     */
    public static void loadProperties() {
        final String propertiesFileName = "./src/main/resources/application.properties";
        try (FileInputStream fileInputStream = new FileInputStream(propertiesFileName)) {
            properties.load(fileInputStream);
        } catch (IOException e) {
            Main.LOGGER.error("IOException");
        }
    }

    public static Properties getProperties() {
        if (properties.size() == 0) {
            loadProperties();
        }
        return properties;
    }
}
