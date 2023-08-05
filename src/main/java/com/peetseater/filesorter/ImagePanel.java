package com.peetseater.filesorter;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class ImagePanel extends JPanel {
    JLabel imageContainerLabel;

    public ImagePanel() {
        imageContainerLabel = new JLabel();
        ImageIcon originalImage = new PlaceHolderImage(64, 64);
        imageContainerLabel.setIcon(originalImage);
        imageContainerLabel.setHorizontalAlignment(JLabel.CENTER);
        setPreferredSize(new Dimension(1080, 720));
        
        setLayout(new BorderLayout());
        add(imageContainerLabel, BorderLayout.CENTER);

        JButton moveButton = new JButton("Move");
        add(moveButton, BorderLayout.PAGE_START);
    }

    void setImage(ImageIcon newImage) {
        imageContainerLabel.setIcon(scaleIcon(newImage, getWidth(), getHeight()));
    }

    private ImageIcon scaleIcon(ImageIcon imageIcon, int width, int height) {
        Image scaledImage = imageIcon.getImage().getScaledInstance(200, 50, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImage);
    }
}
