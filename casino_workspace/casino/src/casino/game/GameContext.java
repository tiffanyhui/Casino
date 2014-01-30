package casino.game;

import java.util.List;

import casino.player.Dealer;
import casino.player.Player;

public interface GameContext <T>{

	/**
	 * Handles anything that needs to happen when a game starts. 
	 * Should at least reset the winners from previous game.
	 */
	public void startGame(Dealer dealer);
	
	/**
	 * 
	 * @return whether or not the game is over
	 */
	public boolean isGameOver();
	
	
	/**
	 * 
	 * @return an integer array indexed by the players in <code? getPlayers() </code>
	 * indicating how much each player's score changed in the past game
	 */
	public int[] getEndGameScoreChanges();
	
	
	/**
	 * @return list of players in the game
	 */
	public List<? extends Player> getPlayers();
	
	/**
	 * @return the cards that were played last. 
	 * returns null if this is queried at the start of a new round
	 */
	public List<T> getLastPlayedCards();
	
	/**
	 * Returns the winners if the game has just ended. Returns null if a game
	 * is in progress. If there are no winners, returns an empty list.
	 * @return 
	 */
	public List<? extends Player> getWinners();
}
