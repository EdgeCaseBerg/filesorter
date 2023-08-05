package com.peetseater.filesorter.trees;

import java.io.IOException;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import com.peetseater.AppLogger;
import com.peetseater.filesorter.FileToMove;
import com.peetseater.filesorter.ImagePanel;

public class SourcesFileTreePanel extends AbstractFileTreePanel implements TreeSelectionListener{
    ImagePanel imagePanel;

    public SourcesFileTreePanel(String browseText, ImagePanel imagePanel) {
        super(browseText, false);
        this.imagePanel = imagePanel;
        jTree.addTreeSelectionListener(this);        
    }

    @Override
    public void valueChanged(TreeSelectionEvent e) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) this.jTree.getLastSelectedPathComponent();
        if (node == null) {
            return;
        }

        if (node.isLeaf()) {
            FileToMove fileToMove = (FileToMove) node.getUserObject();
            try {
                imagePanel.setImage(fileToMove.getPathToFile());
            } catch (IOException ioException) {
                AppLogger.warn(ioException);
            }
        }
    }
}
