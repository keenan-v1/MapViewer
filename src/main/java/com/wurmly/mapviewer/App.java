package com.wurmly.mapviewer;

import com.wurmly.mapviewer.cli.Mapper;
import com.wurmly.mapviewer.configuration.Parameters;
import com.wurmly.mapviewer.ui.MapViewerFrame;

public class App
{
    public static void main(String[] args)
    {
        Parameters.parseCommandLine(args);
        if (Parameters.INSTANCE.isCli()) {
            Mapper.start();
        } else {
            MapViewerFrame.start();
        }
    }
}
