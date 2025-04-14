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
        while (true) {
        	boolean endRound = false;
        	
            while(!endRound) {

	            System.out.println("\n--- Starting Round " + currentRound + " ---");
	           
	            //setup before each round
	            deck.resetDeck();
	            deck.shuffle();
	            dealHoleCards();
	            showHoleCards();
	            
	            //Small Blind && Big Blind
	            
	            //Start betting -> 3 community cards
	            betting.startBettingRound();
	            for(int i=0; i<3; i++) {
		            dealCommunityCard();
	            }
	            showHoleCards();

	            
	            //Second Round of betting -> 1 more community card
	            betting.startBettingRound();
	            dealCommunityCard();
	            showHoleCards();


	            //Third Round of betting -> Last community card
	            betting.startBettingRound();
	            dealCommunityCard();
	            showHoleCards();

	            
	            //Evaluate Hand -> Next Round
	            evaluateHands();
	            currentRound++;
            }
            
            if (shouldEndGame()) {
            	endRound = true;
            }
        }
    }
    
    private void dealHoleCards() {
        List<Player> players = table.getPlayers();
        for (Player player : players) {
            player.clearHand();
            player.addCard(deck.dealCard());
            player.addCard(deck.dealCard());
        }
    }
    
    
    private void showHoleCards() {
    	List<Player> players = table.getPlayers();
    	for (Player player : players) {
    	    if (!player.isFolded()) {
    	        System.out.println(player.getName() + "'s hand:");
    	        for (Card card : player.getHand().getCards()) {
    	            System.out.println("  " + card); // relies on Card.toString()
    	        }
    	        System.out.println(); // blank line between players
    	    } else {
    	        System.out.println(player.getName() + " has folded.");
    	    }
        }
    }
    
    
    private void dealCommunityCard() {
    	Card communityCard = deck.dealCard();
        List<Player> players = table.getPlayers();
        for (Player player : players) {
            player.addCard(communityCard);
        }
    }
    
    
    private void evaluateHands() {
        List<Player> players = table.getPlayers();
        for (Player player : players) {
            if (!player.isFolded()) {
                System.out.println(player.getName() + " evaluates hand: " + player.evaluateHand());
            }
        }
        Player winner = determineWinner(players);
        System.out.println("Winner: " + winner.getName());
        
        winner.setMoney(winner.getMoney() + betting.getPot());
    }
    
    private Player determineWinner(List<Player> players) {
        Player winner = null;
        int bestHand = 0;
        
        for (Player player : players) {
            if (!player.isFolded()) {
                String handStrength = player.evaluateHand();
                int cardValue = player.handValue(handStrength);
                if (winner == null || Integer.compare(cardValue, bestHand) > 0) {
                    winner = player;
                    bestHand = cardValue;
                }
            }
        }
        
        return winner;
    }
    
    private boolean shouldEndGame() {
        long activePlayers = table.getPlayers().stream().filter(p -> p.getMoney() > 0).count();
        return activePlayers <= 1;
    }

}
