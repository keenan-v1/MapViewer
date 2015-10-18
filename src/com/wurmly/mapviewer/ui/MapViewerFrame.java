package com.wurmly.mapviewer.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.wurmonline.mesh.Tiles.Tile;
import com.wurmonline.wurmapi.api.WurmAPI;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;
import javax.swing.JRadioButtonMenuItem;
import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;

public class MapViewerFrame extends JFrame {

	private static final long serialVersionUID = -7904984053821280873L;
	private MapOptions optionWindow;
	private MapPanel mapPanel;
	private JPanel contentPanel;
	private WurmAPI api;
	private JMenuItem mntmSaveMapImage;
	private JMenuItem mntmSaveViewArea;
	public static final int WINDOW_HEIGHT = 980;
	public static final int WINDOW_WIDTH = 980;
	public static final int OPTIONS_HEIGHT = 330;
	public static final int OPTIONS_WIDTH = 320;
	private static final int DEFAULT_X = 500;
	private static final int DEFAULT_Y = 0;
	private MapType mapType = MapType.MAP_NORMAL;
	private static final String[] mapFiles = {"top_layer.map", "flags.map", "map_cave.map", "resources.map", "rock_layer.map" };
	String defaultFolder = ".";
	File mapFolder;
	File saveFile;
	private final ButtonGroup mapViewGroup = new ButtonGroup();

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MapViewerFrame frame = new MapViewerFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public MapViewerFrame() {
		super("Map Viewer");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(DEFAULT_X, DEFAULT_Y, WINDOW_WIDTH + 16, WINDOW_HEIGHT + 62);
		setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);		
		this.mapFolder = new File(defaultFolder);
		this.saveFile = new File(defaultFolder);
		this.optionWindow = new MapOptions(this);
		int oX = (getWidth()/2) - (MapViewerFrame.OPTIONS_WIDTH/2) + getX();
		int oY = (getHeight()/2) - (MapViewerFrame.OPTIONS_HEIGHT/2) + getY();
		optionWindow.setBounds(oX, oY, MapViewerFrame.OPTIONS_WIDTH, MapViewerFrame.OPTIONS_HEIGHT);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmOpenMapFolder = new JMenuItem("Open Map Folder");
		mntmOpenMapFolder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser filePicker = new JFileChooser();
				filePicker.setCurrentDirectory(mapFolder);
				filePicker.setDialogTitle("Choose Map Folder");
				filePicker.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				filePicker.setAcceptAllFileFilterUsed(false);
				if( filePicker.showOpenDialog(MapViewerFrame.this) == JFileChooser.APPROVE_OPTION ) {
					File currentFile = filePicker.getSelectedFile();
					if(currentFile == null)
						mapFolder = filePicker.getCurrentDirectory();
					else if(currentFile.isDirectory())
						mapFolder = currentFile.getAbsoluteFile();
					else {
						JOptionPane.showMessageDialog(MapViewerFrame.this, "Invalid Selection. Please choose a folder.",
								"Invalid Map Folder", JOptionPane.ERROR_MESSAGE);
						return;
					}
					for( int i = 0; i < mapFiles.length; i++ ) {
						currentFile = new File(mapFolder.toString() + "\\" + mapFiles[i]);
						if(!currentFile.exists()) {
							JOptionPane.showMessageDialog(MapViewerFrame.this, "File: " + mapFiles[i] + " not found.",
									"Missing Files", JOptionPane.ERROR_MESSAGE);
							return;						
						}
					}
				}
				loadMapFolder();
				updateMap();
			}
		});
		mnFile.add(mntmOpenMapFolder);
		
		mntmSaveMapImage = new JMenuItem("Save Map Image");
		mntmSaveMapImage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(getSaveFile())
					saveMapImage(false);
			}
		});
		mntmSaveMapImage.setEnabled(false);
		mnFile.add(mntmSaveMapImage);
		
		mntmSaveViewArea = new JMenuItem("Save View Area");
		mntmSaveViewArea.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(getSaveFile())
					saveMapImage(true);
			}
		});
		mntmSaveViewArea.setEnabled(false);
		mnFile.add(mntmSaveViewArea);
		
		JSeparator separator = new JSeparator();
		mnFile.add(separator);
		
	
		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MapViewerFrame.this.dispatchEvent(new WindowEvent(MapViewerFrame.this, WindowEvent.WINDOW_CLOSING));
			}
		});		
		mnFile.add(mntmExit);
		
		JMenu mnEdit = new JMenu("Edit");
		menuBar.add(mnEdit);
		
		JMenuItem mntmOptions = new JMenuItem("Options");
		mntmOptions.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				optionWindow.setVisible(true);
			}
		});
		mnEdit.add(mntmOptions);
		
		JMenu mnView = new JMenu("View");
		menuBar.add(mnView);
		
		JRadioButtonMenuItem rdbtnmntmNormal = new JRadioButtonMenuItem("Normal");
		rdbtnmntmNormal.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				mapType = MapType.MAP_NORMAL;
				updateMap();
			}
		});
		mapViewGroup.add(rdbtnmntmNormal);
		rdbtnmntmNormal.setSelected(true);
		mnView.add(rdbtnmntmNormal);
		
		JRadioButtonMenuItem rdbtnmntmTopographical = new JRadioButtonMenuItem("Topographical");
		rdbtnmntmTopographical.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mapType = MapType.MAP_TOPOGRAPHICAL;
				updateMap();
			}
		});		
		mapViewGroup.add(rdbtnmntmTopographical);
		mnView.add(rdbtnmntmTopographical);
		
		JRadioButtonMenuItem rdbtnmntmCaveMap = new JRadioButtonMenuItem("Cave");
		rdbtnmntmCaveMap.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mapType = MapType.MAP_CAVE;
				updateMap();
			}
		});
		mapViewGroup.add(rdbtnmntmCaveMap);
		mnView.add(rdbtnmntmCaveMap);
		
		JRadioButtonMenuItem rdbtnmntmTerrainMap = new JRadioButtonMenuItem("Terrain");
		rdbtnmntmTerrainMap.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mapType = MapType.MAP_TERRAIN;
				updateMap();
			}
		});
		mapViewGroup.add(rdbtnmntmTerrainMap);
		mnView.add(rdbtnmntmTerrainMap);
		
		contentPanel = new JPanel();
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPanel.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPanel);
		
		mapPanel = new MapPanel();
		mapPanel.setBounds(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
		contentPanel.add(mapPanel, BorderLayout.CENTER);
	}
	
	private void loadMapFolder() {
		try {
			this.api = WurmAPI.open(this.mapFolder.toString());
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	private boolean getSaveFile() {
		JFileChooser filePicker = new JFileChooser();
		filePicker.setCurrentDirectory(saveFile);
		filePicker.setDialogTitle("Save Map Image");
		filePicker.setAcceptAllFileFilterUsed(false);
		filePicker.addChoosableFileFilter(new FileTypeFilter(".jpg", "JPeg"));
		filePicker.addChoosableFileFilter(new FileTypeFilter(".png", "PNG"));
		filePicker.addChoosableFileFilter(new FileTypeFilter(".gif", "GIF"));
		filePicker.addChoosableFileFilter(new FileTypeFilter(".bmp", "BMP"));
		if( filePicker.showOpenDialog(this) == JFileChooser.APPROVE_OPTION ) {
			File currentFile = filePicker.getSelectedFile();
			if(currentFile == null) {
				JOptionPane.showMessageDialog(this, "Invalid Selection. Please choose an image file.",
						"Invalid Image File", JOptionPane.ERROR_MESSAGE);
				return false;
			}
			FileTypeFilter filter = (FileTypeFilter)filePicker.getFileFilter();
			if(!currentFile.toString().endsWith(filter.getExtension()))
				currentFile = new File(currentFile.toString() + filter.getExtension());
			if(currentFile.exists()) {
				int result = JOptionPane.showConfirmDialog(this, "'" + currentFile + "' exists. Overwrite?",
						"File Exists", JOptionPane.YES_NO_OPTION);
				if(result == JOptionPane.NO_OPTION)
					return false;
			}				
			saveFile = currentFile;
		}
		return true;
	}
	
	private Tile[] getCaveTiles() {
		List<Tile> caveTiles = new ArrayList<Tile>();
		
		caveTiles.add(Tile.TILE_CAVE_WALL);
		caveTiles.add(Tile.TILE_CAVE_WALL_REINFORCED);
		
		if(this.optionWindow == null)
			return caveTiles.toArray(new Tile[caveTiles.size()]);
		
		if(this.optionWindow.isCaveShowCaves()) {
			caveTiles.add(Tile.TILE_CAVE);
			caveTiles.add(Tile.TILE_CAVE_FLOOR_REINFORCED);
			caveTiles.add(Tile.TILE_CAVE_EXIT);			
		}
		
		if(this.optionWindow.isCaveShowIron())
			caveTiles.add(Tile.TILE_CAVE_WALL_ORE_IRON);
		
		if(this.optionWindow.isCaveShowLava())
			caveTiles.add(Tile.TILE_CAVE_WALL_LAVA);
		
		if(this.optionWindow.isCaveShowCopper())
			caveTiles.add(Tile.TILE_CAVE_WALL_ORE_COPPER);
		
		if(this.optionWindow.isCaveShowTin())
			caveTiles.add(Tile.TILE_CAVE_WALL_ORE_TIN);
		
		if(this.optionWindow.isCaveShowGold())
			caveTiles.add(Tile.TILE_CAVE_WALL_ORE_GOLD);
		
		if(this.optionWindow.isCaveShowAdamantine())
			caveTiles.add(Tile.TILE_CAVE_WALL_ORE_ADAMANTINE);
		
		if(this.optionWindow.isCaveShowGlimmersteel())
			caveTiles.add(Tile.TILE_CAVE_WALL_ORE_GLIMMERSTEEL);
		
		if(this.optionWindow.isCaveShowSilver())
			caveTiles.add(Tile.TILE_CAVE_WALL_ORE_SILVER);
		
		if(this.optionWindow.isCaveShowLead())
			caveTiles.add(Tile.TILE_CAVE_WALL_ORE_LEAD);
		
		if(this.optionWindow.isCaveShowZinc())
			caveTiles.add(Tile.TILE_CAVE_WALL_ORE_ZINC);
		
		if(this.optionWindow.isCaveShowSlate())
			caveTiles.add(Tile.TILE_CAVE_WALL_SLATE);
		
		if(this.optionWindow.isCaveShowMarble())
			caveTiles.add(Tile.TILE_CAVE_WALL_MARBLE);
			
		return caveTiles.toArray(new Tile[caveTiles.size()]);
	}
	
	public void optionsUpdate() {
		updateMap();
	}
	
	private void updateMap() {
		if(api == null)
			return;
		this.mntmSaveMapImage.setEnabled(true);
		this.mntmSaveViewArea.setEnabled(true);
		switch(mapType) {
		case MAP_TOPOGRAPHICAL:
			this.mapPanel.setMapImage(api.getMapData().createTopographicDump(optionWindow.isTopoShowWater(), 
					optionWindow.getContourLines()));
			break;
		case MAP_CAVE:
			this.mapPanel.setMapImage(api.getMapData().createCaveDump(optionWindow.isCaveShowWater(), getCaveTiles()));
			break;
		case MAP_TERRAIN:
			this.mapPanel.setMapImage(api.getMapData().createTerrainDump(optionWindow.isTerrainShowWater()));
			break;
		case MAP_NORMAL:
		default:
			this.mapPanel.setMapImage(api.getMapData().createMapDump());
			break;
		}		
	}
	
	private void saveMapImage(boolean crop) {
		String imgType = "png";
		if(this.saveFile.toString().endsWith(".jpg"))
			imgType = "jpeg";
		else if(this.saveFile.toString().endsWith(".gif"))
			imgType = "gif";
		else if(this.saveFile.toString().endsWith(".bmp"))
			imgType = "bmp";
		else if(this.saveFile.toString().endsWith(".png"))
			imgType = ".png";
		else {
			JOptionPane.showMessageDialog(MapViewerFrame.this, "Only PNG, JPG, GIF, and BMP supported.",
					"Invalid Image File", JOptionPane.ERROR_MESSAGE);
			return;
		}
			
		try {
			ImageIO.write(this.mapPanel.getMapImage(crop), imgType, this.saveFile);
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		JOptionPane.showMessageDialog(MapViewerFrame.this, "Image saved to: " + this.saveFile,
				"Success", JOptionPane.PLAIN_MESSAGE);
	}
}
