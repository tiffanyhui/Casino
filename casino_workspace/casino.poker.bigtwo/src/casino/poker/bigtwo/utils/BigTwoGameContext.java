package casino.poker.bigtwo.utils;

import java.util.ArrayList;
import java.util.List;

import casino.deck.Card;
import casino.game.GameContext;
import casino.player.Dealer;
import casino.player.Player;
import casino.poker.bigtwo.BigTwoCard;
import casino.poker.bigtwo.player.BigTwoPlayer;
import casino.poker.utils.PokerHand;

public class BigTwoGameContext implements GameContext <BigTwoCard>{

	private List<BigTwoPlayer> players;
	private List<Player> winners;
	private int indexOfCurrentPlayer;
	private Player lastPlayerToAct;
	private List<BigTwoCard> lastPlayedCards;

	private boolean gameIsOver;
	
	private int[] scoreChanges;
	
	/**
	 * Keeps track of the current BigTwo game
	 * @param players the list of players listed in the order in which they will play. Index 0 is the player
	 * with the 3diamonds.
	 */
	public BigTwoGameContext(List<BigTwoPlayer> players){
		BigTwoPlayer.setContext(this);
		this.players = players;
	}

	@Override
	public List<BigTwoPlayer> getPlayers(){
		return players;
	}

	@Override
	public boolean isGameOver(){
		return gameIsOver;
	}
	
	/**
	 * @return the poker hand that is currently in play. returns null if called at the start of a new turn
	 */
	public PokerHand currentBigTwoHandInPlay(){
		if (lastPlayedCards != null){
			return BigTwoUtils.identifyHand(lastPlayedCards);
		}
		return null;
	}
	
	/**
	 * Returns the cards that were last played. If this is called at the start of a new turn, null is returned
	 */
	@Override
	public List<BigTwoCard> getLastPlayedCards() {
		return lastPlayedCards;
	}
	
	// TODO testing purposes only?
	public void setLastPlayedCards(List<Card> lastPlayed){
		lastPlayedCards.clear();
		for (Card card : lastPlayed){
			lastPlayedCards.add((BigTwoCard)card);
		}
	}
	
	/**
	 * @return The next player that will play. Does not activate this player, 
	 * just informs who the next player is. Consecutive calls to this method 
	 * should return the same player every time.
	 */
	public Player getNextPlayer(){
		int nextPlayerIndex = indexOfCurrentPlayer+1;
		nextPlayerIndex = nextPlayerIndex % 4;
		return players.get(nextPlayerIndex);
	}
	
	public Player getCurrentPlayer(){
		return players.get(indexOfCurrentPlayer);
	}
	
	/**
	 * Sets the last played cards and the last player to act. 
	 * Also checks to see if the game ended.
	 * Expects the <code>cardsPlayed</code> to already be out of the 
	 * <code>currentPlayer</code>'s card deck. 
	 * @param currentPlayer
	 * @param cardsPlayed
	 * @return true if the game can continue on to the next player. Should check if the 
	 * game is over though
	 */
	public boolean continuePlay(Player currentPlayer, List<BigTwoCard> cardsPlayed){
		if (!cardsPlayed.isEmpty()){
			if (lastPlayedCards == null){
				lastPlayedCards = new ArrayList<BigTwoCard>();
			} else {
				lastPlayedCards.clear();
			}
			lastPlayedCards.addAll(cardsPlayed);
			System.out.println(currentPlayer.getName() + " plays:");
			for (BigTwoCard card : lastPlayedCards){
				System.out.println(card.toString());
			}
			lastPlayerToAct = currentPlayer;
			// check if game is over with this play
			if (currentPlayer.getCards().isEmpty()){
				// Winner!
				winners = new ArrayList<Player>();
				winners.add(currentPlayer);
				gameIsOver = true;
			}
			return true;
		} 
		System.out.println(currentPlayer.getName() + " passed");
		return false;
	}
	
	public BigTwoPlayer callNext(){
		indexOfCurrentPlayer++;
		indexOfCurrentPlayer = indexOfCurrentPlayer % 4;
		BigTwoPlayer currentPlayer = players.get(indexOfCurrentPlayer);
		if (currentPlayer == lastPlayerToAct){
			// everyone passed, reset last played cards
			lastPlayedCards.clear();
		}
		return currentPlayer;
	}
	
	public Player lastPlayerToAct(){
		return lastPlayerToAct;
	}

	@Override
	public List<Player> getWinners() {
		return winners;
	}

	@Override
	public void startGame(Dealer dealer) {
		winners = null;
		int firstPlayerIndex = BigTwoUtils.identifyFirstPlayer(players);
		players = BigTwoUtils.orderByFirstPlayer(players, firstPlayerIndex);
		indexOfCurrentPlayer = - 1;
		lastPlayedCards = null; // start of the game, no one has played
		lastPlayerToAct = null;
		gameIsOver = false;
	}
	
	public enum BigTwoSortOrder {
		RANK, SUIT;
	}

	public void setEndGameScoreChanges(int[] scoreChanges){
		this.scoreChanges = scoreChanges;
	}
	
	@Override
	public int[] getEndGameScoreChanges() {
		return scoreChanges;
	}
	
}
