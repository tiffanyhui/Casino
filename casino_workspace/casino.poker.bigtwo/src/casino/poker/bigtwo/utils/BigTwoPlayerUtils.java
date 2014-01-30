package casino.poker.bigtwo.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import casino.deck.Card;
import casino.poker.bigtwo.player.BigTwoPlayer;
import casino.poker.deck.PokerCard;
import casino.poker.utils.PokerHand;

public class BigTwoPlayerUtils {

	public static List<Card> cardsToStartRound(BigTwoPlayer player, boolean startingGame){
		List<Card> cardsToPlay = new ArrayList<Card>();
		if (startingGame){
			// must use 3DIAMONDS
			List<Card> cards = player.getCards();
			List<Card> rankSorted = new ArrayList<Card>(cards);
			Collections.sort(rankSorted, PokerCard.getRankSorter());
			Card threeDiamonds = rankSorted.get(0);
			Map<PokerHand, List<Card>> starterHands = handsThatUseCard(rankSorted, threeDiamonds);
			if (starterHands.isEmpty()){
				// no combo can be used with 3diamonds
				// must play solo 3diamonds
				cardsToPlay.add(threeDiamonds);
				
				// check if there is four of a kind to couple with (but not twos)!
				Map<Integer, List<Card>> rankMap = new HashMap<Integer, List<Card>>();
				populateRankMap(rankMap, cards);
				List<Card> lowestQuads = null;
				for (Integer rank : rankMap.keySet()){
					List<Card> sameRank = rankMap.get(rank);
					if (sameRank != null){
						if (sameRank.size() == 4){
							if (lowestQuads == null){
								lowestQuads = sameRank;
							} else {
								// compare ranks
								if (lowestQuads.get(0).compareTo(sameRank.get(0), false) > 0){
									lowestQuads = sameRank;
								}
							}
						}
					}
				}
				
				if (lowestQuads != null && lowestQuads.get(0).getRank() != 1){
					cardsToPlay.addAll(lowestQuads);
				}
			} 
			// check if pair
			List<Card> pair = starterHands.get(PokerHand.PAIR);
			if (pair != null){
				// check if there are trips somewhere that can make it a fullhouse!
				Map<Integer, List<Card>> rankMap = new HashMap<Integer, List<Card>>();
				populateRankMap(rankMap, cards);
				
				// determine whether or not you want lowest trips or highest trips
				// lowest : have another full house, this can trump whatever is used against you
				// highest : dont have another full house (can use low trips later if gain control)
				
				List<Card> lowestTrips = null;
				List<Card> highestTrips = null;
				boolean hasOtherPair = false;
				for (Integer rank : rankMap.keySet()){
					List<Card> sameRank = rankMap.get(rank);
					if (sameRank != null){
						if (sameRank.size() == 2){
							if (sameRank.get(0).getRank() != 2){
								// a pair other than the 3s
								hasOtherPair = true;
							}
						} else if (sameRank.size() == 3){
							if (lowestTrips == null){
								lowestTrips = sameRank;
							} else {
								// compare ranks (lowest trips < highest trips, always)
								if (lowestTrips.get(0).compareTo(sameRank.get(0), false) > 0){
									// lowest trips are actually higher than new trips
									if (highestTrips == null){
										highestTrips = lowestTrips;
									} 
									lowestTrips = sameRank;
								}
							}
						}
					}
				}
				
				if (highestTrips == null && lowestTrips != null){
					if (lowestTrips.get(0).getRank() != 1){
						cardsToPlay.addAll(lowestTrips);
					}
				} else if (hasOtherPair && highestTrips != null){
					// use low trips cause there is another fullhouse 
					// that can potentially be used later
					cardsToPlay.addAll(lowestTrips);
				} else if (highestTrips != null && highestTrips.get(0).getRank() != 1){
					// there are high trips that can be used for the full house
					// save low trips for later 
					cardsToPlay.addAll(highestTrips);
				}
				return cardsToPlay;
			} 
			// TODO not necessarily true that if 3 forms a pair, you play that pair...
			// if it is in a flush, might play flush instead
			// have some sort of 'best hand' metric
			
			
		}
		return cardsToPlay;
	}
	
	/**
	 * 
	 * @param cards
	 * @param card
	 * @return a map of possible hands that can be played with the card. Does not return
	 * the SINGLE card as a possible hand. Does not return a FULLHOUSE, but will return 
	 * if it is part of a TRIPLE. You will need to query whether a PAIR is possible to form 
	 * a FULLHOUSE. Will return STRAIGHTFLUSH separately from FLUSH and STRAIGHT.
	 * FLUSH and STRAIGHT will still be returned, just in context. 
	 */
	public static Map<PokerHand, List<Card>> handsThatUseCard(List<Card> rankSortedCards, Card card){
		Map<PokerHand, List<Card>> possibleHands = new HashMap<PokerHand, List<Card>>();
		
		List<Card> duplicateRank = new ArrayList<Card>();
		List<Card> flush = new ArrayList<Card>();
		
		for (Card loopCard : rankSortedCards){
			int rank = loopCard.getRank();
			if (rank == card.getRank()){
				duplicateRank.add(loopCard);
			}
			if (loopCard.getSuit() == card.getSuit()){
				flush.add(loopCard);
			}
		}
		
		List<List<Card>> straights = BigTwoUtils.findBigTwoStraight(rankSortedCards);
		for (List<Card> straight : straights){
			if(straight.contains(card)){
				// TODO read findBigTwoStraight doc and decide whether or not, duplicate should be stripped
				possibleHands.put(PokerHand.STRAIGHT, straight);
				break;
			}
		}
		
		if (flush.size() >= 5){
			possibleHands.put(PokerHand.FLUSH, flush);
			// find out if flush contains a straight --> STRAIGHT FLUSH!
			List<List<Card>> straightFlushList = BigTwoUtils.findBigTwoStraight(flush);
			for (List<Card> straightFlush : straightFlushList){
				if (straightFlush.contains(card)){
					possibleHands.put(PokerHand.STRAIGHT_FLUSH, straightFlush);
					break;
				}
			}
		}

		switch (duplicateRank.size()){
			case 2:
				possibleHands.put(PokerHand.PAIR,duplicateRank);
				break;
			case 3:
				possibleHands.put(PokerHand.TRIPS,duplicateRank);
				break;
			case 4:
				possibleHands.put(PokerHand.FOUR_OF_A_KIND, duplicateRank);
				break;
		}
		
		return possibleHands;
	}
	
	private static void populateRankMap(Map<Integer, List<Card>> rankMap, List<Card> cards){
		for (Card card : cards){
			List<Card> sameRankCards = rankMap.get(card.getRank());
			if (sameRankCards == null){
				sameRankCards = new ArrayList<Card>();
				rankMap.put(card.getRank(), sameRankCards);
			}
			sameRankCards.add(card); 
		}
	}
}
