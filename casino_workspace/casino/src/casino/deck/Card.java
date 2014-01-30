package casino.deck;

import java.util.Comparator;

public class Card {

	protected static RankSorter rankSorter;
	
	protected int rank; // index value of card (off by 1, Ace == 0)
	protected Suit suit;
	
	public Card(int rank, Suit suit){
		this.rank = rank;
		this.suit = suit;
	}
	
	public int getRank(){
		return rank;
	}
	
	
	public Suit getSuit(){
		return suit;
	}
	
	public static RankSorter getRankSorter(){
		if (rankSorter == null){
			rankSorter = new RankSorter();
		}
		return rankSorter;
	}
	
	/**
	 * Compares two cards. Only by card rank, ignores suit.
	 * Compares card values. Ace is lowest, King is highest. 
	 */
	public int compareTo(Card otherCard, boolean compareSuit) {
		if (rank == otherCard.rank)
			return 0;
		return rank > otherCard.rank ? 1 : -1; 
	}
	
	public String toString(){
		String str_value = String.valueOf(rank+1); // so display actual value, not index value
		if (rank == 10){
			str_value = "Jack";
		} else if (rank == 11){
			str_value = "Queen";
		} else if (rank == 12){
			str_value = "King";
		} else if (rank == 0){
			str_value = "Ace";
		}
		return str_value + " " + suit.toString();
	}
	
	public static class RankSorter implements Comparator<Card>{
		@Override
		public int compare(Card o1, Card o2) {
			return o1.compareTo(o2, false);
		}
	}
	
	
	public static enum Suit{
		DIAMOND, CLUB, HEART, SPADE;
	}


	@Override
	public boolean equals(Object arg0) {
		if (arg0 instanceof Card){
			return suit == ((Card)arg0).getSuit() && rank == ((Card)arg0).getRank();
		}
		return false;
	}

	@Override
	public int hashCode() {
		return rank * suit.hashCode();
	}
}
