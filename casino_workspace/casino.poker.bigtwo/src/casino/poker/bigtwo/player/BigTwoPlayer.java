package casino.poker.bigtwo.player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import casino.deck.Card;
import casino.game.GameContext;
import casino.player.Player;
import casino.player.PlayerImpl;
import casino.poker.bigtwo.utils.BigTwoGameContext;
import casino.poker.bigtwo.utils.BigTwoUtils;
import casino.poker.deck.PokerCard;
import casino.poker.utils.PokerHand;

public abstract class BigTwoPlayer extends PlayerImpl implements Player{

	public static BigTwoGameContext context;

	public BigTwoPlayer(String name) {
		super(name);
		score = 0;
	}
	
	public static void setContext(BigTwoGameContext newContext){
		context = newContext;
	}


	/**
	 * possibleHands should not be null: will contain all the possible hands that this player can play if they can play at all.
	 * possibleHands will show hands in the best context that they can.
	 * - PAIR: if this player has trips, possibleHands will contain trips instead of 2 different pairs from those trips. 
	 * - STRAIGHT: if this player has an 8 card straight, will contain the 8 cards in a single hand, 
	 * 		not break it into 3 hands
	 * - FLUSH: contains all the cards for those suits
	 * - FULL_HOUSE: will put trips and pairs in their own lists
	 */
	@Override
	public boolean canPlay(GameContext gameContext, List<List<Card>> possibleHands) {
		
		BigTwoGameContext btGameContext = (BigTwoGameContext)gameContext;
		PokerHand currentPokerHand = btGameContext.currentBigTwoHandInPlay();
		
		if (currentPokerHand == null){
			// TODO POPULATE THIS with all possible hands????
			for (int j = 0; j < cards.size(); j++){
				List<Card> newHand = new ArrayList<Card>();
				newHand.add(cards.get(j));
				possibleHands.add(newHand);
			}
			return true; // my turn to start!
		}
		
		List<? extends Card> currentCards = btGameContext.getLastPlayedCards();
		Collections.sort(currentCards, PokerCard.getRankSorter());
		Card highestCard = currentCards.get(currentCards.size()-1);
		
		List<Card> copyMyCardsRank = new ArrayList<Card>(cards);
		Collections.sort(copyMyCardsRank, PokerCard.getRankSorter());
		List<Card> copyMyCardsSuit = new ArrayList<Card>(cards);
		Collections.sort(copyMyCardsSuit, PokerCard.getSuitSorter());
				
		// check if this player has cards that can beat the current hand
		if (currentPokerHand == PokerHand.SINGLE){
			for (int i = 0; i < copyMyCardsRank.size(); i++){
				Card myCard = copyMyCardsRank.get(i);
				if (myCard.compareTo(highestCard, true) > 0){
					// myCard beats current card, all cards thereafter beat the current card
					for (int j = i; j < copyMyCardsRank.size(); j++){
						List<Card> newHand = new ArrayList<Card>();
						newHand.add(copyMyCardsRank.get(j));
						possibleHands.add(newHand);
					}
					return !possibleHands.isEmpty();
				}
			}
			return false;
			
		} 
		
		// collect pairs and trips
		List<List<Card>> pairs = new ArrayList<List<Card>>();
		List<List<Card>> trips = new ArrayList<List<Card>>();
		List<List<Card>> quads = new ArrayList<List<Card>>();
		
		Map<Integer, List<Card>> rankToCards = new HashMap<Integer, List<Card>>();
		for (Card card : copyMyCardsRank){
			List<Card> sameRankCards = rankToCards.get(card.getRank());
			if (sameRankCards == null){
				sameRankCards = new ArrayList<Card>();
				rankToCards.put(card.getRank(), sameRankCards);
			}
			sameRankCards.add(card);
		}

		for (Integer rank : rankToCards.keySet()){
			List<Card> sameRankCards = rankToCards.get(rank);
			if (sameRankCards.size() == 2){
				pairs.add(sameRankCards);
			} else if (sameRankCards.size() == 3){
				trips.add(sameRankCards);
			} else if (sameRankCards.size() == 4){
				quads.add(sameRankCards);
			}
		}

		if (currentPokerHand == PokerHand.PAIR || currentPokerHand == PokerHand.TRIPS){
			if (currentPokerHand == PokerHand.PAIR){
				if (cards.size() == 1)
					return false;
			} else {
				if (cards.size() < 3)
					return false;
			}
			List<List<Card>> duplicateRanks = new ArrayList<List<Card>>(quads);
			duplicateRanks.addAll(trips);
			if (currentPokerHand == PokerHand.PAIR){
				duplicateRanks.addAll(pairs);
			}
			for (List<Card> dup: duplicateRanks){
				Card repCard = dup.get(dup.size()-1);
				if (repCard.compareTo(highestCard, true) > 0){
					possibleHands.add(dup);
				}
			}
			return !possibleHands.isEmpty();
		}

		
		int[] myRankCounter = new int[13];
		int[] mySuitCounter = new int[4];
		
		for (Card card : cards){
			myRankCounter[card.getRank()]++;
			mySuitCounter[card.getSuit().ordinal()]++;
		}
		
		// only 5 card hands left to be played
		
		if (cards.size() < 5)
			return false;
		
		// go backwards
		boolean hasStraightFlush = false;
		boolean hasFullHouse = (trips.size() > 0 && pairs.size() > 0) ||
				(trips.size() > 1) || /*two trips can make a full house*/
				(quads.size() > 0 && pairs.size() > 0) ||
				(quads.size() > 0 && trips.size() > 0) ||
				(quads.size() > 1);
		boolean hasFlush = false;
		
		Map<Card.Suit, List<Card>> suitToCards = new HashMap<Card.Suit, List<Card>>(); // first card in list will be highest card
		Set<Card.Suit> suitFlushes = new HashSet<Card.Suit>(); 
		for (int i = cards.size()-1; i >=0 ; i--){
			Card mySuitCard = copyMyCardsSuit.get(i);
			Card.Suit suit = mySuitCard.getSuit();
			List<Card> suitCards = suitToCards.get(suit);
			if (suitCards == null){
				suitCards = new ArrayList<Card>();
				suitToCards.put(suit, suitCards);
			}
			suitCards.add(mySuitCard);
		}

		// check if have straight flush (or flush)
		// check if there are any with count of five
		for (Card.Suit suit : suitToCards.keySet()){
			List<Card> flush = suitToCards.get(suit);
			if (flush != null && flush.size() >= 5){
				// flush!
				suitFlushes.add(flush.get(0).getSuit());
				hasFlush = true;
				// check if it is a STRAIGHT_FLUSH
				List<List<Card>> straightFlushes = BigTwoUtils.findBigTwoStraight(flush);
				if (!straightFlushes.isEmpty()){
					if (currentPokerHand == PokerHand.STRAIGHT_FLUSH){
						for (List<Card> straightFlush : straightFlushes){
							// only a higher straight flush will beat this
							Collections.sort(straightFlush, PokerCard.getRankSorter());
							Card maxStraightFlushCard = straightFlush.get(straightFlush.size()-1);
							// compare highest cards
							if (maxStraightFlushCard.compareTo(highestCard, true) > 0){
								possibleHands.add(straightFlush);
							} // if false, need to wait til other suits have been computed
						} 
					} else {
						possibleHands.add(flush); // all other hands will fall to this (contains a straight flush!)
						hasStraightFlush = true;
						suitFlushes.remove(flush.get(0).getSuit()); // remove so it is not added later
					}
				}
			}
		}
		
		if (currentPokerHand == PokerHand.STRAIGHT_FLUSH){
			return !possibleHands.isEmpty();
		}
		
		// add quads to appropriate
		if (currentPokerHand == PokerHand.STRAIGHT || currentPokerHand == PokerHand.FULL_HOUSE || 
				currentPokerHand == PokerHand.FLUSH){
			possibleHands.addAll(quads);
		}
		
		if (currentPokerHand == PokerHand.FOUR_OF_A_KIND){
			// check if quads are greater than current hand (not necessarily highestCard)
			Collections.sort(currentCards, PokerCard.getRankSorter());
			// cards are either sorted ABBBB or BCCCC, either way index 1-3 are repCards
			Card repCard = currentCards.get(1); 
			for (List<Card> quad : quads){
				if (quad.get(0).compareTo(repCard, false) > 0){
					possibleHands.add(quad);
				}
			}
			return !possibleHands.isEmpty();
		}
		
		// straight flushes and four of a kinds have all been added to possible hands
		// add full houses appropriately
		if (hasFullHouse){
			boolean tripsAdded = false;
			boolean pairsAdded = false;
			
			Card repCard = null;
			
			List<List<Card>> qualifiedTrips = null;
			if (currentPokerHand == PokerHand.FULL_HOUSE){
				Collections.sort(currentCards, PokerCard.getRankSorter());
				// cards are either AAABB or BBCCC, either way, index 2 is repCard
				repCard = currentCards.get(2);
				
				qualifiedTrips = new ArrayList<List<Card>>(); // if a single trip qualifies, they all qualify (the rest can be treated as pairs)
				boolean singleQualify = false;
				for (List<Card> trip : trips){
					if (trip.get(0).compareTo(repCard, false) > 0){
						singleQualify = true;
						break;
					}
				}
				if (singleQualify)
					qualifiedTrips = trips;
			} else {
				qualifiedTrips = trips;
			}
			
			if (qualifiedTrips.size() > 0 && pairs.size() > 0){
				// add all trips and pairs
				possibleHands.addAll(qualifiedTrips);
				possibleHands.addAll(pairs);
				tripsAdded = true;
				pairsAdded = true;
			}
			if (qualifiedTrips.size() > 1 && !tripsAdded){
				possibleHands.addAll(trips);
				tripsAdded = true;
			}
			boolean goodQuad = false;
			for (List<Card> quad : quads){
				if(quad.get(0).compareTo(repCard, false) > 0){
					goodQuad = true;
					break;
				}
			}
			if (quads.size() > 0 && !pairsAdded){
				// make sure there is a quad would even qualify before adding these pairs
				if (goodQuad){
					possibleHands.addAll(pairs);
					pairsAdded = true;
				}
			}
			if (quads.size() > 0 && qualifiedTrips.size() > 0 && !tripsAdded){
				if (goodQuad){
					possibleHands.addAll(qualifiedTrips);
					tripsAdded = true;
				}
			}
		}
		
		if (currentPokerHand == PokerHand.FULL_HOUSE){
			return !possibleHands.isEmpty();
		}
		
		// straight flushes, four of a kinds, full houses have all been added 
		// make sure flushes are added appropriately
		
		if (hasFlush){
			for (Card.Suit suitFlush : suitFlushes){
				List<Card> flush = suitToCards.get(suitFlush);
				// should already be sorted by rank
				if (currentPokerHand == PokerHand.FLUSH){
					// check highest card against this flush
					int result = flush.get(flush.size()-1).compareTo(highestCard, false);
					if (result > 0){
						possibleHands.add(flush);
					} else if (result == 0){
						int cardCount = flush.size();
						List<Card> highestPossibleFlush = flush.subList(cardCount-5, cardCount);
						if (BigTwoUtils.compareHands(highestPossibleFlush, currentCards) > 0){
							possibleHands.add(flush);
						}
					}
				} else {
					possibleHands.add(flush);
				}
			}
		}
		
		if (currentPokerHand == PokerHand.FLUSH){
			return !possibleHands.isEmpty();
		}
		
		// straight flushes, four of a kinds, full houses, flushes have all been added 
		
		// make sure straights are added appropriately
		List<List<Card>> possibleStraights = BigTwoUtils.findBigTwoStraight(cards);
		if (hasStraightFlush){
			// TODO don't want straight flushes added (will be duplicateish)?
			
		} else {
			// just check to see if max card of straight is higher than current hand
			for (List<Card> straight : possibleStraights){
				Collections.sort(straight, PokerCard.getRankSorter());
				Card maxCard = straight.get(straight.size()-1);
				if (maxCard.compareTo(highestCard, true) > 0){
					possibleHands.add(straight);
				}
			}
		}
		
		return !possibleHands.isEmpty();
	}

}
