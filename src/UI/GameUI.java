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
    private static final int FRAME_WIDTH = 1920;
    private static final int FRAME_HEIGHT = 1080;
    private static final int CARD_WIDTH = 80;
    private static final int CARD_HEIGHT = 120;
    
    // Store the original positions for player card panels to scale properly
    private Point[] originalPlayerCardPositions;
    private Dimension[] originalPlayerCardSizes;
    
    private ImagePanel[] cpuIcons;
    private ImagePanel floor;
    private ImagePanel table;

    private JPanel communityCardsPanel;
    private JPanel[] playerCardsPanel;
    private JLabel[] playerLabels;
    private JLabel[] playerChipsLabels;

    private JLabel[] Pot;
    
    private PokerGame pokerGame;
    private Player currentPlayer;
    private boolean hasActed = false;

    private JLayeredPane gamePanel;
    private JPanel buttonPanel;
    private JPanel headerPanel;
    private JLabel statusLabel;
    private JButton call, check, fold, raise, allIn;

    public GameUI() {
        super("Texas Hold 'Em");

        this.setLayout(new BorderLayout());
        this.getContentPane().setBackground(Color.GREEN.darker());

        //Initalize gamePanel and set layout to null
        gamePanel = new JLayeredPane();
        gamePanel.setLayout(null);

        
        //Initalize buttonPanel and set opaque to false
        buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        
        
        //Initalize headerPanel and set layout to boxLayout with opaque set to false
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
        
        //Initialize Card Panels
        initializeCardPanels();
        
        // Store original positions and sizes for scaling calculations
        storeOriginalPositionsAndSizes();
        
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
        
        //Get images
        floor = new ImagePanel("/assets/Floor.jpg", 0, 0, FRAME_WIDTH, FRAME_HEIGHT);
        table = new ImagePanel("/assets/Table.png", 60, -100, FRAME_WIDTH - 525, FRAME_HEIGHT - 250);
        
        //Set bounds (x, y, width, height)
        floor.setBounds(0, 0, FRAME_WIDTH, FRAME_HEIGHT);
        table.setBounds(0, 0, FRAME_WIDTH, FRAME_HEIGHT);

        //Add to gamePanel
        gamePanel.add(floor, Integer.valueOf(0)); // layering
        gamePanel.add(table, Integer.valueOf(1));
        
        // Add cpu Icons
        this.cpuIcons = new ImagePanel[5];
        for (int i=0; i<4; i++) {
            cpuIcons[i] = new ImagePanel("/assets/UserIcon.png", 100, 100, 200, 200); // get cpu Icon
            cpuIcons[i].setBounds(0, 0, FRAME_WIDTH, FRAME_HEIGHT);                  // set bound so doesn't go outside frame
        }
        
        // Setting icon positions and layering them
        cpuIcons[0].setPosition(25, 0);
        gamePanel.add(cpuIcons[0], Integer.valueOf(2));
        
        cpuIcons[1].setPosition(25, 450);
        gamePanel.add(cpuIcons[1], Integer.valueOf(2));

        cpuIcons[2].setPosition(1300, 450);
        gamePanel.add(cpuIcons[2], Integer.valueOf(2));
        
        cpuIcons[3].setPosition(1300, 0);
        gamePanel.add(cpuIcons[3], Integer.valueOf(2));


        //Add window resize listener to update scaling
        this.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                updateAllImageScales();
            }
        });
        
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
        
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        this.setVisible(true);

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
    }
    
    // Store the original positions and sizes for scaling
    private void storeOriginalPositionsAndSizes() {
        originalPlayerCardPositions = new Point[5];
        originalPlayerCardSizes = new Dimension[5];
        
        for (int i = 0; i < 5; i++) {
            if (playerCardsPanel[i] != null) {
                originalPlayerCardPositions[i] = new Point(playerCardsPanel[i].getX(), playerCardsPanel[i].getY());
                originalPlayerCardSizes[i] = new Dimension(playerCardsPanel[i].getWidth(), playerCardsPanel[i].getHeight());
            }
        }
        
        // Also store original community cards panel position and size
        if (communityCardsPanel != null) {
            // We can add these as additional variables if needed
        }
    }
    
    //Initialize card panels for players and community cards
    private void initializeCardPanels() {
        // Set up community cards panel in center of table
        communityCardsPanel = new JPanel();
        communityCardsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
        communityCardsPanel.setBackground(new Color(0, 0, 0, 0)); // Transparent background
        communityCardsPanel.setBounds(FRAME_WIDTH/2 - 250, FRAME_HEIGHT/2 - 60, 500, 120);
        gamePanel.add(communityCardsPanel, Integer.valueOf(3));
        
        // Create panels for each player's cards (5 players)
        playerCardsPanel = new JPanel[5];
        playerLabels = new JLabel[5];
        playerChipsLabels = new JLabel[5];
        
        // Create player card panels and position them
        for (int i = 0; i < 5; i++) {
            playerCardsPanel[i] = new JPanel();
            playerCardsPanel[i].setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
            playerCardsPanel[i].setBackground(new Color(0, 0, 0, 0)); // Transparent background
            playerCardsPanel[i].setSize(FRAME_WIDTH, FRAME_HEIGHT);

            playerLabels[i] = new JLabel("Player " + (i+1));
            playerLabels[i].setForeground(Color.WHITE);
            playerLabels[i].setFont(new Font("Arial", Font.BOLD, 14));
            
            playerChipsLabels[i] = new JLabel("$0");
            playerChipsLabels[i].setForeground(Color.YELLOW);
            playerChipsLabels[i].setFont(new Font("Arial", Font.BOLD, 14));
            
            // Add player name and chips labels to the card panel
            JPanel infoPanel = new JPanel();
            infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
            infoPanel.setBackground(new Color(0, 0, 0, 0)); // Transparent
            infoPanel.add(playerLabels[i]);
            infoPanel.add(playerChipsLabels[i]);
            
            // Add info panel to the player's card panel
            playerCardsPanel[i].add(infoPanel);
            
            // Add the player's card panel to the game panel
            gamePanel.add(playerCardsPanel[i], Integer.valueOf(3));
        }
        
        // Position player cards panels
        // Human player (bottom center)
        playerCardsPanel[0].setBounds(FRAME_WIDTH/2 - 100, FRAME_HEIGHT - 200, 200, 150);
        
        
        
        // Left bottom player - positioned to the right of the icon
        playerCardsPanel[1].setBounds(225, 750, 200, 150); // To the right of player icon at (25,450)
        
        // Left top player - positioned to the right of the icon
        playerCardsPanel[2].setBounds(225, 50, 200, 150); // To the right of player icon at (25,0)
        
        // Right top player - positioned to the left of the icon
        playerCardsPanel[3].setBounds(1625, 50, 200, 150); // To the left of player icon at (1300,0)
        
         // Right bottom player - positioned to the left of the icon
        playerCardsPanel[4].setBounds(1625, 750, 200, 150); // To the left of player icon at (1300,450)
    }
    
    
    // Update cards display based on the current game state
    public void updateCardDisplay() {
        if (pokerGame == null) return;
        
        // Update community cards
        communityCardsPanel.removeAll();
        
        List<Card> communityCards = pokerGame.getCommunityCards();
        if (communityCards != null) {
            for (Card card : communityCards) {
                JLabel cardLabel = createCardLabel(card);
                communityCardsPanel.add(cardLabel);
            }
        }
        
        // Update player cards
        List<Player> players = pokerGame.getPlayers();
        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            
            // Update player info
            playerLabels[i].setText(player.getName());
            playerChipsLabels[i].setText("$" + player.getMoney());
            
            // Clear previous cards
            // Remove all components except the first one (which is the info panel)
            while (playerCardsPanel[i].getComponentCount() > 1) {
                playerCardsPanel[i].remove(1);
            }
            
            // Add current cards
            List<Card> playerCards = player.getHand().getCards();
            if (playerCards != null) {
                for (Card card : playerCards) {
                    // Only show cards for human player or if game is in showdown
                    boolean showCard = player.isHuman() || pokerGame.isShowdown();
                    JLabel cardLabel = showCard ? createCardLabel(card) : createCardBackLabel();
                    playerCardsPanel[i].add(cardLabel);
                }
            }
        }
        
        updateUI();
    }
    
    // Create a JLabel for a card
    private JLabel createCardLabel(Card card) {
        // Construct card image path based on card value and suit
        String cardImagePath = "/assets/cards/" + card.getRank() + "_of_" + card.getSuit().name().toLowerCase() + ".png";
        ImageIcon cardIcon = new ImageIcon(cardImagePath);
        
        // Resize the image
        Image img = cardIcon.getImage().getScaledInstance(CARD_WIDTH, CARD_HEIGHT, Image.SCALE_SMOOTH);
        cardIcon = new ImageIcon(img);
        
        JLabel cardLabel = new JLabel(cardIcon);
        cardLabel.setPreferredSize(new Dimension(CARD_WIDTH, CARD_HEIGHT));
        return cardLabel;
    }
    
    // Create a JLabel for card back (for opponent's hidden cards)
    private JLabel createCardBackLabel() {
        String cardBackPath = "/assets/cards/card_back.png";
        ImageIcon cardIcon = new ImageIcon(getClass().getResource(cardBackPath));
        
        // Resize the image
        Image img = cardIcon.getImage().getScaledInstance(CARD_WIDTH, CARD_HEIGHT, Image.SCALE_SMOOTH);
        cardIcon = new ImageIcon(img);
        
        JLabel cardLabel = new JLabel(cardIcon);
        cardLabel.setPreferredSize(new Dimension(CARD_WIDTH, CARD_HEIGHT));
        return cardLabel;
    }
    
    
    //Update Scale
    private void updateAllImageScales() {
        int width = getWidth();
        int height = getHeight();
        
        // Calculate scale factors based on original frame size
        double widthRatio = (double) width / FRAME_WIDTH;
        double heightRatio = (double) height / FRAME_HEIGHT;
        
        // Update player card panels positions and sizes
        for (int i = 0; i < 5; i++) {
            if (playerCardsPanel[i] != null && originalPlayerCardPositions[i] != null) {
                // Scale x and y positions
                int newX = (int) (originalPlayerCardPositions[i].x * widthRatio);
                int newY = (int) (originalPlayerCardPositions[i].y * heightRatio);
                
                // Scale width and height
                int newWidth = (int) (originalPlayerCardSizes[i].width * widthRatio);
                int newHeight = (int) (originalPlayerCardSizes[i].height * heightRatio);
                
                // Apply new bounds
                playerCardsPanel[i].setBounds(newX, newY, newWidth, newHeight);
            }
        }
        
        // Also scale community cards panel
        if (communityCardsPanel != null) {
            int commX = (int) ((FRAME_WIDTH/2 - 250) * widthRatio);
            int commY = (int) ((FRAME_HEIGHT/2 - 60) * heightRatio);
            int commWidth = (int) (500 * widthRatio);
            int commHeight = (int) (120 * heightRatio);
            communityCardsPanel.setBounds(commX, commY, commWidth, commHeight);
        }
        
        //Update each image panel scale
        floor.updateScale(width, height);
        table.updateScale(width, height);
        
        for (ImagePanel icon : cpuIcons) {
            if (icon != null) {
                icon.updateScale(width, height);
            }
        }
        
        updateUI();
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
    
    //Update Action Buttons if they are allowed to be pushed
    private void updateActionButtons() {
        if (currentPlayer == null || !currentPlayer.isHuman()) {
            disableActionButtons();
            return;
        }

        enableActionButtons(); //enable all by default

        //You can expand this for raise/all-in if needed
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
        
        updateCardDisplay();

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

            updateCardDisplay();

            if (!currentPlayer.isHuman()) {
                simulateAIActions();
            }
        }
        updateActionButtons();
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
        updateActionButtons();
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

        updateCardDisplay();

        // If first player is AI, automatically handle their action
        if (!currentPlayer.isHuman()) {
            simulateAIActions();  //Automatically simulate AI actions if it's AI's turn
        }
        updateActionButtons();
    }
    
    private void updateUI() {
        gamePanel.revalidate();
        gamePanel.repaint();
    }
}