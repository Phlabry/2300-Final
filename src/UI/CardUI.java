package UI;
import Model.*;
import Utilities.Constants;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

public class CardUI extends JPanel{
	private BufferedImage image;
	
	    ImagePanel CardUI(Card card, int x, int y) {
	    	return new ImagePanel(Constants.CARD_IMAGE_PATHS.get(card.toString()), x, y, 25, 40);
	    }
	    
}
