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
    private boolean hasActed;
    private boolean isHuman;
	
	private String HandValue;       
    private Card.Rank highCard;
	
	public Player(String name, int money, boolean isHuman) {
		this.name = name;
		this.money = money;
		this.hand = new Hand();
		this.folded = false;
		this.hasActed = false;
        this.isHuman = isHuman;
	}
	
	public boolean isHuman() {
        return isHuman;
    }
	
	public void addCard(Card card) {
		hand.addCard(card);
	}
	

	public Hand getHand() {
		return this.hand;
	}
	public void clearHand() {
	    hand.clearHand();
	}
	
	
	public String getHandValue() {
        return HandValue;
    }
	
    public void setHandValue(String HandValue) {
        this.HandValue = HandValue;
    }

    
    public Card.Rank getHighCard() {
        return highCard;
    }
    public void setHighCard(Card.Rank highCard) {
        this.highCard = highCard;
    }
	
    
	public String getName() {
		return this.name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
	public int getMoney() {
		return this.money;
	}
	public void setMoney(int money) {
		this.money = money;
	}
	
	
    public boolean isFolded() {
        return folded;
    }
    public void setFolded(boolean status) {
        this.folded = status;
    }
    
    
    public boolean hasActed() {
        return hasActed;
    }
    public void setHasActed(boolean hasActed) {
        this.hasActed = hasActed;
    }
    
    
    public boolean placeBet(BettingAction action, int amount, int highestBet, int baseBet) {
        if (folded) {
            System.out.println(this.name + " has already folded.");
            return false;
        }

        switch (action) {
            case CHECK:
                if (highestBet == baseBet) {
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
    
	public String toString() {
		return name + " has $" + money + " and hand: " + hand;
	}
}