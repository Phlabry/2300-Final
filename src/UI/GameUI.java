// UI
package UI;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.Color;


public class GameUI extends JFrame {

    // constructor that sets up the GUI
    public GameUI() {
        super("Texas Hold 'Em");
        
        JPanel gamePanel = new JPanel();
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        this.getContentPane().setBackground( Color.GREEN );

        // all UI components
        JLabel title = new JLabel("Texas Hold 'Em");

        JButton startButton = new JButton("Start New Game");
        startButton.setPreferredSize(new Dimension(100, 100));

        JButton check = new JButton("Check");
        check.setPreferredSize(new Dimension(100, 100));

        JButton fold = new JButton("Fold");
        fold.setPreferredSize(new Dimension(100, 100));

        JButton raise = new JButton("Raise");
        raise.setPreferredSize(new Dimension(100, 100));

        JButton allIn = new JButton("All In");
        allIn.setPreferredSize(new Dimension(100, 100));

        // set button panel layout
        FlowLayout buttonLayout = new FlowLayout();
        buttonPanel.setLayout(buttonLayout);

        // add initial UI components
        buttonPanel.add(title);
        buttonPanel.add(startButton);

        // add buttonPanel to the frame (not "panel")
        this.add(buttonPanel);
        this.add(gamePanel)

        // add action listener to start button
        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                buttonPanel.add(check);
                buttonPanel.add(fold);
                buttonPanel.add(raise);
                buttonPanel.add(allIn);
                buttonPanel.remove(startButton); // remove start button

                buttonPanel.revalidate();
                buttonPanel.repaint();
            }
        });

        // JFrame properties
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(800, 500);
        this.setVisible(true);
    }
}
