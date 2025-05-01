package Mechanics;

import Model.*;
import java.util.*;

public class PokerGame {
    private Deck deck;
    private Table table;
    private Betting betting;
    private int numPlayers;
    private Player currentPlayer;
    private int currentRound;
    private int leftoverRollover;
    private int phase;

    
    public PokerGame(List<Player> players) {
        this.deck = new Deck();
        this.table = new Table();
        this.betting = new Betting(players);
        this.numPlayers = players.size();
        this.currentPlayer = null;
        this.currentRound = 1;
        //0 = preflop, 1 = flop, 2 = turn, 3 = river, 4 = showdown
        this.phase = 0;
        
        for (Player player : players) {
            table.addPlayer(player);
        }
    }
    

    public void advancePhase() {
        switch (phase) {
            case 0:
                continueToFlop();
                break;
            case 1:
                continueToTurn();
                break;
            case 2:
                continueToRiver();
                break;
            case 3:
                evaluateHandsAndDetermineWinner();
                break;
        }
        if (phase < 3) {
            List<Player> activePlayers = getActivePlayers();
            for (Player player : activePlayers) {
            	player.setHasActed(false);
            }
            phase++;
        } else {
            phase = 0;
        }
    }

    //start game which calls the starting methods
    public void startGame() {
        //Initialize deck, table, and reset everything
        this.deck.resetDeck();
        this.deck.shuffle();
        resetActedStatus();   
        resetFoldedStatus();

        System.out.println("\n--- Starting Round " + currentRound + " ---");

        //Deal the blinds and deal hole cards and show hole cards to Human player
        setupBlinds();
        dealHoleCards();
        showHoleCards();

        //Set Current Player to Next player after players who did small/big blind
        
        currentPlayer = table.getPlayers().get((currentRound + 1) % numPlayers); 

        //Start first betting round -> Next Player
        betting.startBettingRound();
    }

    
    //Called by GUI after first betting round is complete
    public void continueToFlop() {
        for (int i = 0; i < 3; i++) {
            dealCommunityCard();
        }
        showHoleCards();

        //Start Second betting round -> Next Player
        betting.nextBettingRound(); 
        resetActedStatus();   
        int smallBlindIndex = (currentRound - 1) % numPlayers;
        findFirstActivePlayerStartingFrom(smallBlindIndex);
    }

    //Called after second betting round is complete
    public void continueToTurn() {
        dealCommunityCard();
        showHoleCards();

        //Start Third betting round -> Next Player
        betting.nextBettingRound(); 
        resetActedStatus();   
        
        int smallBlindIndex = (currentRound - 1) % numPlayers;
        findFirstActivePlayerStartingFrom(smallBlindIndex);
    }

    //Called after third betting round is complete
    public void continueToRiver() {
        dealCommunityCard();
        showHoleCards();

        //Start Last betting round -> Next Player
        betting.nextBettingRound(); 
        resetActedStatus();   
        int smallBlindIndex = (currentRound - 1) % numPlayers;
        findFirstActivePlayerStartingFrom(smallBlindIndex);
    } 
    
    //Called after Last Betting round is complete
    public void evaluateHandsAndDetermineWinner() {
        //No more betting rounds -> Evaluate and finish
        evaluateHands();
        distributePots();

        currentRound++;
        System.out.println("Round " + (currentRound - 1) + " Over.\n");
        startGame();
    }

    public boolean isBettingRoundComplete() {
        return betting.isBettingRoundComplete();
    }

    //Get all players who are still active in the hand
    public List<Player> getActivePlayers() {
        List<Player> active = new ArrayList<>();
        for (Player player : table.getPlayers()) {  // Assuming table.getPlayers() gives all players
            if (!player.isFolded() && player.getMoney() > 0) {
                active.add(player);
            }
        }
        return active;
    }
    
    private void findFirstActivePlayerStartingFrom(int startIndex) {
        List<Player> allPlayers = table.getPlayers();
        
        // Loop through the players, starting at the specified index
        int index = startIndex;
        int loopCount = 0;
        
        while (loopCount < allPlayers.size()) {
            Player candidate = allPlayers.get(index);
            if (!candidate.isFolded() && candidate.getMoney() > 0) {
                currentPlayer = candidate;
                return;
            }
            index = (index + 1) % allPlayers.size();
            loopCount++;
        }
        
        // Fallback - just get any active player if we can't find one starting from the intended position
        for (Player player : allPlayers) {
            if (!player.isFolded() && player.getMoney() > 0) {
                currentPlayer = player;
                return;
            }
        }
        
        // If no active players at all, this is a serious issue
        System.out.println("WARNING: No active players found!");
    }
    
    //Advance turn to the next active player
    public void advanceToNextPlayer() {
    	List<Player> allPlayers = table.getPlayers();
        List<Player> activePlayers = getActivePlayers();
        
        if (activePlayers.size() <= 1) {
            // Only one player left -> End hand
            endHand();
            return;
        }
        
        if (betting.isBettingRoundComplete()) { 
            return;
        }
        
        if (currentPlayer == null) {
            currentPlayer = activePlayers.get(0);
        } else {
            // Find the current player's position in the ORIGINAL player list
            int currentOriginalIndex = allPlayers.indexOf(currentPlayer);
            
            // Find the next active player in the ORIGINAL order
            Player nextPlayer = null;
            int index = (currentOriginalIndex + 1) % allPlayers.size();
            
            // Loop through the original player list until we find an active player
            int loopCount = 0;
            while (loopCount < allPlayers.size()) {
                Player candidate = allPlayers.get(index);
                if (!candidate.isFolded() && candidate.getMoney() > 0) {
                    nextPlayer = candidate;
                    break;
                }
                index = (index + 1) % allPlayers.size();
                loopCount++;
            }
            
            currentPlayer = nextPlayer != null ? nextPlayer : activePlayers.get(0);
        }
      
        //Print who is acting
        System.out.println(currentPlayer.getName() + "'s turn...");

        if (!currentPlayer.isHuman()) {
            try {
                Thread.sleep(1000);  //Delay for realism
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }

            //Simulate AI action here
            String action = ((AutoPlayer) currentPlayer).decideAction();
            gameAction(action.toLowerCase());
        } else {
            //For human, do NOT auto-advance. Wait for GUI or console input.
            System.out.println("Waiting for game action...");
            //Game loop should now pause until GUI calls gameAction(...) with user input
        }
    }

    // End hand logic (example stub)
    public void endHand() {
        // Determine winner, award pot, etc.
    	evaluateHands();
        distributePots();
        System.out.println("Hand is over. \n");
        currentRound++;
        startGame();
    }
    
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    //Setup the blinds, forcing bets so players can't just fold every time
    private void setupBlinds() {
        List<Player> players = table.getPlayers();

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
        System.out.println(bigBlindPlayer.getName() + " posts big blind: $" + bigBlindAmount);
        
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
                System.out.println(player.getName() + " has folded.\n");
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
    
    //Determines which active player has the best hand
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
            Set<Player> contenders = pot.getEligiblePlayers();
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
    
    //Resets the folded status of all players
    private void resetFoldedStatus() {
        List<Player> players = table.getPlayers();
        for (Player player : players) {
            player.setFolded(false); //Mark player as active again
        }
    }
    
    //Resets the Acted status of all players
    private void resetActedStatus() {
        List<Player> players = table.getPlayers();
        for (Player player : players) {
            player.setHasActed(false); //Marks player as haven't acted yet
        }
    }
    
    //GameActions not including raise
    public void gameAction(String action) {
    	if (currentPlayer.hasActed()) {
            return; // Skip the action if the player already acted.
        }
    	
        System.out.println(currentPlayer.getName() + " chooses to " + action);

        switch (action) {
            case "call":
                betting.handleCall(currentPlayer);
                break;
            case "check":
                betting.handleCheck(currentPlayer);
                break;
            case "fold":
                betting.handleFold(currentPlayer);
                break;
            case "allin":
                betting.handleAllIn(currentPlayer);
                betting.resetHasActedForOthers(currentPlayer);
                break;
            default:
                throw new IllegalArgumentException("Unknown action: " + action);
        }
        
        System.out.println(currentPlayer.getName() + " now has $" + currentPlayer.getMoney());
        currentPlayer.setHasActed(true); //Mark player as having acted in this round.
        
        if (shouldEndGame()) {
            endHand();
        } else if (isBettingRoundComplete()) {
            advancePhase(); // move to flop/turn/etc
        }
        
        advanceToNextPlayer(); // only advance if round isn't done
        
    }

    //GameAction including raise
    public void gameActionRaising(String action, int raiseAmount) {
    	if (currentPlayer.hasActed()) {
            return; // Skip the action if the player already acted.
        }
        System.out.println(currentPlayer.getName() + " chooses to " + action + " $" + raiseAmount);

        switch (action) {
            case "raise":
            	betting.handleRaise(currentPlayer, raiseAmount);
                break;
            default:
                throw new IllegalArgumentException("Unknown action: " + action);
        }
        
        System.out.println(currentPlayer.getName() + " now has $" + currentPlayer.getMoney());
        
        if(action.equals("call") || action.equals("check") || action.equals("fold")) {
        	currentPlayer.setHasActed(true); //Mark player as having acted in this round.
    	}
        
        if (shouldEndGame()) {
            endHand();
        } else if (isBettingRoundComplete()) {
            advancePhase(); // move to flop/turn/etc
        } else {
            advanceToNextPlayer(); // only advance if round isn't done
        }
    }
    
    
    //Checks if only one player remains in the round (all others have folded)
    private boolean shouldEndGame() {
        int activePlayers = 0;
        List<Player> players = table.getPlayers();

        for (Player player : players) {
            if (!player.isFolded() && (player.getMoney() > 0)) {
                activePlayers++;
            }
        }

        System.out.println("There are " + activePlayers + " players left\n");

        return activePlayers <= 1; //Round ends if 0 or 1 players remain
    }
}