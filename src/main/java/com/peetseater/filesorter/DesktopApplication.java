package com.peetseater.filesorter;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import com.peetseater.AppLogger;

public class DesktopApplication extends JFrame {

    FileTreePanel sourcesPanel;// = new FileTreePanel("Load sources");
    FileTreePanel destinationPanel;// = new FileTreePanel("Load destinations");
    ImagePanel imagePanel;

    public DesktopApplication() {
        super("File Sorter");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        imagePanel = new ImagePanel();

        // TODO maybe we subclass the panel instead of making a separate listener...
        TreeSelectedChangeImageListener sourcesListener = new TreeSelectedChangeImageListener(destinationPanel, imagePanel);
        sourcesPanel = new FileTreePanel("Load sources", sourcesListener);
        sourcesListener.setFileTreePanel(sourcesPanel);

        TreeSelectionListener destinationListener = new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
            }
            
        };
        destinationPanel = new FileTreePanel("Load destinations", destinationListener);

        JSplitPane jSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        JPanel trees = new JPanel(new GridLayout(2, 1));
        trees.add(sourcesPanel);
        trees.add(destinationPanel);
        jSplitPane.setLeftComponent(trees);
        jSplitPane.setRightComponent(imagePanel);

        add(jSplitPane);

        setPreferredSize(new Dimension(1280, 720));

        this.pack();
        // true = Forward add/setLayout calls to the rootPane
        this.setVisible(true);
    }
}