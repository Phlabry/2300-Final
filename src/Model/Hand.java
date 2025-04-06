// represents current hand of the player
package Model;
import java.util.*;

public class Hand
{
	private List<Card> hand;
	
	public Hand() {
		this.hand = new ArrayList<>();
	}
	
	public void addCard(Card card) {
		hand.add(card);
	}
	
	public void clearHand() {
		hand.clear();
	}
	
	public void sort() {
		hand.sort(Comparator.comparing(Card::getRank));
	}
	
	
	public List<Card> getCards() {
	    return hand;
	}
	
	public String toString() {
		return hand.toString();
	}
}