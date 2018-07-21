package com.wurmly.mapviewer.properties;

import com.wurmly.mapviewer.localization.Localization;
import org.jetbrains.annotations.Contract;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public enum BuildProperties
{
    INSTANCE;

    private final Properties properties = new Properties();


    BuildProperties()
    {
        try (InputStream inputStream = BuildProperties.class.getResourceAsStream("/build.properties"))
        {
            properties.load(inputStream);
        }
        catch (IOException e)
        {
            throw new RuntimeException(Localization.getInstance().getMessageFor("properties-exception"), e);
        }
    }

    public String getProperty(final String key)
    {
        return properties.getProperty(key);
    }

    @Contract(pure = true)
    public static BuildProperties getInstance()
    {
        return INSTANCE;
    }

    public static String getVersion()
    {
        return getInstance().getProperty("version");
    }
}