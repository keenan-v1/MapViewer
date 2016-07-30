package com.wurmly.mapviewer.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
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
	private MapViewerFrame mapFrame;
	private int imageX = 0;
	private int imageY = 0;
	private int startX = 0;
	private int startY = 0;
	public static final int DIR_UP = 1;
	public static final int DIR_DOWN = 2;
	public static final int DIR_RIGHT = 3;
	public static final int DIR_LEFT = 4;
	public static final double ZOOM_FACTOR = 0.5f;
	public static final double ZOOM_SHIFT = 2f;
	public static final double ZOOM_MOUSE = 1.25f;
	public static final int MOVE_FACTOR = 25;
	public static final double MOVE_SHIFT = 2.5f;

	public MapPanel(MapViewerFrame mapFrame) {
		super();
		this.setMapFrame(mapFrame);
		this.mapImage = new BufferedImage(256, 256, BufferedImage.TYPE_BYTE_GRAY);
	    addMouseWheelListener(new MouseAdapter() {

	        @Override
	        public void mouseWheelMoved(MouseWheelEvent e) {
	            double delta = 0.05f * e.getPreciseWheelRotation();
	            if(delta < 0)
	            	zoomIn(e.isShiftDown(), false, e.getPoint());
	            else
	            	zoomOut(e.isShiftDown(), false, e.getPoint());
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
				Point m = translateMapPointScaled(e.getPoint());
	    		mapFrame.setTileStatus(m.x, m.y);
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
	    
	    this.addMouseMotionListener(new MouseMotionAdapter() {
	    	@Override
	    	public void mouseMoved(MouseEvent e) {
	    		if(mapFrame.getMapType() == MapType.MAP_NORMAL)
	    			return;
	    		if(mapFrame.getMapToolbar().getShowMouseover().isSelected()) {
		    		Point p = translateMapPointScaled(e.getPoint());
		    		String ttText = mapFrame.getToolTipInfo(p.x, p.y);
		    		if(ttText == null)
		    			return;
		    		setToolTipText(ttText);
	    		} else
	    			setToolTipText(null);
	    	}
	    });
	}
	
	public void moveImage(final int d, boolean isShiftDown) {
		int offset = MOVE_FACTOR;
		if(isShiftDown)
			offset *= MOVE_SHIFT;
		switch(d) {
		case DIR_UP:
			imageY += offset;
			break;
		case DIR_DOWN:
			imageY -= offset;
			break;
		case DIR_LEFT:
			imageX += offset;
			break;
		case DIR_RIGHT:
			imageX -= offset;
			break;
		}
		checkBounds();
		repaint();
	}
	
	public void zoomIn(boolean isShiftDown) {
		zoomIn(isShiftDown, false);
	}

	public void zoomIn(boolean isShiftDown, boolean isScroll) {
		zoomIn(isShiftDown, isScroll, null);
	}

	public void zoomIn(boolean isShiftDown, boolean isScroll, Point p) {
        double zoom = ZOOM_FACTOR;
        if(isScroll)
        	zoom = zoom / ZOOM_MOUSE;
        if(isShiftDown)
        	zoom *= ZOOM_SHIFT;
        
        
        int iW = getImageWidth();
        int iH = getImageHeight();
        
        if ((p == null)) {
            scale += zoom;
            if(scale <= minScale)
            	scale = minScale;

	        if(getImageWidth() < getWidth())
	        	imageX = (getWidth()/2) - (getImageWidth()/2);
	        else
	        	imageX -= (getImageWidth() - iW)/4;
	
	        if(getImageHeight() < getHeight())
	        	imageY = (getHeight()/2) - (getImageHeight()/2);
	        else
	        	imageY -= (getImageHeight() - iH)/4;
        } else {
        	Point pScale = translateMapPointScaled(p);
        	
            scale += zoom;
            if(scale <= minScale)
            	scale = minScale;

            imageX = (int)(p.x - pScale.getX()*scale);
        	imageY = (int)(p.y - pScale.getY()*scale);
        }
                
        checkBounds();
        revalidate();
        repaint();
	}
	
	public void zoomOut(boolean isShiftDown) {
		zoomOut(isShiftDown, false);
	}

	public void zoomOut(boolean isShiftDown, boolean isScroll) {
		zoomOut(isShiftDown, false, null);
	}

	public void zoomOut(boolean isShiftDown, boolean isScroll, Point p) {
        double zoom = ZOOM_FACTOR;
        if(isScroll)
        	zoom = zoom / ZOOM_MOUSE;
        if(isShiftDown)
        	zoom *= ZOOM_SHIFT;
        
        
        int iW = getImageWidth();
        int iH = getImageHeight();
        
        if ((p == null)) {
	        scale -= zoom;
	        if(scale <= minScale)
	        	scale = minScale;
	
	        if(getImageWidth() < getWidth())
	        	imageX = (getWidth()/2) - (getImageWidth()/2);
	        else
	        	imageX += (iW - getImageWidth())/4;
	        if(getImageHeight() < getHeight())
	        	imageY = (getHeight()/2) - (getImageHeight()/2);
	        else
	        	imageY += (iH - getImageHeight())/4;
        } else {
        	Point pScale = translateMapPointScaled(p);

            scale -= zoom;
            if(scale <= minScale)
            	scale = minScale;

            imageX = (int)(p.x - pScale.getX()*scale);
        	imageY = (int)(p.y - pScale.getY()*scale);
        }
        
        checkBounds();
        revalidate();
        repaint();		
	}

	private Point translateMapPoint(Point p) {
		Point m = new Point();
		if(imageX > 0) {
			if(p.getX() < imageX)
				m.x = 0;
			else if(p.getX() > (imageX + getImageWidth()))
				m.x = getImageWidth();
			else
				m.x = p.x - imageX;			
		} else if(imageX == 0) {
			m.x = p.x;
		} else if(imageX < 0){
			m.x = p.x + Math.abs(imageX);
		}
		
		if(imageY > 0) {
			if(p.getY() < imageY)
				m.y = 0;
			else if(p.getY() > (imageY + getImageHeight()))
				m.y = getImageHeight();
			else
				m.y = p.y - imageY;
		} else if(imageY == 0) {
			m.y = p.y;
		} else if(imageY < 0){
			m.y = p.y + Math.abs(imageY);
		}
		return m;
	}
	
	private int fastFloor(double x) {
		return x > 0 ? (int) x : (int) x - 1;
	}
	private Point translateMapPointScaled(Point p) {
		Point m = translateMapPoint(p);
		m.x = fastFloor(m.x / scale);
		m.y = fastFloor(m.y / scale);
		return m;
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
		this.setToolTipText(null);
		int oldHeight = mapImage.getHeight();
		this.mapImage = i;
		updateScale();
		if(oldHeight != mapImage.getHeight())
			scale = minScale;
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

	public MapViewerFrame getMapFrame() {
		return mapFrame;
	}

	public void setMapFrame(MapViewerFrame mapFrame) {
		this.mapFrame = mapFrame;
	}	
}
