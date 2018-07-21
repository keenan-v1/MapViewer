package com.wurmly.mapviewer.ui;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.swing.filechooser.FileFilter;
import java.io.File;

class ImageFileTypeFilter extends FileFilter
{
    static final int TYPE_PNG = 0;
    static final int TYPE_JPG = 1;
    static final int TYPE_GIF = 2;
    static final int TYPE_BMP = 3;
    private static final String[] extensions = {".png", ".jpg", ".gif", ".bmp"};
    private static final String[] descriptions = {"PNG", "JPG", "GIF", "BMP"};
    private final String extension;
    private final String description;
    private final int type;

    ImageFileTypeFilter(int fileType)
    {
        this.extension = extensions[fileType];
        this.description = descriptions[fileType];
        this.type = fileType;
    }

    @Override
    public boolean accept(@NotNull File file)
    {
        if (file.isDirectory())
        {
            return true;
        }
        return file.getName().endsWith(extension);
    }

    @NotNull
    @Override
    public String getDescription()
    {
        return description + String.format(" (*%s)", extension);
    }

    @Contract(pure = true)
    public static String getExtension(int type)
    {
        return extensions[type];
    }

    int getType()
    {
        return type;
    }

    String getExtension()
    {
        return this.extension;
    }
}
