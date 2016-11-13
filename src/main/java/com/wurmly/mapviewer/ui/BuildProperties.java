package com.wurmly.mapviewer.ui;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;

public class BuildProperties
{
    private static final Properties properties = new Properties();
    static {
        try (InputStream inputStream = BuildProperties.class.getResourceAsStream("/build.properties")) {
            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read properties file", e);
        }
    }
    private BuildProperties() {}
    public static String getGitSha1() {
        return properties.getProperty("git-sha-1");
    }
    public static String getVersion() {
        return properties.getProperty("version");
    }
    public static String getBuildTimeString() {
        return properties.getProperty("build-time");
    }
}