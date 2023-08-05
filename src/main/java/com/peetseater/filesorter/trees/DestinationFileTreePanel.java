package com.peetseater.filesorter.trees;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import com.peetseater.filesorter.FileToMove;
import com.peetseater.filesorter.ImagePanel;

public class DestinationFileTreePanel extends AbstractFileTreePanel implements TreeSelectionListener{
    ImagePanel imagePanel;

    public DestinationFileTreePanel(String browseText, ImagePanel imagePanel) {
        super(browseText, true);
        this.imagePanel = imagePanel;
        jTree.addTreeSelectionListener(this);        
    }

    @Override
    public void valueChanged(TreeSelectionEvent e) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) this.jTree.getLastSelectedPathComponent();
        if (node == null) {
            return;
        }

        FileToMove fileToMove = (FileToMove) node.getUserObject();
        imagePanel.setDestinationPath(fileToMove.getPathToFile());
    }
}
