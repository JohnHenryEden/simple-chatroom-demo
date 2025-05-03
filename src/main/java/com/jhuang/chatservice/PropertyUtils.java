package com.jhuang.chatservice;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

public class PropertyUtils {
    public static String getProperty(String key, String propertyFileName) throws IOException {

        String rootPath = Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("")).getPath();
        String serverConfigPath = rootPath + propertyFileName;
        Properties serverConfig = new Properties();
        serverConfig.load(new FileInputStream(serverConfigPath));
        return serverConfig.getProperty(key);
    }
}
