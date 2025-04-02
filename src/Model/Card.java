// implements one card
package Model;

public class Card
{
	private final Suit suit;
	private final Rank rank;
	
	public Card(Rank rank, Suit suit) {
		this.rank = rank;
		this.suit = suit;
	}
	
	public Rank getRank() {
		return this.rank;
	}
	
	public Suit getSuit() {
		return this.suit;
	}
	
	public String toString() {
		return this.rank + "of" + this.suit;
	}
	
	public enum Suit {
        HEARTS, DIAMONDS, CLUBS, SPADES;
    }

    public enum Rank {
        TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, 
        JACK, QUEEN, KING, ACE;
    }
}