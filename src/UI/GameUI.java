package UI;

import Mechanics.*;
import Model.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.List;
import java.util.ArrayList;

public class GameUI extends JFrame {

    private static final int BUTTON_WIDTH = 100;
    private static final int BUTTON_HEIGHT = 100;

    private PokerGame pokerGame;
    private Player currentPlayer;
    private boolean hasActed = false;

    private JPanel gamePanel;
    private JPanel buttonPanel;
    private JPanel headerPanel;
    private JLabel statusLabel;
    private JButton call, check, fold, raise, allIn;

    public GameUI() {
        super("Texas Hold 'Em");

        this.setLayout(new BorderLayout());
        this.getContentPane().setBackground(Color.GREEN);

        //Panels
        gamePanel = new JPanel();
        
        buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        
        headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setOpaque(false);

        //Initalize title and add it to headerPanel
        JLabel title = new JLabel("Texas Hold 'Em");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerPanel.add(title);

        
        //Initialize statusLabel and add it to headerPanel
        statusLabel = new JLabel("Waiting for Game to Start");
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerPanel.add(statusLabel);  
        
        
        //Buttons
        JButton startButton = new JButton("Start New Game");
        startButton.setPreferredSize(new Dimension(BUTTON_WIDTH*2, BUTTON_HEIGHT));
        
        JButton easyButton = new JButton("Easy");
        easyButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));

        JButton mediumButton = new JButton("Medium");
        mediumButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));

        JButton hardButton = new JButton("Hard");
        hardButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));

        call = new JButton("Call");
        call.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));

        check = new JButton("Check");
        check.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));

        fold = new JButton("Fold");
        fold.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));

        raise = new JButton("Raise");
        raise.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));

        allIn = new JButton("All In");
        allIn.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));

        //Set Layout
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(startButton);

        this.add(headerPanel, BorderLayout.NORTH);
        this.add(gamePanel, BorderLayout.CENTER);
        this.add(buttonPanel, BorderLayout.SOUTH);

        //Start button logic -> Difficulty selection -> Start game
        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                buttonPanel.removeAll();
                statusLabel.setText("Select difficulty");
                
                //Show difficulty selection
                buttonPanel.add(easyButton);
                buttonPanel.add(mediumButton);
                buttonPanel.add(hardButton);
                
                updateUI();
            }
        });

        easyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	addActionButtons();
                startPokerGameWithDifficulty(10000); //$10000 starting
            }
        });

        mediumButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	addActionButtons();
                startPokerGameWithDifficulty(5000); //$5000 starting
            }
        });

        hardButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	addActionButtons();
                startPokerGameWithDifficulty(1000); //$1000 starting
            }
        });
        
        
        //Button actions to interact with the game
        call.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (currentPlayer.isHuman()) {
                    System.out.println("Call button clicked\n");
                    pokerGame.gameAction("call");
                    checkGameState();
                }
            }
        });

        check.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (currentPlayer.isHuman()) {
                    System.out.println("Check button clicked\n");
                    pokerGame.gameAction("check");
                    checkGameState();
                }
            }
        });

        fold.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (currentPlayer.isHuman()) {
                    System.out.println("Fold button clicked\n");
                    pokerGame.gameAction("fold");
                    checkGameState();
                }
            }
        });

        raise.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (currentPlayer.isHuman()) {
                    System.out.println("Raise button clicked");
                    String raiseAmount = JOptionPane.showInputDialog("Enter raise amount:");
                    try {
                        int amount = Integer.parseInt(raiseAmount);
                        pokerGame.gameActionRaising("raise", amount);
                        checkGameState();
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(GameUI.this, "Please enter a valid number.");
                    }
                }
            }
        });

        allIn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (currentPlayer.isHuman()) {
                    System.out.println("All In button clicked\n");
                    pokerGame.gameAction("allin");
                    checkGameState();
                }
            }
        });


        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(800, 500);
        this.setVisible(true);
    }
    
    //Add game actions buttons
    private void addActionButtons() {
        buttonPanel.removeAll();
        buttonPanel.add(call);
        buttonPanel.add(check);
        buttonPanel.add(fold);
        buttonPanel.add(raise);
        buttonPanel.add(allIn);

        updateUI();
    }
    
    private void disableActionButtons() {
        call.setEnabled(false);
        check.setEnabled(false);
        fold.setEnabled(false);
        raise.setEnabled(false);
        allIn.setEnabled(false);
        updateUI();
    }

    private void enableActionButtons() {
        call.setEnabled(true);
        check.setEnabled(true);
        fold.setEnabled(true);
        raise.setEnabled(true);
        allIn.setEnabled(true);
        updateUI();
    }

    // Check game state after an action and manage AI actions
    private void checkGameState() {
        // Update current player reference
        currentPlayer = pokerGame.getCurrentPlayer();
        updateUI();
        
        
        if (!currentPlayer.isHuman()) {
            simulateAIActions();  
        }

        
        //Check if betting round is complete
        if (pokerGame.isBettingRoundComplete()) {
            statusLabel.setText("Betting round complete. Advancing phase...");
            updateUI();
            // Slight delay before advancing phase for visual feedback
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            pokerGame.advancePhase();
            currentPlayer = pokerGame.getCurrentPlayer(); //Update player after advancing phase

            if (!currentPlayer.isHuman()) {
                simulateAIActions();
            }
        }
    }
    
    private void simulateAIActions() {
        // Handle AI actions in sequence until it's the human's turn again
        while (!currentPlayer.isHuman()) {
            // Let the AI decide and execute an action
            if (currentPlayer instanceof AutoPlayer) {
                String action = ((AutoPlayer) currentPlayer).decideAction();
                pokerGame.gameAction(action.toLowerCase());
                
                //Update current player after action
                currentPlayer = pokerGame.getCurrentPlayer();
                
                // Check if betting round is complete after AI move
                if (pokerGame.isBettingRoundComplete()) {
                    statusLabel.setText("Betting round complete. Advancing phase...");
                    updateUI();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    pokerGame.advancePhase();
                    updateUI();
                }
            }
        }
    }
    
    //Start game
    private void startPokerGameWithDifficulty(int startingMoney) {
        List<Player> players = new ArrayList<>();
        int numPlayers = 5;

        //Real player
        String name = JOptionPane.showInputDialog("Enter your name:");
        if (name == null || name.trim().isEmpty()) {
            name = "Player";  // Default name
        }
        players.add(new Player(name.trim(), startingMoney, true));
        
        for (int i = 0; i < numPlayers-1; i++) {
            players.add(new AutoPlayer("Bot " + (i + 1), startingMoney, false));
        }

        pokerGame = new PokerGame(players);         
        //Setup action buttons and update UI
        pokerGame.startGame();
        currentPlayer = pokerGame.getCurrentPlayer();

        // If first player is AI, automatically handle their action
        if (!currentPlayer.isHuman()) {
            simulateAIActions();  //Automatically simulate AI actions if it's AI's turn
        }
    }
    
    private void updateUI() {
        gamePanel.revalidate();
        gamePanel.repaint();
    }
}
