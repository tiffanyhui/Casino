package casino.player;

import java.util.List;

import casino.deck.Card;
import casino.game.GameContext;


public interface ComputerPlayer extends Player {
	/**
	 * Depending on the game, outputs the cards the player wants to play. Cards to be 
	 * played should be removed from player's deck after this method is called. 
	 * @param possibleHands all the possible hands that the player can play with to complete their turn
	 * @return list of cards to play for this player's turn. Returns empty
	 * list if player does not play any cards. Returns null if player optionally passes
	 * their turn.
	 */
	@SuppressWarnings("rawtypes")
	public List<Card> play(GameContext gameContext, List<List<Card>> possibleHands);
	

}
