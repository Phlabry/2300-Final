// player with all their attributes: money, deck, etc.
package Model;
import Mechanics.HandEvaluator;
import Mechanics.Betting.BettingAction;

public class Player
{
	private String name;
	private int money;
	private Hand hand;
	private boolean folded;
	
	public Player(String name, int money) {
		this.name = name;
		this.money = money;
		this.hand = new Hand();
		this.folded = false;
	}
	
	public void addCard(Card card) {
		hand.addCard(card);
	}
	
	public void clearHand() {
	    hand.clearHand();
	}

	public String evaluateHand() {
	    return HandEvaluator.evaluateHand(hand.getCards());
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
	
	public Hand getHand() {
		return this.hand;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setMoney(int money) {
		this.money = money;
	}
	
	public void setFolded(boolean status) {
        this.folded = status;
    }
    
    public boolean placeBet(BettingAction action, int amount, int highestBet) {
        if (folded) {
            System.out.println(this.name + " has already folded.");
            return false;
        }

        switch (action) {
            case CHECK:
                if (highestBet == 0) {
                    System.out.println(this.name + " checks.");
                    return true;
                } else {
                    System.out.println(this.name + " cannot check, must call or fold.");
                    return false;
                }

            case CALL:
                int callAmount = highestBet;
                if (money >= callAmount) {
                    money -= callAmount;
                    System.out.println(this.name + " calls with $" + callAmount);
                    return true;
                } else {
                    System.out.println(this.name + " does not have enough to call.");
                    return false;
                }

            case RAISE:
                if (amount > highestBet) {
                    int raiseAmount = amount - highestBet;
                    if (this.money >= raiseAmount) {
                    	this.money -= raiseAmount;
                        System.out.println(this.name + " raises to $" + amount);
                        return true;
                    } else {
                        System.out.println(this.name + " does not have enough to raise.");
                        return false;
                    }
                } else {
                    System.out.println(this.name + " must raise above the highest bet.");
                    return false;
                }

            case FOLD:
                folded = true;
                System.out.println(this.name + " folds.");
                return true;

            case ALL_IN:
                System.out.println(this.name + " goes all-in with $" + this.money);
                this.money = 0;
                	
                return true;

            default:
                return false;
        }
    }
	
    public static int handValue(String handDescription) {
        switch (handDescription) {
            case "Royal Flush":
                return 10;
            case "Straight Flush":
                return 9;
            case "Four of a Kind":
                return 8;
            case "Full House":
                return 7;
            case "Flush":
                return 6;
            case "Straight":
                return 5;
            case "Three of a Kind":
                return 4;
            case "Two Pair":
                return 3;
            case "One Pair":
                return 2;
            default:
                return 1;  // High Card
        }
    }
    
	public String toString() {
		return name + " has $" + money + " and hand: " + hand;
	}
}