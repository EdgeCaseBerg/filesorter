package com.peetseater.filesorter;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import com.peetseater.filesorter.trees.DestinationFileTreePanel;
import com.peetseater.filesorter.trees.SourcesFileTreePanel;

public class DesktopApplication extends JFrame {

    SourcesFileTreePanel sourcesPanel;
    DestinationFileTreePanel destinationPanel;
    ImagePanel imagePanel;

    public DesktopApplication() {
        super("File Sorter");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        imagePanel = new ImagePanel();
        sourcesPanel = new SourcesFileTreePanel("Load sources", imagePanel);
        destinationPanel = new DestinationFileTreePanel("Load destinations", imagePanel);
        imagePanel.addListener(sourcesPanel);

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