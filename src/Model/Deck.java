// implements the deck
package Model;
import java.util.*;
import java.util.Collections;


public class Deck 
{
	private  List<Card> deck;
	private static final int DECK_LENGTH = 52;
	private int topCardIndex;
	
	public Deck() {
		deck = new ArrayList<>(DECK_LENGTH);
		initializeDeck();
		shuffle();
	}
	
	public void initializeDeck() {
		deck.clear();
		for(Card.Suit suit : Card.Suit.values()) {
			for(Card.Rank rank : Card.Rank.values()) {
				deck.add(new Card(rank,suit));
			}
		}
		
		topCardIndex = 0;
	}
	
	public void shuffle() {
		Collections.shuffle(deck);
		topCardIndex = 0;
	}
	
	public Card dealCard() {
		if(topCardIndex < deck.size()) {
			return deck.get(topCardIndex++);
		} else {
			throw new IllegalStateException("Out of Cards");
		}
	}
	
	public void resetDeck() {
		initializeDeck();
		shuffle();
	}
	
	public void displayDeck() {
		for(Card card : deck) {
			System.out.println(card.toString());
		}
	}
}