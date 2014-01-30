package casino.player;

import java.util.ArrayList;
import java.util.List;

import casino.deck.Card;

public abstract class PlayerImpl implements Player {
	
	protected String name; 
	protected int score;
	protected List<Card> cards;
	
	public PlayerImpl(String name){
		this.name = name;
		score = 0;
	}
	
	@Override
	public String getName(){
		return name;
	}
	
	@Override
	public int getScore() {
		return score;
	}

	@Override
	public void changeScore(int scoreChange) {
		score = score + scoreChange;
	}
	
	@Override
	public void assignCard(List<Card> newCards){
		if (cards == null){
			cards = new ArrayList<Card>();
		}
		cards.addAll(newCards);
	}
	
	@Override
	public void clearCards(){
		cards = null;
	}
	
	@Override
	public List<Card> getCards() {
		return cards;
	}

	public String toString(){
		StringBuffer playerString = new StringBuffer(name + ":\n");
		if (cards != null){
			for (Card card : cards){
				playerString.append(card.toString());
				playerString.append(" \n");
			}
		}
		return playerString.toString();
	}
}
