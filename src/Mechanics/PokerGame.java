package Mechanics;

import Model.*;
import java.util.*;

public class PokerGame {
    private Deck deck;
    private Table table;
    private Betting betting;
    private int currentRound;
    private int leftoverRollover;
    
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
	            deck.resetDeck(); //Reset Deck
	            deck.shuffle(); //Shuffle Deck
	            resetStatus(); //Reset Folded Status
	            setupBlinds(); //Setup blinds
	            dealHoleCards(); //Deal hole Cards
	            showHoleCards(); //Show Hole Cards
	            
	            
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
	            
	            
	            //Evaluate Hand -> Distribute Pot(s) -> Next Game Round
	            evaluateHands();
	            distributePots(); 

	            currentRound++;
            }
        }
    }
    
    //Setup the blinds, forcing bets so players can't just fold every time
    private void setupBlinds() {
        List<Player> players = table.getPlayers();
        int numPlayers = players.size();

        //Calculate positions of small blind and big blind based on the round number
        int smallBlindIndex = (currentRound - 1) % numPlayers;  //Small blind: (round - 1) % number of players
        int bigBlindIndex = currentRound % numPlayers;          //Big blind: round % number of players

        //Get the players in these positions
        Player smallBlindPlayer = players.get(smallBlindIndex);
        Player bigBlindPlayer = players.get(bigBlindIndex);

        //Blind amounts, small is half of big
        int bigBlindAmount = 10;
        int smallBlindAmount = bigBlindAmount/2;
        
        //Post blinds, set money of those players affected by the blind
        smallBlindPlayer.setMoney(smallBlindPlayer.getMoney() - smallBlindAmount);
        bigBlindPlayer.setMoney(bigBlindPlayer.getMoney() - bigBlindAmount);

        //Print the blinds for debugging purposes
        System.out.println(smallBlindPlayer.getName() + " posts small blind: $" + smallBlindAmount);
        System.out.println(bigBlindPlayer.getName() + " posts big blind: $" + bigBlindAmount + "\n");
        
        betting.createInitialPot(smallBlindAmount, bigBlindAmount, leftoverRollover);
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
                HandStrength result = HandEvaluator.evaluateHandWithMax(player.getHand().getCards());
                player.setHandValue(result.getHandValue());
                player.setHighCard(result.getHighCard());
                System.out.println(player.getName() + " evaluates hand: " + player.getHandValue() + " (High Card:" + player.getHighCard()+ ")");
            }
        }
    }
    
    // Determines which active player has the best hand
    private List<Player> determineWinners(List<Player> players) {
        List<Player> winners = new ArrayList<>();
        int bestHandValue = -1;
        Card.Rank bestHighCard = null;

        for (Player player : players) {
            if (player.isFolded()) continue;

            int currentHandValue = HandEvaluator.handValue(player.getHandValue());
            Card.Rank currentHighCard = player.getHighCard();

            if (currentHandValue > bestHandValue) {
                winners.clear();
                winners.add(player);
                bestHandValue = currentHandValue;
                bestHighCard = currentHighCard;
            } else if (currentHandValue == bestHandValue) {
                int compare = currentHighCard.compareTo(bestHighCard);
                if (compare > 0) {
                    winners.clear();
                    winners.add(player);
                    bestHighCard = currentHighCard;
                } else if (compare == 0) {
                    winners.add(player); // Tie
                }
            }
        }

        return winners;
    }

    //Distribute the pots to the winners (main and side pots)
    private void distributePots() {
        List<Pot> pots = betting.getPots();  //Get the list of pots (main and side pots)
        Map<Player, Integer> totalWinnings = new HashMap<>(); //To accumulate the winnings for each player

        //Iterate over each pot and distribute to the winner
        for (Pot pot : pots) {
            Set<Player> contenders = pot.getContenders();
            List<Player> winners = determineWinners(new ArrayList<>(contenders));
            int potAmount = pot.getAmount();
            int share = pot.getAmount() / winners.size();

            for (Player winner : winners) {
                totalWinnings.put(winner, totalWinnings.getOrDefault(winner, 0) + share);
            }
            
            int remainder = potAmount % winners.size();
            leftoverRollover += remainder;
            
            if (remainder > 0) {
                System.out.println("Undistributed remainder of $" + remainder + " left in pot.");
            }
        }
        //Add the total winnings to each player's balance and print total winnings
        for (Map.Entry<Player, Integer> entry : totalWinnings.entrySet()) {
            Player winner = entry.getKey();
            int totalAmountWon = entry.getValue();
            winner.setMoney(winner.getMoney() + totalAmountWon);  //Add total winnings to the player's money

            //Print the total amount won by the winner
            System.out.println("Total amount won by " + winner.getName() + ": $" + totalAmountWon);
        }

        //Clear the pots after distributing the money
        betting.getPots().clear();
    }
    
    //Resets the folded status of all players at the beginning of a new round
    private void resetStatus() {
        List<Player> players = table.getPlayers();
        for (Player player : players) {
            player.setFolded(false); // Mark player as active again
        }
    }
    
    //Checks if only one player remains in the round (all others have folded)
    private boolean shouldEndGame() {
        int activePlayers = 0;
        List<Player> players = table.getPlayers();

        for (Player player : players) {
            if (!player.isFolded()) {
                activePlayers++;
            }
        }

        System.out.println("There are " + activePlayers + " players left\n");

        return activePlayers <= 1; //Round ends if 0 or 1 players remain
    }
}