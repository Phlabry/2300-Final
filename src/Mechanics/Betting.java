package Mechanics;
import Model.*;
import java.util.*;

public class Betting 
{
    private List<Pot> pots;
    private int totalPotAmount;
    private int highestBet;
    private int baseBet;
    private Map<Player, Integer> playerBets;
    private List<Player> players;
    private Set<Player> playersActedThisRound;


    public Betting(List<Player> players) {
        this.pots = new ArrayList<>();
        this.totalPotAmount = 0;
        this.highestBet = 0;
        this.baseBet = 0;
        this.playerBets = new HashMap<>();
        this.players = players;
        this.playersActedThisRound = new HashSet<>();
    }

    public void startBettingRound() {
        this.highestBet = this.baseBet;
        this.playerBets.clear();
        this.playersActedThisRound.clear();
        nextBettingRound();
    }
    
    public void nextBettingRound() {
    	//for loop that skips folded players or players that do not have money
        for (Player player : players) {
            if (player.isFolded()) continue;
            if (player.getMoney() == 0 && highestBet > 0) {
                System.out.println(player.getName() + " is out of money and cannot continue in this betting round. \n");
                continue;
            }
        }
        System.out.println("Total Pot: $" + totalPotAmount);

        //Check if only one active player remains (others folded)
        List<Player> activePlayers = new ArrayList<>();
        for (Player player : players) {
            if (!player.isFolded()) {
                activePlayers.add(player);
            }
        }

        if (activePlayers.size() == 1) {
            //Only one player left, auto-win
            Player winner = activePlayers.get(0);
            System.out.println(winner.getName() + " wins the pot by default since everyone else folded.");
            winner.setMoney(totalPotAmount + winner.getMoney());  //Add entire pot to winner
            pots.clear();  //Clear pots as the round is over
        } else {
            //Continue with pot generation if more than one player is still active
            generatePots(); //Create main & side pots after betting
        }    
    }

    
    public boolean isBettingRoundComplete() {
        for (Player player : players) {
            if (player.isFolded()) continue;

            // If player hasn't acted this round, round is not complete
            if (!playersActedThisRound.contains(player)) {
                return false;
            }

            int playerBet = playerBets.getOrDefault(player, 0);
            int playerMoney = player.getMoney();

            // If they are not all-in, they must match the highest bet
            if (playerMoney > 0 && playerBet < highestBet) {
                return false;
            }
        }
        return true;
    }

    public void createInitialPot(int smallBlindAmount, int bigBlindAmount, int leftoverRollover) {
        //Update the blind amount in the Betting class
    	this.baseBet = bigBlindAmount;
    	this.totalPotAmount = 0;

        //Calculate total pot with small and big blinds
        int totalPot = smallBlindAmount + bigBlindAmount + leftoverRollover;
        
        //Update the TotalPotAmount
        this.totalPotAmount += totalPot;
        
        //Add the pot to the pots list
        Pot initialPot = new Pot(totalPot, new HashSet<>(players));
        pots.add(initialPot);

        System.out.println("Initial Pot Created with $" + totalPot + "\n");
    }

    
    private void updateBets(Player player, int amount) {
        int current = playerBets.getOrDefault(player, 0);
        playerBets.put(player, current + amount);
        System.out.println(player.getName() + " has bet $" + (current + amount) + " (total)");

        //Update the total pot amount every time a bet is placed
        totalPotAmount += amount;
    }

    private void generatePots() {
    	//Create main pot with the current highest bet
    	Map<Player, Integer> remainingBets = new HashMap<>(playerBets);
        List<Player> activePlayers = new ArrayList<>();
        for (Player player : players) {
            if (!player.isFolded()) {
                activePlayers.add(player);
            }
        }

        
        while (true) {
            //Find the smallest bet > 0
            int minBet = Integer.MAX_VALUE;
            for (Player player : activePlayers) {
                int bet = remainingBets.getOrDefault(player, 0);
                if (bet > 0 && bet < minBet) {
                    minBet = bet;
                }
            }

            if (minBet == Integer.MAX_VALUE || minBet == 0) {
                break; //No more bets left
            }

            //Create a pot with all players who have bet > 0
            Set<Player> potContenders = new HashSet<>();
            int potAmount = 0;

            for (Player player : activePlayers) {
                int bet = remainingBets.getOrDefault(player, 0);
                if (bet > 0) {
                    potContenders.add(player);
                    potAmount += minBet;
                    remainingBets.put(player, bet - minBet);
                }
            }

            pots.add(new Pot(potAmount, potContenders));
            System.out.println("Pot created: $" + potAmount + " with " + potContenders.size() + " players.");
        }

        System.out.println("Total pots created: " + pots.size() + "\n");    
        playerBets.clear(); //Reset for next betting round
    }

    public void handleCheck(Player player) {
        if (highestBet == baseBet) {
            System.out.println(player.getName() + " checks.");
            playersActedThisRound.add(player);
        } else {
            System.out.println("Check not allowed unless the bet is equal to the base bet.");
        }
    }

    public void handleCall(Player player) {
        if (player.getMoney() >= highestBet) {
            updateBets(player, highestBet);
            System.out.println(player.getName() + " calls with $" + highestBet);
            playersActedThisRound.add(player);
        } else {
            System.out.println("Insufficient funds to call.");
        }
    }

    public void handleRaise(Player player, int raiseAmount) {
        if (raiseAmount > highestBet) {
            highestBet = raiseAmount;
            updateBets(player, raiseAmount - highestBet);
            System.out.println(player.getName() + " raises by $" + (raiseAmount - highestBet));
            playersActedThisRound.add(player);
        } else {
            System.out.println("Raise amount must be greater than the current highest bet.");
        }
    }

    public void handleFold(Player player) {
        player.setFolded(true);
        System.out.println(player.getName() + " folds.");
        playersActedThisRound.add(player);
    }

    public void handleAllIn(Player player) {
        int allInAmount = player.getMoney();
        highestBet = Math.max(highestBet, allInAmount);
        updateBets(player, allInAmount);
        System.out.println(player.getName() + " goes all-in with $" + allInAmount);
        playersActedThisRound.add(player);
    }

    public List<Pot> getPots() {
        return pots;
    }


    public enum BettingAction {
        CHECK,    
        CALL,   
        RAISE,    
        FOLD,   
        ALL_IN;  
    }
}
