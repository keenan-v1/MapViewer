package com.wurmly.mapviewer.ui;

import com.wurmly.mapviewer.localization.Localization;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;

class MapHelpWindow extends JFrame
{

    private static final long serialVersionUID = 4123586154123956589L;

    MapHelpWindow(MapViewerFrame mapFrame)
    {
        super(Localization.getInstance().getMessageFor("title-help-window"));
        this.setDefaultCloseOperation(HIDE_ON_CLOSE);
        this.setAlwaysOnTop(true);
        this.setResizable(false);
        getRootPane().setWindowDecorationStyle(JRootPane.NONE);
        setUndecorated(true);
        JPanel contentPanel = new JPanel();
        contentPanel.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[]{122, 145, 193, 0};
        gridBagLayout.rowHeights = new int[]{14, 0, 0, 0};
        gridBagLayout.columnWeights = new double[]{1.0, 0.0, 1.0, Double.MIN_VALUE};
        gridBagLayout.rowWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
        getContentPane().add(contentPanel);
        contentPanel.setLayout(gridBagLayout);

        JLabel lblHelpTitle = new JLabel("Map Viewer v" + mapFrame.getVersion());
        lblHelpTitle.setFont(new Font("Tahoma", Font.BOLD, 28));
        GridBagConstraints gbcLblHelpTitle = new GridBagConstraints();
        gbcLblHelpTitle.insets = new Insets(0, 0, 5, 0);
        gbcLblHelpTitle.gridwidth = 3;
        gbcLblHelpTitle.gridx = 0;
        gbcLblHelpTitle.gridy = 0;
        contentPanel.add(lblHelpTitle, gbcLblHelpTitle);

        JTabbedPane helpTabbedPane = new JTabbedPane(JTabbedPane.TOP);
        GridBagConstraints gbcHelpTabbedPane = new GridBagConstraints();
        gbcHelpTabbedPane.insets = new Insets(0, 0, 5, 0);
        gbcHelpTabbedPane.gridwidth = 3;
        gbcHelpTabbedPane.fill = GridBagConstraints.BOTH;
        gbcHelpTabbedPane.gridx = 0;
        gbcHelpTabbedPane.gridy = 1;
        contentPanel.add(helpTabbedPane, gbcHelpTabbedPane);

        JPanel hotKeyHelpPanel = new JPanel();
        helpTabbedPane.addTab(Localization.getInstance().getMessageFor("tab-hot-keys"), null, hotKeyHelpPanel, null);

        JLabel lblHotKeyHelp = new JLabel(Localization.getInstance().getHtmlFor("html/hot_key_help.html"));
        hotKeyHelpPanel.add(lblHotKeyHelp);
        helpTabbedPane.setBackgroundAt(0, new Color(128, 128, 128));

        JPanel aboutPanel = new JPanel();
        helpTabbedPane.addTab(Localization.getInstance().getMessageFor("tab-about"), null, aboutPanel, null);

        JLabel lblAbout = new JLabel(Localization.getInstance().getHtmlFor("html/about.html"));
        aboutPanel.add(lblAbout);

        JPanel licensePanel = new JPanel();
        helpTabbedPane.addTab(Localization.getInstance().getMessageFor("tab-license"), null, licensePanel, null);
        GridBagLayout gblLicensePanel = new GridBagLayout();
        gblLicensePanel.columnWidths = new int[]{167, 0};
        gblLicensePanel.rowHeights = new int[]{14, 0};
        gblLicensePanel.columnWeights = new double[]{1.0, Double.MIN_VALUE};
        gblLicensePanel.rowWeights = new double[]{1.0, Double.MIN_VALUE};
        licensePanel.setLayout(gblLicensePanel);

        JLabel lblLicense = new JLabel(Localization.getInstance().getHtmlFor("html/license.html"));
        GridBagConstraints gbcLblLicense = new GridBagConstraints();
        gbcLblLicense.fill = GridBagConstraints.BOTH;
        gbcLblLicense.gridx = 0;
        gbcLblLicense.gridy = 0;
        licensePanel.add(lblLicense, gbcLblLicense);

        JButton btnClose = new JButton(Localization.getInstance().getMessageFor("button-close"));
        btnClose.addActionListener(e -> {
            setVisible(false);
            setFocusable(false);
        });
        GridBagConstraints gbcBtnClose = new GridBagConstraints();
        gbcBtnClose.gridwidth = 3;
        gbcBtnClose.insets = new Insets(0, 0, 0, 5);
        gbcBtnClose.gridx = 0;
        gbcBtnClose.gridy = 2;
        contentPanel.add(btnClose, gbcBtnClose);

    }

}
