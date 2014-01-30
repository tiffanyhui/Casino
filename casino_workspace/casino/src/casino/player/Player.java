package casino.player;

import java.util.List;

import casino.deck.Card;
import casino.game.GameContext;


public interface Player {
	
	public String getName();
	
	public int getScore();
	
	/**
	 * Changes the player's score. 
	 * @param scoreChange If <code>scoreChange</code> < 0, reduces the player's
	 * score, if <code>scoreChange</code> > 0, increases the player's score. 
	 */
	public void changeScore(int scoreChange);
	
	public void assignCard(List<Card> cards);
	
	public void clearCards();
	
	public List<Card> getCards();
	
	@SuppressWarnings("rawtypes")
	public boolean canPlay(GameContext gameContext, List<List<Card>> possibleCardsToPlay);
}
