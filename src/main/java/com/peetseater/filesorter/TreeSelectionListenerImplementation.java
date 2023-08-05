package com.peetseater.filesorter;

import java.io.IOException;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import com.peetseater.AppLogger;

final class TreeSelectedChangeImageListener implements TreeSelectionListener {
    private FileTreePanel fileTreePanel;
    private ImagePanel imagePanel;

    TreeSelectedChangeImageListener(FileTreePanel fileTreePanel, ImagePanel imagePanel) {
        this.fileTreePanel = fileTreePanel;
        this.imagePanel = imagePanel;
    }

    public void valueChanged(TreeSelectionEvent e) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) this.fileTreePanel.jTree.getLastSelectedPathComponent();
        if (node == null) {
            return;
        }

        if (node.isLeaf()) {
            FileToMove fileToMove = (FileToMove) node.getUserObject();
            try {
                imagePanel.setImage(fileToMove.pathToFile);
            } catch (IOException ioException) {
                AppLogger.warn(ioException);
            }
        }
    }

    public void setFileTreePanel(FileTreePanel sourcesPanel) {
        this.fileTreePanel = sourcesPanel;
    }
}