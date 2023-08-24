package it.unipi.lab3.abalderi1.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigHandler {
    private Properties properties;

    public ConfigHandler(String filePath) throws IOException {
        properties = new Properties();
        FileInputStream input = new FileInputStream(filePath);
        properties.load(input);
        input.close();
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }
}
