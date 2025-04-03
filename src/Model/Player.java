// player with all their attributes: money, deck, etc.
package Model;
import java.util.*;
import Mechanics.HandEvaluator;
import Mechanics.Betting.BettingAction;

public class Player
{
	private String name;
	private int money;
	private List<Card> playerHand;
	private boolean folded;
	
	public Player(String name, int money) {
		this.name = name;
		this.money = money;
		this.playerHand = new ArrayList<>();
		this.folded = false;
	}
	
	public void addCard(Card card) {
		playerHand.add(card);
	}
	
	public void clearHand() {
		playerHand.clear();
	}
	
	public String getName() {
		return this.name;
	}
	
	public int getMoney() {
		return this.money;
	}
	
    public boolean isFolded() {
        return folded;
    }
	
	public List<Card> getHand() {
		return this.playerHand;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setMoney(int money) {
		this.money = money;
	}
	

    public String evaluateHand() {
        return HandEvaluator.evaluateHand(playerHand);
    }
    
    public boolean placeBet(BettingAction action, int amount, int highestBet) {
        if (folded) {
            System.out.println(name + " has already folded.");
            return false;
        }

        switch (action) {
            case CHECK:
                if (highestBet == 0) {
                    System.out.println(name + " checks.");
                    return true;
                } else {
                    System.out.println(name + " cannot check, must call or fold.");
                    return false;
                }

            case CALL:
                int callAmount = highestBet;
                if (money >= callAmount) {
                    money -= callAmount;
                    System.out.println(name + " calls with $" + callAmount);
                    return true;
                } else {
                    System.out.println(name + " does not have enough to call.");
                    return false;
                }

            case RAISE:
                if (amount > highestBet) {
                    int raiseAmount = amount - highestBet;
                    if (money >= raiseAmount) {
                        money -= raiseAmount;
                        System.out.println(name + " raises to $" + amount);
                        return true;
                    } else {
                        System.out.println(name + " does not have enough to raise.");
                        return false;
                    }
                } else {
                    System.out.println(name + " must raise above the highest bet.");
                    return false;
                }

            case FOLD:
                folded = true;
                System.out.println(name + " folds.");
                return true;

            case ALL_IN:
                System.out.println(name + " goes all-in with $" + money);
                money = 0;
                return true;

            default:
                return false;
        }
    }
	
	public String toString() {
		return name + "has $" + money + "and hand: " + playerHand;
	}
}