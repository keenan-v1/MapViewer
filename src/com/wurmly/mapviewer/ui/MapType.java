package com.wurmly.mapviewer.ui;

public enum MapType {
	MAP_NORMAL("Normal"),
	MAP_TOPOGRAPHICAL("Topographical"),
	MAP_CAVE("Cave"),
	MAP_TERRAIN("Terrain"),
	MAP_ALL("All");
	
	private final String map;
	
	private MapType(String type) {
		this.map = type;
	}
	
	@Override
	public String toString() {
		return this.map;
	}
	
	public static MapType getType(String type) {
		switch(type) {
		case "Topographical":
			return MAP_TOPOGRAPHICAL;
		case "Cave":
			return MAP_CAVE;
		case "Terrain":
			return MAP_TERRAIN;
		case "All":
			return MAP_ALL;
		case "Normal":
		default:
			return MAP_NORMAL;

		}
	}
}
