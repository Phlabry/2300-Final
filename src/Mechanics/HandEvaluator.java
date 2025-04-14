// Get the strength of players deck
package Mechanics;
import Model.Card;
import java.util.*;

public class HandEvaluator {

    public static String evaluateHand(List<Card> hand) {
        if (hand.size() <2 || hand.size() > 5) return "Invalid hand size";

        hand.sort(Comparator.comparing(Card::getRank));

        if (isRoyalFlush(hand)) return "Royal Flush";
        if (isStraightFlush(hand)) return "Straight Flush";
        if (isFourOfAKind(hand)) return "Four of a Kind";
        if (isFullHouse(hand)) return "Full House";
        if (isFlush(hand)) return "Flush";
        if (isStraight(hand)) return "Straight";
        if (isThreeOfAKind(hand)) return "Three of a Kind";
        if (isTwoPair(hand)) return "Two Pair";
        if (isOnePair(hand)) return "One Pair";
        return "High Card";
    }

    private static boolean isFlush(List<Card> hand) {
        Card.Suit suit = hand.get(0).getSuit();
        return hand.stream().allMatch(card -> card.getSuit() == suit);
    }

    private static boolean isStraight(List<Card> hand) {
        for (int i = 0; i < hand.size(); i++) {
            if (hand.get(i).getRank().ordinal() + 1 != hand.get(i + 1).getRank().ordinal()) {
                return false;
            }
        }
        return true;
    }

    private static boolean isRoyalFlush(List<Card> hand) {
        return isFlush(hand) && isStraight(hand) && hand.get(4).getRank() == Card.Rank.ACE;
    }

    private static boolean isStraightFlush(List<Card> hand) {
        return isFlush(hand) && isStraight(hand);
    }

    private static boolean isFourOfAKind(List<Card> hand) {
        return hasSameRank(hand, 4);
    }

    private static boolean isFullHouse(List<Card> hand) {
        return hasSameRank(hand, 3) && hasSameRank(hand, 2);
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
}
