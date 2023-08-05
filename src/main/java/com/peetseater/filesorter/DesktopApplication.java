package com.peetseater.filesorter;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import com.peetseater.AppLogger;

public class DesktopApplication extends JFrame {

    FileTreePanel sourcesPanel;// = new FileTreePanel("Load sources");
    FileTreePanel destinationPanel;// = new FileTreePanel("Load destinations");

    public DesktopApplication() {
        super("File Sorter");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        sourcesPanel = new FileTreePanel("Load sources");
        destinationPanel = new FileTreePanel("Load destinations");

        JSplitPane jSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        JPanel trees = new JPanel(new GridLayout(2, 1));
        trees.add(sourcesPanel);
        trees.add(destinationPanel);
        jSplitPane.setLeftComponent(trees);
        jSplitPane.setRightComponent(new ImagePanel());

        add(jSplitPane);

        setPreferredSize(new Dimension(1280, 720));

        this.pack();
        // true = Forward add/setLayout calls to the rootPane
        this.setVisible(true);
    }
}