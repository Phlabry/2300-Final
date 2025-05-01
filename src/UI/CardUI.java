package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/** Draws one card; if hidden==true, shows common back. */
public class CardUI extends JPanel {

    private final BufferedImage face;
    private final boolean hidden;
    private static BufferedImage back;   // shared

    public CardUI(BufferedImage face, boolean hidden) {
        this.face   = face;
        this.hidden = hidden;
        setOpaque(false);
    }

    /** Called once from Constants after loading sprites. */
    public static void setBackImage(BufferedImage b) { back = b; }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        BufferedImage img = hidden ? back : face;
        if (img != null)
            g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
    }
}
