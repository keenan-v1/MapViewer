package com.wurmly.mapviewer.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Image;
import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;

import com.wurmonline.mesh.GrassData;
import com.wurmonline.mesh.GrassData.FlowerType;
import com.wurmonline.mesh.GrassData.GrassType;
import com.wurmonline.mesh.GrassData.GrowthStage;
import com.wurmonline.mesh.Tiles;
import com.wurmonline.mesh.Tiles.Tile;
import com.wurmonline.wurmapi.api.WurmAPI;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.awt.event.ActionEvent;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;

import java.awt.Toolkit;
import javax.swing.KeyStroke;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;

public class MapViewerFrame extends JFrame {

	private static final long serialVersionUID = -7904984053821280873L;
	private static final String VERSION = "1.2.0";
	private MapToolbar mapToolbar;
	private MapPanel mapPanel;
	private JPanel contentPanel;
	private JPanel statusPanel;
	private WurmAPI api;
	private JMenuItem mntmSaveMapImage;
	private JMenuItem mntmSaveViewArea;
	public static final int WINDOW_HEIGHT = 980;
	public static final int WINDOW_WIDTH = 980;
	public static final int OPTIONS_HEIGHT = 980;
	public static final int OPTIONS_WIDTH = 200;
	public static final int TASKFRAME_WIDTH = 460;
	public static final int TASKFRAME_HEIGHT = 60;
	private static final int DEFAULT_X = 500;
	private static final int DEFAULT_Y = 0;
	private MapType mapType = MapType.MAP_TERRAIN;
	private static final String[] mapFiles = {"top_layer.map", "flags.map", "map_cave.map", "resources.map", "rock_layer.map" };
	private static final String defaultMapFile = mapFiles[0];
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yymmdd-hhmmss");
	private static final File defaultFolder = new File(System.getProperty("user.home"));
	private File lastMapFolder = defaultFolder;
	private File lastMapImageFile = defaultFolder;
	private File lastScreenCapFile = defaultFolder;
	private static final ImageFileTypeFilter[] fileTypes = {
			new ImageFileTypeFilter(ImageFileTypeFilter.TYPE_PNG),
			new ImageFileTypeFilter(ImageFileTypeFilter.TYPE_JPG),
			new ImageFileTypeFilter(ImageFileTypeFilter.TYPE_GIF),
			new ImageFileTypeFilter(ImageFileTypeFilter.TYPE_BMP)
	};
	private static final int defaultFileType = ImageFileTypeFilter.TYPE_PNG;
	private String mapName = "Unknown";
	private final ButtonGroup mapViewGroup = new ButtonGroup();
	private JLabel statusLabel;
	private int mapWidth;
	private int mapHeight;
	private static final String defaultTitle = "MapViewer - Version " + VERSION;
	private JRadioButtonMenuItem rdbtnmntmNormal;
	private JRadioButtonMenuItem rdbtnmntmTopographical;
	private JRadioButtonMenuItem rdbtnmntmCaveMap;
	private JRadioButtonMenuItem rdbtnmntmTerrainMap;
	private JMenu mnHelp;
	private JMenuItem mntmHelpAbout;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					String folderToOpen = null;
					if(args.length > 0)
					{
						folderToOpen = args[0];
						if(folderToOpen.endsWith("\"")) //Windows stupidity
							folderToOpen = folderToOpen.substring(0, folderToOpen.length() - 1);
					}
					
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					MapViewerFrame frame = new MapViewerFrame(folderToOpen);
					frame.setVisible(true);
				}
				catch (UnsupportedLookAndFeelException e) {
					try {
						UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
					}
					catch(Exception ex) {
						Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ex.getMessage(), ex);
					}
				}
				catch (Exception e) {
					Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
				}
			}
		});
	}

	public MapViewerFrame(String folderToOpen) {
		super(defaultTitle);
		List<Image> icons = new ArrayList<Image>();
		icons.add(Toolkit.getDefaultToolkit().getImage(MapViewerFrame.class.getResource("/com/wurmly/mapviewer/icons/128x128.png")));
		icons.add(Toolkit.getDefaultToolkit().getImage(MapViewerFrame.class.getResource("/com/wurmly/mapviewer/icons/64x64.png")));
		icons.add(Toolkit.getDefaultToolkit().getImage(MapViewerFrame.class.getResource("/com/wurmly/mapviewer/icons/32x32.png")));
		icons.add(Toolkit.getDefaultToolkit().getImage(MapViewerFrame.class.getResource("/com/wurmly/mapviewer/icons/16x16.png")));
		setIconImages(icons);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(DEFAULT_X, DEFAULT_Y, WINDOW_WIDTH + 16, WINDOW_HEIGHT + 62);
		setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
		mapToolbar = new MapToolbar(this);
		mapToolbar.setPreferredSize(new Dimension(MapViewerFrame.OPTIONS_WIDTH, MapViewerFrame.OPTIONS_HEIGHT));
		mapToolbar.setVisible(true);
		statusPanel = new JPanel();
		statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
		statusPanel.setPreferredSize(new Dimension(this.getWidth(), 16));
		statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));
		statusLabel = new JLabel("Status: ");
		statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
		statusPanel.add(statusLabel);
		statusPanel.setVisible(true);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		mnFile.setMnemonic('F');
		menuBar.add(mnFile);
		
		JMenuItem mntmOpenMapFolder = new JMenuItem("Open Map Folder");
		mntmOpenMapFolder.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, 0));
		mntmOpenMapFolder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				loadMapFolder(getMapFile(lastMapFolder));
			}
		});
		mnFile.add(mntmOpenMapFolder);
		
		mntmSaveMapImage = new JMenuItem("Save Full Map Image");
		mntmSaveMapImage.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, 0));
		mntmSaveMapImage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveMapImage(getMapImageSaveFile());
			}
		});
		mntmSaveMapImage.setEnabled(false);
		mnFile.add(mntmSaveMapImage);
		
		mntmSaveViewArea = new JMenuItem("Save Screen Capture");
		mntmSaveViewArea.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, 0));
		mntmSaveViewArea.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveScreenCapImage(getScreenCapSaveFile());
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
		
		JMenu mnView = new JMenu("View");
		mnView.setMnemonic('V');
		menuBar.add(mnView);
		
		rdbtnmntmTopographical = new JRadioButtonMenuItem("Topographical");
		rdbtnmntmTopographical.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2, 0));
		rdbtnmntmTopographical.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setMapType(MapType.MAP_TOPOGRAPHICAL);
			}
		});		
		
		rdbtnmntmTerrainMap = new JRadioButtonMenuItem("Terrain");
		rdbtnmntmTerrainMap.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, 0));
		rdbtnmntmTerrainMap.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setMapType(MapType.MAP_TERRAIN);
			}
		});
		mapViewGroup.add(rdbtnmntmTerrainMap);
		mnView.add(rdbtnmntmTerrainMap);
		mapViewGroup.add(rdbtnmntmTopographical);
		mnView.add(rdbtnmntmTopographical);
		
		rdbtnmntmCaveMap = new JRadioButtonMenuItem("Cave");
		rdbtnmntmCaveMap.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_3, 0));
		rdbtnmntmCaveMap.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setMapType(MapType.MAP_CAVE);
			}
		});
		mapViewGroup.add(rdbtnmntmCaveMap);
		mnView.add(rdbtnmntmCaveMap);
		
		rdbtnmntmNormal = new JRadioButtonMenuItem("Isometric (3D)");
		rdbtnmntmNormal.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_4, 0));
		rdbtnmntmNormal.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setMapType(MapType.MAP_NORMAL);
			}
		});
		mapViewGroup.add(rdbtnmntmNormal);
		rdbtnmntmNormal.setSelected(true);
		mnView.add(rdbtnmntmNormal);
		
		mnHelp = new JMenu("Help");
		mnHelp.setMnemonic('H');
		menuBar.add(mnHelp);
		
		mntmHelpAbout = new JMenuItem("Help & About");
		mntmHelpAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MapHelpWindow helpPanel = new MapHelpWindow(MapViewerFrame.this);
				helpPanel.setSize(new Dimension(400,450));
				helpPanel.setBounds((MapViewerFrame.WINDOW_WIDTH/2)+200, (MapViewerFrame.WINDOW_HEIGHT/2)-225,400,450);
				helpPanel.setVisible(true);
			}
		});
		mntmHelpAbout.setIcon(null);
		mntmHelpAbout.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
		mnHelp.add(mntmHelpAbout);
		
		contentPanel = new JPanel();
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPanel);
		contentPanel.setLayout(new BorderLayout(0, 0));		
		contentPanel.add(statusPanel, BorderLayout.SOUTH);		
		mapPanel = new MapPanel(this);
		mapPanel.setBounds(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
		contentPanel.add(mapPanel);
		contentPanel.add(mapToolbar, BorderLayout.EAST);
		mapToolbar.setFocusable(false);
		this.requestFocusInWindow();
		
		this.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent evt) {
				switch(evt.getKeyCode()) {
				case KeyEvent.VK_EQUALS:
				case KeyEvent.VK_ADD:
					mapPanel.zoomIn(evt.isShiftDown());
					break;
				case KeyEvent.VK_MINUS:
				case KeyEvent.VK_SUBTRACT:
					mapPanel.zoomOut(evt.isShiftDown());
					break;
				case KeyEvent.VK_W:
				case KeyEvent.VK_NUMPAD8:
					mapPanel.moveImage(MapPanel.DIR_UP, evt.isShiftDown());
					break;
				case KeyEvent.VK_A:
				case KeyEvent.VK_NUMPAD4:
					mapPanel.moveImage(MapPanel.DIR_LEFT, evt.isShiftDown());
					break;
				case KeyEvent.VK_D:
				case KeyEvent.VK_NUMPAD6:
					mapPanel.moveImage(MapPanel.DIR_RIGHT, evt.isShiftDown());
					break;
				case KeyEvent.VK_S:
				case KeyEvent.VK_NUMPAD2:
					mapPanel.moveImage(MapPanel.DIR_DOWN, evt.isShiftDown());
					break;
				}
			}
		});
		
		if(folderToOpen != null)
		{
			File folder = new File(folderToOpen);
			if(folder.isFile())
				folder = folder.getParentFile();
			loadMapFolder(folder);
		}
	}
	
	public void setApi(WurmAPI api) {
		this.api = api;
		this.mapWidth = api.getMapData().getWidth();
		this.mapHeight = api.getMapData().getHeight();
		setStatus();
		TaskFrame loadFrame = new TaskFrame();
		loadFrame.setSize(TASKFRAME_WIDTH, TASKFRAME_HEIGHT);
		loadFrame.setLocation((this.getWidth()/2)-(loadFrame.getWidth()/2), (this.getHeight()/2)-(loadFrame.getHeight()/2));
		loadFrame.setVisible(true);
		loadFrame.updateMap(this);
	}
	
	private void loadMapFolder(File openFolder) {
		if(openFolder == null)
			return;
		this.lastMapFolder = openFolder;
		this.mapName = openFolder.toString().substring(openFolder.toString().lastIndexOf(File.separator)+1);
		this.setTitle(defaultTitle + " (" + mapName + ")");
		TaskFrame loadFrame = new TaskFrame();
		loadFrame.setSize(TASKFRAME_WIDTH, TASKFRAME_HEIGHT);
		loadFrame.setLocation((this.getWidth()/2)-(loadFrame.getWidth()/2), (this.getHeight()/2)-(loadFrame.getHeight()/2));
		loadFrame.setVisible(true);
		loadFrame.loadApi(this, openFolder);
	}
	
	public void setMapType(MapType t) {
		mapType = t;
		switch(t) {
		case MAP_CAVE:
			mapToolbar.setVisible(true);
			getCaveViewMenu().setSelected(true);
			break;
		case MAP_TERRAIN:
			mapToolbar.setVisible(true);
			getTerrainViewMenu().setSelected(true);
			break;
		case MAP_TOPOGRAPHICAL:
			mapToolbar.setVisible(true);
			getTopoViewMenu().setSelected(true);
			break;
		case MAP_NORMAL:
		case MAP_ALL:
		default:
			mapToolbar.setVisible(false);
			setStatus("Fullscreen Isometric View");
			getNormalViewMenu().setSelected(true);
			break;
		}
		mapToolbar.setOptionTab(t);
		taskUpdateMap();
	}
	
	private void taskUpdateMap() {
		if(api == null)
			return;
		TaskFrame loadFrame = new TaskFrame();
		loadFrame.setSize(TASKFRAME_WIDTH, TASKFRAME_HEIGHT);
		loadFrame.setLocation((this.getWidth()/2)-(loadFrame.getWidth()/2), (this.getHeight()/2)-(loadFrame.getHeight()/2));
		loadFrame.setVisible(true);
		loadFrame.updateMap(this);		
	}
	
	private File getMapFile(File folder) {
		JFileChooser filePicker = new JFileChooser();
		File mFolder = null;
		filePicker.setCurrentDirectory(folder);
		filePicker.setDialogTitle("Choose Map Folder");
		filePicker.setSelectedFile(new File(defaultMapFile));
		filePicker.setApproveButtonText("Open Map");
		filePicker.addChoosableFileFilter(new MapFileTypeFilter(".map", "WurmAPI Map"));
		filePicker.setAcceptAllFileFilterUsed(false);
		switch(filePicker.showOpenDialog(MapViewerFrame.this)) {
		case JFileChooser.APPROVE_OPTION: 
			File currentFile = filePicker.getSelectedFile();
			if(currentFile == null)
				mFolder = filePicker.getCurrentDirectory();
			else if(currentFile.isDirectory())
				mFolder = currentFile.getAbsoluteFile();
			else if(currentFile.toString().endsWith(".map"))
				mFolder = filePicker.getCurrentDirectory();
			else {
				JOptionPane.showMessageDialog(MapViewerFrame.this, "Invalid Selection. Please choose a folder.",
						"Invalid Map Folder", JOptionPane.ERROR_MESSAGE);
				return getMapFile(filePicker.getCurrentDirectory());
			}
			for( int i = 0; i < mapFiles.length; i++ ) {
				currentFile = new File(mFolder.toString() + File.separator + mapFiles[i]);
				if(!currentFile.exists()) {
					JOptionPane.showMessageDialog(MapViewerFrame.this, "File: '" + currentFile + "' not found.",
							"Missing Files", JOptionPane.ERROR_MESSAGE);
					return getMapFile(filePicker.getCurrentDirectory());
				}
				if((currentFile.length() * 5) > Runtime.getRuntime().maxMemory()) {
					JOptionPane.showMessageDialog(MapViewerFrame.this, "File: '" + currentFile + "' is too large to load into memory." +
				"Please use 64-bit Java, increase your heap space, or load a smaller map.",
							"Missing Files", JOptionPane.ERROR_MESSAGE);
					return null;					
				}
			}
			return mFolder;
			default:
				return null;
		}
	}
	
	private File getScreenCapSaveFile() {
		return getSaveFile(true, lastScreenCapFile);
	}
	
	private File getMapImageSaveFile() {
		return getSaveFile(false, lastMapImageFile);
	}

	private String getSaveFileName(boolean isScreenCap) {
		return getSaveFileName(isScreenCap,defaultFileType);
	}
	
	private String getSaveFileName(boolean isScreenCap, int fileType) {
		String fDateTime = dateFormat.format(new Date());
		String saveFileName = mapName + "_" + fDateTime;
		if(isScreenCap) {
			saveFileName = mapName + "-mapdetail_" + fDateTime;
		}
		else {
			switch(mapType) {
			case MAP_TOPOGRAPHICAL:
				saveFileName = mapName + "-topographical_" + fDateTime;
				break;
			case MAP_CAVE:
				saveFileName = mapName + "-ores_" + fDateTime;
				break;
			case MAP_TERRAIN:
				saveFileName = mapName + "-terrain_" + fDateTime;
				break;
			case MAP_NORMAL:
				saveFileName = mapName + "-isometric_" + fDateTime;
				break;
			case MAP_ALL:
			default:
				saveFileName = mapName + fDateTime;
			}
		}
		if(fileType != -1)
			saveFileName += fileTypes[fileType].getExtension();
		return saveFileName;
	}
	
	public void setStatus() {
		setStatus("");
	}
	public void setStatus(String status) {
		this.statusLabel.setText(status);
	}
	
	private File getSaveFile(boolean isScreenCap, File folder) {
		JFileChooser filePicker = new JFileChooser();
		String saveFileName = getSaveFileName(isScreenCap);

		filePicker.setCurrentDirectory(folder);
		filePicker.setMultiSelectionEnabled(false);
		filePicker.setDialogTitle("Save Image");
		filePicker.setAcceptAllFileFilterUsed(false);
		filePicker.setSelectedFile(new File(saveFileName));
		filePicker.setApproveButtonText("Save Image");

		for(int i = 0; i < fileTypes.length; i++)
			filePicker.addChoosableFileFilter(fileTypes[i]);
		
		filePicker.addPropertyChangeListener(JFileChooser.FILE_FILTER_CHANGED_PROPERTY, new PropertyChangeListener()
		{
			@Override
			public void propertyChange(PropertyChangeEvent evt)
			{
				ImageFileTypeFilter filter = (ImageFileTypeFilter)evt.getNewValue();
			    String extension = filter.getExtension();
			
			    File selectedFile = filePicker.getSelectedFile();
			    String path = "";

			    if(selectedFile == null)
			    	path = getSaveFileName(isScreenCap, filter.getType());
			    else
			    	path = selectedFile.getName();

			    path = path.substring(0, path.lastIndexOf("."));
			
			    filePicker.setSelectedFile(new File(path + extension));
			}
		});
		
		switch(filePicker.showOpenDialog(MapViewerFrame.this)) {
		case JFileChooser.APPROVE_OPTION:
			File currentFile = filePicker.getSelectedFile();
			if(currentFile == null) {
				JOptionPane.showMessageDialog(this, "Invalid Selection. Please choose an image file.",
						"Invalid Image File", JOptionPane.ERROR_MESSAGE);
				return getSaveFile(isScreenCap, filePicker.getCurrentDirectory());
			}
			ImageFileTypeFilter filter = (ImageFileTypeFilter)filePicker.getFileFilter();
			if(!currentFile.toString().endsWith(filter.getExtension()))
				currentFile = new File(currentFile.toString() + filter.getExtension());
			if(currentFile.exists()) {
				int result = JOptionPane.showConfirmDialog(this, "'" + currentFile + "' exists. Overwrite?",
						"File Exists", JOptionPane.YES_NO_OPTION);
				if(result == JOptionPane.NO_OPTION)
					return getSaveFile(isScreenCap, filePicker.getCurrentDirectory());
			}
			return currentFile;
			default:
				return null;
		}
	}
	
	private Tile[] getCaveTiles() {
		List<Tile> caveTiles = new ArrayList<Tile>();
		
		caveTiles.add(Tile.TILE_CAVE_WALL);
		caveTiles.add(Tile.TILE_CAVE_WALL_REINFORCED);
		
		if(this.mapToolbar == null)
			return caveTiles.toArray(new Tile[caveTiles.size()]);
		
		if(this.mapToolbar.isCaveShowCaves()) {
			caveTiles.add(Tile.TILE_CAVE);
			caveTiles.add(Tile.TILE_CAVE_FLOOR_REINFORCED);
			caveTiles.add(Tile.TILE_CAVE_EXIT);			
		}
		
		if(this.mapToolbar.isCaveShowIron())
			caveTiles.add(Tile.TILE_CAVE_WALL_ORE_IRON);
		
		if(this.mapToolbar.isCaveShowLava())
			caveTiles.add(Tile.TILE_CAVE_WALL_LAVA);
		
		if(this.mapToolbar.isCaveShowCopper())
			caveTiles.add(Tile.TILE_CAVE_WALL_ORE_COPPER);
		
		if(this.mapToolbar.isCaveShowTin())
			caveTiles.add(Tile.TILE_CAVE_WALL_ORE_TIN);
		
		if(this.mapToolbar.isCaveShowGold())
			caveTiles.add(Tile.TILE_CAVE_WALL_ORE_GOLD);
		
		if(this.mapToolbar.isCaveShowAdamantine())
			caveTiles.add(Tile.TILE_CAVE_WALL_ORE_ADAMANTINE);
		
		if(this.mapToolbar.isCaveShowGlimmersteel())
			caveTiles.add(Tile.TILE_CAVE_WALL_ORE_GLIMMERSTEEL);
		
		if(this.mapToolbar.isCaveShowSilver())
			caveTiles.add(Tile.TILE_CAVE_WALL_ORE_SILVER);
		
		if(this.mapToolbar.isCaveShowLead())
			caveTiles.add(Tile.TILE_CAVE_WALL_ORE_LEAD);
		
		if(this.mapToolbar.isCaveShowZinc())
			caveTiles.add(Tile.TILE_CAVE_WALL_ORE_ZINC);
		
		if(this.mapToolbar.isCaveShowSlate())
			caveTiles.add(Tile.TILE_CAVE_WALL_SLATE);
		
		if(this.mapToolbar.isCaveShowMarble())
			caveTiles.add(Tile.TILE_CAVE_WALL_MARBLE);
			
		return caveTiles.toArray(new Tile[caveTiles.size()]);
	}
	
	public void optionsUpdate(MapType type) {
		if(this.mapType == type || type == MapType.MAP_ALL)
			taskUpdateMap();
	}
	
	public void updateMap() {
		if(api == null)
			return;
		// Get current size of heap in bytes
		long heapSize = Runtime.getRuntime().totalMemory(); 

		// Get maximum size of heap in bytes. The heap cannot grow beyond this size.// Any attempt will result in an OutOfMemoryException.
		long heapMaxSize = Runtime.getRuntime().maxMemory();

		 // Get amount of free memory within the heap in bytes. This size will increase // after garbage collection and decrease as new objects are created.
		long heapFreeSize = Runtime.getRuntime().freeMemory(); 
		Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Heap Size: " + readableFileSize(heapSize));
		Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Max Heap Size: " + readableFileSize(heapMaxSize));
		Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Free Heap Size: " + readableFileSize(heapFreeSize));
		this.mntmSaveMapImage.setEnabled(true);
		this.mntmSaveViewArea.setEnabled(true);
		switch(mapType) {
		case MAP_TOPOGRAPHICAL:
			this.mapPanel.setMapImage(api.getMapData().createTopographicDump(mapToolbar.isTopoShowWater(), 
					mapToolbar.getContourLines()));
			break;
		case MAP_CAVE:
			this.mapPanel.setMapImage(api.getMapData().createCaveDump(mapToolbar.isCaveShowWater(), getCaveTiles()));
			break;
		case MAP_TERRAIN:
			this.mapPanel.setMapImage(api.getMapData().createTerrainDump(mapToolbar.isTerrainShowWater()));
			break;
		case MAP_NORMAL:
		default:
			this.mapPanel.setMapImage(api.getMapData().createMapDump());
			break;
		}		
	}
	
	public static String readableFileSize(long size) {
	    if(size <= 0) return "0";
	    final String[] units = new String[] { "B", "kB", "MB", "GB", "TB" };
	    int digitGroups = (int) (Math.log10(size)/Math.log10(1024));
	    return new DecimalFormat("#,##0.#").format(size/Math.pow(1024, digitGroups)) + " " + units[digitGroups];
	}
	
	private void saveMapImage(File destFile) {
		if(destFile == null)
			return;
		this.lastMapImageFile = destFile;
		saveImage(destFile, false);
	}
	
	private void saveScreenCapImage(File destFile) {
		if(destFile == null)
			return;
		this.lastScreenCapFile = destFile;
		saveImage(destFile, true);
	}
	
	private void saveImage(File destFile, boolean crop) {
		String imgType = "png";
		if(destFile.toString().endsWith(".jpg"))
			imgType = "jpeg";
		else if(destFile.toString().endsWith(".gif"))
			imgType = "gif";
		else if(destFile.toString().endsWith(".bmp"))
			imgType = "bmp";
		else if(destFile.toString().endsWith(".png"))
			imgType = "png";
		else {
			JOptionPane.showMessageDialog(MapViewerFrame.this, "Only PNG, JPG, GIF, and BMP supported.",
					"Invalid Image File", JOptionPane.ERROR_MESSAGE);
			return;
		}
		TaskFrame saveFrame = new TaskFrame();
		saveFrame.setSize(TASKFRAME_WIDTH, TASKFRAME_HEIGHT);
		saveFrame.setLocation((this.getWidth()/2)-(saveFrame.getWidth()/2), (this.getHeight()/2)-(saveFrame.getHeight()/2));
		saveFrame.saveImage(this.mapPanel.getMapImage(crop), imgType, destFile);
		saveFrame.setVisible(true);
	}
	
	public String getToolTipInfo(int x, int y) {
		if(api == null)
			return null;
		if(mapType == MapType.MAP_NORMAL)
			return null;
		boolean water = (api.getMapData().getSurfaceHeight(x, y) < 0 ? true : false);
		String ttText = "<html>";
		ttText += "[" + x + "," + y + "] ";
		if(water)
			ttText += "[Water] ";
		ttText += "<br/>";
		switch(mapType) {
		case MAP_CAVE:
			Tile ct = api.getMapData().getCaveTile(x, y);
			if(ct == null) {
				ttText += "Empty";
				break;
			}
			ttText += "Type: " + ct.getName() + "<br/>";
			ttText += "Quantity: " + api.getMapData().getCaveResourceCount(x, y);
			break;
		case MAP_TERRAIN:
		case MAP_TOPOGRAPHICAL:
		case MAP_NORMAL:
		default:
			Tile st = api.getMapData().getSurfaceTile(x, y);
			if(st == null) {
				ttText += "Empty";
				break;
			}
			int sHeight = api.getMapData().getSurfaceHeight(x, y);
			int rHeight = api.getMapData().getRockHeight(x, y);
			int dHeight = api.getMapData().getDirtLayerHeight(x, y);
			String flags = (st.canBearFruit() ? "Fruit " : "") + (st.canBotanize() ? "Botanize " : "") + (st.canForage() ? "Forage " : "");
			ttText += "(" + st.getName() + ") Height: "+ sHeight + "<br/>";
			ttText += "Dirt Depth: " + dHeight + " Rock Height: " + rHeight +"<br/>";
			ttText += "Flags: " + flags + "<br/>";
			if(st.isGrass()) {
				byte tileData = Tiles.decodeData(st.getIntId());
				GrassType gt = GrassData.GrassType.decodeTileData(tileData);
				FlowerType ft = GrassData.FlowerType.decodeTileData(tileData);
				GrowthStage gs = GrassData.GrowthStage.decodeTileData(tileData);
				String flowerName = GrassData.getFlowerTypeName(tileData);
				int flowerType = GrassData.getFlowerType(tileData);
				String hover = GrassData.getHover(tileData);
				ttText += "GrassData: [Flower: " + flowerName + "(" + flowerType + ")]<br/>";
				ttText += gs.toString() + "|" + gt.toString() + "|" + ft.toString() + "<br/>";
				ttText += hover;
			}
			break;
		}
		return ttText + "</html>";
	}
	public void setTileStatus(int x, int y) {
		if(api == null)
			return;
		if(mapType == MapType.MAP_NORMAL)
			return;
		boolean water = (api.getMapData().getSurfaceHeight(x, y) < 0 ? true : false);
		String ttText = "[Size: " + this.mapWidth + "x"+ this.mapHeight + "] ";
		ttText += "[Tile: " + x + "," + y + "] ";
		if(water)
			ttText += "[Water] ";
		switch(mapType) {
		case MAP_CAVE:
			Tile ct = api.getMapData().getCaveTile(x, y);
			if(ct == null) {
				break;
			}
			ttText += "[Type: " + ct.getName() + " | ";
			ttText += "Quantity: " + api.getMapData().getCaveResourceCount(x, y) + "]";
			break;
		case MAP_TERRAIN:
		case MAP_TOPOGRAPHICAL:
		case MAP_NORMAL:
		default:
			Tile st = api.getMapData().getSurfaceTile(x, y);
			if(st == null) {
				ttText += "";
				break;
			}
			int sHeight = api.getMapData().getSurfaceHeight(x, y);
			int rHeight = api.getMapData().getRockHeight(x, y);
			int dHeight = api.getMapData().getDirtLayerHeight(x, y);
			String flags = (st.canBearFruit() ? "Fruit " : "") + (st.canBotanize() ? "Botanize " : "") + (st.canForage() ? "Forage " : "");
			ttText += "(" + st.getName() + ") Height: "+ sHeight + " | ";
			ttText += "Dirt Depth: " + dHeight + " | Rock Height: " + rHeight +" | ";
			ttText += "Flags: " + flags;
			break;
		}
		setStatus(ttText);		
	}
	
	public JRadioButtonMenuItem getNormalViewMenu() {
		return rdbtnmntmNormal;
	}
	public JRadioButtonMenuItem getTopoViewMenu() {
		return rdbtnmntmTopographical;
	}
	public JRadioButtonMenuItem getCaveViewMenu() {
		return rdbtnmntmCaveMap;
	}
	public JRadioButtonMenuItem getTerrainViewMenu() {
		return rdbtnmntmTerrainMap;
	}
	public MapToolbar getMapToolbar() {
		return mapToolbar;
	}
	public MapType getMapType() {
		return mapType;
	}

	public MapPanel getMapPanel() {
		return mapPanel;
	}

	public String getVersion() {
		return VERSION;
	}
}
