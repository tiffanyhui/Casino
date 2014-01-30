package casino.poker.bigtwo.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import casino.deck.Card;
import casino.deck.Card.Suit;
import casino.deck.Deck;
import casino.logger.CasinoFileLoggerHandler;
import casino.logger.CasinoLogger;
import casino.logger.CasinoLogger.CasinoLoggerLevel;
import casino.player.Dealer;
import casino.player.Player;
import casino.poker.bigtwo.BigTwoCard;
import casino.poker.bigtwo.player.BigTwoDealer;
import casino.poker.bigtwo.player.BigTwoPlayer;
import casino.poker.bigtwo.player.ComputerBigTwoPlayer;
import casino.poker.bigtwo.player.HumanBigTwoPlayer;
import casino.poker.deck.PokerCard;
import casino.poker.utils.PokerHand;

public class BigTwoUtils {

	private static final String BIG_TWO_GAME_NAME = "BigTwo";
	
	public static BigTwoGameContext initGame(boolean print, int numHumanPlayers, List<BigTwoPlayer> players){
		
		try {
			CasinoLogger.INSTANCE.addLogHandler(new CasinoFileLoggerHandler(BIG_TWO_GAME_NAME));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		for (int i = 0; i < 4; i++){
			if (i < numHumanPlayers){
				players.add(new HumanBigTwoPlayer("Player"+(i+1)));
			} else {
				players.add(new ComputerBigTwoPlayer("Player"+(i+1)));
			}
		}
		
		Dealer dealer = new BigTwoDealer(new Deck(){
			@Override
			protected Card createNewCard(int rank, Suit suit) {
				return new BigTwoCard(rank, suit);
			}
		});
		
		dealer.deal(players);

//		System.out.println("---- Sort player cards by suit ----");
//		for (Player player : players){
//			Collections.sort(player.getCards(), PokerCard.getSuitSorter());
//		}
		
		// sort cards for players (default by rank)
//		System.out.println("--- Sort player cards by rank ----");
		
		for (Player player : players){
			Collections.sort(player.getCards(), PokerCard.getRankSorter());
		}
		
		CasinoLogger.INSTANCE.log(CasinoLoggerLevel.INFO, "Players:", null);
		for (Player player : players){
			CasinoLogger.INSTANCE.log(CasinoLoggerLevel.INFO, player.toString());
		}
		
		BigTwoGameContext gameContext = new BigTwoGameContext(players);
		gameContext.startGame(dealer);
		
		return gameContext;
	}
	
	/**
	 * Identifies the player with the 3 of diamonds in their hand
	 * @param players 
	 * @return the index of the player in the list of players given
	 */
	public static int identifyFirstPlayer(List<BigTwoPlayer> players){
		// find the player that has the 3 of diamonds
		int value = 2; // in terms of card rank indexing
		Card.Suit suit = Card.Suit.DIAMOND;
		
		for (int i = 0; i < players.size(); i++){
			Player player = players.get(i);
			// go through each player's cards
			for (Card card : player.getCards()){
				if (card.getSuit() == suit && card.getRank() == value){
					return i;
				}
			}
		}
		
		return -1;
	}
	
	
	public static List<BigTwoPlayer> orderByFirstPlayer(List<BigTwoPlayer> players, int firstPlayerIndex){
		List<BigTwoPlayer> orderedPlayers = new ArrayList<BigTwoPlayer>();
		int j = 0;
		for (int i = firstPlayerIndex; j <= 4 ; i++){
			i = i % 4;
			orderedPlayers.add(players.get(i));
			j++;
		}
		return orderedPlayers;
	}
	
	/**
	 * Identifies the type of <code>PokerHand</code> that the cards can represent
	 * @param cards the hand to identify
	 * @return the PokerHand that the hand can be identified by. Returns null if it is an invalid hand. 
	 */
	public static PokerHand identifyHand(List<? extends Card> cards){
		
		if (cards.size() > 5)
			return null; // is not a valid 5card poker hand
		
		if (cards.size() == 1)
			return PokerHand.SINGLE;

		
		int[] suits = new int[4]; // DIAMOND, CLUB, HEART, SPADE indexing
		int[] values = new int[13]; // ACE(0) : KING(12)
		for (Card card : cards){
			suits[card.getSuit().ordinal()]++;
			values[card.getRank()]++;
		}
		
		boolean hasPair = false;
		boolean hasTrips = false;
		boolean hasStraight = false;
		boolean hasFour = false;
		int consec = 0;
		// go through value based array
		for (int i = 0; i < values.length; i++){
			int num_values = values[i];
			if (num_values == 2)
				hasPair = true;
			if (num_values == 3)
				hasTrips = true;
			if (num_values == 4)
				hasFour = true;
			if (num_values == 1){
				consec++;
				if (consec == 5)
					// straight!
					hasStraight = true;
				if (consec == 4 && i == 12 && values[0] != 0) // at 4consecutives and inspecting king (10,j,q,k), check ace
					hasStraight = true;
					
			} else 
				consec = 0;
		}
		
		boolean hasFlush = false;
		// go through suit based array
		for (int num_suits : suits){
			if (num_suits == 5){
				hasFlush = true;
				break;
			}
		}
		
		if (cards.size() == 2 && hasPair)
			return PokerHand.PAIR;
		if (cards.size() == 3 && hasTrips)
			return PokerHand.TRIPS;
		if (hasFlush && hasStraight)
			return PokerHand.STRAIGHT_FLUSH;
		if (hasFlush)
			return PokerHand.FLUSH;
		if (hasStraight)
			return PokerHand.STRAIGHT;
		if (hasPair && hasTrips)
			return PokerHand.FULL_HOUSE; 
		if (hasFour)
			return PokerHand.FOUR_OF_A_KIND;
		return null;
	}

	/**
	 * Compares the two hands and determines which is higher. If the two hands do not have the same
	 * number of cards or second hand is not a valid BigTwoHand, assumes first hand is larger.
	 * Always assumes first hand to be a valid BigTwoHand. Assumes hands to have unique cards.
	 * @param hand1 
	 * @param hand2
	 * @return a negative integer, zero, or a positive integer as the first argument is less than, equal to, or greater than the second.
	 */
	public static int compareHands(List<? extends Card> hand1, List<? extends Card> hand2){
		PokerHand btHand1 = identifyHand(hand1);
		assert btHand1 != null;
		PokerHand btHand2 = identifyHand(hand2);
		if (hand1.size() != hand2.size() || btHand2 == null){
			return 1; // default to first being larger
		}
		int size = hand1.size();
		
		// SINGLES
		if (size == 1){
			Card card1 = hand1.get(0);
			Card card2 = hand2.get(0);
			return card1.compareTo(card2, true);
		}
		
		// PAIR
		if (size == 2){
			Card card1 = hand1.get(0);
			Card card2 = hand2.get(0);
			int result = card1.compareTo(card2, false);

			if (result == 0){
				// equal value pairs
				if (hand1.get(0).getSuit() == Card.Suit.SPADE || hand1.get(1).getSuit() == Card.Suit.SPADE){
					return 1;
				}
				return -1;
			}
			return result;
		}
		
		// TRIPS
		if (size == 3){
			Card card1 = hand1.get(0);
			Card card2 = hand2.get(0);
			// no need to compare suits, impossible to have two trips be the same value
			return card1.compareTo(card2, false);
		}
		
		// five card hands left
		// just compare BigTwoHand: STRAIGHT < FLUSH < FULL_HOUSE < STRAIGHT_FLUSH
		if (btHand1 != btHand2){
			return btHand1.compareTo(btHand2);
		}
		
		// STRAIGHT, FLUSH, STRAIGHT_FLUSH: compare highest ranked card, then compare suit
		if (btHand1 == PokerHand.STRAIGHT || btHand1 == PokerHand.STRAIGHT_FLUSH || btHand1 == PokerHand.FLUSH){
			return compareMaxInHand(hand1, hand2);
		}
		
		// FULL_HOUSE, FOUR OF A KIND
		if (btHand1 == PokerHand.FULL_HOUSE || btHand1 == PokerHand.FOUR_OF_A_KIND){
			
			boolean isFullHouse = btHand1 == PokerHand.FULL_HOUSE;
			
			Card dup1 = null;
			Card dup2 = null;
			
			int[] ranks1 = new int[13];
			int[] ranks2 = new int[13];

			for (int i = 0; i < hand1.size(); i++){
				if (dup1 == null){
					Card card1 = hand1.get(i);
					ranks1[card1.getRank()]++;
					if (isFullHouse){
						if (ranks1[card1.getRank()] == 3){
							dup1 = card1;
						}
					} else {
						// four of a kind
						if (ranks1[card1.getRank()] == 4){
							dup1 = card1;
						}
					}
				}
				if (dup2 == null){
					Card card2 = hand2.get(i);
					ranks2[card2.getRank()]++;
					if (isFullHouse){
						if (ranks2[card2.getRank()] == 3){
							dup2 = card2;
						}
					} else {
						// four of a kind
						if (ranks2[card2.getRank()] == 4){
							dup2 = card2;
						}
					}
				}
			}
			
			return dup1.compareTo(dup2, false);
		}
		
		return 1; // wasnt compared in anyway, assume first
	}
	
	/**
	 * Compare the maximum values in each hand used to determine which hand is higher.
	 * If each card has the same rank, then go by suit. 
	 * Used for straights and flushes. 
	 * @param hand1
	 * @param hand2
	 * @return
	 */
	private static int compareMaxInHand(List<? extends Card> hand1, List<? extends Card> hand2){
		Collections.sort(hand1, PokerCard.getRankSorter());
		Collections.sort(hand2, PokerCard.getRankSorter());
		// hands are now sorted lowest to highest by rank
		// loop through backwards and see which one has the higher cards
		int result = 0;
		for (int i = hand1.size()-1; i >= 0; i--){
			Card card1 = hand1.get(i);
			Card card2 = hand2.get(i);
			result = card1.compareTo(card2, false);
			if (result != 0)
				return result;
		}
		if (result == 0){
			// all cards are the same, grab the highest and compare suit 
			Card highest1 = hand1.get(hand1.size()-1);
			Card highest2 = hand2.get(hand2.size()-1);
			return highest1.compareTo(highest2, true);
		}
		return 0; // should never reach here
	}
	
	/**
	 * Determines whether or not the given hand contains a valid bigtwo straight. Hand does not need to 
	 * already be sorted by rank. If hand does contain one, then is returned. Returns straights with 
	 * their duplicates (ie if there is pair3 that is part of a straight: 3,3,4,5,6,7)
	 * @param hand
	 * @return a list of straights contained in the hand (not limited to 5 cards, but never under 5). Returns empty
	 * list if there are none
	 */
	public static List<List<Card>> findBigTwoStraight(List<Card> hand){
		if (hand.size() < 5){
			Collections.emptyList();
		}
		Collections.sort(hand, PokerCard.getRankSorter()); // sorts by rank, not necessarily by straight ability
		// ex: A2345 sorts to 345A2, 23456 sorts to 34562, 10JQKA is index ranked sorted as A10JQK
		
		int[] values = new int[13]; // ACE(0) : KING(12)
		Map<Integer, List<Card>> rankToCard = new HashMap<Integer, List<Card>>(); // list will automatically be sorted by suit
		for (Card card : hand){
			values[card.getRank()]++;
			List<Card> sameRankCards = rankToCard.get(card.getRank());
			if (sameRankCards == null){
				sameRankCards = new ArrayList<Card>();
				rankToCard.put(card.getRank(), sameRankCards);
			}
			sameRankCards.add(card); 
		}
		
		int consec = 0;
		List<List<Card>> straights = new ArrayList<List<Card>>();
		List<Card> straightSoFar = new ArrayList<Card>();
		for (int i = 0; i < values.length; i++){
			int valueCount = values[i];
			if (valueCount != 0){
				consec++;
				straightSoFar.addAll(rankToCard.get(i)); // add all the cards of that rank
				if (consec == 5){
					// found a straight!
					// if i = 4 (at 5, A-5 straight), if i = 5 (at 6, 2-6 straight)
					// 2 is maxCard
					straights.add(straightSoFar);
				} else if (i == values.length-1){
					// is at K, has processed xx10JQK, do we have ace?
					if (values[0] != 0){
						// yes!
						List<Card> aces = rankToCard.get(0);
						straightSoFar.addAll(aces);
						if (consec == 4){
							// otherwise would have already been added
							straights.add(straightSoFar);
						}
					}
				}
			} else {
				consec = 0;
				straightSoFar = new ArrayList<Card>(); // don't clear! could be in list of straights!
			}
		}
		
		return straights;
	}
	
}
