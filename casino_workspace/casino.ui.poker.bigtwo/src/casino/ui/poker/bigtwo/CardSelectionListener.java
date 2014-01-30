package casino.ui.poker.bigtwo;

import casino.deck.Card;

public interface CardSelectionListener {

	/**
	 * Notification that a card was selected in the BigTwoCardsComposite
	 * @param card
	 * @param selected 
	 */
	public void cardSelected(Card card, boolean selected);
	
}
