package com.wurmly.mapviewer.configuration;

import org.apache.commons.cli.*;
import org.jetbrains.annotations.Nullable;

public enum Parameters
{
    INSTANCE;

    private CommandLine commandLine;

    public String getInputFolder()
    {
        return getOption("input");
    }

    public String getOutputFolder(final String defaultFolder)
    {
        return getOption("output", defaultFolder);
    }

    public boolean isCli()
    {
        return hasOption("cli");
    }

    public boolean isTopographic()
    {
        return hasOption("topographic");
    }

    public boolean isIsometric()
    {
        return hasOption("isometric");
    }

    public boolean isTerrain()
    {
        return hasOption("terrain");
    }

    @SuppressWarnings("SameParameterValue") // Remove when adding more options
    @Nullable
    private String getOption(final String key)
    {
        return commandLine == null ? null : commandLine.getOptionValue(key);
    }

    private String getOption(final String key, final String defaultValue)
    {
        return commandLine == null ? defaultValue : commandLine.getOptionValue(key, defaultValue);
    }

    private boolean hasOption(final String key)
    {
        return commandLine != null && commandLine.hasOption(key);
    }

    public short contourLines()
    {
        return Short.parseShort(getOption("contour", "250"));
    }

    public String imageType()
    {
        return getOption("type", "png");
    }

    public boolean showWater()
    {
        return !hasOption("nowater");
    }

    public static void parseCommandLine(String[] args)
    {
        Options options = new Options();
        options.addOption(new Option("input", true,"map folder for processing"));
        options.addOption(new Option("output", true, "image output folder"));
        options.addOption(new Option("cli", "don't initiate graphical interface"));
        options.addOption(new Option("topographic", "Output a topographic map image"));
        options.addOption(new Option("contour", true, "Contour line interval for topography."));
        options.addOption(new Option("nowater", "Don't show water on output"));
        options.addOption(new Option("isometric", "Output a isometric map image"));
        options.addOption(new Option("terrain", "Output a terrain map image"));
        options.addOption(new Option("type", true, "Image type: jpeg, png, bmp. Default: png"));

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();

        try {
            INSTANCE.commandLine = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("mapviewer", options);
            System.exit(1);
        }
    }
}
