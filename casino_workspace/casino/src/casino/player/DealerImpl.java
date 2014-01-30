package casino.player;

import java.util.Collections;
import java.util.List;

import casino.deck.Card;
import casino.deck.Deck;

public class DealerImpl implements Dealer {

	private Deck deck;
	
	public DealerImpl(Deck deck){
		this.deck = deck;
	}
	
	@Override
	public void deal(List<? extends Player> players) {
		deck.shuffle();
		for (int i = 0; i < deck.getCards().size(); i++){
			Card card = deck.getCards().get(i);
			int playerIndex = i % 4;
			Player player = players.get(playerIndex);
			player.assignCard(Collections.singletonList(card));
		}
	}

}
