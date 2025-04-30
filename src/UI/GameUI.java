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

    public GameUI() {
        super("Texas Hold 'Em");

        this.setLayout(new BorderLayout());
        this.getContentPane().setBackground(Color.GREEN);

        // Panels
        JPanel gamePanel = new JPanel();
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
        
        ImagePanel table = new ImagePanel("/assets/Table.png", 40, -125, 700, 550);
        table.setPreferredSize(new Dimension(800, 500)); // Optional

        gamePanel.add(table);
        
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
        this.setSize(800, 500);
        this.setVisible(true);
    }
}
