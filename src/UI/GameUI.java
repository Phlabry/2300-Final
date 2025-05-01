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
    private static final int FRAME_WIDTH = 1366;
    private static final int FRAME_HEIGHT = 768;
    private ImagePanel[] cpuIcons;

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
            
        
        // layout of buttons at bottom of screen
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(startButton);
        
        
        // position each panel
        this.add(headerPanel, BorderLayout.NORTH);
        this.add(gamePanel, BorderLayout.CENTER);
        this.add(buttonPanel, BorderLayout.SOUTH);
        
        // get images
        ImagePanel floor = new ImagePanel("/assets/Floor.jpg", 0, 0, 1400, 800);
        ImagePanel table = new ImagePanel("/assets/Table.png", -10, -220, 1350, 1000);
        
        //Set bounds (x, y, width, height)
        floor.setBounds(0, 0, FRAME_WIDTH, FRAME_HEIGHT);
        table.setBounds(0, 0, FRAME_WIDTH, FRAME_HEIGHT);

        // Add to gamePanel
        gamePanel.add(floor, Integer.valueOf(0)); // layering
        gamePanel.add(table, Integer.valueOf(1));
        
        // add cpu Icons
        this.cpuIcons = new ImagePanel[5];
        for (int i=0; i<4; i++) {
        	cpuIcons[i] = new ImagePanel("/assets/UserIcon.png", 100,100, 200, 200); // get cpu Icon
            cpuIcons[i].setBounds(0, 0, FRAME_WIDTH, FRAME_HEIGHT);                  // set bound so doesn't go outside frame
        }
        
        // setting icon positions and layering them
        cpuIcons[0].setPosition(25, 25);
        gamePanel.add(cpuIcons[0], Integer.valueOf(2));
        
        cpuIcons[1].setPosition(40, 350);
        gamePanel.add(cpuIcons[1], Integer.valueOf(2));

        cpuIcons[2].setPosition(1100, 350);
        gamePanel.add(cpuIcons[2], Integer.valueOf(2));
        
        cpuIcons[3].setPosition(1120, 25);
        gamePanel.add(cpuIcons[3], Integer.valueOf(2));

        
        
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
