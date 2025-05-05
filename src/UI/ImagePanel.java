package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;

/** Generic sprite that keeps its position & size as a % of the frame. */
public class ImagePanel extends JPanel {

    private BufferedImage img;
    private double xR, yR, wR, hR;   // ratios relative to 1920×1080
    private int x, y, w, h;

    public ImagePanel(String path,int px,int py,int pw,int ph){
        Dimension scr = Toolkit.getDefaultToolkit().getScreenSize();
        double fw = scr.getWidth(), fh = scr.getHeight();
        xR=px/fw; yR=py/fh; wR=pw/fw; hR=ph/fh;
        x=px; y=py; w=pw; h=ph;
        setOpaque(false);
        try{ img=ImageIO.read(getClass().getResourceAsStream(path)); }
        catch(IOException|NullPointerException e){System.err.println("No "+path);}
    }

    public void setPosition(int px,int py){
        x=px; y=py;
        Dimension scr=Toolkit.getDefaultToolkit().getScreenSize();
        xR=px/scr.getWidth(); yR=py/scr.getHeight();
        repaint();
    }

    public void updateScale(int fw,int fh){
        x=(int)(xR*fw); y=(int)(yR*fh);
        w=(int)(wR*fw); h=(int)(hR*fh);
        repaint();
    }

    @Override protected void paintComponent(Graphics g){
        super.paintComponent(g);
        if(img!=null) g.drawImage(img,x,y,w,h,this);
    }
}
