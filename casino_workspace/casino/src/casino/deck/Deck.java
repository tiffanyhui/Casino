package casino.deck;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {

	private ArrayList<Card> cards;
	
	public Deck(){
		init();
	}
	
	protected Card createNewCard(int rank, Card.Suit suit){
		return new Card(rank, suit);
	}
	
	private void init(){
		cards = new ArrayList<Card>(52);
		for (int i = 0; i < 13; i++){
			for (Card.Suit suit : Card.Suit.values()){
				cards.add(createNewCard(i, suit));
			}
		}
	}
	
	public void shuffle(){
		Collections.shuffle(cards);
	}
	
	public List<Card> getCards(){
		return cards;
	}
	
	public String toString(){
		String deck = "";
		for (Card card : cards){
			deck = deck + card.toString() + "\n";
		}
		return deck;
	}
}
