package Mechanics;
import Model.Player;
import java.util.*;

public class Betting 
{
    private List<Pot> pots;
    private int totalPotAmount;
    private int highestBet;
    private int baseBet;
    private Map<Player, Integer> playerBets;
    private List<Player> players;
    private Scanner scanner;

    public Betting(List<Player> players, Scanner scanner) {
        this.pots = new ArrayList<>();
        this.totalPotAmount = 0;
        this.highestBet = 0;
        this.baseBet = 0;
        this.playerBets = new HashMap<>();
        this.players = players;
        this.scanner = scanner;
    }

    public void startBettingRound() {
        this.highestBet = this.baseBet;
        this.playerBets.clear();
        nextBettingRound();
    }
    
    public void nextBettingRound() {
    	//for loop that skips folded players or players that do not have money
        for (Player player : players) {
            if (player.isFolded()) continue;
            if (player.getMoney() == 0 && highestBet > 0) {
                System.out.println(player.getName() + " is out of money and cannot continue in this betting round.");
                continue;
            }
            
            boolean validActionTaken = false;

            System.out.println("\nTotal Pot: $" + totalPotAmount);

            while (!validActionTaken) {
	            System.out.println("\n" + player.getName() + "'s turn (Money: $" + player.getMoney() + ")");
	            System.out.println("Highest bet: $" + highestBet);
	            System.out.println("Options: CHECK (c), CALL (a), RAISE (r), FOLD (f), ALL-IN (i)");
	
	            String input = scanner.next();
	            BettingAction action = parseInput(input);
	
	            if (action == BettingAction.RAISE) {
	                System.out.print("Enter raise amount: ");
	                int raiseAmount = scanner.nextInt();
	                if (player.placeBet(action, raiseAmount, highestBet, baseBet)) {
	                	int added = raiseAmount - highestBet;
	                	updateBets(player, added);
	                    highestBet = raiseAmount;
	                    validActionTaken = true;
	                }
	                
	            }else if(action == BettingAction.ALL_IN) {
	            	int allInAmount = player.getMoney();
	            	if(player.placeBet(action, 0, highestBet, baseBet)) {
	                    if (allInAmount >= highestBet) {
	                        highestBet = allInAmount;
	                    }
	                    updateBets(player, allInAmount);
	                    validActionTaken = true;
	            	}
	            } else {
	            	//For Check or Call (Check if the highestBet == baseBet)
	                if (action == BettingAction.CHECK && highestBet == baseBet) {
	                    System.out.println(player.getName() + " checks.");
	                    validActionTaken = true;
	                } else if (player.placeBet(action, highestBet, highestBet, baseBet)) {
	                    updateBets(player, highestBet);
	                    validActionTaken = true;
	                }
	            }
            }
        }
        generatePots(); //Create main & side pots after betting
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
        Set<Player> mainPotContenders = new HashSet<>();
        int mainPotAmount = 0;

        
    	for (Map.Entry<Player, Integer> entry : playerBets.entrySet()) {
            Player player = entry.getKey();
            int bet = entry.getValue();

            if (!player.isFolded() && bet >= highestBet) {
                mainPotContenders.add(player);
                mainPotAmount += highestBet;  //The main pot only gets contributions up to the highest bet
            }
        }

        //Add the main pot
        if (mainPotAmount > 0) {
            pots.add(new Pot(mainPotAmount, mainPotContenders));
        }

        //Handle side pots for all-in players
        for (Map.Entry<Player, Integer> entry : playerBets.entrySet()) {
            Player player = entry.getKey();
            int bet = entry.getValue();

            if (!player.isFolded() && bet < highestBet) {
                //This player is all-in, so we need to create a side pot for them
                Set<Player> sidePotContenders = new HashSet<>();
                int sidePotAmount = 0;

                for (Map.Entry<Player, Integer> sideEntry : playerBets.entrySet()) {
                    Player sidePlayer = sideEntry.getKey();
                    int sideBet = sideEntry.getValue();

                    if (!sidePlayer.isFolded() && sideBet >= bet) {
                        sidePotContenders.add(sidePlayer);
                        sidePotAmount += sideBet - bet;  // The difference goes into the side pot
                    }
                }

                if (sidePotAmount > 0) {
                    System.out.println("Side pot created for $" + sidePotAmount);
                    pots.add(new Pot(sidePotAmount, sidePotContenders));
                }
            }
        }

        System.out.println("Pots created: " + pots.size());
    }

    private BettingAction parseInput(String input) {
        return switch (input.toLowerCase()) {
            case "c" -> BettingAction.CHECK;
            case "a" -> BettingAction.CALL;
            case "r" -> BettingAction.RAISE;
            case "f" -> BettingAction.FOLD;
            case "i" -> BettingAction.ALL_IN;
            default -> throw new IllegalArgumentException("Invalid action.");
        };
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
