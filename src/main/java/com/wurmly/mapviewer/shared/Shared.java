package com.wurmly.mapviewer.shared;

import org.jetbrains.annotations.Contract;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Shared
{
    @Contract(pure = true)
    private Shared() {}

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMdd-hhmmss");
    public static final ImageFileTypeFilter[] fileTypes = {
            new ImageFileTypeFilter(ImageFileTypeFilter.TYPE_PNG),
            new ImageFileTypeFilter(ImageFileTypeFilter.TYPE_JPG),
            new ImageFileTypeFilter(ImageFileTypeFilter.TYPE_GIF),
            new ImageFileTypeFilter(ImageFileTypeFilter.TYPE_BMP)
    };

    public static String getSaveFileName(boolean isScreenCap, int fileType, MapType mapType, String mapName)
    {
        String fDateTime = dateFormat.format(new Date());
        String saveFileName;
        if (isScreenCap)
        {
            saveFileName = mapName + "-mapdetail_" + fDateTime;
        }
        else
        {
            switch (mapType)
            {
                case MAP_TOPOGRAPHICAL:
                    saveFileName = mapName + "-topographical_" + fDateTime;
                    break;
                case MAP_CAVE:
                    saveFileName = mapName + "-ores_" + fDateTime;
                    break;
                case MAP_TERRAIN:
                    saveFileName = mapName + "-terrain_" + fDateTime;
                    break;
                case MAP_NORMAL:
                    saveFileName = mapName + "-isometric_" + fDateTime;
                    break;
                case MAP_ALL:
                default:
                    saveFileName = mapName + fDateTime;
            }
        }
        if (fileType != -1)
            saveFileName += fileTypes[fileType].getExtension();
        return saveFileName;
    }
}
