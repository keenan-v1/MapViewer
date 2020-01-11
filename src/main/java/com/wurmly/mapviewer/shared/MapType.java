package com.wurmly.mapviewer.shared;

import com.wurmly.mapviewer.localization.Localization;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public enum MapType
{
    MAP_NORMAL(Localization.getInstance().getMessageFor("map-normal")),
    MAP_TOPOGRAPHICAL(Localization.getInstance().getMessageFor("map-topographical")),
    MAP_CAVE(Localization.getInstance().getMessageFor("map-cave")),
    MAP_TERRAIN(Localization.getInstance().getMessageFor("map-terrain")),
    MAP_ALL(Localization.getInstance().getMessageFor("map-all"));

    private final String map;

    MapType(String type)
    {
        this.map = type;
    }

    @Override
    public String toString()
    {
        return this.map;
    }

    @NotNull
    @Contract(pure = true)
    public static MapType getType(String type)
    {
        for (MapType value : values())
            if (value.map.equals(type))
                return value;
        return MAP_NORMAL;
    }
}
