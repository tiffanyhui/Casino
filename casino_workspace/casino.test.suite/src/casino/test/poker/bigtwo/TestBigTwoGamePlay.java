package casino.test.poker.bigtwo;

import java.util.ArrayList;
import java.util.List;

import casino.poker.bigtwo.player.BigTwoPlayer;
import casino.poker.bigtwo.utils.BigTwoGameContext;
import casino.poker.bigtwo.utils.BigTwoUtils;

public class TestBigTwoGamePlay {
	
	private static int NUM_HUMAN_PLAYERS = 3;

	public static BigTwoGameContext gameContext;
	static {
		List<BigTwoPlayer> players = new ArrayList<BigTwoPlayer>(); 
		gameContext = BigTwoUtils.initGame(false, NUM_HUMAN_PLAYERS, players);

	}
	
//	public static BigTwoGameContext gameContext = BigTwoUtils.initGame(false, NUM_HUMAN_PLAYERS, players);
	
	public static void main(String[] args) {

		List<BigTwoPlayer> players = new ArrayList<BigTwoPlayer>(); 
		gameContext = BigTwoUtils.initGame(false, NUM_HUMAN_PLAYERS, players);
		
		// game loop...
		
		
		
	}
	
}
