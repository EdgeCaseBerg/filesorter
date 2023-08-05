package com.peetseater.filesorter;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.ImageIcon;

class PlaceHolderImage extends ImageIcon {
    protected int iconWidth = 32;
    protected int iconHeight = 32;
    protected transient BasicStroke stroke = new BasicStroke(4);

    public PlaceHolderImage(int width, int height) {
        this.iconWidth = width;
        this.iconHeight = height;
    }

    @Override
    public synchronized void paintIcon(Component c, Graphics g, int x, int y) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setColor(Color.WHITE);
        g2d.fillRect(x +1 ,y + 1,iconWidth -2 ,iconHeight -2);

        g2d.setColor(Color.BLACK);
        g2d.drawRect(x +1 ,y + 1,iconWidth -2 ,iconHeight -2);

        g2d.setColor(Color.RED);

        g2d.setStroke(stroke);
        g2d.drawLine(x +10, y + 10, x + iconWidth -10, y + iconHeight -10);
        g2d.drawLine(x +10, y + iconHeight -10, x + iconWidth -10, y + 10);

        g2d.dispose();
    }

    @Override
    public int getIconWidth() {
        return iconWidth;
    }

    @Override
    public int getIconHeight() {
        return iconHeight;
    }

}