package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;

public class ImagePanel extends JPanel {
    private BufferedImage image;
    private double xRatio; //x position as percentage of frame width
    private double yRatio; //y position as percentage of frame height
    private double widthRatio; //width as percentage of frame width
    private double heightRatio; //height as percentage of frame height
    
    private int currentX;
    private int currentY;
    private int currentWidth;
    private int currentHeight;

    public ImagePanel(String path, int x, int y, int width, int height) {
        //Calculate ratios based on the GameUI dimensions
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double frameWidth = screenSize.getWidth();
        double frameHeight = screenSize.getHeight();
        
        this.xRatio = x / frameWidth;
        this.yRatio = y / frameHeight;
        this.widthRatio = width / frameWidth;
        this.heightRatio = height / frameHeight;
        
        //Set initial values
        this.currentX = x;
        this.currentY = y;
        this.currentWidth = width;
        this.currentHeight = height;
        
        this.setOpaque(false); //make background transparent

        //Try to load the image
        try {
            image = ImageIO.read(getClass().getResourceAsStream(path));
            if (image == null) {
                System.out.println("Image could not be loaded from: " + path);
            }
        } catch (IOException e) {
            System.out.println("Failed to load " + path);
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Unexpected error loading " + path);
            e.printStackTrace();
        }
    }

    
    public void setPosition(int x, int y) {
        //Update current position
        this.currentX = x;
        this.currentY = y;
        
        //Update ratios
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double frameWidth = screenSize.getWidth();
        double frameHeight = screenSize.getHeight();
        
        this.xRatio = x / frameWidth;
        this.yRatio = y / frameHeight;
        
        repaint();
    }
    

    public void updateScale(int frameWidth, int frameHeight) {
        //Calculate new position and size based on ratios
        this.currentX = (int)(xRatio * frameWidth);
        this.currentY = (int)(yRatio * frameHeight);
        this.currentWidth = (int)(widthRatio * frameWidth);
        this.currentHeight = (int)(heightRatio * frameHeight);
        
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // Always call the superclass method first
        if (image != null) {
            // Draw image at calculated position and size
            g.drawImage(image, currentX, currentY, currentWidth, currentHeight, this);
        }
    }
    
    //Getter for current X
    public int getCurrentX() {
        return currentX;
    }
    
    //Getter for current Y
    public int getCurrentY() {
        return currentY;
    }
    
    //Getter for current Width
    public int getCurrentWidth() {
        return currentWidth;
    }
    
    //Getter for current Height
    public int getCurrentHeight() {
        return currentHeight;
    }
}