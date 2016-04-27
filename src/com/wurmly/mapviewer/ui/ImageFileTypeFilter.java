package com.wurmly.mapviewer.ui;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class ImageFileTypeFilter extends FileFilter {
	public static final int TYPE_PNG = 0;
	public static final int TYPE_JPG = 1;
	public static final int TYPE_GIF = 2;
	public static final int TYPE_BMP = 3;	
	private static final String[] extensions = { ".png", ".jpg", ".gif", ".bmp" };
	private static final String[] descriptions = { "PNG", "JPG", "GIF", "BMP" };
	private String extension;
	private String description;
	private int type;

    public ImageFileTypeFilter(int fileType) {
        this.extension = extensions[fileType];
        this.description = descriptions[fileType];
        this.type = fileType;
    }

    @Override
    public boolean accept(File file) {
        if (file.isDirectory()) {
            return true;
        }
        return file.getName().endsWith(extension);
    }
	
	@Override
    public String getDescription() {
        return description + String.format(" (*%s)", extension);
    }
	
	public static String getExtenstion(int type) {
		return extensions[type];
	}
	
	public int getType() {
		return type;
	}
	
	public String getExtension() {
		return this.extension;
	}
}
