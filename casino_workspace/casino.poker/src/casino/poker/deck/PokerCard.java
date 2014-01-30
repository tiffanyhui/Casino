package casino.poker.deck;

import java.util.Comparator;

import casino.deck.Card;

public class PokerCard extends Card {

	private static PokerSuitSorter suitSorter;
	
	public PokerCard(int rank, Suit suit) {
		super(rank, suit);
	}
	
	/**
	 * Compares card values. First by rank (A > K > .. > 2)
	 * then by suit (S > H > C > D).
	 * a negative integer, zero, or a positive integer as this 
	 * object is less than, equal to, or greater than the 
	 * specified object.
	 */
	@Override
	public int compareTo(Card otherCard, boolean compareSuit) {
		int rank1 = rank;
		int rank2 = otherCard.getRank();
		
		if (compareSuit){
			if (rank1 == rank2){
				return suit.compareTo(otherCard.getSuit());
			}
		}
		if (rank1 == 0) // aces are highest
			return 1;
		if (rank2 == 0)
			return -1;
		return super.compareTo(otherCard, compareSuit); // compare basic ranks
	}
	
	public static PokerRankSorter getRankSorter(){
		if (rankSorter == null){
			rankSorter = new PokerRankSorter();
		}
		return (PokerRankSorter)rankSorter;
			
	}
	
	public static PokerSuitSorter getSuitSorter(){
		if (suitSorter == null)
			suitSorter = new PokerSuitSorter();
		return suitSorter;
	}
	
	public static class PokerRankSorter extends Card.RankSorter implements Comparator<Card>{

		@Override
		public int compare(Card card1, Card card2) {
			int result = super.compare(card1, card2);
			if (result == 0){
				result = getSuitSorter().compare(card1, card2);
			}
			return result;
		}
		
	}
	
	public static class PokerSuitSorter implements Comparator<Card>{
		@Override
		public int compare(Card o1, Card o2) {
			Card.Suit suit1 = o1.getSuit();
			Card.Suit suit2 = o2.getSuit();
			int result = suit1.compareTo(suit2);
			if (result == 0)
				return PokerCard.getRankSorter().compare(o1, o2);
			return result;
		}
	}
	
}
