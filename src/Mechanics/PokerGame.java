package Mechanics;

import Model.*;
import java.util.*;

public class PokerGame {
    private Deck deck;
    private Table table;
    private Betting betting;
    private int currentRound;
    
    public PokerGame(List<Player> players, Scanner scanner) {
        this.deck = new Deck();
        this.table = new Table();
        this.betting = new Betting(players, scanner);
        this.currentRound = 1;
        
        for (Player player : players) {
            table.addPlayer(player);
        }
    }
    
    public void startGame() {
        //Starting the game loop
    	while (true) {
    		//Boolean endRound Variable to start and end the game round loop
        	boolean endRound = false;
        	
        	//Starting the Game round loop
            while(!endRound) {
            	
            	//Printing the Game round number
	            System.out.println("\n--- Starting Round " + currentRound + " ---");
	           
	            //Setup before each round
	            //Reset Deck -> Shuffle Deck -> Reset Status-> Deal Hole Cards -> Show Hole Cards
	            deck.resetDeck();
	            deck.shuffle();
	            resetStatus();
	            dealHoleCards();
	            showHoleCards();
	            
	            
	            //Small Blind && Big Blind
	            //Need to setup
	            
	            
	            //First Betting Round -> Deal 3 community cards
	            betting.startBettingRound();
	            
	            //If there is only one player still in the game, then end this game round
	            if (shouldEndGame()) {
	            	endRound = true;
	            	break;
	            }
	            for(int i=0; i<3; i++) {
		            dealCommunityCard();
	            }
	            showHoleCards();

	            
	            //Second & Third Betting Rounds -> Each Round Deal 1 more Community Card
	            for(int i=0; i<2; i++) {
	            	betting.nextBettingRound();
		            
		            //If there is only one player, then end round
		            if (shouldEndGame()) {
		            	endRound = true;
		            	break;
		            }
		            dealCommunityCard();
		            showHoleCards();
	            }
	            
	            
	            //Evaluate Hand -> Next Game Round
	            evaluateHands();
	            currentRound++;
            }
        }
    }
    
    //Deals two hole cards to each player
    //Get List of Players -> Iterates through -> Clear Hands -> Deal two hole cards
    private void dealHoleCards() {
        List<Player> players = table.getPlayers(); 
        for (Player player : players) {  
            player.clearHand();                  
            player.addCard(deck.dealCard());     
            player.addCard(deck.dealCard());  
        }
    }

    //Displays the hole cards of each player unless they folded
    //Get List of Players -> Iterates through -> If not Folded -> Print Card in Hand -> Else Folded
    private void showHoleCards() {
        List<Player> players = table.getPlayers(); 
        for (Player player : players) { 
            if (!player.isFolded()) { 
                System.out.println(player.getName() + "'s hand:"); 
                for (Card card : player.getHand().getCards()) {
                    System.out.println("  " + card); 
                }
                System.out.println(); 
            } else {
                System.out.println(player.getName() + " has folded.");
            }
        }
    }

    // Deals a single community card to all players (adds it to each player's hand)
    private void dealCommunityCard() {
        Card communityCard = deck.dealCard();    // Draw a card from the deck
        List<Player> players = table.getPlayers();
        for (Player player : players) {
            player.addCard(communityCard);       // Add the community card to the player's hand
        }
    }

    // Evaluates each active (non-folded) player's hand and determines the winner
    private void evaluateHands() {
        List<Player> players = table.getPlayers();
        for (Player player : players) {
            if (!player.isFolded()) {
                System.out.println(player.getName() + " evaluates hand: " + player.evaluateHand());
            }
        }

        Player winner = determineWinner(players); // Determine the winner among active players
        System.out.println("Winner: " + winner.getName());

        // Award the pot to the winner
        winner.setMoney(winner.getMoney() + betting.getPot());
    }

    // Determines which active player has the best hand
    private Player determineWinner(List<Player> players) {
        Player winner = null;
        int bestHand = 0;

        for (Player player : players) {
            if (!player.isFolded()) {
                String handStrength = player.evaluateHand();    // Get string representing hand (e.g., "Flush")
                int cardValue = player.handValue(handStrength); // Convert to numeric value for comparison
                if (winner == null || Integer.compare(cardValue, bestHand) > 0) {
                    winner = player;
                    bestHand = cardValue;
                }
            }
        }

        return winner;
    }

    // Resets the folded status of all players at the beginning of a new round
    private void resetStatus() {
        List<Player> players = table.getPlayers();
        for (Player player : players) {
            player.setFolded(false); // Mark player as active again
        }
    }

    // Checks if only one player remains in the round (all others have folded)
    private boolean shouldEndGame() {
        int activePlayers = 0;
        List<Player> players = table.getPlayers();

        for (Player player : players) {
            if (!player.isFolded()) {
                activePlayers++;
            }
        }

        System.out.println("There are " + activePlayers + " players left\n");

        return activePlayers <= 1; // Round ends if 0 or 1 players remain
    }
}