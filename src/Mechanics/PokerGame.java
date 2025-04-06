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
            System.out.println("\n--- Starting Round " + currentRound + " ---");
            deck.resetDeck();
            deck.shuffle();
            dealCards();
            betting.startBettingRound();
            evaluateHands();
            currentRound++;
            
            if (shouldEndGame()) {
                break;
            }
        }
    }
    
    private void dealCards() {
        List<Player> players = table.getPlayers();
        for (Player player : players) {
            player.clearHand();
            player.addCard(deck.dealCard());
            player.addCard(deck.dealCard());
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
        String bestHand = "";
        
        for (Player player : players) {
            if (!player.isFolded()) {
                String handStrength = player.evaluateHand();
                if (winner == null || handStrength.compareTo(bestHand) > 0) {
                    winner = player;
                    bestHand = handStrength;
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
