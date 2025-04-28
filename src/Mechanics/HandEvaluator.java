// Get the strength of players deck
package Mechanics;
import Model.Card;
import java.util.*;

public class HandEvaluator {

	public static HandStrength evaluateHandWithMax(List<Card> hand) {
	    if (hand.size() < 2 || hand.size() > 7) return new HandStrength("Invalid", Card.Rank.TWO);

	    hand.sort(Comparator.comparing(Card::getRank));

	    // Check hand types in order of strength
	    if (isRoyalFlush(hand)) return new HandStrength("Royal Flush", Card.Rank.ACE);
	    if (isStraightFlush(hand)) return new HandStrength("Straight Flush", getHighestInStraight(hand));
	    if (isFourOfAKind(hand)) return new HandStrength("Four of a Kind", getNOfAKindRank(hand, 4));
	    if (isFullHouse(hand)) return new HandStrength("Full House", getNOfAKindRank(hand, 3));
	    if (isFlush(hand)) return new HandStrength("Flush", getHighestOfSuit(hand));
	    if (isStraight(hand)) return new HandStrength("Straight", getHighestInStraight(hand));
	    if (isThreeOfAKind(hand)) return new HandStrength("Three of a Kind", getNOfAKindRank(hand, 3));
	    if (isTwoPair(hand)) return new HandStrength("Two Pair", getHighestPair(hand));
	    if (isOnePair(hand)) return new HandStrength("One Pair", getNOfAKindRank(hand, 2));

	    // High Card
	    Card highest = Collections.max(hand, Comparator.comparing(Card::getRank));
	    return new HandStrength("High Card", highest.getRank());
	}

    private static boolean isFlush(List<Card> hand) {
    	//Creates a hashMap of the Suits
    	Map<Card.Suit, Integer> suitCounts = new HashMap<>();
        
    	//For every card in hand, get the suit and counts it (0 if new or add 1 if seen before)
        for (Card card : hand) {
            Card.Suit suit = card.getSuit();
            suitCounts.put(suit, suitCounts.getOrDefault(suit, 0) + 1);
        }

        //Checking all the suit counts, if more than or equal to 5, then return true
        for (int count : suitCounts.values()) {
            if (count >= 5) return true;
        }

        return false;
    }


    private static boolean isStraight(List<Card> hand) {
    	//Creates a HashSet of the cards which collects all unique card ranks (set ignores duplicates)
    	Set<Integer> uniqueRanks = new HashSet<>();
        
    	//For every card in hand, get the rank and add the rank to the HashSet
        for (Card card : hand) {
            int rankValue = card.getRank().ordinal();
            uniqueRanks.add(rankValue);
            
            //Handle Ace being lowest card as well
            if (card.getRank() == Card.Rank.ACE) {
                uniqueRanks.add(0); //Using 0 for Ace-low straight
            }
        }

        //Converts the set of unique ranks to a list in ascending order
        List<Integer> sorted = new ArrayList<>(uniqueRanks);
        Collections.sort(sorted);

        //Counts the consective cards, 5 consective cards -> return true, else reset count
        int consecutive = 0;
        for (int i = 0; i < sorted.size(); i++) {
            if (i > 0 && sorted.get(i) == sorted.get(i - 1) + 1) {
                consecutive++;
            } else {
                consecutive = 1;
            }

            if (consecutive >= 5) {
                return true;
            }
        }

        return false;
    }

    private static boolean isRoyalFlush(List<Card> hand) {
    	//Checks if it is a straight flush as well as if the last card is an ace
    	hand.sort(Comparator.comparing(Card::getRank));  //Sort by rank
    	return isStraightFlush(hand) && hand.get(hand.size() - 1).getRank() == Card.Rank.ACE;
    }

    private static boolean isStraightFlush(List<Card> hand) {
    	//Creates a hashMap that has the suit and the list of cards that are in that suit
        Map<Card.Suit, List<Card>> suitGroups = new HashMap<>();
        
    	//For every card in hand, get the Suit, if not suitGroup doesn't have that suit create an Arraylist and add card to group
        for (Card card : hand) {
        	Card.Suit suit = card.getSuit();
        	if (!suitGroups.containsKey(suit)) {
        	    suitGroups.put(suit, new ArrayList<>());
        	}
        	suitGroups.get(suit).add(card); 

        }

        //Check each suit group with 5+ cards for a straight
        for (List<Card> suitedCards : suitGroups.values()) {
            if (suitedCards.size() >= 5) {
                if (isStraight(suitedCards)) {
                    return true;
                }
            }
        }

        return false;
    }

    private static boolean isFourOfAKind(List<Card> hand) {
        return hasSameRank(hand, 4);
    }

    private static boolean isFullHouse(List<Card> hand) {
    	int count = 1;
        Card.Rank threeRank = null;
        Card.Rank pairRank = null;

        for (int i = 1; i < hand.size(); i++) {
            if (hand.get(i).getRank() == hand.get(i - 1).getRank()) {
                count++;
            } else {
                if (count == 3 && threeRank == null) {
                    threeRank = hand.get(i - 1).getRank();
                } else if (count == 2) {
                    pairRank = hand.get(i - 1).getRank();
                }
                count = 1; //Reset the count for the next rank
            }
        }

        //Check the last rank group
        if (count == 3 && threeRank == null) {
            threeRank = hand.get(hand.size() - 1).getRank();
        } else if (count == 2) {
            pairRank = hand.get(hand.size() - 1).getRank();
        }

        //Ensure we have both a three-of-a-kind and a distinct pair
        return threeRank != null && pairRank != null && !threeRank.equals(pairRank);
    }

    private static boolean isThreeOfAKind(List<Card> hand) {
        return hasSameRank(hand, 3);
    }

    private static boolean isTwoPair(List<Card> hand) {
        return countPairs(hand) == 2;
    }

    private static boolean isOnePair(List<Card> hand) {
        return countPairs(hand) == 1;
    }

    private static boolean hasSameRank(List<Card> hand, int count) {
        Map<Card.Rank, Integer> rankCounts = countRanks(hand);
        return rankCounts.containsValue(count);
    }

    private static int countPairs(List<Card> hand) {
        return (int) countRanks(hand).values().stream().filter(v -> v == 2).count();
    }

    private static Map<Card.Rank, Integer> countRanks(List<Card> hand) {
        Map<Card.Rank, Integer> rankCounts = new HashMap<>();
        for (Card card : hand) {
            rankCounts.put(card.getRank(), rankCounts.getOrDefault(card.getRank(), 0) + 1);
        }
        return rankCounts;
    }
    
    private static Card.Rank getNOfAKindRank(List<Card> hand, int n) {
        Map<Card.Rank, Integer> counts = countRanks(hand);
        return counts.entrySet().stream()
            .filter(e -> e.getValue() == n)
            .map(Map.Entry::getKey)
            .max(Comparator.naturalOrder())
            .orElse(Card.Rank.TWO);
    }

    private static Card.Rank getHighestPair(List<Card> hand) {
        Map<Card.Rank, Integer> counts = countRanks(hand);
        return counts.entrySet().stream()
            .filter(e -> e.getValue() >= 2)
            .map(Map.Entry::getKey)
            .max(Comparator.naturalOrder())
            .orElse(Card.Rank.TWO);
    }

    private static Card.Rank getHighestOfSuit(List<Card> hand) {
        Map<Card.Suit, List<Card>> suits = new HashMap<>();
        for (Card card : hand) {
            suits.computeIfAbsent(card.getSuit(), k -> new ArrayList<>()).add(card);
        }

        return suits.values().stream()
            .filter(list -> list.size() >= 5)
            .flatMap(List::stream)
            .map(Card::getRank)
            .max(Comparator.naturalOrder())
            .orElse(Card.Rank.TWO);
    }

    private static Card.Rank getHighestInStraight(List<Card> hand) {
        Set<Integer> uniqueRanks = new HashSet<>();
        for (Card card : hand) {
            uniqueRanks.add(card.getRank().ordinal());
            if (card.getRank() == Card.Rank.ACE) uniqueRanks.add(-1);  // Ace low
        }

        List<Integer> sorted = new ArrayList<>(uniqueRanks);
        Collections.sort(sorted);

        int count = 1;
        int bestHigh = sorted.get(0);
        for (int i = 1; i < sorted.size(); i++) {
            if (sorted.get(i) == sorted.get(i - 1) + 1) {
                count++;
                if (count >= 5) {
                    bestHigh = sorted.get(i);
                }
            } else {
                count = 1;
            }
        }

        return bestHigh == -1 ? Card.Rank.FIVE : Card.Rank.values()[bestHigh];
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
}
