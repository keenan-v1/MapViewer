package com.wurmly.mapviewer.ui;

import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.JCheckBox;
import java.awt.GridLayout;
import javax.swing.JButton;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Point;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.awt.event.ActionEvent;
import java.awt.Button;
import javax.swing.JTabbedPane;
import javax.swing.border.EtchedBorder;

public class MapToolbar extends JPanel {

	private static final long serialVersionUID = -2134161019085782872L;
	private final JPanel controlPanel = new JPanel();
	private final JPanel topoPanel = new JPanel();
	private final MapViewerFrame parent;
	private JTextField txtContourLines;
	private JCheckBox topoShowWater;
	private JCheckBox caveShowCaves;
	private JCheckBox caveShowLava;
	private JCheckBox caveShowAdamantine;
	private JCheckBox caveShowSilver;
	private JCheckBox caveShowGlimmersteel;
	private JCheckBox caveShowCopper;
	private JCheckBox caveShowGold;
	private JCheckBox caveShowLead;
	private JCheckBox caveShowMarble;
	private JCheckBox caveShowWater;
	private JCheckBox caveShowTin;
	private JCheckBox caveShowSlate;
	private JCheckBox caveShowZinc;
	private JCheckBox terrainShowWater;
	private JCheckBox caveShowIron;
	private Button cLineBtn;
	private Point dPoint;
	private JTabbedPane tabOptions;
	private static final int TAB_TOPOGRAPHICAL = 1;
	private static final int TAB_TERRAIN = 0;
	private static final int TAB_CAVE = 2;
	private JCheckBox chckbxShowMouseover;

	public MapToolbar(MapViewerFrame viewer) {
		this.parent = viewer;
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 80, 86, 0 };
		gridBagLayout.rowHeights = new int[] { 0, -32, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 1.0, 0.0, 0.0, Double.MIN_VALUE };
		controlPanel.setLayout(gridBagLayout);
		add(controlPanel);
		JButton btnReset = new JButton("Reset");
		btnReset.setMnemonic('R');
		btnReset.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setDefaults();

				parent.optionsUpdate(MapType.MAP_ALL);
			}
		});

		tabOptions = new JTabbedPane(SwingConstants.TOP);
		GridBagConstraints gbc_tabOptions = new GridBagConstraints();
		gbc_tabOptions.gridwidth = 2;
		gbc_tabOptions.insets = new Insets(0, 0, 5, 0);
		gbc_tabOptions.fill = GridBagConstraints.BOTH;
		gbc_tabOptions.gridx = 0;
		gbc_tabOptions.gridy = 1;
		controlPanel.add(tabOptions, gbc_tabOptions);

		JPanel terrainPanel = new JPanel();
		tabOptions.addTab("Terrain (1)", null, terrainPanel, null);
		terrainPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		terrainPanel.setLayout(new GridLayout(0, 1, 0, 0));

		terrainShowWater = new JCheckBox("Show Water");
		terrainShowWater.setMnemonic('W');
		terrainShowWater.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				parent.optionsUpdate(MapType.MAP_TERRAIN);
			}
		});
		terrainShowWater.setSelected(true);
		terrainPanel.add(terrainShowWater);
		tabOptions.addTab("Topo (2)", null, topoPanel, null);
		topoPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		GridBagLayout gbl_topoPanel = new GridBagLayout();
		gbl_topoPanel.columnWidths = new int[] { 99, 53, 0 };
		gbl_topoPanel.rowHeights = new int[] { 20, 20, 0, 0 };
		gbl_topoPanel.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		gbl_topoPanel.rowWeights = new double[] { 0.0, 0.0, 0.0, Double.MIN_VALUE };
		topoPanel.setLayout(gbl_topoPanel);

		topoShowWater = new JCheckBox("Show Water");
		topoShowWater.setMnemonic('W');
		topoShowWater.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				parent.optionsUpdate(MapType.MAP_TOPOGRAPHICAL);
			}
		});
		topoShowWater.setSelected(true);
		GridBagConstraints gbc_topoShowWater = new GridBagConstraints();
		gbc_topoShowWater.insets = new Insets(0, 0, 5, 5);
		gbc_topoShowWater.anchor = GridBagConstraints.NORTHWEST;
		gbc_topoShowWater.gridx = 0;
		gbc_topoShowWater.gridy = 0;
		topoPanel.add(topoShowWater, gbc_topoShowWater);

		JLabel lblContourLineInterval = new JLabel("Contour Line Interval");
		GridBagConstraints gbc_lblContourLineInterval = new GridBagConstraints();
		gbc_lblContourLineInterval.anchor = GridBagConstraints.EAST;
		gbc_lblContourLineInterval.insets = new Insets(0, 0, 5, 5);
		gbc_lblContourLineInterval.gridx = 0;
		gbc_lblContourLineInterval.gridy = 1;
		topoPanel.add(lblContourLineInterval, gbc_lblContourLineInterval);

		txtContourLines = new JTextField();
		txtContourLines.setText("250");
		GridBagConstraints gbc_txtContourLines = new GridBagConstraints();
		gbc_txtContourLines.insets = new Insets(0, 0, 0, 5);
		gbc_txtContourLines.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtContourLines.gridx = 0;
		gbc_txtContourLines.gridy = 2;
		topoPanel.add(txtContourLines, gbc_txtContourLines);
		txtContourLines.setColumns(10);

		cLineBtn = new Button("Set");
		cLineBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				parent.optionsUpdate(MapType.MAP_TOPOGRAPHICAL);
			}
		});
		GridBagConstraints gbc_cLineBtn = new GridBagConstraints();
		gbc_cLineBtn.gridx = 1;
		gbc_cLineBtn.gridy = 2;
		topoPanel.add(cLineBtn, gbc_cLineBtn);

		JPanel cavePanel = new JPanel();
		tabOptions.addTab("Cave (3)", null, cavePanel, null);
		cavePanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		GridBagLayout gbl_cavePanel = new GridBagLayout();
		gbl_cavePanel.columnWidths = new int[] { 79, 70, 0 };
		gbl_cavePanel.rowHeights = new int[] { 22, 0, 0, 0, 0, 0, 0, 0 };
		gbl_cavePanel.columnWeights = new double[] { 0.0, 0.0, Double.MIN_VALUE };
		gbl_cavePanel.rowWeights = new double[] { 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		cavePanel.setLayout(gbl_cavePanel);

		caveShowCaves = new JCheckBox("Caves");
		caveShowCaves.setMnemonic('v');
		caveShowCaves.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				parent.optionsUpdate(MapType.MAP_CAVE);
			}
		});
		caveShowCaves.setSelected(true);
		GridBagConstraints gbc_caveShowCaves = new GridBagConstraints();
		gbc_caveShowCaves.anchor = GridBagConstraints.WEST;
		gbc_caveShowCaves.insets = new Insets(0, 0, 5, 5);
		gbc_caveShowCaves.gridx = 0;
		gbc_caveShowCaves.gridy = 0;
		cavePanel.add(caveShowCaves, gbc_caveShowCaves);

		caveShowLava = new JCheckBox("Lava");
		caveShowLava.setMnemonic('l');
		caveShowLava.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				parent.optionsUpdate(MapType.MAP_CAVE);
			}
		});
		caveShowLava.setSelected(true);
		GridBagConstraints gbc_caveShowLava = new GridBagConstraints();
		gbc_caveShowLava.anchor = GridBagConstraints.WEST;
		gbc_caveShowLava.insets = new Insets(0, 0, 5, 0);
		gbc_caveShowLava.gridx = 1;
		gbc_caveShowLava.gridy = 0;
		cavePanel.add(caveShowLava, gbc_caveShowLava);

		caveShowIron = new JCheckBox("Iron");
		caveShowIron.setMnemonic('i');
		caveShowIron.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				parent.optionsUpdate(MapType.MAP_CAVE);
			}
		});
		caveShowIron.setSelected(true);
		GridBagConstraints gbc_caveShowIron = new GridBagConstraints();
		gbc_caveShowIron.anchor = GridBagConstraints.WEST;
		gbc_caveShowIron.insets = new Insets(0, 0, 5, 5);
		gbc_caveShowIron.gridx = 0;
		gbc_caveShowIron.gridy = 1;
		cavePanel.add(caveShowIron, gbc_caveShowIron);

		caveShowCopper = new JCheckBox("Copper");
		caveShowCopper.setMnemonic('p');
		caveShowCopper.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				parent.optionsUpdate(MapType.MAP_CAVE);
			}
		});
		caveShowCopper.setSelected(true);
		GridBagConstraints gbc_caveShowCopper = new GridBagConstraints();
		gbc_caveShowCopper.anchor = GridBagConstraints.WEST;
		gbc_caveShowCopper.insets = new Insets(0, 0, 5, 0);
		gbc_caveShowCopper.gridx = 1;
		gbc_caveShowCopper.gridy = 1;
		cavePanel.add(caveShowCopper, gbc_caveShowCopper);

		caveShowGold = new JCheckBox("Gold");
		caveShowGold.setMnemonic('o');
		caveShowGold.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				parent.optionsUpdate(MapType.MAP_CAVE);
			}
		});
		caveShowGold.setSelected(true);
		GridBagConstraints gbc_caveShowGold = new GridBagConstraints();
		gbc_caveShowGold.anchor = GridBagConstraints.WEST;
		gbc_caveShowGold.insets = new Insets(0, 0, 5, 5);
		gbc_caveShowGold.gridx = 0;
		gbc_caveShowGold.gridy = 2;
		cavePanel.add(caveShowGold, gbc_caveShowGold);

		caveShowTin = new JCheckBox("Tin");
		caveShowTin.setMnemonic('t');
		caveShowTin.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				parent.optionsUpdate(MapType.MAP_CAVE);
			}
		});
		caveShowTin.setSelected(true);
		GridBagConstraints gbc_caveShowTin = new GridBagConstraints();
		gbc_caveShowTin.anchor = GridBagConstraints.WEST;
		gbc_caveShowTin.insets = new Insets(0, 0, 5, 0);
		gbc_caveShowTin.gridx = 1;
		gbc_caveShowTin.gridy = 2;
		cavePanel.add(caveShowTin, gbc_caveShowTin);

		caveShowZinc = new JCheckBox("Zinc");
		caveShowZinc.setMnemonic('z');
		caveShowZinc.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				parent.optionsUpdate(MapType.MAP_CAVE);
			}
		});
		caveShowZinc.setSelected(true);
		GridBagConstraints gbc_caveShowZinc = new GridBagConstraints();
		gbc_caveShowZinc.anchor = GridBagConstraints.WEST;
		gbc_caveShowZinc.insets = new Insets(0, 0, 5, 5);
		gbc_caveShowZinc.gridx = 0;
		gbc_caveShowZinc.gridy = 3;
		cavePanel.add(caveShowZinc, gbc_caveShowZinc);

		caveShowSilver = new JCheckBox("Silver");
		caveShowSilver.setMnemonic('s');
		caveShowSilver.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				parent.optionsUpdate(MapType.MAP_CAVE);
			}
		});
		caveShowSilver.setSelected(true);
		GridBagConstraints gbc_caveShowSilver = new GridBagConstraints();
		gbc_caveShowSilver.anchor = GridBagConstraints.WEST;
		gbc_caveShowSilver.insets = new Insets(0, 0, 5, 0);
		gbc_caveShowSilver.gridx = 1;
		gbc_caveShowSilver.gridy = 3;
		cavePanel.add(caveShowSilver, gbc_caveShowSilver);

		caveShowMarble = new JCheckBox("Marble");
		caveShowMarble.setMnemonic('m');
		caveShowMarble.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				parent.optionsUpdate(MapType.MAP_CAVE);
			}
		});
		caveShowMarble.setSelected(true);
		GridBagConstraints gbc_caveShowMarble = new GridBagConstraints();
		gbc_caveShowMarble.anchor = GridBagConstraints.WEST;
		gbc_caveShowMarble.insets = new Insets(0, 0, 5, 5);
		gbc_caveShowMarble.gridx = 0;
		gbc_caveShowMarble.gridy = 4;
		cavePanel.add(caveShowMarble, gbc_caveShowMarble);

		caveShowSlate = new JCheckBox("Slate");
		caveShowSlate.setMnemonic('e');
		caveShowSlate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				parent.optionsUpdate(MapType.MAP_CAVE);
			}
		});

		caveShowAdamantine = new JCheckBox("Adamantine");
		caveShowAdamantine.setMnemonic('a');
		caveShowAdamantine.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				parent.optionsUpdate(MapType.MAP_CAVE);
			}
		});
		caveShowAdamantine.setSelected(true);
		GridBagConstraints gbc_caveShowAdamantine = new GridBagConstraints();
		gbc_caveShowAdamantine.anchor = GridBagConstraints.WEST;
		gbc_caveShowAdamantine.insets = new Insets(0, 0, 5, 0);
		gbc_caveShowAdamantine.gridx = 1;
		gbc_caveShowAdamantine.gridy = 4;
		cavePanel.add(caveShowAdamantine, gbc_caveShowAdamantine);
		caveShowSlate.setSelected(true);
		GridBagConstraints gbc_caveShowSlate = new GridBagConstraints();
		gbc_caveShowSlate.anchor = GridBagConstraints.WEST;
		gbc_caveShowSlate.insets = new Insets(0, 0, 5, 5);
		gbc_caveShowSlate.gridx = 0;
		gbc_caveShowSlate.gridy = 5;
		cavePanel.add(caveShowSlate, gbc_caveShowSlate);

		caveShowLead = new JCheckBox("Lead");
		caveShowLead.setMnemonic('d');
		caveShowLead.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				parent.optionsUpdate(MapType.MAP_CAVE);
			}
		});

		caveShowGlimmersteel = new JCheckBox("Glimmersteel");
		caveShowGlimmersteel.setMnemonic('g');
		caveShowGlimmersteel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				parent.optionsUpdate(MapType.MAP_CAVE);
			}
		});
		caveShowGlimmersteel.setSelected(true);
		GridBagConstraints gbc_caveShowGlimmersteel = new GridBagConstraints();
		gbc_caveShowGlimmersteel.anchor = GridBagConstraints.WEST;
		gbc_caveShowGlimmersteel.insets = new Insets(0, 0, 5, 0);
		gbc_caveShowGlimmersteel.gridx = 1;
		gbc_caveShowGlimmersteel.gridy = 5;
		cavePanel.add(caveShowGlimmersteel, gbc_caveShowGlimmersteel);
		caveShowLead.setSelected(true);
		GridBagConstraints gbc_caveShowLead = new GridBagConstraints();
		gbc_caveShowLead.anchor = GridBagConstraints.WEST;
		gbc_caveShowLead.insets = new Insets(0, 0, 0, 5);
		gbc_caveShowLead.gridx = 0;
		gbc_caveShowLead.gridy = 6;
		cavePanel.add(caveShowLead, gbc_caveShowLead);

		caveShowWater = new JCheckBox("Water");
		caveShowWater.setMnemonic('w');
		caveShowWater.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				parent.optionsUpdate(MapType.MAP_CAVE);
			}
		});
		caveShowWater.setSelected(true);
		GridBagConstraints gbc_caveShowWater = new GridBagConstraints();
		gbc_caveShowWater.anchor = GridBagConstraints.WEST;
		gbc_caveShowWater.gridx = 1;
		gbc_caveShowWater.gridy = 6;
		cavePanel.add(caveShowWater, gbc_caveShowWater);

		tabOptions.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent evt) {
				switch (tabOptions.getSelectedIndex()) {
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
			}
		});
		tabOptions.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent arg0) {
				setFocusable(false);
				parent.requestFocusInWindow();				
			}

			@Override
			public void focusLost(FocusEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
		GridBagConstraints gbc_btnReset = new GridBagConstraints();
		gbc_btnReset.insets = new Insets(0, 0, 0, 5);
		gbc_btnReset.fill = GridBagConstraints.BOTH;
		gbc_btnReset.gridx = 0;
		gbc_btnReset.gridy = 2;
		controlPanel.add(btnReset, gbc_btnReset);

		chckbxShowMouseover = new JCheckBox("Show Mouseover");
		chckbxShowMouseover.setMnemonic('u');
		chckbxShowMouseover.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (!getShowMouseover().isSelected())
					parent.getMapPanel().setToolTipText(null);
			}
		});
		GridBagConstraints gbc_chckbxShowMouseover = new GridBagConstraints();
		gbc_chckbxShowMouseover.fill = GridBagConstraints.VERTICAL;
		gbc_chckbxShowMouseover.gridx = 1;
		gbc_chckbxShowMouseover.gridy = 2;
		controlPanel.add(chckbxShowMouseover, gbc_chckbxShowMouseover);
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent evt) {
				dPoint = evt.getPoint();
			}
		});
		addMouseMotionListener(new MouseAdapter() {
			@Override
			public void mouseDragged(MouseEvent evt) {
				Point lP = getLocation();
				lP.x = lP.x + evt.getX() - (int) dPoint.getX();
				lP.y = lP.y + evt.getY() - (int) dPoint.getY();

				setLocation(lP);
			}
		});

		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent evt) {
				parent.dispatchEvent(evt);
				Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Dispatching key event to " + parent.getClass().getName());
			}
		});
	}

	private void setDefaults() {
		txtContourLines.setText("250");
		topoShowWater.setSelected(true);
		caveShowCaves.setSelected(true);
		caveShowLava.setSelected(true);
		caveShowAdamantine.setSelected(true);
		caveShowSilver.setSelected(true);
		caveShowGlimmersteel.setSelected(true);
		caveShowCopper.setSelected(true);
		caveShowGold.setSelected(true);
		caveShowLead.setSelected(true);
		caveShowMarble.setSelected(true);
		caveShowWater.setSelected(true);
		caveShowTin.setSelected(true);
		caveShowSlate.setSelected(true);
		caveShowZinc.setSelected(true);
		terrainShowWater.setSelected(true);
		caveShowIron.setSelected(true);
	}

	public void setOptionTab(MapType t) {
		switch (t) {
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

	public boolean isTopoShowWater() {
		return topoShowWater.isSelected();
	}

	public short getContourLines() {
		return Short.parseShort(txtContourLines.getText());
	}

	public boolean isCaveShowCaves() {
		return caveShowCaves.isSelected();
	}

	public boolean isCaveShowLava() {
		return caveShowLava.isSelected();
	}

	public boolean isCaveShowAdamantine() {
		return caveShowAdamantine.isSelected();
	}

	public boolean isCaveShowSilver() {
		return caveShowSilver.isSelected();
	}

	public boolean isCaveShowGlimmersteel() {
		return caveShowGlimmersteel.isSelected();
	}

	public boolean isCaveShowCopper() {
		return caveShowCopper.isSelected();
	}

	public boolean isCaveShowGold() {
		return caveShowGold.isSelected();
	}

	public boolean isCaveShowLead() {
		return caveShowLead.isSelected();
	}

	public boolean isCaveShowMarble() {
		return caveShowMarble.isSelected();
	}

	public boolean isCaveShowWater() {
		return caveShowWater.isSelected();
	}

	public boolean isCaveShowTin() {
		return caveShowTin.isSelected();
	}

	public boolean isCaveShowSlate() {
		return caveShowSlate.isSelected();
	}

	public boolean isCaveShowZinc() {
		return caveShowZinc.isSelected();
	}

	public boolean isTerrainShowWater() {
		return terrainShowWater.isSelected();
	}

	public boolean isCaveShowIron() {
		return caveShowIron.isSelected();
	}

	public Button getCLineBtn() {
		return cLineBtn;
	}

	public JCheckBox getShowMouseover() {
		return chckbxShowMouseover;
	}
}
