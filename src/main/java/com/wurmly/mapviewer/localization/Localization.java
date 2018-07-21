package com.wurmly.mapviewer.localization;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.common.io.Resources;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

public enum Localization
{
    INSTANCE;

    private ResourceBundle messages;
    private ResourceBundle mnemonics;
    private Locale locale;
    private HashMap<String, Integer> keys = new HashMap<>();

    Localization()
    {
        setLocale(Locale.getDefault());
    }

    @Contract(pure = true)
    public static Localization getInstance()
    {
        return INSTANCE;
    }

    @NotNull
    public String getMessageFor(final String key)
    {
        return messages.getString(key);
    }

    @NotNull
    public String getTemplatedMessageFor(final String key, String... pairs)
    {
        if (pairs.length == 0 || pairs.length % 2 != 0)
            throw new IllegalArgumentException("Pairs must be key-value pairs of strings");
        HashMap<String, String> scope = new HashMap<>();
        for(int i = 0; i < pairs.length; i+=2)
        {
            scope.put(pairs[i], pairs[i+1]);
        }
        return getTemplatedMessageFor(key, scope);
    }

    @NotNull
    public String getTemplatedMessageFor(final String key, final Map vars)
    {
        MustacheFactory mf = new DefaultMustacheFactory();
        Mustache m = mf.compile(new StringReader(getMessageFor(key)), key);
        StringWriter sw = new StringWriter();
        m.execute(sw, vars);
        return sw.toString();
    }

    private int getKeyValue(final String key)
    {
        if (keys.isEmpty())
        {
            try
            {
                Field[] fields = KeyEvent.class.getFields();
                for (Field field : fields)
                {
                    if (!field.getName().startsWith("VK_"))
                        continue;
                    Class<?> typ = field.getType();
                    if (typ == int.class)
                        keys.put(field.getName(), field.getInt(null));
                }
            }
            catch (IllegalAccessException ignored)
            {
                // we simply won't cache fields we cannot access
            }
        }
        return keys.getOrDefault(key, Integer.MAX_VALUE);
    }

    public int getMnemonicFor(final String key)
    {
        return getKeyValue(mnemonics.getString(key));
    }

    private String tryGet(final String file)
    {
        try
        {
            final URL resource = Resources.getResource(file);
            return Resources.toString(resource, Charsets.UTF_8);
        }
        catch (IOException | IllegalArgumentException ignored)
        {
            // can ignore, we return empty string if not found
        }
        return "";
    }

    private String getLocaleIdentifier()
    {
        return locale.getLanguage() + "_" + locale.getCountry();
    }

    @NotNull
    private String applyLocale(final String file)
    {
        return Files.getNameWithoutExtension(file) + "_" + getLocaleIdentifier() + "." + Files.getFileExtension(file);
    }

    private static String html(final String file)
    {
        if (file.startsWith("html/")) return file;
        return "html/" + file;
    }

    public String getHtmlFor(final String file)
    {
        String html = tryGet(html(applyLocale(file)));
        if (html.isEmpty())
            html = tryGet(html(file));
        return html;
    }

    @Contract(pure = true)
    public Locale getLocale()
    {
        return locale;
    }

    public void setLocale(final Locale newLocale)
    {
        locale = newLocale;
        messages = ResourceBundle.getBundle("localization/Messages", locale);
        mnemonics = ResourceBundle.getBundle("localization/Mnemonics", locale);
    }
}
