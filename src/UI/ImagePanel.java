package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;

public class ImagePanel extends JPanel {
    private BufferedImage image;
	private int x;
	private int y;
	private int width; 
	private int height;

    public ImagePanel(String url, int x, int y, int width, int height) {
    	this.x = x;
    	this.y = y;
    	this.width = width;
    	this.height = height;
    	this.setOpaque(false);

        try {
            image = ImageIO.read(getClass().getResourceAsStream(url));
        } catch (IOException e) {
        	System.out.println("Failed to load " + url);
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // Always call the superclass method first
        if (image != null) {
            // Draw image scaled to panel size
            g.drawImage(image, x, y, width, height, this);
        }
    }
}
