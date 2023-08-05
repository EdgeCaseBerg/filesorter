package com.peetseater.filesorter.trees;

import java.io.IOException;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import com.peetseater.AppLogger;
import com.peetseater.filesorter.FileToMove;
import com.peetseater.filesorter.ImagePanel;
import com.peetseater.filesorter.Listener;

public class SourcesFileTreePanel extends AbstractFileTreePanel implements TreeSelectionListener, Listener {
    ImagePanel imagePanel;
    DefaultMutableTreeNode currentNode;

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
                this.currentNode = node;
            } catch (IOException ioException) {
                AppLogger.warn(ioException);
            }
        }
    }

    public DefaultMutableTreeNode getCurrentNode() {
        return this.currentNode;
    }

    @Override
    public void doListenerAction() {
        // We listen from the ImagePanel when a user has decided to Move an image, so we can remove it from the tree display
        if (this.currentNode == null) {
            return;
        }

        DefaultMutableTreeNode nextSelection = this.currentNode.getNextSibling();
        treeModel.removeNodeFromParent(this.currentNode);

        // If there is a sibling, move our selection over to it.
        if (nextSelection != null) {
           jTree.setSelectionPath(new TreePath(nextSelection.getPath()));
        }
    }
}
