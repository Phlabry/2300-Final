package Mechanics;
import Model.Card;

public class HandStrength {
    private String handValue; //The hand strength (numeric value like 10 for Royal Flush, etc.)
    private Card.Rank highestCard;  //Highest card value for tie-breaking

    public HandStrength(String handValue, Card.Rank highestCard) {
        this.handValue = handValue;
        this.highestCard = highestCard;
    }
}