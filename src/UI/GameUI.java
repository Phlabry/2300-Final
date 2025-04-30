package UI;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class GameUI extends JFrame {

    private static final int BUTTON_WIDTH = 100;
    private static final int BUTTON_HEIGHT = 100;
    private static final int FRAME_WIDTH = 800;
    private static final int FRAME_HEIGHT = 500;

    public GameUI() {
        super("Texas Hold 'Em");

        this.setLayout(new BorderLayout());
        this.getContentPane().setBackground(Color.GREEN);

        // Panels
        JLayeredPane gamePanel = new JLayeredPane();
        gamePanel.setLayout(null);
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        
        JPanel headerPanel = new JPanel();
        headerPanel.setOpaque(false);

        // Title
        JLabel title = new JLabel("Texas Hold 'Em");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        headerPanel.add(title);

        // Buttons
        JButton startButton = new JButton("Start New Game");
        startButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        
        JButton call = new JButton("Call");
        call.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        
        JButton check = new JButton("Check");
        check.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));

        JButton fold = new JButton("Fold");
        fold.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));

        JButton raise = new JButton("Raise");
        raise.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));

        JButton allIn = new JButton("All In");
        allIn.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
                
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(startButton);
        
        this.add(headerPanel, BorderLayout.NORTH);
        this.add(gamePanel, BorderLayout.CENTER);
        this.add(buttonPanel, BorderLayout.SOUTH);
        
        ImagePanel floor = new ImagePanel("/assets/Floor.jpg", 0, 0, 800, 600);
        ImagePanel table = new ImagePanel("/assets/Table.png", 40, -125, 700, 550);

        //Set their bounds (x, y, width, height)
        floor.setBounds(0, 0, FRAME_WIDTH, FRAME_HEIGHT);
        table.setBounds(0, 0, 800, 400); // Even though it's offset internally, bounds should match canvas

        // Add to layers
        gamePanel.add(floor, Integer.valueOf(0)); // Bottom layer
        gamePanel.add(table, Integer.valueOf(1)); 
        
        // Start button logic
        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                buttonPanel.removeAll();
                buttonPanel.add(call);
                buttonPanel.add(check);
                buttonPanel.add(fold);
                buttonPanel.add(raise);
                buttonPanel.add(allIn);

                buttonPanel.revalidate();
                buttonPanel.repaint();
            }
        });

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        this.setVisible(true);
    }
}
