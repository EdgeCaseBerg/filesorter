package com.peetseater.filesorter.trees;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;

import com.peetseater.AppLogger;
import com.peetseater.filesorter.FileToMove;

public abstract class AbstractFileTreePanel extends JPanel implements ActionListener, FileVisitor<Path> {

    JButton browseButton = new JButton("Browse");
    JButton refreshButton = new JButton("Refresh");
    transient Path loadedPath = null;
    DefaultMutableTreeNode rootNode;
    JTree jTree;
    String browseText;
    DefaultTreeModel treeModel;
    JFileChooser jFileChooser;
    boolean onlyIncludeDirectories;
    
    protected AbstractFileTreePanel(String browseText, boolean onlyIncludeDirectories) {
        this.browseText = browseText;
        this.rootNode = new DefaultMutableTreeNode("Press the button");
        this.treeModel = new DefaultTreeModel(this.rootNode);
        this.jTree = new JTree(treeModel);
        this.jFileChooser = new JFileChooser();
        this.jFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        this.onlyIncludeDirectories = onlyIncludeDirectories;

        this.browseButton.setText(browseText);
        JScrollPane scrollPane = new JScrollPane(jTree);

        jTree.setEditable(false);
        jTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        jTree.setShowsRootHandles(true);

        
        

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        JPanel buttonPanel = new JPanel();
        BoxLayout sideBySideButtons = new BoxLayout(buttonPanel, BoxLayout.X_AXIS);
        
        buttonPanel.add(browseButton);
        buttonPanel.add(refreshButton);
        add(buttonPanel);
        add(scrollPane);

        browseButton.addActionListener(this);
        refreshButton.addActionListener(this);
    }

    DefaultMutableTreeNode currentParent = rootNode;
    public void loadPath(Path rootPath) {
        try {
            AppLogger.info("Walking file tree at " + rootPath);
            this.rootNode.removeAllChildren();
            this.currentParent = null;
            Files.walkFileTree(rootPath, this);
            AppLogger.info("Completed walking file tree at " + rootPath);
            this.rootNode = currentParent;
            this.treeModel.setRoot(this.rootNode);
            jTree.revalidate();
            jTree.setModel(treeModel);
        } catch (IOException e) {
            AppLogger.warn(e);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        AppLogger.info("FileTreePanel with text \"" + browseText + "\" button clicked " + this.rootNode);
        if (e.getActionCommand().equals("Refresh")) {
            this.loadPath(this.loadedPath);
        } else if (e.getActionCommand().equals(this.browseText)) {
            if (jFileChooser.showOpenDialog(this.getParent()) == JFileChooser.APPROVE_OPTION) {
                File file = jFileChooser.getSelectedFile();
                this.loadPath(file.toPath());
                this.loadedPath = file.toPath();
            }
        }
    }

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
        AppLogger.trace("Visiting "+ dir);
        DefaultMutableTreeNode newChild = new DefaultMutableTreeNode();
        newChild.setUserObject(new FileToMove(dir, dir.getFileName().toString()));
        newChild.setAllowsChildren(true);
        if (currentParent != null) {
            this.currentParent.insert(newChild, currentParent.getChildCount());
        }
        this.currentParent = newChild;
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        if (onlyIncludeDirectories) {
            return FileVisitResult.CONTINUE;
        }
        DefaultMutableTreeNode newChild = new DefaultMutableTreeNode();
        newChild.setUserObject(new FileToMove(file, file.getFileName().toString()));
        newChild.setAllowsChildren(false);
        currentParent.insert(newChild, currentParent.getChildCount());
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
        AppLogger.warn(exc);
        throw exc;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
        if (this.currentParent.getParent() != null) {
            this.currentParent = (DefaultMutableTreeNode) this.currentParent.getParent();
        }
       return FileVisitResult.CONTINUE;
    }
}
