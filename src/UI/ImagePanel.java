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

	// constructor
    public ImagePanel(String path, int x, int y, int width, int height) {
    	this.x = x;
    	this.y = y;
    	this.width = width;
    	this.height = height;
    	this.setOpaque(false);  // make background transparent

    	// try catch for getting the image
        try {
            image = ImageIO.read(getClass().getResourceAsStream(path)); 
        } catch (IOException e) {
        	System.out.println("Failed to load " + path);
            e.printStackTrace();
        }
    }

    public void setPosition(int x, int y) {
    	this.x = x;
    	this.y = y;
  
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
