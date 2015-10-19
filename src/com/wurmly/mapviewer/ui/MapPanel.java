package com.wurmly.mapviewer.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class MapPanel extends JPanel {

	private static final long serialVersionUID = 7045193695244132412L;
	private BufferedImage mapImage;
	private double scale = 0.0f;
	private double minScale = 1.0f;
	private int imageX = 0;
	private int imageY = 0;
	private int startX = 0;
	private int startY = 0;

	public MapPanel() {
		super();
		this.mapImage = new BufferedImage(1024, 1024, BufferedImage.TYPE_BYTE_GRAY);
		this.setDefaultImage();	
	    addMouseWheelListener(new MouseAdapter() {

	        @Override
	        public void mouseWheelMoved(MouseWheelEvent e) {
	            double delta = 0.05f * e.getPreciseWheelRotation();
	            if(e.isShiftDown())
	            	delta *= 2;
	            int preH = getImageHeight();
	            int preW = getImageWidth();
	            scale -= delta;
	            if(scale <= minScale)
	            	scale = minScale;
	            int offY = (int)((getImageHeight() - preH) / 2);
	            int offX = (int)((getImageWidth() - preW) / 2);
            	imageX -= offX;
            	imageY -= offY;
	            checkBounds();
	            revalidate();
	            repaint();
	        }

	    });
	    
	    this.addComponentListener(new ComponentAdapter() {
	    	public void componentResized(ComponentEvent e) {
	    		updateScale();
	    		checkBounds();
	    	}
	    });
	    
	    this.addMouseListener(new MouseAdapter() {
	    	@Override
	    	public void mousePressed(MouseEvent e) {
	    		super.mousePressed(e);
	    		startX = e.getX();
	    		startY = e.getY();
	    	}
	    });
	    
	    this.addMouseMotionListener(new MouseMotionAdapter() {
	    	@Override
	    	public void mouseDragged(MouseEvent e) {
	    		if(e.getX() < startX)
	    			imageX -= (startX - e.getX());
	    		else if(e.getX() > startX)
	    			imageX += (e.getX() - startX);
	    		if(e.getY() < startY)
	    			imageY -= (startY - e.getY());
	    		else if(e.getY() > startY)
	    			imageY += (e.getY() - startY);
	    		startX = e.getX();
	    		startY = e.getY();
	    		checkBounds();
	    		repaint();
	    	}
	    });
	}
	
	@Override
	public void paintComponent(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		g.drawImage(this.mapImage, imageX, imageY, getImageWidth(), getImageHeight(), null);
	}

	public void setMapImage(BufferedImage i) {
		if(i == null)
			return;
		this.mapImage = i;

		updateScale();
		checkBounds();
		this.repaint();
	}
	
	private void updateScale() {
		if(this.getWidth() < this.getHeight())
			this.minScale = (double)this.getWidth() / (double)mapImage.getWidth();
		if(this.getHeight() < this.getWidth())
			this.minScale = (double)this.getHeight() / (double)mapImage.getHeight();
		if(this.scale < this.minScale)
			this.scale = this.minScale;
	}
	
	public BufferedImage getMapImage(boolean crop) {
		if(crop) {
			BufferedImage c = new BufferedImage(getWidth(), getHeight(), mapImage.getType());
			this.paint(c.getGraphics());
			return c;
		}
		else
			return this.mapImage;
	}
	
	private void setDefaultImage() {
		this.setDefaultImage("File > Open Map Folder");
	}
	
	private int getImageWidth() {
		return (int)Math.round(this.mapImage.getWidth() * this.scale);
	}
	
	private int getImageHeight() {
		return (int)Math.round(this.mapImage.getHeight() * this.scale);
	}
	
	private void checkBounds() {
		int wH = this.getHeight();
		int wW = this.getWidth();
		int iH = this.getImageHeight();
		int iW = this.getImageWidth();
		int minY = wH - iH;
		int minX = wW - iW;

		if(wW > iW)
			imageX = (wW / 2) - (iW / 2);
		else if(imageX < minX)
			imageX = minX;
		else if(imageX > 0)
			imageX = 0;

		if(wH > iH)
			imageY = (wH / 2) - (iH / 2);
		else if(imageY < minY)
			imageY = minY;
		else if(imageY > 0)
			imageY = 0;
	}
	
	//TODO Create a meaningful default image buffer
	private void setDefaultImage(String message) {
		//int w = this.mapImage.getWidth();
		//int h = this.mapImage.getHeight();
	}
	
}
