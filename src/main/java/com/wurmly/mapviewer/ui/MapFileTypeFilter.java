package com.wurmly.mapviewer.ui;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.swing.filechooser.FileFilter;
import java.io.File;

class MapFileTypeFilter extends FileFilter
{
    private final String extension;
    private final String description;

    MapFileTypeFilter()
    {
        extension = ".map";
        description = "WurmAPI Map";
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
    public String getExtension()
    {
        return this.extension;
    }
}
