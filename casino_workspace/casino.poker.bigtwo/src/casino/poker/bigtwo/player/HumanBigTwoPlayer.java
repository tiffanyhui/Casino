package casino.poker.bigtwo.player;

import java.util.ArrayList;
import java.util.List;

import casino.player.HumanPlayer;
import casino.poker.bigtwo.BigTwoCard;
import casino.poker.bigtwo.utils.BigTwoGameContext.BigTwoSortOrder;

public class HumanBigTwoPlayer extends BigTwoPlayer implements HumanPlayer <BigTwoCard> {

	List<BigTwoCard> selectedCards;
	public BigTwoSortOrder sortOrder = BigTwoSortOrder.RANK;
	
	public HumanBigTwoPlayer(String name) {
		super(name);
		selectedCards = new ArrayList<BigTwoCard>();
	}

	@Override
	public void addSelectedCards(List<BigTwoCard> cardsToAdd) {
		selectedCards.addAll(cardsToAdd);
	}

	@Override
	public void removeSelectedCards(List<BigTwoCard> cardsToRemove) {
		selectedCards.removeAll(cardsToRemove);
	}

	@Override
	public List<BigTwoCard> getSelectedCards() {
		return selectedCards;
	}

	@Override
	public void clearSelectedCards() {
		selectedCards.clear();
	}

	@Override
	public void playSelectedCards() {
		cards.removeAll(selectedCards);
	}
}
