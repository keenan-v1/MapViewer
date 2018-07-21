package com.wurmly.mapviewer.ui;

import com.wurmly.mapviewer.localization.Localization;
import com.wurmly.mapviewer.properties.BuildProperties;
import com.wurmonline.mesh.GrassData;
import com.wurmonline.mesh.GrassData.FlowerType;
import com.wurmonline.mesh.GrassData.GrassType;
import com.wurmonline.mesh.GrassData.GrowthStage;
import com.wurmonline.mesh.Tiles;
import com.wurmonline.mesh.Tiles.Tile;
import com.wurmonline.wurmapi.api.WurmAPI;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

class MapViewerFrame extends JFrame
{

    private static final long serialVersionUID = -7904984053821280873L;
    private static final String VERSION = BuildProperties.getVersion();
    @NotNull
    private final MapToolbar mapToolbar;
    @NotNull
    private final MapPanel mapPanel;
    private WurmAPI api;
    @NotNull
    private final JMenuItem itmSaveMapImage;
    @NotNull
    private final JMenuItem itmSaveViewArea;
    private static final int WINDOW_HEIGHT = 980;
    private static final int WINDOW_WIDTH = 980;
    private static final int OPTIONS_HEIGHT = 980;
    private static final int OPTIONS_WIDTH = 200;
    static final int TASKFRAME_WIDTH = 460;
    static final int TASKFRAME_HEIGHT = 60;
    private static final int DEFAULT_X = 500;
    private static final int DEFAULT_Y = 0;
    @NotNull
    private MapType mapType = MapType.MAP_TERRAIN;
    private static final String[] mapFiles = {"top_layer.map", "flags.map", "map_cave.map", "resources.map", "rock_layer.map"};
    private static final String defaultMapFile = mapFiles[0];
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMdd-hhmmss");
    private static final File defaultFolder = new File(System.getProperty("user.home"));
    @Nullable
    private File lastMapFolder = defaultFolder;
    @Nullable
    private File lastMapImageFile = defaultFolder;
    @Nullable
    private File lastScreenCapFile = defaultFolder;
    private static final ImageFileTypeFilter[] fileTypes = {
            new ImageFileTypeFilter(ImageFileTypeFilter.TYPE_PNG),
            new ImageFileTypeFilter(ImageFileTypeFilter.TYPE_JPG),
            new ImageFileTypeFilter(ImageFileTypeFilter.TYPE_GIF),
            new ImageFileTypeFilter(ImageFileTypeFilter.TYPE_BMP)
    };
    private static final int defaultFileType = ImageFileTypeFilter.TYPE_PNG;
    @NotNull
    private String mapName = "Unknown";
    @NotNull
    private final JLabel statusLabel;
    private int mapWidth;
    private int mapHeight;
    private static final String defaultTitle = "MapViewer - Version " + VERSION;
    @NotNull
    private final JRadioButtonMenuItem btnItmNormal;
    @NotNull
    private final JRadioButtonMenuItem btnItmTopographical;
    @NotNull
    private final JRadioButtonMenuItem btnItmCaveMap;
    @NotNull
    private final JRadioButtonMenuItem btnItmTerrainMap;
    private final HashMap<Tile, Boolean> tileVisibility = new HashMap<>();
    private boolean showCaveWater = true;

    public static void main(@NotNull String[] args)
    {
        EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                try
                {
                    String folderToOpen = null;
                    if (args.length > 0)
                    {
                        folderToOpen = args[0];
                        if (folderToOpen.endsWith("\"")) //Windows stupidity
                            folderToOpen = folderToOpen.substring(0, folderToOpen.length() - 1);
                    }

                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    MapViewerFrame frame = new MapViewerFrame(folderToOpen);
                    frame.setVisible(true);
                }
                catch (UnsupportedLookAndFeelException e)
                {
                    try
                    {
                        UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                    }
                    catch (Exception ex)
                    {
                        Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ex.getMessage(), ex);
                    }
                }
                catch (Exception e)
                {
                    Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
                }
            }
        });
    }

    private MapViewerFrame(@Nullable String folderToOpen)
    {
        super(defaultTitle);
        List<Image> icons = new ArrayList<>();
        icons.add(Toolkit.getDefaultToolkit().getImage(MapViewerFrame.class.getResource("/icons/128x128.png")));
        icons.add(Toolkit.getDefaultToolkit().getImage(MapViewerFrame.class.getResource("/icons/64x64.png")));
        icons.add(Toolkit.getDefaultToolkit().getImage(MapViewerFrame.class.getResource("/icons/32x32.png")));
        icons.add(Toolkit.getDefaultToolkit().getImage(MapViewerFrame.class.getResource("/icons/16x16.png")));
        setIconImages(icons);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(DEFAULT_X, DEFAULT_Y, WINDOW_WIDTH + 16, WINDOW_HEIGHT + 62);
        setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
        mapToolbar = new MapToolbar(this);
        mapToolbar.setPreferredSize(new Dimension(MapViewerFrame.OPTIONS_WIDTH, MapViewerFrame.OPTIONS_HEIGHT));
        mapToolbar.setVisible(true);
        JPanel statusPanel = new JPanel();
        statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
        statusPanel.setPreferredSize(new Dimension(this.getWidth(), 16));
        statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));
        statusLabel = new JLabel(Localization.getInstance().getMessageFor("status-prefix") + " ");
        statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
        statusPanel.add(statusLabel);
        statusPanel.setVisible(true);

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu mnFile = new JMenu(Localization.getInstance().getMessageFor("menu-file"));
        mnFile.setMnemonic(Localization.getInstance().getMnemonicFor("menu-file"));
        menuBar.add(mnFile);

        JMenuItem itmOpenMapFolder = new JMenuItem(Localization.getInstance().getMessageFor("menu-file-open"));
        itmOpenMapFolder.setAccelerator(KeyStroke.getKeyStroke(Localization.getInstance().getMnemonicFor("menu-file-open"), 0));
        itmOpenMapFolder.addActionListener(e -> loadMapFolder(getMapFile(lastMapFolder)));
        mnFile.add(itmOpenMapFolder);

        itmSaveMapImage = new JMenuItem(Localization.getInstance().getMessageFor("menu-file-save-full"));
        itmSaveMapImage.setAccelerator(KeyStroke.getKeyStroke(Localization.getInstance().getMnemonicFor("menu-file-save-full"), 0));
        itmSaveMapImage.addActionListener(e -> saveMapImage(getMapImageSaveFile()));
        itmSaveMapImage.setEnabled(false);
        mnFile.add(itmSaveMapImage);

        itmSaveViewArea = new JMenuItem(Localization.getInstance().getMessageFor("menu-file-save-capture"));
        itmSaveViewArea.setAccelerator(KeyStroke.getKeyStroke(Localization.getInstance().getMnemonicFor("menu-file-save-capture"), 0));
        itmSaveViewArea.addActionListener(e -> saveScreenCapImage(getScreenCapSaveFile()));
        itmSaveViewArea.setEnabled(false);
        mnFile.add(itmSaveViewArea);

        JSeparator separator = new JSeparator();
        mnFile.add(separator);


        JMenuItem itmExit = new JMenuItem(Localization.getInstance().getMessageFor("menu-file-exit"));
        itmExit.addActionListener(e -> MapViewerFrame.this.dispatchEvent(new WindowEvent(MapViewerFrame.this, WindowEvent.WINDOW_CLOSING)));
        itmExit.setAccelerator(KeyStroke.getKeyStroke(Localization.getInstance().getMnemonicFor("menu-file-exit"), 0));
        mnFile.add(itmExit);

        JMenu mnView = new JMenu(Localization.getInstance().getMessageFor("menu-view"));
        mnView.setMnemonic(Localization.getInstance().getMnemonicFor("menu-view"));
        menuBar.add(mnView);

        btnItmTopographical = new JRadioButtonMenuItem(Localization.getInstance().getMessageFor("menu-view-topographical"));
        btnItmTopographical.setAccelerator(KeyStroke.getKeyStroke(Localization.getInstance().getMnemonicFor("menu-view-topographical"), 0));
        btnItmTopographical.addActionListener(e -> setMapType(MapType.MAP_TOPOGRAPHICAL));

        btnItmTerrainMap = new JRadioButtonMenuItem(Localization.getInstance().getMessageFor("menu-view-terrain"));
        btnItmTerrainMap.setAccelerator(KeyStroke.getKeyStroke(Localization.getInstance().getMnemonicFor("menu-view-terrain"), 0));
        btnItmTerrainMap.addActionListener(e -> setMapType(MapType.MAP_TERRAIN));
        ButtonGroup mapViewGroup = new ButtonGroup();
        mapViewGroup.add(btnItmTerrainMap);
        mnView.add(btnItmTerrainMap);
        mapViewGroup.add(btnItmTopographical);
        mnView.add(btnItmTopographical);

        btnItmCaveMap = new JRadioButtonMenuItem(Localization.getInstance().getMessageFor("menu-view-cave"));
        btnItmCaveMap.setAccelerator(KeyStroke.getKeyStroke(Localization.getInstance().getMnemonicFor("menu-view-cave"), 0));
        btnItmCaveMap.addActionListener(e -> setMapType(MapType.MAP_CAVE));
        mapViewGroup.add(btnItmCaveMap);
        mnView.add(btnItmCaveMap);

        btnItmNormal = new JRadioButtonMenuItem(Localization.getInstance().getMessageFor("menu-view-isometric"));
        btnItmNormal.setAccelerator(KeyStroke.getKeyStroke(Localization.getInstance().getMnemonicFor("menu-view-isometric"), 0));
        btnItmNormal.addActionListener(arg0 -> setMapType(MapType.MAP_NORMAL));
        mapViewGroup.add(btnItmNormal);
        btnItmNormal.setSelected(true);
        mnView.add(btnItmNormal);

        JMenu mnHelp = new JMenu(Localization.getInstance().getMessageFor("menu-help"));
        mnHelp.setMnemonic(Localization.getInstance().getMnemonicFor("menu-help"));
        menuBar.add(mnHelp);

        JMenuItem itmHelpAbout = new JMenuItem(Localization.getInstance().getMessageFor("menu-help-about"));
        itmHelpAbout.addActionListener(e -> {
            MapHelpWindow helpPanel = new MapHelpWindow(MapViewerFrame.this);
            helpPanel.setSize(new Dimension(450, 450));
            helpPanel.setBounds((MapViewerFrame.WINDOW_WIDTH / 2) + 200, (MapViewerFrame.WINDOW_HEIGHT / 2) - 225, 400, 450);
            helpPanel.setVisible(true);
        });
        itmHelpAbout.setIcon(null);
        itmHelpAbout.setAccelerator(KeyStroke.getKeyStroke(Localization.getInstance().getMnemonicFor("menu-help-about"), 0));
        mnHelp.add(itmHelpAbout);

        JPanel contentPanel = new JPanel();
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

        this.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyPressed(@NotNull KeyEvent evt)
            {
                switch (evt.getKeyCode())
                {
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

        if (folderToOpen != null)
        {
            File folder = new File(folderToOpen);
            if (folder.isFile())
                folder = folder.getParentFile();
            loadMapFolder(folder);
        }
    }

    void setApi(@NotNull WurmAPI api)
    {
        this.api = api;
        this.mapWidth = api.getMapData().getWidth();
        this.mapHeight = api.getMapData().getHeight();
        setStatus();
        TaskFrame loadFrame = new TaskFrame();
        loadFrame.setSize(TASKFRAME_WIDTH, TASKFRAME_HEIGHT);
        loadFrame.setLocation((this.getWidth() / 2) - (loadFrame.getWidth() / 2), (this.getHeight() / 2) - (loadFrame.getHeight() / 2));
        loadFrame.setVisible(true);
        loadFrame.updateMap(this);
    }

    private void loadMapFolder(@Nullable File openFolder)
    {
        if (openFolder == null)
            return;
        this.lastMapFolder = openFolder;
        this.mapName = openFolder.toString().substring(openFolder.toString().lastIndexOf(File.separator) + 1);
        this.setTitle(defaultTitle + " (" + mapName + ")");
        TaskFrame loadFrame = new TaskFrame();
        loadFrame.setSize(TASKFRAME_WIDTH, TASKFRAME_HEIGHT);
        loadFrame.setLocation((this.getWidth() / 2) - (loadFrame.getWidth() / 2), (this.getHeight() / 2) - (loadFrame.getHeight() / 2));
        loadFrame.setVisible(true);
        loadFrame.loadApi(this, openFolder);
    }

    void setMapType(@NotNull MapType t)
    {
        mapType = t;
        switch (t)
        {
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
                getTopographicalViewMenu().setSelected(true);
                break;
            case MAP_NORMAL:
            case MAP_ALL:
            default:
                mapToolbar.setVisible(false);
                setStatus(Localization.getInstance().getMessageFor("status-fullscreen"));
                getNormalViewMenu().setSelected(true);
                break;
        }
        mapToolbar.setOptionTab(t);
        taskUpdateMap();
    }

    private void taskUpdateMap()
    {
        if (api == null)
            return;
        TaskFrame loadFrame = new TaskFrame();
        loadFrame.setSize(TASKFRAME_WIDTH, TASKFRAME_HEIGHT);
        loadFrame.setLocation((this.getWidth() / 2) - (loadFrame.getWidth() / 2), (this.getHeight() / 2) - (loadFrame.getHeight() / 2));
        loadFrame.setVisible(true);
        loadFrame.updateMap(this);
    }

    @Nullable
    private File getMapFile(File folder)
    {
        JFileChooser filePicker = new JFileChooser();
        File mFolder;
        filePicker.setCurrentDirectory(folder);
        filePicker.setDialogTitle(Localization.getInstance().getMessageFor("dialog-file-title"));
        filePicker.setSelectedFile(new File(defaultMapFile));
        filePicker.setApproveButtonText(Localization.getInstance().getMessageFor("dialog-file-approve"));
        filePicker.addChoosableFileFilter(new MapFileTypeFilter());
        filePicker.setAcceptAllFileFilterUsed(false);
        switch (filePicker.showOpenDialog(MapViewerFrame.this))
        {
            case JFileChooser.APPROVE_OPTION:
                File currentFile = filePicker.getSelectedFile();
                if (currentFile == null)
                    mFolder = filePicker.getCurrentDirectory();
                else if (currentFile.isDirectory())
                    mFolder = currentFile.getAbsoluteFile();
                else if (currentFile.toString().endsWith(".map"))
                    mFolder = filePicker.getCurrentDirectory();
                else
                {
                    JOptionPane.showMessageDialog(MapViewerFrame.this,
                            Localization.getInstance().getMessageFor("dialog-file-error-folder-text"),
                            Localization.getInstance().getMessageFor("dialog-file-error-folder-title"),
                            JOptionPane.ERROR_MESSAGE);
                    return getMapFile(filePicker.getCurrentDirectory());
                }
                for (String mapFile : mapFiles)
                {
                    currentFile = new File(mFolder.toString() + File.separator + mapFile);
                    if (!currentFile.exists())
                    {
                        JOptionPane.showMessageDialog(MapViewerFrame.this,
                                Localization.getInstance().getTemplatedMessageFor("dialog-file-error-missing-text", "currentFile", currentFile.getName()),
                                Localization.getInstance().getMessageFor("dialog-file-error-missing-title"),
                                JOptionPane.ERROR_MESSAGE);
                        return getMapFile(filePicker.getCurrentDirectory());
                    }
                    if ((currentFile.length() * 5) > Runtime.getRuntime().maxMemory())
                    {
                        JOptionPane.showMessageDialog(MapViewerFrame.this,
                                Localization.getInstance().getTemplatedMessageFor("dialog-file-error-memory-text", "currentFile", currentFile.getName()),
                                Localization.getInstance().getMessageFor("dialog-file-error-memory-title"),
                                JOptionPane.ERROR_MESSAGE);
                        return null;
                    }
                }
                return mFolder;
            default:
                return null;
        }
    }

    @Nullable
    private File getScreenCapSaveFile()
    {
        return getSaveFile(true, lastScreenCapFile);
    }

    @Nullable
    private File getMapImageSaveFile()
    {
        return getSaveFile(false, lastMapImageFile);
    }

    private String getSaveFileName(boolean isScreenCap)
    {
        return getSaveFileName(isScreenCap, defaultFileType);
    }

    private String getSaveFileName(boolean isScreenCap, int fileType)
    {
        String fDateTime = dateFormat.format(new Date());
        String saveFileName;
        if (isScreenCap)
        {
            saveFileName = mapName + "-mapdetail_" + fDateTime;
        }
        else
        {
            switch (mapType)
            {
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
        if (fileType != -1)
            saveFileName += fileTypes[fileType].getExtension();
        return saveFileName;
    }

    private void setStatus()
    {
        setStatus("");
    }

    private void setStatus(String status)
    {
        this.statusLabel.setText(status);
    }

    @Nullable
    private File getSaveFile(boolean isScreenCap, File folder)
    {
        JFileChooser filePicker = new JFileChooser();
        String saveFileName = getSaveFileName(isScreenCap);

        filePicker.setCurrentDirectory(folder);
        filePicker.setMultiSelectionEnabled(false);
        filePicker.setDialogTitle(Localization.getInstance().getMessageFor("dialog-save-title"));
        filePicker.setAcceptAllFileFilterUsed(false);
        filePicker.setSelectedFile(new File(saveFileName));
        filePicker.setApproveButtonText(Localization.getInstance().getMessageFor("dialog-save-approve"));

        for (ImageFileTypeFilter fileType : fileTypes) filePicker.addChoosableFileFilter(fileType);

        filePicker.addPropertyChangeListener(JFileChooser.FILE_FILTER_CHANGED_PROPERTY, evt -> {
            ImageFileTypeFilter filter = (ImageFileTypeFilter) evt.getNewValue();
            String extension = filter.getExtension();

            File selectedFile = filePicker.getSelectedFile();
            String path;

            if (selectedFile == null)
                path = getSaveFileName(isScreenCap, filter.getType());
            else
                path = selectedFile.getName();

            path = path.substring(0, path.lastIndexOf("."));

            filePicker.setSelectedFile(new File(path + extension));
        });

        switch (filePicker.showOpenDialog(MapViewerFrame.this))
        {
            case JFileChooser.APPROVE_OPTION:
                File currentFile = filePicker.getSelectedFile();
                if (currentFile == null)
                {
                    JOptionPane.showMessageDialog(this,
                            Localization.getInstance().getMessageFor("dialog-save-error-invalid-text"),
                            Localization.getInstance().getMessageFor("dialog-save-error-invalid-title"),
                            JOptionPane.ERROR_MESSAGE);
                    return getSaveFile(isScreenCap, filePicker.getCurrentDirectory());
                }
                ImageFileTypeFilter filter = (ImageFileTypeFilter) filePicker.getFileFilter();
                if (!currentFile.toString().endsWith(filter.getExtension()))
                    currentFile = new File(currentFile.toString() + filter.getExtension());
                if (currentFile.exists())
                {
                    int result = JOptionPane.showConfirmDialog(this,
                            Localization.getInstance().getTemplatedMessageFor("dialog-save-confirm-overwrite-text", "currentFile", currentFile.getName()),
                            Localization.getInstance().getMessageFor("dialog-save-confirm-overwrite-title"),
                            JOptionPane.YES_NO_OPTION);
                    if (result == JOptionPane.NO_OPTION)
                        return getSaveFile(isScreenCap, filePicker.getCurrentDirectory());
                }
                return currentFile;
            default:
                return null;
        }
    }

    @NotNull
    private Tile[] getCaveTiles()
    {
        List<Tile> caveTiles = new ArrayList<>();

        caveTiles.add(Tile.TILE_CAVE_WALL);
        caveTiles.add(Tile.TILE_CAVE_WALL_REINFORCED);

        for (Tile tile : Tile.getTiles(Tiles.CAT_CAVE, "*"))
        {
            if (tileVisibility.containsKey(tile) && !tileVisibility.get(tile)) continue;
            caveTiles.add(tile);
            if (tile == Tile.TILE_CAVE)
            {
                caveTiles.add(Tile.TILE_CAVE_FLOOR_REINFORCED);
                caveTiles.add(Tile.TILE_CAVE_EXIT);
            }
        }
        return caveTiles.toArray(new Tile[0]);
    }

    void setTileVisibility(final Tile tile, final boolean visible)
    {
        tileVisibility.put(tile, visible);
    }

    void optionsUpdate(MapType type)
    {
        if (this.mapType == type || type == MapType.MAP_ALL)
            taskUpdateMap();
    }

    void updateMap()
    {
        if (api == null) return;
        // Get current size of heap in bytes
        long heapSize = Runtime.getRuntime().totalMemory();

        // Get maximum size of heap in bytes. The heap cannot grow beyond this size.
        // Any attempt will result in an OutOfMemoryException.
        long heapMaxSize = Runtime.getRuntime().maxMemory();

        // Get amount of free memory within the heap in bytes. This size will increase
        // after garbage collection and decrease as new objects are created.
        long heapFreeSize = Runtime.getRuntime().freeMemory();
        Logger.getLogger(this.getClass().getName()).log(Level.INFO, Localization.getInstance().getTemplatedMessageFor("log-heap-size", "size", readableFileSize(heapSize)));
        Logger.getLogger(this.getClass().getName()).log(Level.INFO, Localization.getInstance().getTemplatedMessageFor("log-max-heap-size", "size", readableFileSize(heapMaxSize)));
        Logger.getLogger(this.getClass().getName()).log(Level.INFO, Localization.getInstance().getTemplatedMessageFor("log-free-heap-size", "size", readableFileSize(heapFreeSize)));
        this.itmSaveMapImage.setEnabled(true);
        this.itmSaveViewArea.setEnabled(true);
        switch (mapType)
        {
            case MAP_TOPOGRAPHICAL:
                this.mapPanel.setMapImage(api.getMapData().createTopographicDump(mapToolbar.getTopographyShowWater(),
                        mapToolbar.getContourLines()));
                break;
            case MAP_CAVE:
                Tile[] ct = getCaveTiles();
                BufferedImage bi = api.getMapData().createCaveDump(isShowCaveWater(), ct);
                this.mapPanel.setMapImage(bi);
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

    @NotNull
    private static String readableFileSize(long size)
    {
        if (size <= 0) return "0";
        final String[] units = new String[]{"B", "kB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    private void saveMapImage(@Nullable File destFile)
    {
        if (destFile == null) return;
        this.lastMapImageFile = destFile;
        saveImage(destFile, false);
    }

    private void saveScreenCapImage(@Nullable File destFile)
    {
        if (destFile == null) return;
        this.lastScreenCapFile = destFile;
        saveImage(destFile, true);
    }

    private void saveImage(@NotNull File destFile, boolean crop)
    {
        String imgType;
        if (destFile.toString().endsWith(".jpg"))
            imgType = "jpeg";
        else if (destFile.toString().endsWith(".gif"))
            imgType = "gif";
        else if (destFile.toString().endsWith(".bmp"))
            imgType = "bmp";
        else if (destFile.toString().endsWith(".png"))
            imgType = "png";
        else
        {
            JOptionPane.showMessageDialog(MapViewerFrame.this,
                    Localization.getInstance().getMessageFor("dialog-save-error-unsupported-text"),
                    Localization.getInstance().getMessageFor("dialog-save-error-unsupported-title"),
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        TaskFrame saveFrame = new TaskFrame();
        saveFrame.setSize(TASKFRAME_WIDTH, TASKFRAME_HEIGHT);
        saveFrame.setLocation((this.getWidth() / 2) - (saveFrame.getWidth() / 2), (this.getHeight() / 2) - (saveFrame.getHeight() / 2));
        saveFrame.saveImage(Objects.requireNonNull(this.mapPanel.getMapImage(crop)), imgType, destFile);
        saveFrame.setVisible(true);
    }

    // TODO: Refactor and localize
    @Nullable String getToolTipInfo(int x, int y)
    {
        if (api == null)
            return null;
        if (mapType == MapType.MAP_NORMAL)
            return null;
        boolean water = (api.getMapData().getSurfaceHeight(x, y) < 0);
        String ttText = "<html>";
        ttText += "[" + x + "," + y + "] ";
        if (water)
            ttText += "[Water] ";
        ttText += "<br/>";
        switch (mapType)
        {
            case MAP_CAVE:
                Tile ct = api.getMapData().getCaveTile(x, y);
                if (ct == null)
                {
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
                if (st == null)
                {
                    ttText += "Empty";
                    break;
                }
                int sHeight = api.getMapData().getSurfaceHeight(x, y);
                int rHeight = api.getMapData().getRockHeight(x, y);
                int dHeight = api.getMapData().getDirtLayerHeight(x, y);
                String flags = (st.canBearFruit() ? "Fruit " : "") + (st.canBotanize() ? "Botanize " : "") + (st.canForage() ? "Forage " : "");
                ttText += "(" + st.getName() + ") Height: " + sHeight + "<br/>";
                ttText += "Dirt Depth: " + dHeight + " Rock Height: " + rHeight + "<br/>";
                ttText += "Flags: " + flags + "<br/>";
                if (st.isGrass())
                {
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

    // TODO: Refactor and localize
    void setTileStatus(int x, int y)
    {
        if (api == null)
            return;
        if (mapType == MapType.MAP_NORMAL)
            return;
        boolean water = (api.getMapData().getSurfaceHeight(x, y) < 0);
        String ttText = "[Size: " + this.mapWidth + "x" + this.mapHeight + "] ";
        ttText += "[Tile: " + x + "," + y + "] ";
        if (water)
            ttText += "[Water] ";
        switch (mapType)
        {
            case MAP_CAVE:
                Tile ct = api.getMapData().getCaveTile(x, y);
                if (ct == null)
                {
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
                if (st == null)
                {
                    ttText += "";
                    break;
                }
                int sHeight = api.getMapData().getSurfaceHeight(x, y);
                int rHeight = api.getMapData().getRockHeight(x, y);
                int dHeight = api.getMapData().getDirtLayerHeight(x, y);
                String flags = (st.canBearFruit() ? "Fruit " : "") + (st.canBotanize() ? "Botanize " : "") + (st.canForage() ? "Forage " : "");
                ttText += "(" + st.getName() + ") Height: " + sHeight + " | ";
                ttText += "Dirt Depth: " + dHeight + " | Rock Height: " + rHeight + " | ";
                ttText += "Flags: " + flags;
                break;
        }
        setStatus(ttText);
    }

    @Contract(pure = true)
    @NotNull
    private JRadioButtonMenuItem getNormalViewMenu()
    {
        return btnItmNormal;
    }

    @Contract(pure = true)
    @NotNull
    private JRadioButtonMenuItem getTopographicalViewMenu()
    {
        return btnItmTopographical;
    }

    @Contract(pure = true)
    @NotNull
    private JRadioButtonMenuItem getCaveViewMenu()
    {
        return btnItmCaveMap;
    }

    @Contract(pure = true)
    @NotNull
    private JRadioButtonMenuItem getTerrainViewMenu()
    {
        return btnItmTerrainMap;
    }

    @NotNull MapToolbar getMapToolbar()
    {
        return mapToolbar;
    }

    @NotNull MapType getMapType()
    {
        return mapType;
    }

    @NotNull MapPanel getMapPanel()
    {
        return mapPanel;
    }

    String getVersion()
    {
        return VERSION;
    }

    @Contract(pure = true)
    private boolean isShowCaveWater()
    {
        return showCaveWater;
    }

    void setShowCaveWater(boolean showCaveWater)
    {
        this.showCaveWater = showCaveWater;
    }
}

