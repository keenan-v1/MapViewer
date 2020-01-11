package com.wurmly.mapviewer.cli;

import com.wurmly.mapviewer.configuration.Parameters;
import com.wurmly.mapviewer.shared.ImageFileTypeFilter;
import com.wurmly.mapviewer.shared.MapType;
import com.wurmly.mapviewer.shared.Shared;
import com.wurmonline.wurmapi.api.MapData;
import com.wurmonline.wurmapi.api.WurmAPI;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

public class Mapper
{
    private static String getInputFolder()
    {
        String inputFolder = Parameters.INSTANCE.getInputFolder();
        if (inputFolder == null)
        {
            System.err.println("Input folder required in CLI mode.");
            System.exit(1);
        }
        return inputFolder;
    }

    private static String getOutputFolder()
    {
        String outputFolder = Parameters.INSTANCE.getOutputFolder(null);
        if (outputFolder == null)
        {
            System.err.println("Output folder required in CLI mode.");
            System.exit(1);
        }
        return outputFolder;
    }

    private static WurmAPI openMap(final String inputFolder)
    {
        try
        {
            return WurmAPI.open(inputFolder);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            System.exit(1);
        }
        return null;
    }

    private static int getFileTypeFilter()
    {
        switch(Parameters.INSTANCE.imageType())
        {
            case "jpeg": return ImageFileTypeFilter.TYPE_JPG;
            case "bmp": return ImageFileTypeFilter.TYPE_BMP;
            default:
            case "png": return ImageFileTypeFilter.TYPE_PNG;
        }
    }

    private static void saveImage(BufferedImage img, File outFile)
    {
        System.out.println("Saving image to " + outFile.toString());
        try
        {
            ImageIO.write(img, Parameters.INSTANCE.imageType(), outFile);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static void writeTopographical(MapData data, String mapName)
    {
        BufferedImage img = data.createTopographicDump(Parameters.INSTANCE.showWater(), Parameters.INSTANCE.contourLines());
        File outFile = getFullOutputFile(Shared.getSaveFileName(false, getFileTypeFilter(), MapType.MAP_TOPOGRAPHICAL, mapName));
        saveImage(img, outFile);
    }

    private static void writeTerrain(MapData data, String mapName)
    {
        BufferedImage img = data.createTerrainDump(Parameters.INSTANCE.showWater());
        File outFile = getFullOutputFile(Shared.getSaveFileName(false, getFileTypeFilter(), MapType.MAP_TERRAIN, mapName));
        saveImage(img, outFile);
    }

    private static File getFullOutputFile(final String fileName)
    {
        return Paths.get(getOutputFolder(), fileName).toFile();
    }

    private static void writeIsometric(MapData data, String mapName)
    {
        BufferedImage img = data.createMapDump();
        File outFile = getFullOutputFile(Shared.getSaveFileName(false, getFileTypeFilter(), MapType.MAP_NORMAL, mapName));
        saveImage(img, outFile);
    }

    public static void start()
    {
        String mapName = getInputFolder().substring(getInputFolder().lastIndexOf(File.separator) + 1);
        WurmAPI wurmAPI = openMap(getInputFolder());
        if (Parameters.INSTANCE.isTopographic())
            writeTopographical(wurmAPI.getMapData(), mapName);
        if (Parameters.INSTANCE.isTerrain())
            writeTerrain(wurmAPI.getMapData(), mapName);
        if (Parameters.INSTANCE.isIsometric())
            writeIsometric(wurmAPI.getMapData(), mapName);
    }
}
