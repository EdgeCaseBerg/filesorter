package com.peetseater.filesorter;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import com.peetseater.AppLogger;

public class ImagePanel extends JPanel implements ActionListener {
    JLabel imageContainerLabel;
    Path destinationPath;
    Path currentImagePath;

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
    }

    public void setImage(Path imagePath) throws IOException {
        this.currentImagePath = imagePath;
        ImageIcon newImage = new ImageIcon(Files.readAllBytes(imagePath));
        imageContainerLabel.setIcon(scaleIcon(newImage, getWidth(), getHeight()));
    }

    private ImageIcon scaleIcon(ImageIcon imageIcon, int width, int height) {
        Image scaledImage = imageIcon.getImage().getScaledInstance(1080, 720, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImage);
    }

    public void setDestinationPath(Path pathToFile) {
        this.destinationPath = pathToFile;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        AppLogger.info("Moving file from " + currentImagePath + " to " + destinationPath);
        if (currentImagePath != null && destinationPath != null) {
            try {
                Files.move(currentImagePath, destinationPath.resolve(currentImagePath.getFileName()), StandardCopyOption.ATOMIC_MOVE);
            } catch (IOException ioException) {
                AppLogger.warn(ioException);
            }
        }
    }
}
