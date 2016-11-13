package com.wurmly.mapviewer.ui;

import com.wurmonline.wurmapi.api.WurmAPI;

import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.event.IIOWriteProgressListener;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRootPane;
import javax.swing.SwingWorker;

import java.awt.GridBagLayout;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;

public class TaskFrame extends JFrame {

	private static final long serialVersionUID = -5775039732655720287L;
	private JLabel label;
	private JPanel contentPane;
	private JProgressBar progressBar;
	private MapViewerFrame mapFrame;
	private String labelStyle = "text-align: center; font: bold 15px tahoma; vertial-align: middle;";

	public TaskFrame() {
		super();
		getContentPane().setLayout(new BorderLayout());
		setAlwaysOnTop(true);
		setResizable(false);
		getRootPane().setWindowDecorationStyle(JRootPane.NONE);
		getRootPane().setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, Color.DARK_GRAY));
		setUndecorated(true);
		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
	}
	
	private JLabel setLabel(String text) {
		return new JLabel("<html><p style='"+labelStyle+"'>" + text + "</p></html>");
	}
	
	private void buildElements(boolean showProgress, String labelText) {
		contentPane = new JPanel();
		contentPane.setSize(395,45);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{MapViewerFrame.TASKFRAME_WIDTH - 10, 0};
		if(showProgress)
			gridBagLayout.rowHeights = new int[]{35, 15, 0};
		else
			gridBagLayout.rowHeights = new int[]{50, 0};
		gridBagLayout.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		if(showProgress)
			gridBagLayout.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		else
			gridBagLayout.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		contentPane.setLayout(gridBagLayout);
		label = setLabel(labelText);
		GridBagConstraints gbc_label = new GridBagConstraints();
		gbc_label.anchor = GridBagConstraints.CENTER;
		if(showProgress)
			gbc_label.insets = new Insets(0, 0, 5, 0);
//		else
	//		gbc_label.fill = GridBagConstraints.HORIZONTAL;
		gbc_label.gridx = 0;
		gbc_label.gridy = 0;
		contentPane.add(label, gbc_label);
		if(showProgress) {
			progressBar = new JProgressBar(0, 100);
			progressBar.setValue(0);
			progressBar.setStringPainted(true);
			GridBagConstraints gbc_progressBar = new GridBagConstraints();
			gbc_progressBar.fill = GridBagConstraints.BOTH;
			gbc_progressBar.gridx = 0;
			gbc_progressBar.gridy = 1;
			contentPane.add(progressBar, gbc_progressBar);
		}
		getContentPane().add(contentPane);
	}
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(MapViewerFrame.TASKFRAME_WIDTH, MapViewerFrame.TASKFRAME_HEIGHT);
	}
	
	public void saveImage(BufferedImage img, String imgType, File destFile) {
		Logger.getLogger(TaskFrame.class.getName()).log(Level.INFO, "Saving to " + destFile);
		buildElements(true, "Saving");
		SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
			@Override
			public Void doInBackground() {
				try (ImageOutputStream ios = ImageIO.createImageOutputStream(destFile)) {
					Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName(imgType);
					if (writers.hasNext()) {
						ImageWriter writer = writers.next();
						writer.addIIOWriteProgressListener(new IIOWriteProgressListener() {
							@Override
							public void imageProgress(ImageWriter source, float percentageDone) {
								setProgress(Math.round(percentageDone));
							}
	
							@Override
							public void imageComplete(ImageWriter arg0) {
								// TODO Auto-generated method stub
								
							}
	
							@Override
							public void imageStarted(ImageWriter arg0, int arg1) {
								// TODO Auto-generated method stub
								
							}
	
							@Override
							public void thumbnailComplete(ImageWriter arg0) {
								// TODO Auto-generated method stub
								
							}
	
							@Override
							public void thumbnailProgress(ImageWriter arg0, float arg1) {
								// TODO Auto-generated method stub
								
							}
	
							@Override
							public void thumbnailStarted(ImageWriter arg0, int arg1, int arg2) {
								// TODO Auto-generated method stub
								
							}
	
							@Override
							public void writeAborted(ImageWriter arg0) {
								// TODO Auto-generated method stub
								
							}
						});
						writer.setOutput(ios);
						try {
							writer.write(img);
						} catch(Exception ex) {
							Logger.getLogger(TaskFrame.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
						} finally {
							writer.removeAllIIOWriteProgressListeners();
						}
					}
				
				} catch(Exception ex) {
					Logger.getLogger(TaskFrame.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
				}
				return null;
			}
			
			@Override
			public void done() {
				TaskFrame.this.setVisible(false);
		        Logger.getLogger(TaskFrame.class.getName()).log(Level.INFO, "Save completed.");				
				//JOptionPane.showMessageDialog(TaskFrame.this, "Image saved to: " + destFile, "Success", JOptionPane.PLAIN_MESSAGE);
				return;
			}
		};
        worker.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(final PropertyChangeEvent evt) {
                if ("progress".equalsIgnoreCase(evt.getPropertyName())) {
                    progressBar.setValue((int) evt.getNewValue());
                }
            }
        });
        worker.execute();
		setVisible(true);
	}
	
	public void loadApi(MapViewerFrame mFrame, File destFile) {
		String filename = destFile.toString().substring(destFile.toString().lastIndexOf(File.separator)+1);
		Logger.getLogger(TaskFrame.class.getName()).log(Level.INFO, "Loading map from " + destFile);
		//label.setFont(new Font("Tahoma", Font.BOLD, 15));
		buildElements(false, "Loading " + filename);
		mapFrame = mFrame;
		SwingWorker<WurmAPI, Void> worker = new SwingWorker<WurmAPI, Void>() {
			@Override
			public WurmAPI doInBackground() {
				WurmAPI api = null;
				try {
					api = WurmAPI.open(destFile.toString());
				}
				catch(IOException e) {
					Logger.getLogger(TaskFrame.class.getName()).log(Level.SEVERE, e.getMessage(), e);
				}					
				return api;
			}
			
			@Override
			public void done() {
				TaskFrame.this.setVisible(false);
		        Logger.getLogger(TaskFrame.class.getName()).log(Level.INFO, "Load completed.");
		        try {
					mapFrame.setApi(get());
				} catch (InterruptedException e) {
					Logger.getLogger(TaskFrame.class.getName()).log(Level.SEVERE, e.getMessage(), e);
				} catch (ExecutionException e) {
					Logger.getLogger(TaskFrame.class.getName()).log(Level.SEVERE, e.getMessage(), e);
				}
				return;
			}
		};
        worker.execute();
		setVisible(true);
	}
	
	public void updateMap(MapViewerFrame mFrame) {
		Logger.getLogger(TaskFrame.class.getName()).log(Level.INFO, "Updating map...");
		//label.setFont(new Font("Tahoma", Font.BOLD, 20));
		buildElements(false,"Rendering map...");
		mapFrame = mFrame;
		SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
			@Override
			public Void doInBackground() {
				mFrame.updateMap();
				return null;
			}
			
			@Override
			public void done() {
				TaskFrame.this.setVisible(false);
		        Logger.getLogger(TaskFrame.class.getName()).log(Level.INFO, "Update completed.");
				return;
			}
		};
        worker.execute();
		setVisible(true);
	}
}

