package Mechanics;
import Model.Card;

public class HandStrength {
    private String handValue; //The hand strength (numeric value like 10 for Royal Flush, etc.)
    private Card.Rank highCard;  //High card value for tie-breaking

    public HandStrength(String handValue, Card.Rank highCard) {
        this.handValue = handValue;
        this.highCard = highCard;
    }
    
    public String getHandValue() {
        return handValue;
    }

    public Card.Rank getHighCard() {
        return highCard;
    }

    @Override
    public String toString() {
        return handValue + " (High card: " + highCard + ")";
    }
}