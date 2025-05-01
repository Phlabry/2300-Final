package Utilities;

import java.util.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;

/**
 * 52-card sprite map  ➜  PNG path        +
 * an in-memory cache ➜ CARD_IMAGES[key]  +
 * the common back     ➜ CARD_IMAGES["BACK"]
 */
public class Constants {

    /** “ACE of SPADES”  →  "assets/Cards/ace_of_spades.png" */
    public static final Map<String,String> CARD_IMAGE_PATHS = new HashMap<>();

    static {
        // 2s
        CARD_IMAGE_PATHS.put("TWO of CLUBS",    "assets/Cards/2_of_clubs.png");
        CARD_IMAGE_PATHS.put("TWO of DIAMONDS", "assets/Cards/2_of_diamonds.png");
        CARD_IMAGE_PATHS.put("TWO of HEARTS",   "assets/Cards/2_of_hearts.png");
        CARD_IMAGE_PATHS.put("TWO of SPADES",   "assets/Cards/2_of_spades.png");
        // 3s
        CARD_IMAGE_PATHS.put("THREE of CLUBS",    "assets/Cards/3_of_clubs.png");
        CARD_IMAGE_PATHS.put("THREE of DIAMONDS", "assets/Cards/3_of_diamonds.png");
        CARD_IMAGE_PATHS.put("THREE of HEARTS",   "assets/Cards/3_of_hearts.png");
        CARD_IMAGE_PATHS.put("THREE of SPADES",   "assets/Cards/3_of_spades.png");
        // 4s
        CARD_IMAGE_PATHS.put("FOUR of CLUBS",    "assets/Cards/4_of_clubs.png");
        CARD_IMAGE_PATHS.put("FOUR of DIAMONDS", "assets/Cards/4_of_diamonds.png");
        CARD_IMAGE_PATHS.put("FOUR of HEARTS",   "assets/Cards/4_of_hearts.png");
        CARD_IMAGE_PATHS.put("FOUR of SPADES",   "assets/Cards/4_of_spades.png");
        // 5s
        CARD_IMAGE_PATHS.put("FIVE of CLUBS",    "assets/Cards/5_of_clubs.png");
        CARD_IMAGE_PATHS.put("FIVE of DIAMONDS", "assets/Cards/5_of_diamonds.png");
        CARD_IMAGE_PATHS.put("FIVE of HEARTS",   "assets/Cards/5_of_hearts.png");
        CARD_IMAGE_PATHS.put("FIVE of SPADES",   "assets/Cards/5_of_spades.png");
        // 6s
        CARD_IMAGE_PATHS.put("SIX of CLUBS",    "assets/Cards/6_of_clubs.png");
        CARD_IMAGE_PATHS.put("SIX of DIAMONDS", "assets/Cards/6_of_diamonds.png");
        CARD_IMAGE_PATHS.put("SIX of HEARTS",   "assets/Cards/6_of_hearts.png");
        CARD_IMAGE_PATHS.put("SIX of SPADES",   "assets/Cards/6_of_spades.png");
        // 7s
        CARD_IMAGE_PATHS.put("SEVEN of CLUBS",    "assets/Cards/7_of_clubs.png");
        CARD_IMAGE_PATHS.put("SEVEN of DIAMONDS", "assets/Cards/7_of_diamonds.png");
        CARD_IMAGE_PATHS.put("SEVEN of HEARTS",   "assets/Cards/7_of_hearts.png");
        CARD_IMAGE_PATHS.put("SEVEN of SPADES",   "assets/Cards/7_of_spades.png");
        // 8s
        CARD_IMAGE_PATHS.put("EIGHT of CLUBS",    "assets/Cards/8_of_clubs.png");
        CARD_IMAGE_PATHS.put("EIGHT of DIAMONDS", "assets/Cards/8_of_diamonds.png");
        CARD_IMAGE_PATHS.put("EIGHT of HEARTS",   "assets/Cards/8_of_hearts.png");
        CARD_IMAGE_PATHS.put("EIGHT of SPADES",   "assets/Cards/8_of_spades.png");
        // 9s
        CARD_IMAGE_PATHS.put("NINE of CLUBS",    "assets/Cards/9_of_clubs.png");
        CARD_IMAGE_PATHS.put("NINE of DIAMONDS", "assets/Cards/9_of_diamonds.png");
        CARD_IMAGE_PATHS.put("NINE of HEARTS",   "assets/Cards/9_of_hearts.png");
        CARD_IMAGE_PATHS.put("NINE of SPADES",   "assets/Cards/9_of_spades.png");
        // 10s
        CARD_IMAGE_PATHS.put("TEN of CLUBS",    "assets/Cards/10_of_clubs.png");
        CARD_IMAGE_PATHS.put("TEN of DIAMONDS", "assets/Cards/10_of_diamonds.png");
        CARD_IMAGE_PATHS.put("TEN of HEARTS",   "assets/Cards/10_of_hearts.png");
        CARD_IMAGE_PATHS.put("TEN of SPADES",   "assets/Cards/10_of_spades.png");
        // Aces
        CARD_IMAGE_PATHS.put("ACE of CLUBS",    "assets/Cards/ace_of_clubs.png");
        CARD_IMAGE_PATHS.put("ACE of DIAMONDS", "assets/Cards/ace_of_diamonds.png");
        CARD_IMAGE_PATHS.put("ACE of HEARTS",   "assets/Cards/ace_of_hearts.png");
        CARD_IMAGE_PATHS.put("ACE of SPADES",   "assets/Cards/ace_of_spades.png");
        // Jacks (png names end with “2”)
        CARD_IMAGE_PATHS.put("JACK of CLUBS",    "assets/Cards/jack_of_clubs2.png");
        CARD_IMAGE_PATHS.put("JACK of DIAMONDS", "assets/Cards/jack_of_diamonds2.png");
        CARD_IMAGE_PATHS.put("JACK of HEARTS",   "assets/Cards/jack_of_hearts2.png");
        CARD_IMAGE_PATHS.put("JACK of SPADES",   "assets/Cards/jack_of_spades2.png");
        // Queens
        CARD_IMAGE_PATHS.put("QUEEN of CLUBS",    "assets/Cards/queen_of_clubs2.png");
        CARD_IMAGE_PATHS.put("QUEEN of DIAMONDS", "assets/Cards/queen_of_diamonds2.png");
        CARD_IMAGE_PATHS.put("QUEEN of HEARTS",   "assets/Cards/queen_of_hearts2.png");
        CARD_IMAGE_PATHS.put("QUEEN of SPADES",   "assets/Cards/queen_of_spades2.png");
        // Kings
        CARD_IMAGE_PATHS.put("KING of CLUBS",    "assets/Cards/king_of_clubs2.png");
        CARD_IMAGE_PATHS.put("KING of DIAMONDS", "assets/Cards/king_of_diamonds2.png");
        CARD_IMAGE_PATHS.put("KING of HEARTS",   "assets/Cards/king_of_hearts2.png");
        CARD_IMAGE_PATHS.put("KING of SPADES",   "assets/Cards/king_of_spades2.png");
    }

    /* -------- in-memory bitmaps (faces + back) -------- */
    public static final Map<String,BufferedImage> CARD_IMAGES = new HashMap<>();

    static {
        // load faces
        for (var e : CARD_IMAGE_PATHS.entrySet()) {
            try {
                BufferedImage img = ImageIO.read(Constants.class
                        .getResourceAsStream("/" + e.getValue()));
                if (img != null) CARD_IMAGES.put(e.getKey().toUpperCase(), img);
            } catch (IOException ex) { System.err.println("Missing " + e.getValue()); }
        }
        // load back
        try {
            BufferedImage back = ImageIO.read(Constants.class
                    .getResourceAsStream("/assets/cards/card_back.png"));
            CARD_IMAGES.put("BACK", back);
            UI.CardUI.setBackImage(back);     // hand it to CardUI once
        } catch (IOException ex) { System.err.println("back image missing"); }
    }
}
