package com.wurmly.mapviewer.ui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.border.TitledBorder;
import javax.swing.border.LineBorder;
import java.awt.Color;
import javax.swing.JCheckBox;
import java.awt.GridLayout;
import javax.swing.JButton;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Button;

public class MapOptions extends JFrame {

	private static final long serialVersionUID = -2134161019085782872L;
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
	private JCheckBox autoRefreshMap;
	private Button cLineBtn;
	
	public MapOptions(MapViewerFrame viewer) {
		super("Map Viewer - Options");
		this.parent = viewer;
		this.setDefaultCloseOperation(HIDE_ON_CLOSE);
		this.setSize(MapViewerFrame.OPTIONS_WIDTH, MapViewerFrame.OPTIONS_HEIGHT);
		this.setAlwaysOnTop(true);
		this.setAutoRequestFocus(true);
		this.setResizable(false);
		setUndecorated(true);
		getRootPane().setWindowDecorationStyle(JRootPane.NONE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{150, 284, 0};
		gridBagLayout.rowHeights = new int[]{0, 73, 50, 156, 30, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		getContentPane().setLayout(gridBagLayout);
		
		autoRefreshMap = new JCheckBox("Auto Refresh Map");
		autoRefreshMap.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(getAutoRefreshMap().isSelected())
					getCLineBtn().setEnabled(true);
				else
					getCLineBtn().setEnabled(false);
			}
		});
		autoRefreshMap.setSelected(true);
		GridBagConstraints gbc_autoRefreshMap = new GridBagConstraints();
		gbc_autoRefreshMap.anchor = GridBagConstraints.WEST;
		gbc_autoRefreshMap.insets = new Insets(0, 0, 5, 5);
		gbc_autoRefreshMap.gridx = 0;
		gbc_autoRefreshMap.gridy = 0;
		getContentPane().add(autoRefreshMap, gbc_autoRefreshMap);
		topoPanel.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0)), "Topographical Map", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_topoPanel = new GridBagConstraints();
		gbc_topoPanel.gridwidth = 2;
		gbc_topoPanel.fill = GridBagConstraints.BOTH;
		gbc_topoPanel.insets = new Insets(0, 0, 5, 0);
		gbc_topoPanel.gridx = 0;
		gbc_topoPanel.gridy = 1;
		getContentPane().add(topoPanel, gbc_topoPanel);
		GridBagLayout gbl_topoPanel = new GridBagLayout();
		gbl_topoPanel.columnWidths = new int[]{99, 85, 0, 0};
		gbl_topoPanel.rowHeights = new int[]{20, 20, 0};
		gbl_topoPanel.columnWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		gbl_topoPanel.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		topoPanel.setLayout(gbl_topoPanel);
		
		topoShowWater = new JCheckBox("Show Water");
		topoShowWater.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(getAutoRefreshMap().isSelected())
					parent.optionsUpdate();
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
		gbc_lblContourLineInterval.insets = new Insets(0, 0, 0, 5);
		gbc_lblContourLineInterval.gridx = 0;
		gbc_lblContourLineInterval.gridy = 1;
		topoPanel.add(lblContourLineInterval, gbc_lblContourLineInterval);
		
		txtContourLines = new JTextField();
		txtContourLines.setText("250");
		GridBagConstraints gbc_txtContourLines = new GridBagConstraints();
		gbc_txtContourLines.insets = new Insets(0, 0, 0, 5);
		gbc_txtContourLines.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtContourLines.gridx = 1;
		gbc_txtContourLines.gridy = 1;
		topoPanel.add(txtContourLines, gbc_txtContourLines);
		txtContourLines.setColumns(10);
		
		cLineBtn = new Button("Set");
		cLineBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(getAutoRefreshMap().isSelected())
					parent.optionsUpdate();
			}
		});
		GridBagConstraints gbc_cLineBtn = new GridBagConstraints();
		gbc_cLineBtn.gridx = 2;
		gbc_cLineBtn.gridy = 1;
		topoPanel.add(cLineBtn, gbc_cLineBtn);
		
		JPanel terrainPanel = new JPanel();
		terrainPanel.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0)), "Terrain Map", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_terrainPanel = new GridBagConstraints();
		gbc_terrainPanel.gridwidth = 2;
		gbc_terrainPanel.fill = GridBagConstraints.BOTH;
		gbc_terrainPanel.insets = new Insets(0, 0, 5, 0);
		gbc_terrainPanel.gridx = 0;
		gbc_terrainPanel.gridy = 2;
		getContentPane().add(terrainPanel, gbc_terrainPanel);
		terrainPanel.setLayout(new GridLayout(0, 1, 0, 0));
		
		terrainShowWater = new JCheckBox("Show Water");
		terrainShowWater.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(getAutoRefreshMap().isSelected())
					parent.optionsUpdate();
			}
		});
		terrainShowWater.setSelected(true);
		terrainPanel.add(terrainShowWater);
		
		JPanel cavePanel = new JPanel();
		cavePanel.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0)), "Cave Map", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_cavePanel = new GridBagConstraints();
		gbc_cavePanel.gridwidth = 2;
		gbc_cavePanel.fill = GridBagConstraints.BOTH;
		gbc_cavePanel.insets = new Insets(0, 0, 5, 0);
		gbc_cavePanel.gridx = 0;
		gbc_cavePanel.gridy = 3;
		getContentPane().add(cavePanel, gbc_cavePanel);
		GridBagLayout gbl_cavePanel = new GridBagLayout();
		gbl_cavePanel.columnWidths = new int[]{94, 95, 0, 0};
		gbl_cavePanel.rowHeights = new int[]{22, 0, 0, 0, 0, 0};
		gbl_cavePanel.columnWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_cavePanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		cavePanel.setLayout(gbl_cavePanel);
		
		caveShowCaves = new JCheckBox("Caves");
		caveShowCaves.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(getAutoRefreshMap().isSelected())
					parent.optionsUpdate();
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
		caveShowLava.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(getAutoRefreshMap().isSelected())
					parent.optionsUpdate();
			}
		});
		caveShowLava.setSelected(true);
		GridBagConstraints gbc_caveShowLava = new GridBagConstraints();
		gbc_caveShowLava.anchor = GridBagConstraints.WEST;
		gbc_caveShowLava.insets = new Insets(0, 0, 5, 5);
		gbc_caveShowLava.gridx = 1;
		gbc_caveShowLava.gridy = 0;
		cavePanel.add(caveShowLava, gbc_caveShowLava);
		
		caveShowAdamantine = new JCheckBox("Adamantine");
		caveShowAdamantine.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(getAutoRefreshMap().isSelected())
					parent.optionsUpdate();
			}
		});
		caveShowAdamantine.setSelected(true);
		GridBagConstraints gbc_caveShowAdamantine = new GridBagConstraints();
		gbc_caveShowAdamantine.anchor = GridBagConstraints.WEST;
		gbc_caveShowAdamantine.insets = new Insets(0, 0, 5, 0);
		gbc_caveShowAdamantine.gridx = 2;
		gbc_caveShowAdamantine.gridy = 0;
		cavePanel.add(caveShowAdamantine, gbc_caveShowAdamantine);
		
		caveShowIron = new JCheckBox("Iron");
		caveShowIron.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(getAutoRefreshMap().isSelected())
					parent.optionsUpdate();
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
		caveShowCopper.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(getAutoRefreshMap().isSelected())
					parent.optionsUpdate();
			}
		});
		caveShowCopper.setSelected(true);
		GridBagConstraints gbc_caveShowCopper = new GridBagConstraints();
		gbc_caveShowCopper.anchor = GridBagConstraints.WEST;
		gbc_caveShowCopper.insets = new Insets(0, 0, 5, 5);
		gbc_caveShowCopper.gridx = 1;
		gbc_caveShowCopper.gridy = 1;
		cavePanel.add(caveShowCopper, gbc_caveShowCopper);
		
		caveShowGlimmersteel = new JCheckBox("Glimmersteel");
		caveShowGlimmersteel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(getAutoRefreshMap().isSelected())
					parent.optionsUpdate();
			}
		});
		caveShowGlimmersteel.setSelected(true);
		GridBagConstraints gbc_caveShowGlimmersteel = new GridBagConstraints();
		gbc_caveShowGlimmersteel.anchor = GridBagConstraints.WEST;
		gbc_caveShowGlimmersteel.insets = new Insets(0, 0, 5, 0);
		gbc_caveShowGlimmersteel.gridx = 2;
		gbc_caveShowGlimmersteel.gridy = 1;
		cavePanel.add(caveShowGlimmersteel, gbc_caveShowGlimmersteel);
		
		caveShowGold = new JCheckBox("Gold");
		caveShowGold.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(getAutoRefreshMap().isSelected())
					parent.optionsUpdate();
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
		caveShowTin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(getAutoRefreshMap().isSelected())
					parent.optionsUpdate();
			}
		});
		caveShowTin.setSelected(true);
		GridBagConstraints gbc_caveShowTin = new GridBagConstraints();
		gbc_caveShowTin.anchor = GridBagConstraints.WEST;
		gbc_caveShowTin.insets = new Insets(0, 0, 5, 5);
		gbc_caveShowTin.gridx = 1;
		gbc_caveShowTin.gridy = 2;
		cavePanel.add(caveShowTin, gbc_caveShowTin);
		
		caveShowLead = new JCheckBox("Lead");
		caveShowLead.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(getAutoRefreshMap().isSelected())
					parent.optionsUpdate();
			}
		});
		caveShowLead.setSelected(true);
		GridBagConstraints gbc_caveShowLead = new GridBagConstraints();
		gbc_caveShowLead.anchor = GridBagConstraints.WEST;
		gbc_caveShowLead.insets = new Insets(0, 0, 5, 0);
		gbc_caveShowLead.gridx = 2;
		gbc_caveShowLead.gridy = 2;
		cavePanel.add(caveShowLead, gbc_caveShowLead);
		
		caveShowZinc = new JCheckBox("Zinc");
		caveShowZinc.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(getAutoRefreshMap().isSelected())
					parent.optionsUpdate();
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
		caveShowSilver.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(getAutoRefreshMap().isSelected())
					parent.optionsUpdate();
			}
		});
		caveShowSilver.setSelected(true);
		GridBagConstraints gbc_caveShowSilver = new GridBagConstraints();
		gbc_caveShowSilver.anchor = GridBagConstraints.WEST;
		gbc_caveShowSilver.insets = new Insets(0, 0, 5, 5);
		gbc_caveShowSilver.gridx = 1;
		gbc_caveShowSilver.gridy = 3;
		cavePanel.add(caveShowSilver, gbc_caveShowSilver);
		
		caveShowSlate = new JCheckBox("Slate");
		caveShowSlate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(getAutoRefreshMap().isSelected())
					parent.optionsUpdate();
			}
		});
		caveShowSlate.setSelected(true);
		GridBagConstraints gbc_caveShowSlate = new GridBagConstraints();
		gbc_caveShowSlate.anchor = GridBagConstraints.WEST;
		gbc_caveShowSlate.insets = new Insets(0, 0, 5, 0);
		gbc_caveShowSlate.gridx = 2;
		gbc_caveShowSlate.gridy = 3;
		cavePanel.add(caveShowSlate, gbc_caveShowSlate);
		
		caveShowMarble = new JCheckBox("Marble");
		caveShowMarble.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(getAutoRefreshMap().isSelected())
					parent.optionsUpdate();
			}
		});
		caveShowMarble.setSelected(true);
		GridBagConstraints gbc_caveShowMarble = new GridBagConstraints();
		gbc_caveShowMarble.anchor = GridBagConstraints.WEST;
		gbc_caveShowMarble.insets = new Insets(0, 0, 0, 5);
		gbc_caveShowMarble.gridx = 0;
		gbc_caveShowMarble.gridy = 4;
		cavePanel.add(caveShowMarble, gbc_caveShowMarble);
		
		caveShowWater = new JCheckBox("Water");
		caveShowWater.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(getAutoRefreshMap().isSelected())
					parent.optionsUpdate();
			}
		});
		caveShowWater.setSelected(true);
		GridBagConstraints gbc_caveShowWater = new GridBagConstraints();
		gbc_caveShowWater.anchor = GridBagConstraints.WEST;
		gbc_caveShowWater.insets = new Insets(0, 0, 0, 5);
		gbc_caveShowWater.gridx = 1;
		gbc_caveShowWater.gridy = 4;
		cavePanel.add(caveShowWater, gbc_caveShowWater);
		
		JButton btnOk = new JButton("Save Settings");
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setVisible(false);
				parent.optionsUpdate();				
			}
		});
		GridBagConstraints gbc_btnOk = new GridBagConstraints();
		gbc_btnOk.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnOk.insets = new Insets(0, 0, 0, 5);
		gbc_btnOk.gridx = 0;
		gbc_btnOk.gridy = 4;
		getContentPane().add(btnOk, gbc_btnOk);
		
		JButton btnCancel = new JButton("Restore Defaults");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setDefaults();
				if(getAutoRefreshMap().isSelected())
					parent.optionsUpdate();				
			}
		});
		GridBagConstraints gbc_btnCancel = new GridBagConstraints();
		gbc_btnCancel.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnCancel.gridx = 1;
		gbc_btnCancel.gridy = 4;
		getContentPane().add(btnCancel, gbc_btnCancel);
		this.setVisible(false);
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
	public JCheckBox getAutoRefreshMap() {
		return autoRefreshMap;
	}
	public Button getCLineBtn() {
		return cLineBtn;
	}
}
