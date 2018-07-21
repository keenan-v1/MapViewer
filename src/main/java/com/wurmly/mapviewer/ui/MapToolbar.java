package com.wurmly.mapviewer.ui;

import com.wurmly.mapviewer.localization.Localization;
import com.wurmonline.mesh.Tiles.Tile;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

class MapToolbar extends JPanel
{

    private static final long serialVersionUID = -2134161019085782872L;
    private final MapViewerFrame parent;
    @NotNull
    private final JTextField txtContourLines;
    @NotNull
    private final JCheckBox topographyShowWater;
    @NotNull
    private final JCheckBox terrainShowWater;
    private Point dPoint;
    @NotNull
    private final JTabbedPane tabOptions;
    private static final int TAB_TOPOGRAPHICAL = 1;
    private static final int TAB_TERRAIN = 0;
    private static final int TAB_CAVE = 2;
    @NotNull
    private final JCheckBox boxShowMouseover;
    private final HashMap<JCheckBox, Tile> tileVisibilityBoxes = new HashMap<>();


    private JCheckBox createCheckBox(final String key, final Tile tile)
    {
        final JCheckBox box = new JCheckBox(Localization.getInstance().getMessageFor(key));
        box.setMnemonic(Localization.getInstance().getMnemonicFor(key));
        box.setSelected(true);
        if (tile != null)
        {
            parent.setTileVisibility(tile, true);
            box.addActionListener(e -> {
                parent.setTileVisibility(tile, box.isSelected());
                parent.optionsUpdate(MapType.MAP_CAVE);
            });
        }
        else
        {
            box.addActionListener(e -> {
                parent.setShowCaveWater(box.isSelected());
                parent.optionsUpdate(MapType.MAP_CAVE);
            });
        }
        tileVisibilityBoxes.put(box, tile);
        return box;
    }

    private GridBagConstraints createCheckBoxConstraints(final int x, final int y)
    {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 5, (x == 1) ? 0 : 5);
        gbc.gridx = x;
        gbc.gridy = y;
        return gbc;
    }

    MapToolbar(MapViewerFrame viewer)
    {
        this.parent = viewer;
        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[]{80, 86, 0};
        gridBagLayout.rowHeights = new int[]{0, -32, 0, 0};
        gridBagLayout.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
        gridBagLayout.rowWeights = new double[]{1.0, 0.0, 0.0, Double.MIN_VALUE};
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(gridBagLayout);
        add(controlPanel);
        JButton btnReset = new JButton(Localization.getInstance().getMessageFor("button-reset"));
        btnReset.setMnemonic(Localization.getInstance().getMnemonicFor("button-reset"));
        btnReset.addActionListener(e -> {
            setDefaults();
            parent.optionsUpdate(MapType.MAP_ALL);
        });

        tabOptions = new JTabbedPane(SwingConstants.TOP);
        GridBagConstraints gbcTabOptions = new GridBagConstraints();
        gbcTabOptions.gridwidth = 2;
        gbcTabOptions.insets = new Insets(0, 0, 5, 0);
        gbcTabOptions.fill = GridBagConstraints.BOTH;
        gbcTabOptions.gridx = 0;
        gbcTabOptions.gridy = 1;
        controlPanel.add(tabOptions, gbcTabOptions);

        JPanel terrainPanel = new JPanel();
        tabOptions.addTab(Localization.getInstance().getMessageFor("tab-terrain"), null, terrainPanel, null);
        terrainPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
        terrainPanel.setLayout(new GridLayout(0, 1, 0, 0));

        terrainShowWater = new JCheckBox(Localization.getInstance().getMessageFor("box-show") + Localization.getInstance().getMessageFor("box-show-water"));
        terrainShowWater.setMnemonic(Localization.getInstance().getMnemonicFor("box-show-water"));
        terrainShowWater.addActionListener(e -> parent.optionsUpdate(MapType.MAP_TERRAIN));
        terrainShowWater.setSelected(true);
        terrainPanel.add(terrainShowWater);
        JPanel topographyPanel = new JPanel();
        tabOptions.addTab(Localization.getInstance().getMessageFor("tab-topography"), null, topographyPanel, null);
        topographyPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
        GridBagLayout gblTopographyPanel = new GridBagLayout();
        gblTopographyPanel.columnWidths = new int[]{99, 53, 0};
        gblTopographyPanel.rowHeights = new int[]{20, 20, 0, 0};
        gblTopographyPanel.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
        gblTopographyPanel.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
        topographyPanel.setLayout(gblTopographyPanel);

        topographyShowWater = new JCheckBox(Localization.getInstance().getMessageFor("box-show") + Localization.getInstance().getMessageFor("box-show-water"));
        topographyShowWater.setMnemonic(Localization.getInstance().getMnemonicFor("box-show-water"));
        topographyShowWater.addActionListener(e -> parent.optionsUpdate(MapType.MAP_TOPOGRAPHICAL));
        topographyShowWater.setSelected(true);
        GridBagConstraints gbcTopographyShowWater = new GridBagConstraints();
        gbcTopographyShowWater.insets = new Insets(0, 0, 5, 5);
        gbcTopographyShowWater.anchor = GridBagConstraints.NORTHWEST;
        gbcTopographyShowWater.gridx = 0;
        gbcTopographyShowWater.gridy = 0;
        topographyPanel.add(topographyShowWater, gbcTopographyShowWater);

        JLabel lblContourLineInterval = new JLabel(Localization.getInstance().getMessageFor("contour-line-interval"));
        GridBagConstraints gbcLblContourLineInterval = new GridBagConstraints();
        gbcLblContourLineInterval.anchor = GridBagConstraints.EAST;
        gbcLblContourLineInterval.insets = new Insets(0, 0, 5, 5);
        gbcLblContourLineInterval.gridx = 0;
        gbcLblContourLineInterval.gridy = 1;
        topographyPanel.add(lblContourLineInterval, gbcLblContourLineInterval);

        txtContourLines = new JTextField();
        txtContourLines.setText("250");
        GridBagConstraints gbcTxtContourLines = new GridBagConstraints();
        gbcTxtContourLines.insets = new Insets(0, 0, 0, 5);
        gbcTxtContourLines.fill = GridBagConstraints.HORIZONTAL;
        gbcTxtContourLines.gridx = 0;
        gbcTxtContourLines.gridy = 2;
        topographyPanel.add(txtContourLines, gbcTxtContourLines);
        txtContourLines.setColumns(10);

        Button contourLineBtn = new Button(Localization.getInstance().getMessageFor("button-set"));
        contourLineBtn.addActionListener(e -> parent.optionsUpdate(MapType.MAP_TOPOGRAPHICAL));
        GridBagConstraints gbcContourLineBtn = new GridBagConstraints();
        gbcContourLineBtn.gridx = 1;
        gbcContourLineBtn.gridy = 2;
        topographyPanel.add(contourLineBtn, gbcContourLineBtn);

        JPanel cavePanel = new JPanel();
        tabOptions.addTab(Localization.getInstance().getMessageFor("tab-cave"), null, cavePanel, null);
        cavePanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
        GridBagLayout gblCavePanel = new GridBagLayout();
        gblCavePanel.columnWidths = new int[]{79, 70, 0};
        gblCavePanel.rowHeights = new int[]{20, 0, 0, 0, 0, 0, 0, 0, 0};
        gblCavePanel.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
        gblCavePanel.rowWeights = new double[]{1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0};
        cavePanel.setLayout(gblCavePanel);

        cavePanel.add(createCheckBox("box-show-caves", Tile.TILE_CAVE), createCheckBoxConstraints(0, 0));
        cavePanel.add(createCheckBox("box-show-lava", Tile.TILE_CAVE_WALL_LAVA), createCheckBoxConstraints(1, 0));
        cavePanel.add(createCheckBox("box-show-iron", Tile.TILE_CAVE_WALL_ORE_IRON), createCheckBoxConstraints(0, 1));
        cavePanel.add(createCheckBox("box-show-copper", Tile.TILE_CAVE_WALL_ORE_COPPER), createCheckBoxConstraints(1, 1));
        cavePanel.add(createCheckBox("box-show-gold", Tile.TILE_CAVE_WALL_ORE_GOLD), createCheckBoxConstraints(0, 2));
        cavePanel.add(createCheckBox("box-show-tin", Tile.TILE_CAVE_WALL_ORE_TIN), createCheckBoxConstraints(1, 2));
        cavePanel.add(createCheckBox("box-show-zinc", Tile.TILE_CAVE_WALL_ORE_ZINC), createCheckBoxConstraints(0, 3));
        cavePanel.add(createCheckBox("box-show-silver", Tile.TILE_CAVE_WALL_ORE_SILVER), createCheckBoxConstraints(1, 3));
        cavePanel.add(createCheckBox("box-show-marble", Tile.TILE_CAVE_WALL_MARBLE), createCheckBoxConstraints(0, 4));
        cavePanel.add(createCheckBox("box-show-adamantine", Tile.TILE_CAVE_WALL_ORE_ADAMANTINE), createCheckBoxConstraints(1, 4));
        cavePanel.add(createCheckBox("box-show-slate", Tile.TILE_CAVE_WALL_SLATE), createCheckBoxConstraints(0, 5));
        cavePanel.add(createCheckBox("box-show-glimmersteel", Tile.TILE_CAVE_WALL_ORE_GLIMMERSTEEL), createCheckBoxConstraints(1, 5));
        cavePanel.add(createCheckBox("box-show-lead", Tile.TILE_CAVE_WALL_ORE_LEAD), createCheckBoxConstraints(0, 6));
        cavePanel.add(createCheckBox("box-show-water", null), createCheckBoxConstraints(1, 6));
        cavePanel.add(createCheckBox("box-show-rocksalt", Tile.TILE_CAVE_WALL_ROCKSALT), createCheckBoxConstraints(0, 7));
        cavePanel.add(createCheckBox("box-show-sandstone", Tile.TILE_CAVE_WALL_SANDSTONE), createCheckBoxConstraints(1, 7));

        tabOptions.addChangeListener(evt -> {
            switch (tabOptions.getSelectedIndex())
            {
                case TAB_CAVE:
                    parent.setMapType(MapType.MAP_CAVE);
                    break;
                case TAB_TOPOGRAPHICAL:
                    parent.setMapType(MapType.MAP_TOPOGRAPHICAL);
                    break;
                case TAB_TERRAIN:
                    parent.setMapType(MapType.MAP_TERRAIN);
                    break;
            }
        });

        tabOptions.addFocusListener(new FocusListener()
        {
            @Override
            public void focusGained(FocusEvent arg0)
            {
                setFocusable(false);
                parent.requestFocusInWindow();
            }

            @Override
            public void focusLost(FocusEvent arg0)
            {
                // TODO Auto-generated method stub

            }
        });
        GridBagConstraints gbcBtnReset = new GridBagConstraints();
        gbcBtnReset.insets = new Insets(0, 0, 0, 5);
        gbcBtnReset.fill = GridBagConstraints.BOTH;
        gbcBtnReset.gridx = 0;
        gbcBtnReset.gridy = 2;
        controlPanel.add(btnReset, gbcBtnReset);

        boxShowMouseover = new JCheckBox(Localization.getInstance().getMessageFor("box-show-mouseover"));
        boxShowMouseover.setMnemonic(Localization.getInstance().getMnemonicFor("box-show-mouseover"));
        boxShowMouseover.addActionListener(arg0 -> {
            if (!getShowMouseover().isSelected())
                parent.getMapPanel().setToolTipText(null);
        });
        GridBagConstraints gbcBoxShowMouseover = new GridBagConstraints();
        gbcBoxShowMouseover.fill = GridBagConstraints.VERTICAL;
        gbcBoxShowMouseover.gridx = 1;
        gbcBoxShowMouseover.gridy = 2;
        controlPanel.add(boxShowMouseover, gbcBoxShowMouseover);
        addMouseListener(new MouseAdapter()
        {
            @Override
            public void mousePressed(@NotNull MouseEvent evt)
            {
                dPoint = evt.getPoint();
            }
        });
        addMouseMotionListener(new MouseAdapter()
        {
            @Override
            public void mouseDragged(@NotNull MouseEvent evt)
            {
                Point lP = getLocation();
                lP.x = lP.x + evt.getX() - (int) dPoint.getX();
                lP.y = lP.y + evt.getY() - (int) dPoint.getY();

                setLocation(lP);
            }
        });
        addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyPressed(KeyEvent evt)
            {
                parent.dispatchEvent(evt);
                Logger.getLogger(this.getClass().getName()).log(Level.INFO, Localization.getInstance().getMessageFor("log-key-event") + " " + parent.getClass().getName());
            }
        });
        setDefaults();
    }

    private void setDefaults()
    {
        txtContourLines.setText("250");
        topographyShowWater.setSelected(true);
        terrainShowWater.setSelected(true);
        tileVisibilityBoxes.forEach((box, tile) -> {
            if (tile != null)
                parent.setTileVisibility(tile, true);
            else
                parent.setShowCaveWater(true);
            box.setSelected(true);
        });
    }

    void setOptionTab(@NotNull MapType t)
    {
        switch (t)
        {
            case MAP_CAVE:
                tabOptions.setSelectedIndex(TAB_CAVE);
                break;
            case MAP_TOPOGRAPHICAL:
                tabOptions.setSelectedIndex(TAB_TOPOGRAPHICAL);
                break;
            case MAP_TERRAIN:
                tabOptions.setSelectedIndex(TAB_TERRAIN);
                break;
            case MAP_NORMAL:
            case MAP_ALL:
            default:
        }
    }

    boolean getTopographyShowWater()
    {
        return topographyShowWater.isSelected();
    }

    short getContourLines()
    {
        return Short.parseShort(txtContourLines.getText());
    }

    boolean isTerrainShowWater()
    {
        return terrainShowWater.isSelected();
    }

    @NotNull JCheckBox getShowMouseover()
    {
        return boxShowMouseover;
    }
}
