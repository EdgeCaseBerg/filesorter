package com.peetseater.filesorter;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.util.LinkedList;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import com.peetseater.AppLogger;

public class ImagePanel extends JPanel implements ActionListener {
    JLabel imageContainerLabel;
    JLabel statusLabel;
    transient Path destinationPath;
    transient Path currentImagePath;
    transient List<Listener> listeners = new LinkedList<>();

    public ImagePanel() {
        imageContainerLabel = new JLabel();
        ImageIcon originalImage = new PlaceHolderImage(640, 640);
        imageContainerLabel.setIcon(originalImage);
        imageContainerLabel.setHorizontalAlignment(JLabel.CENTER);
        setPreferredSize(new Dimension(1080, 720));
        
        setLayout(new BorderLayout());
        add(imageContainerLabel, BorderLayout.CENTER);

        JButton moveButton = new JButton("Move");
        add(moveButton, BorderLayout.PAGE_START);
        moveButton.addActionListener(this);

        ImagePanel me = this;
        moveButton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "Move");
        moveButton.getActionMap().put("Move", new AbstractAction("Move") {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                me.actionPerformed(actionEvent);
            }
        });

        statusLabel = new JLabel("...");
        add(statusLabel, BorderLayout.PAGE_END);
    }

    public void addListener(Listener listener) {
        this.listeners.add(listener);
    }

    public void setImage(Path imagePath) throws IOException {
        AppLogger.info("Set source image to " + imagePath);
        this.currentImagePath = imagePath;
        ImageIcon newImage = new ImageIcon(Files.readAllBytes(imagePath));
        int width = newImage.getIconWidth();
        int height = newImage.getIconHeight();
        int largest = Math.max(width, height);
        if (largest == width) {
            height = -1;
        } else {
            width = -1;
        }
        imageContainerLabel.setIcon(scaleIcon(newImage, Math.min(width, getWidth()), Math.min(height, getHeight())));
        statusLabel.setText(imagePath.toString());
    }

    private ImageIcon scaleIcon(ImageIcon imageIcon, int width, int height) {
        Image scaledImage = imageIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImage);
    }

    public void setDestinationPath(Path pathToFile) {
        AppLogger.info("Set destination to " + pathToFile);
        this.destinationPath = pathToFile;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        AppLogger.info("Moving file from " + currentImagePath + " to " + destinationPath);
        if (currentImagePath != null && destinationPath != null) {
            try {
                Path desiredPath = destinationPath.resolve(currentImagePath.getFileName());
                int next = 1;
                while(Files.exists(desiredPath)) {
                    destinationPath.resolve(next + "-" + currentImagePath.getFileName());
                    next++;
                }
                Files.move(currentImagePath, desiredPath, StandardCopyOption.ATOMIC_MOVE);
                statusLabel.setText("Moved file to " + desiredPath.toString());
                for (Listener listener : listeners) {
                    listener.doListenerAction();
                }
            } catch (IOException ioException) {
                AppLogger.warn(ioException);
            }
        }
    }
}
