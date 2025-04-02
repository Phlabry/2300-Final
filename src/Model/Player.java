// player with all their attributes: money, deck, etc.
package Model;
import java.util.*;

public class Player
{
	private String name;
	private int money;
	private List<Card> playerHand;
	
	public Player(String name, int money) {
		this.name = name;
		this.money = money;
		this.playerHand = new ArrayList<>();
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
	
	public List<Card> getHand() {
		return this.playerHand;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setMoney(int money) {
		this.money = money;
	}
	
	public String toString() {
		return name + "has $" + money + "and hand: " + playerHand;
	}
}