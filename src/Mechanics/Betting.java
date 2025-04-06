package Mechanics;
import Model.Player;
import java.util.*;

public class Betting 
{
    private int pot;
    private int highestBet;
    private List<Player> players;
    private Scanner scanner;

    public Betting(List<Player> players, Scanner scanner) {
        this.players = players;
        this.pot = 0;
        this.highestBet = 0;
        this.scanner = scanner;
    }

    public void startBettingRound() {
    	this.highestBet = 0;
        System.out.println("\n--- Betting Round Starts ---");

        for (Player player : players) {
            if (player.isFolded()) {
                continue;
            }

            System.out.println("\n" + player.getName() + "'s turn (Money: $" + player.getMoney() + ")");
            System.out.println("Highest bet: $" + highestBet);
            System.out.println("Options: CHECK (c), CALL (a), RAISE (r), FOLD (f), ALL-IN (i)");

            String input = scanner.next();
            BettingAction action = parseInput(input);

            if (action == BettingAction.RAISE) {
                System.out.print("Enter raise amount: ");
                int raiseAmount = scanner.nextInt();
                if (player.placeBet(action, raiseAmount, highestBet)) {
                    highestBet = raiseAmount;
                    pot += raiseAmount;
                }
            }else if(action == BettingAction.ALL_IN) {
            	int allInAmount = player.getMoney();
            	if(allInAmount >= highestBet) {
            		highestBet = allInAmount;
            	}
            } else {
                if (player.placeBet(action, highestBet, highestBet)) {
                    pot += highestBet;
                }
            }
        }
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

    public int getPot() {
        return pot;
    }

    public enum BettingAction {
        CHECK,    
        CALL,   
        RAISE,    
        FOLD,   
        ALL_IN;  
    }
}
