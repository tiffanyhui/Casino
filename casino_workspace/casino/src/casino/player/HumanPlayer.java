package casino.player;

import java.util.List;

import casino.deck.Card;


public interface HumanPlayer <T extends Card> extends Player {

	public void addSelectedCards(List<T> cardToAdd);
	
	public void removeSelectedCards(List<T> cardsToRemove);
	
	public List<T> getSelectedCards();
	
	public void clearSelectedCards();
	
	/**
	 * Removes the selected cards from the player's deck
	 */
	public void playSelectedCards();
}
