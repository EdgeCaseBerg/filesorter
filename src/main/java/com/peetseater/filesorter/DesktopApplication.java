package com.peetseater.filesorter;

import javax.swing.JFrame;
import com.peetseater.AppLogger;

public class DesktopApplication extends JFrame {

    FileTreePanel sourcesPanel = new FileTreePanel("Load sources");
    FileTreePanel destinationPanel = new FileTreePanel("Load destinations");

    public DesktopApplication() {
        super("File Sorter");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        add(sourcesPanel);
        add(destinationPanel);

        this.pack();
        // true = Forward add/setLayout calls to the rootPane
        this.setVisible(true);
    }
}