package casino.poker.bigtwo.player;

import java.util.List;

import casino.deck.Card;
import casino.game.GameContext;
import casino.player.ComputerPlayer;
import casino.player.Player;
import casino.poker.bigtwo.utils.BigTwoGameContext;
import casino.poker.bigtwo.utils.BigTwoPlayerUtils;
import casino.poker.bigtwo.utils.BigTwoUtils;
import casino.poker.utils.PokerHand;

public class ComputerBigTwoPlayer extends BigTwoPlayer implements ComputerPlayer{

	public ComputerBigTwoPlayer(String name) {
		super(name);
	}
	
	private List<Card> playImpl(List<Card> cards){
		cards.removeAll(cards);
		return cards;
	}

	@Override
	public List<Card> play(GameContext gameContext,
			List<List<Card>> possibleHands) {

		BigTwoGameContext btGameContext = (BigTwoGameContext)gameContext;
		
		/**
		 * Basic things:
		 * *) Player after you has 1 card, put out highest card
		 *    *) If you have control, put out multiple cards (if possible)
		 * *) Any player has 1 card and you have 2SP, play it
		 * *) If you have control and a player has 1 card, put out multiple cards
		 * 
		 *    
		 * **) If you have 2SP and have 2 distinct hands left (excluding 2SP),
		 *    and are on single cards, play middle single card
		 * **) If you have 2 of spades and have 2 distinct hands left (excluding 2SP),
		 * 	  play 2SP
		 * **) Single hand left, play! Win!
		 * 
		 */
		List<? extends Card> lastPlayedCards = btGameContext.getLastPlayedCards();
		if (lastPlayedCards == null){
			// has control
			System.out.println("has control!");

			if (btGameContext.getNextPlayer().getCards().size() == 1){
				// next player only has 1 card, put out multiple card hands
			} else {
			}
		}
		
		// pairs are easiest to continue off of...
		PokerHand currentBigTwoHand = btGameContext.currentBigTwoHandInPlay();
		
		if (currentBigTwoHand == PokerHand.SINGLE) {
			// Check if player after you has 1 card
			Player nextPlayer = btGameContext.getNextPlayer();
			if (nextPlayer.getCards().size() == 1){
				// put out highest card! 
				// TODO make sure that possibleHands is ordered by rank
				return playImpl(possibleHands.get(possibleHands.size()-1));
			} else {
				// play lowest single that isn't attached to another hang
				for(List<Card> possibleHand : possibleHands){
					if(BigTwoPlayerUtils.handsThatUseCard(getCards(), possibleHand.get(0)).isEmpty()){
						return playImpl(possibleHand);
					}
				}
			}
		}
		
		if (currentBigTwoHand == PokerHand.PAIR){
			// only pairs, triples, and quads can be in here
			
		}
		
		
		
		// LOGIX
		
		// TODO for now just play first hand
		List<Card> handToPlay = possibleHands.get(0);
		// can only play the number of cards that are allowed!
		if (lastPlayedCards != null){
			int sizeOfLastPlayed = lastPlayedCards.size();
			handToPlay = handToPlay.subList(0, sizeOfLastPlayed);
		}
		return playImpl(handToPlay);
	}

}
