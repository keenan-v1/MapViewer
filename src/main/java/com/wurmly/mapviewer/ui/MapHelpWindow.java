package com.wurmly.mapviewer.ui;

import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.border.BevelBorder;
import javax.swing.JLabel;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Font;
import javax.swing.JTabbedPane;
import java.awt.Insets;
import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JFrame;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MapHelpWindow extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4123586154123956589L;
	private JPanel contentPanel;
	private MapViewerFrame mapFrame;

	/**
	 * Create the panel.
	 */
	public MapHelpWindow(MapViewerFrame mapFrame) {
		super("Map Viewer - Help");
		this.mapFrame = mapFrame;
		this.setDefaultCloseOperation(HIDE_ON_CLOSE);
		this.setAlwaysOnTop(true);
		this.setResizable(false);
		getRootPane().setWindowDecorationStyle(JRootPane.NONE);
		setUndecorated(true);		
		contentPanel = new JPanel();
		contentPanel.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{122, 145, 193, 0};
		gridBagLayout.rowHeights = new int[]{14, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 0.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		getContentPane().add(contentPanel);
		contentPanel.setLayout(gridBagLayout);
		
		JLabel lblHelpTitle = new JLabel("Map Viewer v" + this.mapFrame.getVersion());
		lblHelpTitle.setFont(new Font("Tahoma", Font.BOLD, 28));
		GridBagConstraints gbc_lblHelpTitle = new GridBagConstraints();
		gbc_lblHelpTitle.insets = new Insets(0, 0, 5, 0);
		gbc_lblHelpTitle.gridwidth = 3;
		gbc_lblHelpTitle.gridx = 0;
		gbc_lblHelpTitle.gridy = 0;
		contentPanel.add(lblHelpTitle, gbc_lblHelpTitle);
		
		JTabbedPane helpTabbedPane = new JTabbedPane(JTabbedPane.TOP);
		GridBagConstraints gbc_helpTabbedPane = new GridBagConstraints();
		gbc_helpTabbedPane.insets = new Insets(0, 0, 5, 0);
		gbc_helpTabbedPane.gridwidth = 3;
		gbc_helpTabbedPane.fill = GridBagConstraints.BOTH;
		gbc_helpTabbedPane.gridx = 0;
		gbc_helpTabbedPane.gridy = 1;
		contentPanel.add(helpTabbedPane, gbc_helpTabbedPane);
		
		JPanel hotkeyHelpPanel = new JPanel();
		helpTabbedPane.addTab("Hot Keys", null, hotkeyHelpPanel, null);
		
		JLabel lblNewLabel_1 = new JLabel("<html><table>\r\n<tr><td><b>O</b></td><td>Opens Map</td></tr>\r\n<tr><td><b>M</b></td><td>Save Map Image (Map Dump)</td></tr>\r\n<tr><td><b>C</b></td><td>Save Zoomed Area (Screen Capture)</td></tr>\r\n<tr><td><b>+/-</b></td><td>Zoom In/Out (Also NumPad +/-)</td></tr>\r\n<tr><td><b>W/A/S/D</b></td><td>Moves Zoomed Map (Also NumPad 8/4/6/2)</td></tr>\r\n<tr><td><b>Shift</b></td><td>Hold to Zoom or Move faster</td></tr>\r\n<tr><td><b>1</b></td><td>Terrain View (default)</td></tr>\r\n<tr><td><b>2</b></td><td>Topographical View</td></tr>\r\n<tr><td><b>3</b></td><td>Cave View</td></tr>\r\n<tr><td><b>4</b></td><td>Isometric View (Wurm-style 3D Maps)</td></tr>\r\n</table>\r\n<p><center>Hold <b>ALT</b> in the main window to see other shortcuts.</center></p></html>");
		hotkeyHelpPanel.add(lblNewLabel_1);
		helpTabbedPane.setBackgroundAt(0, new Color(128, 128, 128));
		
		JPanel aboutPanel = new JPanel();
		helpTabbedPane.addTab("About", null, aboutPanel, null);
		
		JLabel lblLblabout = new JLabel("<html>\r\n<center><h1>Map Viewer for Wurm Unlimited</h1></center>\r\n<p><b>Written by:</b> Jonathan Walker<br />\r\n" +
				"<b>Email:</b> <a href=\"mailto:xorith@gmail.com\">xorith@gmail.com</a><br/>\r\n" +
				"<b>Github:</b> <a href=\"https://github.com/xorith/MapViewer\">https://github.com/xorith/MapViewer</a></p>\r\n" +
				"<p>Wurm Online and Wurm Unlimited is owned by Code Club AB.<br/>\r\n<a href=\"http://www.wurmunlimited.com\">Wurm Unlimited</a><br/>\r\n" +
				"<a href=\"http://www.wurmonline.com\">Wurm Online</a></p>\r\n\r\n<p>Build: "+ BuildProperties.getGitSha1().substring(0, 7)+" @ "+
				BuildProperties.getBuildTimeString() +"</p>\r\n</html>");
		aboutPanel.add(lblLblabout);
		
		JPanel licensePanel = new JPanel();
		helpTabbedPane.addTab("License", null, licensePanel, null);
		GridBagLayout gbl_licensePanel = new GridBagLayout();
		gbl_licensePanel.columnWidths = new int[]{167, 0};
		gbl_licensePanel.rowHeights = new int[]{14, 0};
		gbl_licensePanel.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_licensePanel.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		licensePanel.setLayout(gbl_licensePanel);
		
		JLabel lblLicense = new JLabel("<html>\r\n<p>The MIT License (MIT)</p>\r\n<br/>\r\n<p>Copyright (c) 2015 Jonathan Walker</p>\r\n<br/>\r\n<p>Permission is hereby granted, free of charge, to any person obtaining a copy\r\nof this software and associated documentation files (the \"Software\"), to deal\r\nin the Software without restriction, including without limitation the rights\r\nto use, copy, modify, merge, publish, distribute, sublicense, and/or sell\r\ncopies of the Software, and to permit persons to whom the Software is\r\nfurnished to do so, subject to the following conditions:</p>\r\n<br/>\r\n<p>The above copyright notice and this permission notice shall be included in all\r\ncopies or substantial portions of the Software.</p>\r\n<br/>\r\n<p>THE SOFTWARE IS PROVIDED \"AS IS\", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR\r\nIMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,\r\nFITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE\r\nAUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER\r\nLIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,\r\nOUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE\r\nSOFTWARE.</p>\r\n</html>");
		GridBagConstraints gbc_lblLicense = new GridBagConstraints();
		gbc_lblLicense.fill = GridBagConstraints.BOTH;
		gbc_lblLicense.gridx = 0;
		gbc_lblLicense.gridy = 0;
		licensePanel.add(lblLicense, gbc_lblLicense);
		
		JButton btnClose = new JButton("Close");
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				setFocusable(false);
			}
		});
		GridBagConstraints gbc_btnClose = new GridBagConstraints();
		gbc_btnClose.gridwidth = 3;
		gbc_btnClose.insets = new Insets(0, 0, 0, 5);
		gbc_btnClose.gridx = 0;
		gbc_btnClose.gridy = 2;
		contentPanel.add(btnClose, gbc_btnClose);

	}

}
