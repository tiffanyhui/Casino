package casino.poker.bigtwo;

import casino.deck.Card;
import casino.poker.deck.PokerCard;

public class BigTwoCard extends PokerCard {

	public BigTwoCard(int rank, Suit suit) {
		super(rank, suit);
	}

	/**
	 * Compares card values. First by rank (2> A > K > .. > 3)
	 * then by suit (S > H > C > D).
	 * a negative integer, zero, or a positive integer as this 
	 * object is less than, equal to, or greater than the 
	 * specified object.
	 */
	@Override
	public int compareTo(Card otherCard, boolean compareSuit) {
		int rank1 = rank;
		int rank2 = otherCard.getRank();
		if (rank1 == rank2){
			if (compareSuit){
				return suit.compareTo(otherCard.getSuit());
			}
			return 0;
		}
			
		if (rank == 1) // 2's are highest
			return 1; 
		if (rank2 == 1)
			return -1;
		return super.compareTo(otherCard, compareSuit);
	}
	
}
