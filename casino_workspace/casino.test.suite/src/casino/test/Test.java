package casino.test;
import casino.deck.Deck;


public class Test {
	public static void main(String[] args){
		Deck deck = new Deck();
		System.out.println(deck.toString());
		deck.shuffle();
		System.out.println(deck.toString());
	}
}
