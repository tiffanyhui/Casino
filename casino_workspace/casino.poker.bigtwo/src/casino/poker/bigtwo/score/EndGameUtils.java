package casino.poker.bigtwo.score;

import java.util.List;

import casino.player.Player;
import casino.poker.bigtwo.player.BigTwoPlayer;
import casino.poker.bigtwo.utils.BigTwoGameContext;

public class EndGameUtils {

	/**
	 * Tallies the score for the player. If the player lost, depends on the number
	 * of cards left at the end of the game. 
	 * 1-5 cards: 1xcardsLeft
	 * 6-10 cards: 2xcardsLeft
	 * 11-12 cards: 3xcardsLeft
	 * 13 cards: 4xcardsLeft
	 * Winner receives all the points from deductions. 
	 * @param player
	 * @param context
	 * @return integer array with the point change for each player in context
	 */
	public static int[] tallyDeduction(BigTwoGameContext context){
		int [] points = new int[4];
		List<BigTwoPlayer> players = context.getPlayers();
		Player winner = context.getWinners().get(0);
		int totalDeductions = 0;
		int winnerIndex = -1;
		for (int i = 0; i < 4; i++){
			Player player = players.get(i);
			if (player == winner){
				winnerIndex = i;
				continue;
			}
			int cardCount = player.getCards().size();
			int playerDeduction = 0;
			if (cardCount >= 1 && cardCount < 6){
				playerDeduction = cardCount;
			} else if (cardCount >= 6 && cardCount <= 10){
				playerDeduction = cardCount*2;
			} else if (cardCount >= 11 && cardCount <= 12){
				playerDeduction = cardCount*3;
			} else {
				// only possible to have 13 cards left, SCREWED.
				playerDeduction = cardCount*4;
			}
			player.changeScore(-playerDeduction);
			points[i] = -playerDeduction;
			totalDeductions = totalDeductions + playerDeduction;
		} 
		winner.changeScore(totalDeductions);
		points[winnerIndex] = totalDeductions;
		context.setEndGameScoreChanges(points);
		return points;
	}
	
}
