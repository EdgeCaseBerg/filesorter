package com.peetseater.filesorter;

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
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;

import com.peetseater.AppLogger;

class FileTreePanel extends JPanel implements ActionListener, FileVisitor<Path> {

    //TODO we'll probably pass this in via constructor or something
    private final class TreeSelectionListenerImplementation implements TreeSelectionListener {
        public void valueChanged(TreeSelectionEvent e) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) jTree.getLastSelectedPathComponent();
            if (node == null) {
                return;
            }

            if (node.isLeaf()) {
                AppLogger.info("is leaf");
            } else {
                AppLogger.info("not leaf");
            }
        }
    }

    JButton browseButton = new JButton("Browse");
    DefaultMutableTreeNode rootNode;
    JTree jTree;
    String browseText;
    DefaultTreeModel treeModel;
    JFileChooser jFileChooser;
    
    public FileTreePanel(String browseText) {
        this.browseText = browseText;
        this.rootNode = new DefaultMutableTreeNode("Press the button");
        this.treeModel = new DefaultTreeModel(this.rootNode);
        this.jTree = new JTree(treeModel);
        this.jFileChooser = new JFileChooser();
        this.jFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        this.browseButton.setText(browseText);
        JScrollPane scrollPane = new JScrollPane(jTree);

        jTree.setEditable(false);
        jTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        jTree.setShowsRootHandles(true);
        jTree.addTreeSelectionListener(new TreeSelectionListenerImplementation());

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(browseButton);
        add(scrollPane);

        browseButton.addActionListener(this);
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
        if (jFileChooser.showOpenDialog(this.getParent()) == JFileChooser.APPROVE_OPTION) {
            File file = jFileChooser.getSelectedFile();
            this.loadPath(file.toPath());
        }
    }

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
        AppLogger.info("Visiting "+ dir);
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
        DefaultMutableTreeNode newChild = new DefaultMutableTreeNode();
        newChild.setUserObject(new FileToMove(file, file.getFileName().toString()));
        newChild.setAllowsChildren(false);
        currentParent.insert(newChild, currentParent.getChildCount());
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
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
