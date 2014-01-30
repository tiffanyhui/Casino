package casino.ui.poker.bigtwo;

import java.util.List;

import casino.poker.bigtwo.BigTwoCard;
import casino.poker.bigtwo.player.HumanBigTwoPlayer;

public interface BigTwoHumanListener {

	public void humanPlayed(HumanBigTwoPlayer humanPlayer, List<BigTwoCard> cardsPlayed);
	
	public void humanPassed(HumanBigTwoPlayer humanPlayer);
	
}
