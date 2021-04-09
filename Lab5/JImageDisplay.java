package Lab5;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class JImageDisplay extends javax.swing.JComponent {
    private BufferedImage displayImage;
    public JImageDisplay(int width, int height){
        displayImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Dimension dimension = new Dimension(width, height);
        setPreferredSize(dimension);
    }
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawImage (displayImage, 0, 0, displayImage.getWidth(), displayImage.getHeight(), null);
    }
    public void clearImage() {
        int[] blankArray = new int[getWidth()*getHeight()];
        displayImage.setRGB(0, 0, getWidth(), getHeight(),blankArray, 0, 1);
    }
    public BufferedImage getImage() {
        return displayImage;
    }
    public void drawPixel(int x, int y, int rgbColor)
    {
        displayImage.setRGB(x, y, rgbColor);
    }
}
