package casino.test.poker.bigtwo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import casino.deck.Card;
import casino.player.Dealer;
import casino.player.Player;
import casino.poker.bigtwo.BigTwoCard;
import casino.poker.bigtwo.player.BigTwoPlayer;
import casino.poker.bigtwo.player.ComputerBigTwoPlayer;
import casino.poker.bigtwo.score.EndGameUtils;
import casino.poker.bigtwo.utils.BigTwoGameContext;
import casino.poker.bigtwo.utils.BigTwoPlayerUtils;
import casino.poker.bigtwo.utils.BigTwoUtils;
import casino.poker.utils.PokerHand;


public class TestBigTwo {
		
	public static void main(String[] args){
		
		// TEST BASIC POKER STRUCTURE
		testHandRecognition();
		testCompareHands();

		// TEST GAME SET UP
		List<BigTwoPlayer> players = new ArrayList<BigTwoPlayer>(); 
		BigTwoUtils.initGame(false, 1, players);
		loopGame(players);
		
		testPossibleHands();
		
		testHandsWithCard();
		
	}
	

	
	private static void loopGame(List<BigTwoPlayer> players){
		BigTwoGameContext btGameContext = new BigTwoGameContext(players);
		while (!btGameContext.isGameOver()){
//			List<Card> playedCards = btGameContext.callNext();
			// TODO UIness
		}
		int[] tallyDeductions = EndGameUtils.tallyDeduction(btGameContext);
		// TODO UIness
	}
	
	private static void testHandRecognition(){
		System.out.println(">>>> Start test hand recognition <<<<<<<");

		int testNum = 0;
		
		// SINGLE
		Object[] base = new Object[]{5, Card.Suit.CLUB};
		handRecognitionHelper(base, testNum++, PokerHand.SINGLE);
		
		// PAIR
		base = new Object[]{5, Card.Suit.CLUB, 5, Card.Suit.DIAMOND};
		handRecognitionHelper(base, testNum++, PokerHand.PAIR);

		// TRIPS
		base = new Object[]{5, Card.Suit.CLUB, 5, Card.Suit.DIAMOND, 5, Card.Suit.HEART};
		handRecognitionHelper(base, testNum++, PokerHand.TRIPS);


		// FULLHOUSE
		base = new Object[]{5, Card.Suit.CLUB, 
							5, Card.Suit.DIAMOND, 
							5, Card.Suit.HEART, 
							4, Card.Suit.DIAMOND,
							4, Card.Suit.CLUB};
		handRecognitionHelper(base, testNum++, PokerHand.FULL_HOUSE);

		
		// STRAIGHT
		base = new Object[]{5, Card.Suit.CLUB,
				6, Card.Suit.DIAMOND,
				7, Card.Suit.HEART,
				8, Card.Suit.DIAMOND,
				9, Card.Suit.CLUB};
		handRecognitionHelper(base, testNum++, PokerHand.STRAIGHT);

		base = new Object[] {0, Card.Suit.CLUB, 
				11, Card.Suit.DIAMOND,
				12, Card.Suit.CLUB,
				9, Card.Suit.HEART,
				10, Card.Suit.DIAMOND};
		handRecognitionHelper(base, testNum++, PokerHand.STRAIGHT);
		
		base = new Object[] {0, Card.Suit.HEART,
				2, Card.Suit.CLUB,
				1, Card.Suit.DIAMOND,
				3, Card.Suit.SPADE,
				4, Card.Suit.HEART};
		handRecognitionHelper(base, testNum++, PokerHand.STRAIGHT);

		base = new Object[] {1, Card.Suit.HEART,
				2, Card.Suit.CLUB,
				5, Card.Suit.DIAMOND,
				3, Card.Suit.SPADE,
				4, Card.Suit.HEART};
		handRecognitionHelper(base, testNum++, PokerHand.STRAIGHT);

		
		
		// FLUSH
		base = new Object[]{5, Card.Suit.CLUB,
				6, Card.Suit.CLUB,
				7, Card.Suit.CLUB,
				8, Card.Suit.CLUB,
				10, Card.Suit.CLUB
		};
		handRecognitionHelper(base, testNum++, PokerHand.FLUSH);

		// FOUR OF A KIND
		base = new Object[]{5, Card.Suit.CLUB, 
				5, Card.Suit.DIAMOND, 
				5, Card.Suit.HEART, 
				5, Card.Suit.SPADE,
				4, Card.Suit.CLUB};
		handRecognitionHelper(base, testNum++, PokerHand.FOUR_OF_A_KIND);	
		
		// STRAIGHT FLUSH
		base = new Object[]{5, Card.Suit.CLUB,
				6, Card.Suit.CLUB,
				7, Card.Suit.CLUB,
				8, Card.Suit.CLUB,
				9, Card.Suit.CLUB
		};
		handRecognitionHelper(base, testNum++, PokerHand.STRAIGHT_FLUSH);

		
		// BOGUS HANDS
		base = new Object[]{5, Card.Suit.CLUB,
				6, Card.Suit.CLUB};
		handRecognitionHelper(base, testNum++, null);

		
		base = new Object[]{5, Card.Suit.CLUB,
				6, Card.Suit.CLUB,
				7, Card.Suit.CLUB,
				8, Card.Suit.DIAMOND,
				10, Card.Suit.CLUB
		};
		handRecognitionHelper(base, testNum++, null);

		
		base = new Object[]{5, Card.Suit.CLUB,
				6, Card.Suit.CLUB,
				7, Card.Suit.CLUB,
				8, Card.Suit.DIAMOND,
				10, Card.Suit.CLUB,
				11, Card.Suit.DIAMOND
		};
		handRecognitionHelper(base, testNum++, null);
		
		System.out.println(" ---- Start straight detection -----");
		
		base = new Object[]{1, Card.Suit.DIAMOND,
				3, Card.Suit.CLUB,
				4, Card.Suit.DIAMOND,
				1, Card.Suit.HEART,
				8, Card.Suit.CLUB,
				7, Card.Suit.DIAMOND,
				2, Card.Suit.SPADE,
				5, Card.Suit.HEART,
				0, Card.Suit.SPADE
		};
		int[][] expectedResult = new int[][]{{0,1,1,2,3,4,5}};
		straightDetectionHelper(base, testNum++, expectedResult);
		
		base = new Object[] {1, Card.Suit.DIAMOND,
				2, Card.Suit.DIAMOND,
				3, Card.Suit.DIAMOND,
				4, Card.Suit.DIAMOND,
				6, Card.Suit.DIAMOND,
				7, Card.Suit.DIAMOND,
				8, Card.Suit.DIAMOND,
				9, Card.Suit.DIAMOND,
				11, Card.Suit.CLUB};
		expectedResult = new int[][]{{}};
		straightDetectionHelper(base, testNum++, expectedResult);
		
		base = new Object[]{11, Card.Suit.DIAMOND,
				12, Card.Suit.DIAMOND,
				10, Card.Suit.CLUB,
				9, Card.Suit.DIAMOND,
				3, Card.Suit.SPADE,
				4, Card.Suit.SPADE,
				4, Card.Suit.DIAMOND,
				0, Card.Suit.HEART};
		expectedResult =new int[][]{{9,10,11,12,0}};
		straightDetectionHelper(base, testNum++, expectedResult);

		base = new Object[]{11, Card.Suit.DIAMOND,
				12, Card.Suit.DIAMOND,
				10, Card.Suit.CLUB,
				9, Card.Suit.DIAMOND,
				3, Card.Suit.SPADE,
				4, Card.Suit.SPADE,
				4, Card.Suit.DIAMOND,
				0, Card.Suit.HEART,
				8, Card.Suit.SPADE,
				8, Card.Suit.HEART};
		expectedResult =new int[][]{{8,8,9,10,11,12,0}};
		straightDetectionHelper(base, testNum++, expectedResult);

		base = new Object[]{11, Card.Suit.DIAMOND,
				12, Card.Suit.DIAMOND,
				10, Card.Suit.CLUB,
				9, Card.Suit.DIAMOND,
				3, Card.Suit.SPADE,
				4, Card.Suit.SPADE,
				4, Card.Suit.DIAMOND,
				0, Card.Suit.HEART,
				8, Card.Suit.SPADE,
				8, Card.Suit.HEART,
				1, Card.Suit.CLUB,
				2, Card.Suit.CLUB,
				4, Card.Suit.CLUB,
				3, Card.Suit.CLUB,
				5, Card.Suit.CLUB,};
		expectedResult =new int[][]{{0,1,2,3,3,4,4,4,5},{8,8,9,10,11,12,0}};
		straightDetectionHelper(base, testNum++, expectedResult);
		
		base = new Object[]{11, Card.Suit.DIAMOND,
				12, Card.Suit.DIAMOND,
				10, Card.Suit.CLUB,
				9, Card.Suit.DIAMOND,
				3, Card.Suit.SPADE,
				2, Card.Suit.SPADE,
				4, Card.Suit.DIAMOND,
				0, Card.Suit.HEART,
				8, Card.Suit.SPADE,
				7, Card.Suit.HEART,
				1, Card.Suit.CLUB,
				6, Card.Suit.CLUB,
				5, Card.Suit.CLUB};
		expectedResult =new int[][]{{0,1,2,3,4,5,6,7,8,9,10,11,12,0}}; // let ace wrap around...
		straightDetectionHelper(base, testNum++, expectedResult);
		
		System.out.println("----- End straight detection -----");
		
		
		
		System.out.println(">>>>>>> End test hand recognition <<<<<<<");
	}
	
	private static boolean testCompareHands(){
		System.out.println("Start test compare hands");
		
		boolean correctComparison = true;
		int testCase = 0;
		
		// SINGLE
		Object[] base1 = new Object[]{5, Card.Suit.CLUB};
		Object[] base2 = new Object[]{5, Card.Suit.DIAMOND};
		compareHandHelper(base1, base2, testCase++, 1);
		
		base1 = new Object[]{0, Card.Suit.CLUB}; // ACE
		base2 = new Object[]{1, Card.Suit.DIAMOND}; // 2
		compareHandHelper(base1, base2, testCase++, -1);
		
		// PAIR
		base1 = new Object[]{0, Card.Suit.CLUB, 0, Card.Suit.HEART};
		base2 = new Object[]{1, Card.Suit.DIAMOND, 1, Card.Suit.SPADE};
		compareHandHelper(base1, base2, testCase++, -1);


		// EQUAL VALUE PAIR
		base1 = new Object[]{5, Card.Suit.CLUB, 5, Card.Suit.HEART};
		base2 = new Object[]{5, Card.Suit.DIAMOND, 5, Card.Suit.SPADE};
		compareHandHelper(base1, base2, testCase++, -1);


		// TRIPS
		base1 = new Object[]{5, Card.Suit.CLUB, 5, Card.Suit.HEART, 5, Card.Suit.DIAMOND};
		base2 = new Object[]{11, Card.Suit.DIAMOND, 11, Card.Suit.SPADE, 11, Card.Suit.CLUB};
		compareHandHelper(base1, base2, testCase++, -1);


		// STRAIGHTS
		base1 = new Object[]{5, Card.Suit.CLUB, /*2-6club*/
				4, Card.Suit.HEART, 
				3, Card.Suit.HEART, 
				2, Card.Suit.HEART, 
				1, Card.Suit.DIAMOND};
		base2 = new Object[]{5, Card.Suit.DIAMOND, /*2-6dia*/
				4, Card.Suit.CLUB, 
				3, Card.Suit.SPADE, 
				2, Card.Suit.CLUB, 
				1, Card.Suit.CLUB};
		compareHandHelper(base1, base2, testCase++, -1);

		base2 = new Object[]{7, Card.Suit.DIAMOND, /*4-8*/
				6, Card.Suit.CLUB, 
				5, Card.Suit.SPADE, 
				4, Card.Suit.CLUB, 
				3, Card.Suit.CLUB};
		compareHandHelper(base1, base2, testCase++, 1);


		base2 =  new Object[]{4, Card.Suit.DIAMOND, /*A-5*/
				3, Card.Suit.CLUB, 
				2, Card.Suit.SPADE, 
				1, Card.Suit.CLUB, 
				0, Card.Suit.CLUB};
		compareHandHelper(base1, base2, testCase++, -1);

		// FULL HOUSE
		testCase++;
		base2 = new Object[]{11, Card.Suit.CLUB, 
				11, Card.Suit.DIAMOND,
				11, Card.Suit.HEART,
				10, Card.Suit.DIAMOND, 
				10, Card.Suit.CLUB};
		compareHandHelper(base1, base2, testCase++, -1);

		testCase++;
		base1 = new Object[]{9, Card.Suit.CLUB, 
				9, Card.Suit.DIAMOND,
				8, Card.Suit.HEART,
				8, Card.Suit.DIAMOND, 
				9, Card.Suit.SPADE};
		compareHandHelper(base1, base2, testCase++, -1);

		// FLUSH
		testCase++;
		base1 = new Object[]{0, Card.Suit.DIAMOND, 
						1, Card.Suit.DIAMOND,
						2, Card.Suit.DIAMOND,
						3, Card.Suit.DIAMOND,
						5, Card.Suit.DIAMOND
		};
		base2 = new Object[]{1, Card.Suit.HEART, 
				2, Card.Suit.HEART,
				3, Card.Suit.HEART,
				0, Card.Suit.HEART,
				5, Card.Suit.HEART
		};
		compareHandHelper(base1, base2, testCase++, -1);
		
		// FOUR OF A KIND
		base2 = new Object[]{7, Card.Suit.CLUB, 
				7, Card.Suit.SPADE,
				7, Card.Suit.DIAMOND,
				7, Card.Suit.HEART,
				5, Card.Suit.HEART
		};
		compareHandHelper(base1, base2, testCase++, -1);
		
		System.out.println("End test compare hands");		
		
		return correctComparison;

	}
	
	private static void testPossibleHands(){
		
		int testNum = 0;
		
		// SINGLES
		Object[] handPlayed = new Object[]{2, Card.Suit.CLUB};
		Object[] currentPlayerCards = new Object[]{2, Card.Suit.HEART, 1, Card.Suit.DIAMOND};
		List<List<Card>> expectedResult = new ArrayList<List<Card>>();
		List<Card> result1 = constructHand(new Object[]{2, Card.Suit.HEART});
		expectedResult.add(result1);
		List<Card> result2 = constructHand(new Object[]{1, Card.Suit.DIAMOND});
		expectedResult.add(result2);
		possibleHandsHelper(handPlayed, currentPlayerCards, testNum++, expectedResult);
		
		currentPlayerCards = new Object[]{2, Card.Suit.HEART, 2, Card.Suit.DIAMOND, 1, Card.Suit.DIAMOND};
		possibleHandsHelper(handPlayed, currentPlayerCards, testNum++, expectedResult);

		// PAIRS
		expectedResult.clear();
		handPlayed = new Object[]{5, Card.Suit.DIAMOND, 5, Card.Suit.SPADE};
		currentPlayerCards = new Object[]{3, Card.Suit.HEART, 3, Card.Suit.SPADE, 
				5, Card.Suit.CLUB, 5, Card.Suit.HEART,
				8, Card.Suit.SPADE, 9, Card.Suit.DIAMOND,
				10, Card.Suit.SPADE, 10, Card.Suit.DIAMOND,
				4, Card.Suit.DIAMOND, 4, Card.Suit.HEART, 4, Card.Suit.CLUB, // not in results
				6, Card.Suit.DIAMOND, 6, Card.Suit.HEART, 6, Card.Suit.SPADE, // in results
				1, Card.Suit.DIAMOND, 1, Card.Suit.HEART}; // in results (2s)
		result1 = constructHand(new Object[]{10, Card.Suit.DIAMOND, 10, Card.Suit.SPADE});
		expectedResult.add(result1);
		result2 = constructHand(new Object[]{1, Card.Suit.DIAMOND, 1, Card.Suit.HEART});
		expectedResult.add(result2);
		List<Card> result3 = constructHand(new Object[]{6, Card.Suit.DIAMOND, 6, Card.Suit.HEART, 6, Card.Suit.SPADE});
		expectedResult.add(result3);
		possibleHandsHelper(handPlayed, currentPlayerCards, testNum++, expectedResult);
		
		// PAIRS no possible hands
		expectedResult.clear();
		currentPlayerCards = new Object[]{3, Card.Suit.HEART, 3, Card.Suit.SPADE, 
				5, Card.Suit.CLUB, 5, Card.Suit.HEART,
				8, Card.Suit.SPADE, 9, Card.Suit.DIAMOND,
				10, Card.Suit.SPADE, 11, Card.Suit.DIAMOND,
				1, Card.Suit.DIAMOND, 2, Card.Suit.HEART};
		possibleHandsHelper(handPlayed, currentPlayerCards, testNum++, expectedResult);

		// TRIPS
		handPlayed = new Object[]{5, Card.Suit.DIAMOND, 5, Card.Suit.SPADE, 5, Card.Suit.CLUB};
		currentPlayerCards = new Object[]{3, Card.Suit.HEART, 3, Card.Suit.SPADE, 
				8, Card.Suit.SPADE, 9, Card.Suit.DIAMOND,
				10, Card.Suit.SPADE, 10, Card.Suit.DIAMOND, 10, Card.Suit.CLUB,
				4, Card.Suit.DIAMOND, 4, Card.Suit.HEART, 4, Card.Suit.CLUB, // not in results
				6, Card.Suit.DIAMOND, 6, Card.Suit.HEART, // not in results
				1, Card.Suit.DIAMOND, 1, Card.Suit.HEART, 1, Card.Suit.SPADE, 1, Card.Suit.CLUB}; // in results (2s)
		result1 = constructHand(new Object[]{10, Card.Suit.DIAMOND, 10, Card.Suit.CLUB, 10, Card.Suit.SPADE});
		expectedResult.add(result1);
		result2 = constructHand(new Object[]{1, Card.Suit.DIAMOND, 1, Card.Suit.CLUB, 1, Card.Suit.HEART, 1, Card.Suit.SPADE});
		expectedResult.add(result2);
		possibleHandsHelper(handPlayed, currentPlayerCards, testNum++, expectedResult);

		// STRAIGHT FLUSHES
		// A-5 spade flush! nothing can beat it
		expectedResult.clear();
		handPlayed = new Object[]{0, Card.Suit.SPADE, 1, Card.Suit.SPADE, 
				2, Card.Suit.SPADE, 3, Card.Suit.SPADE, 4, Card.Suit.SPADE};
		currentPlayerCards = new Object[]{3, Card.Suit.HEART, 3, Card.Suit.SPADE, 
				8, Card.Suit.SPADE, 9, Card.Suit.DIAMOND,
				10, Card.Suit.SPADE, 10, Card.Suit.DIAMOND, 10, Card.Suit.CLUB,
				4, Card.Suit.DIAMOND, 4, Card.Suit.HEART, 4, Card.Suit.CLUB, 
				6, Card.Suit.DIAMOND, 6, Card.Suit.HEART,
				1, Card.Suit.DIAMOND, 1, Card.Suit.HEART, 1, Card.Suit.SPADE, 1, Card.Suit.CLUB}; 
		possibleHandsHelper(handPlayed, currentPlayerCards, testNum++, expectedResult);

		currentPlayerCards = new Object[]{
				0, Card.Suit.HEART, 10, Card.Suit.HEART, 11, Card.Suit.HEART, 12, Card.Suit.HEART, 9, Card.Suit.HEART,
				1, Card.Suit.HEART, 2, Card.Suit.HEART, 3, Card.Suit.HEART, 4, Card.Suit.HEART, // wraps around, should be 2 straights
				6, Card.Suit.DIAMOND, 6, Card.Suit.HEART,
				1, Card.Suit.DIAMOND, 1, Card.Suit.CLUB}; 

		possibleHandsHelper(handPlayed, currentPlayerCards, testNum++, expectedResult);

		handPlayed = new Object[]{6, Card.Suit.SPADE, 7, Card.Suit.SPADE, 
				8, Card.Suit.SPADE, 9, Card.Suit.SPADE, 10, Card.Suit.SPADE};
		result1 = constructHand(new Object[]{9, Card.Suit.HEART, 10, Card.Suit.HEART, 11, Card.Suit.HEART, 12, Card.Suit.HEART,0, Card.Suit.HEART});
		expectedResult.add(result1);
		result2 = constructHand(new Object[]{2, Card.Suit.HEART, 3, Card.Suit.HEART, 4, Card.Suit.HEART, 0, Card.Suit.HEART, 1, Card.Suit.HEART});
		expectedResult.add(result2);
		possibleHandsHelper(handPlayed, currentPlayerCards, testNum++, expectedResult);

		// FOUR OF A KIND
		expectedResult.clear();
		handPlayed = new Object[]{6, Card.Suit.SPADE, 6, Card.Suit.CLUB, 
				6, Card.Suit.HEART, 6, Card.Suit.DIAMOND, 5, Card.Suit.SPADE}; // 7, 7, 7, 7, 6
		// result will contain the whole flush... (which has a straight flush)
		result1 = constructHand(new Object[]{2, Card.Suit.HEART, 3, Card.Suit.HEART, 4, Card.Suit.HEART, 6, Card.Suit.HEART,
				9, Card.Suit.HEART, 10, Card.Suit.HEART, 11, Card.Suit.HEART, 12, Card.Suit.HEART,0, Card.Suit.HEART, 1, Card.Suit.HEART});
		expectedResult.add(result1);
		possibleHandsHelper(handPlayed, currentPlayerCards, testNum++, expectedResult);
		
		expectedResult.clear();
		currentPlayerCards = new Object[]{
				0, Card.Suit.HEART, 10, Card.Suit.HEART, 11, Card.Suit.HEART, 12, Card.Suit.HEART, 9, Card.Suit.HEART,
				1, Card.Suit.HEART, 2, Card.Suit.HEART, 3, Card.Suit.HEART, 4, Card.Suit.HEART, // wraps around, should be 2 straights
				7, Card.Suit.DIAMOND, 7, Card.Suit.HEART,
				7, Card.Suit.SPADE, 7, Card.Suit.CLUB, 
				11, Card.Suit.DIAMOND, 11, Card.Suit.SPADE}; 
		result1 = constructHand(new Object[]{2, Card.Suit.HEART, 3, Card.Suit.HEART, 4, Card.Suit.HEART, 7, Card.Suit.HEART,
				9, Card.Suit.HEART, 10, Card.Suit.HEART, 11, Card.Suit.HEART, 12, Card.Suit.HEART, 0, Card.Suit.HEART, 1, Card.Suit.HEART});
		expectedResult.add(result1);
		result2 = constructHand(new Object[]{7, Card.Suit.DIAMOND, 7, Card.Suit.CLUB, 
				7, Card.Suit.HEART, 7, Card.Suit.SPADE});
		expectedResult.add(result2);
		
		possibleHandsHelper(handPlayed, currentPlayerCards, testNum++, expectedResult);

		// FULL HOUSE
		/**
		 * play: 6, 6, 6, 7, 7
		 * hold: ace, ace, ace, 4, 4, 4, 8, 8, 8, 9, 9, 10, q
		 * expected: {ace, ace, ace}, {4, 4, 4}, {8, 8, 8,}, {9, 9}
		 **/
		expectedResult.clear();
		handPlayed = new Object[]{5, Card.Suit.DIAMOND, 5, Card.Suit.HEART, 5, Card.Suit.SPADE,
				6, Card.Suit.CLUB, 6, Card.Suit.DIAMOND};
		currentPlayerCards = new Object[]{
				0, Card.Suit.DIAMOND, 0, Card.Suit.HEART, 0, Card.Suit.CLUB, 
				3, Card.Suit.DIAMOND, 3, Card.Suit.HEART, 3, Card.Suit.SPADE, 7, Card.Suit.SPADE, 7, Card.Suit.DIAMOND,
				8, Card.Suit.DIAMOND, 8, Card.Suit.SPADE, 9, Card.Suit.CLUB, 11, Card.Suit.HEART};
		result1 = constructHand(new Object[]{0, Card.Suit.DIAMOND, 0, Card.Suit.CLUB, 0, Card.Suit.HEART});
		expectedResult.add(result1);
		result2 = constructHand(new Object[]{7, Card.Suit.DIAMOND, 7, Card.Suit.SPADE});
		expectedResult.add(result2);
		result3 = constructHand(new Object[]{3, Card.Suit.DIAMOND, 3, Card.Suit.HEART, 3, Card.Suit.SPADE});
		expectedResult.add(result3);
		List<Card> result4 = constructHand(new Object[]{8, Card.Suit.DIAMOND, 8, Card.Suit.SPADE});
		expectedResult.add(result4);
		possibleHandsHelper(handPlayed, currentPlayerCards, testNum++, expectedResult);

		/**
		 * play: same
		 * hold: 5, 5, 5, 6, 7, 7, 9, 9, q, q
		 * expected: {}
		 **/
		expectedResult.clear();
		currentPlayerCards = new Object[]{
				4, Card.Suit.DIAMOND, 4, Card.Suit.HEART, 4, Card.Suit.CLUB, 
				5, Card.Suit.DIAMOND, 6, Card.Suit.HEART, 6, Card.Suit.SPADE, 8, Card.Suit.SPADE, 8, Card.Suit.DIAMOND,
				11, Card.Suit.DIAMOND, 11, Card.Suit.HEART};
		possibleHandsHelper(handPlayed, currentPlayerCards, testNum++, expectedResult);

		/**
		 * play: same
		 * hold: 5, 5, 5, 5, 9, 10, j, q, k, 2 (straight present)
		 * expected: {5, 5, 5, 5}
		 **/
		currentPlayerCards = new Object[]{4, Card.Suit.DIAMOND, 4, Card.Suit.CLUB, 4, Card.Suit.HEART, 4, Card.Suit.SPADE,
				8, Card.Suit.DIAMOND, 9, Card.Suit.SPADE, 10, Card.Suit.DIAMOND, 11, Card.Suit.HEART, 12, Card.Suit.DIAMOND};
		result1 = constructHand(new Object[]{4, Card.Suit.DIAMOND, 4, Card.Suit.CLUB, 4, Card.Suit.HEART, 4, Card.Suit.SPADE});
		expectedResult.add(result1);
		possibleHandsHelper(handPlayed, currentPlayerCards, testNum++, expectedResult);
		
		/**
		 * play: same
		 * hold: 8, 8, 8, 8, 9, 9, 10, j, k, 2 (diamond flush present)
		 * expected: {8, 8, 8, 8}, {9, 9} 
		 **/
		expectedResult.clear();
		currentPlayerCards = new Object[]{7, Card.Suit.DIAMOND, 7, Card.Suit.CLUB, 7, Card.Suit.HEART, 7, Card.Suit.SPADE,
				8, Card.Suit.DIAMOND, 8, Card.Suit.HEART, 9, Card.Suit.DIAMOND, 10, Card.Suit.DIAMOND, 1, Card.Suit.DIAMOND};
		result1 = constructHand(new Object[]{7, Card.Suit.DIAMOND, 7, Card.Suit.CLUB, 7, Card.Suit.HEART, 7, Card.Suit.SPADE});
		expectedResult.add(result1);
		result2 = constructHand(new Object[]{8, Card.Suit.DIAMOND, 8, Card.Suit.HEART});
		expectedResult.add(result2);
		possibleHandsHelper(handPlayed, currentPlayerCards, testNum++, expectedResult);
		
		/**
		 * play: same
		 * hold: something with straight flush
		 * expect: everything in that flush
		 */
		expectedResult.clear();
		currentPlayerCards = new Object[]{7, Card.Suit.DIAMOND, 7, Card.Suit.CLUB, 11, Card.Suit.DIAMOND, 12, Card.Suit.DIAMOND,
				8, Card.Suit.DIAMOND, 8, Card.Suit.HEART, 9, Card.Suit.DIAMOND, 10, Card.Suit.DIAMOND, 1, Card.Suit.DIAMOND};
		result1 = constructHand(new Object[]{7, Card.Suit.DIAMOND, 8, Card.Suit.DIAMOND, 
				9, Card.Suit.DIAMOND, 10, Card.Suit.DIAMOND, 11, Card.Suit.DIAMOND, 12, Card.Suit.DIAMOND, 1, Card.Suit.DIAMOND});
		expectedResult.add(result1);
		possibleHandsHelper(handPlayed, currentPlayerCards, testNum++, expectedResult);
		
		// FLUSH
		/**
		 * play: 2, 3, 4, 5, 7 (high 2 flush, HEARTS)
		 * hold: 2, 3, 4, 5, 7 (diamonds), 8, 8, 8
		 * expected: {}
		 **/ 
		 handPlayed = new Object[]{3, Card.Suit.HEART, 6, Card.Suit.HEART,
				 4, Card.Suit.HEART, 2, Card.Suit.HEART, 1, Card.Suit.HEART};
		 currentPlayerCards = new Object[]{3, Card.Suit.DIAMOND, 6, Card.Suit.DIAMOND,
				 4, Card.Suit.DIAMOND, 2, Card.Suit.DIAMOND, 1, Card.Suit.DIAMOND,
				 8, Card.Suit.HEART, 8, Card.Suit.SPADE, 8, Card.Suit.CLUB};
		expectedResult.clear();
		possibleHandsHelper(handPlayed, currentPlayerCards, testNum++, expectedResult);

		 handPlayed = new Object[]{3, Card.Suit.DIAMOND, 6, Card.Suit.DIAMOND,
				 4, Card.Suit.DIAMOND, 2, Card.Suit.DIAMOND, 1, Card.Suit.DIAMOND};
		 currentPlayerCards = new Object[]{3, Card.Suit.HEART, 6, Card.Suit.HEART,
				 4, Card.Suit.HEART, 2, Card.Suit.HEART, 1, Card.Suit.HEART,
				 8, Card.Suit.DIAMOND, 8, Card.Suit.SPADE, 8, Card.Suit.CLUB};
		result1 = constructHand(new Object[]{2, Card.Suit.HEART, 3, Card.Suit.HEART, 
				 4, Card.Suit.HEART, 6, Card.Suit.HEART, 1, Card.Suit.HEART});
		expectedResult.add(result1);
		possibleHandsHelper(handPlayed, currentPlayerCards, testNum++, expectedResult);

		
		/**
		 * play: ace, 10, 9, 8, 6 (heart)
		 * hold: ace, 9, 8, 7, 6 (spades), 11, 12 (non spades)
		 * expected: {}
		 */
		expectedResult.clear();
		handPlayed = new Object[]{0, Card.Suit.HEART, 9, Card.Suit.HEART, 
				8, Card.Suit.HEART, 7, Card.Suit.HEART, 5, Card.Suit.HEART};
		currentPlayerCards = new Object[]{0, Card.Suit.SPADE, 8, Card.Suit.SPADE,
				7, Card.Suit.SPADE, 5, Card.Suit.SPADE, 6, Card.Suit.SPADE, 
				11, Card.Suit.DIAMOND, 12, Card.Suit.HEART};
		possibleHandsHelper(handPlayed, currentPlayerCards, testNum++, expectedResult);

		/**
		 * play: flush (jack high)
		 * hold: 8, 8, 8, 6, 6, xxxxxxx
		 * full house, flush with some of those full house cards that beat it,
		 * flush with some of those full house cards that don't beat it
		 * expected: full house, flush that beats it
		 */
		handPlayed = new Object[]{6, Card.Suit.SPADE, 4, Card.Suit.SPADE, 10, Card.Suit.SPADE,
				8, Card.Suit.SPADE, 3, Card.Suit.SPADE};
		currentPlayerCards = new Object[]{7, Card.Suit.CLUB, 7, Card.Suit.HEART, 7, Card.Suit.DIAMOND,
				5, Card.Suit.CLUB, 5, Card.Suit.HEART, 8, Card.Suit.CLUB, 10, Card.Suit.CLUB,
				2, Card.Suit.DIAMOND, 11, Card.Suit.HEART, 10, Card.Suit.HEART, 4, Card.Suit.HEART};
		result1 = constructHand(new Object[]{7, Card.Suit.DIAMOND, 7, Card.Suit.CLUB, 7, Card.Suit.HEART});
		expectedResult.add(result1);
		result2 = constructHand(new Object[]{4, Card.Suit.HEART, 5, Card.Suit.HEART,7, Card.Suit.HEART, 
				10, Card.Suit.HEART, 11, Card.Suit.HEART});
		expectedResult.add(result2);
		result3 = constructHand(new Object[]{5, Card.Suit.CLUB, 5, Card.Suit.HEART});
		expectedResult.add(result3);
		result4 = constructHand(new Object[]{10, Card.Suit.CLUB, 10, Card.Suit.HEART});
		expectedResult.add(result4);
		possibleHandsHelper(handPlayed, currentPlayerCards, testNum++, expectedResult);
		
		// STRAIGHT
		/**
		 * play: a,2,3,4,5 (spades)
		 * hand: a,2,3,4,5 (diamonds) ,5,5,4 
		 * expected:{5,5,5}, {4,4}
		 */
		expectedResult.clear();
		handPlayed = new Object[]{0, Card.Suit.SPADE, 1, Card.Suit.SPADE,
				2,Card.Suit.SPADE,3,Card.Suit.SPADE,4,Card.Suit.SPADE};
		currentPlayerCards = new Object[]{0, Card.Suit.DIAMOND, 1, Card.Suit.DIAMOND,
				2, Card.Suit.DIAMOND, 3, Card.Suit.DIAMOND, 4, Card.Suit.DIAMOND, 4, Card.Suit.HEART, 4, Card.Suit.CLUB, 
				3, Card.Suit.HEART};
		result1 = constructHand(new Object[]{4, Card.Suit.DIAMOND, 4, Card.Suit.HEART, 4, Card.Suit.CLUB});
		expectedResult.add(result1);
		result2 = constructHand(new Object[]{3, Card.Suit.DIAMOND,3, Card.Suit.HEART});
		expectedResult.add(result2);
		possibleHandsHelper(handPlayed, currentPlayerCards, testNum++, expectedResult);
		
		/**
		 * play: a,2,3,4,5 (diamonds)
		 * hand: a,2,3,4,5,6 (spades), 5,5,4
		 * expected: {a,2,3,4,5}, {5,5,5}, {4,4}
		 */
		
		expectedResult.clear();
		handPlayed = new Object[]{0, Card.Suit.DIAMOND, 1, Card.Suit.DIAMOND,
				2, Card.Suit.DIAMOND, 3, Card.Suit.DIAMOND, 4, Card.Suit.DIAMOND};
		currentPlayerCards = new Object[]{0, Card.Suit.SPADE, 1, Card.Suit.SPADE,
				2,Card.Suit.SPADE,3,Card.Suit.SPADE,4,Card.Suit.SPADE, 5, Card.Suit.SPADE,
				4, Card.Suit.HEART, 4, Card.Suit.CLUB, 3, Card.Suit.HEART};
		result1 = constructHand(new Object[]{0, Card.Suit.SPADE, 
				1, Card.Suit.SPADE, 2, Card.Suit.SPADE,
				3, Card.Suit.SPADE, 4, Card.Suit.SPADE, 
				5, Card.Suit.SPADE});
		expectedResult.add(result1);
		result2 = constructHand(new Object[]{4, Card.Suit.SPADE, 4, Card.Suit.HEART, 4, Card.Suit.CLUB});
		expectedResult.add(result2);
		result3 = constructHand(new Object[]{3, Card.Suit.SPADE, 3, Card.Suit.HEART});
		expectedResult.add(result3);
		possibleHandsHelper(handPlayed, currentPlayerCards, testNum++, expectedResult);

		
		/**
		 * play: a,2,3,4,5 (diamonds)
		 * hand: 2,3,4,5,6 (spades)
		 * expected: {}
		 */
		expectedResult.clear();
		handPlayed = new Object[]{0, Card.Suit.DIAMOND, 1, Card.Suit.DIAMOND,
				2, Card.Suit.DIAMOND, 3, Card.Suit.DIAMOND, 4, Card.Suit.DIAMOND};
		currentPlayerCards = new Object[]{1, Card.Suit.SPADE,
				2,Card.Suit.SPADE,3,Card.Suit.SPADE,4,Card.Suit.SPADE, 5, Card.Suit.SPADE};
		possibleHandsHelper(handPlayed, currentPlayerCards, testNum++, expectedResult);

	}
	
	private static void testHandsWithCard(){
		
		int testNum = 0;
		
		Object[] currentPlayerCards = new Object[]{};
		List<Card> cards = constructHand(currentPlayerCards);
		Card card = cards.get(0);
		Map<PokerHand, List<Card>> potentialHands = BigTwoPlayerUtils.handsThatUseCard(cards, card);
		Map<PokerHand, List<Card>> expectedHands = new HashMap<PokerHand, List<Card>>();
		
		
		
		
	}
	
	private static BigTwoGameContext setUpBigGameContext(final List<Card> cardsPlayed){
		List<BigTwoPlayer> players = new ArrayList<BigTwoPlayer>();
		BigTwoGameContext gameContext = new BigTwoGameContext(players);
		gameContext.setLastPlayedCards(cardsPlayed);
		return gameContext;
	}
	
	private static String handToString(List<Card> hand){
		String str_hand = "";
		for (Card card : hand){
			str_hand = str_hand+card.toString() + ", "; 
		}
		return str_hand;
	}
	
	private static void printFailure(List<Card> hand1, List<Card> hand2, int test_case){
		System.out.println("Test failure: " + test_case);
		System.out.println("Hand1: " + handToString(hand1));
		if (hand2 != null)
			System.out.println("Hand2: " + handToString(hand2));
	}
	
	private static void compareHandHelper(Object[] base1, Object[] base2, int testCase, int expectedResult){
		List<Card> hand1 = constructHand(base1);
		List<Card> hand2 = constructHand(base2);
		int result = BigTwoUtils.compareHands(hand1, hand2);
		if (result != expectedResult){
			boolean correctComparison = false;
			if ((result < 0 && expectedResult < 0) || result > 0 && expectedResult > 0)
				correctComparison = true;
			if (!correctComparison)
				printFailure(hand1, hand2, testCase);
		}
	}
	
	private static void handRecognitionHelper(Object[] base, int testNum, PokerHand expectedResult){
		List<Card> hand = constructHand(base);
		PokerHand identifiedHand = BigTwoUtils.identifyHand(hand);
		boolean correctHand = identifiedHand == expectedResult;
		if (!correctHand){
			printFailure(hand, null, testNum);
		}
	}
	
	private static void straightDetectionHelper(Object[] base, int testNum, int[][] expectedResult){
		List<Card> hand = constructHand(base);
		List<List<Card>> straights = BigTwoUtils.findBigTwoStraight(hand);
		boolean correctStraights = true;
		for (int i = 0; i < straights.size(); i++){
			List<Card> straight = straights.get(i);
			int[] expected = expectedResult[i];
			for (int j = 0; j < straight.size(); j++){
				Card card = straight.get(j);
				if (card.getRank() != expected[j]){
					correctStraights =false;
					break;
				}
			}
		}
		if (!correctStraights){
			printFailure(hand, null, testNum);
		}
	}
	
	private static void possibleHandsHelper(Object[] handPlayed, Object[] playerCards, int testNum, List<List<Card>> expectedResult){
		List<Card> cardsPlayed = constructHand(handPlayed);
		BigTwoGameContext gameContext = setUpBigGameContext(cardsPlayed);
		Player player = new ComputerBigTwoPlayer("Player1");
		List<Card> playerCardsList = constructHand(playerCards);
		player.assignCard(playerCardsList);
		List<List<Card>> results = new ArrayList<List<Card>>();
		player.canPlay(gameContext, results);

		// compare results...
		boolean equalResults = expectedResult.size() == results.size();
		if (equalResults){
			// go hand by hand and make sure they are the same...
			boolean foundExpected = false;
			for (int i = 0; i < expectedResult.size(); i++){
				List<Card> expected = expectedResult.get(i);
				for (int j = 0; j < results.size(); j++){
					List<Card> actual = results.get(j);
					foundExpected = expected.equals(actual);
					if (foundExpected)
						break;
				}

				if (!foundExpected){
					equalResults = false;
					break;
				}
			} 
		}
		if (!equalResults){
			printFailure(cardsPlayed, playerCardsList, testNum);
			System.out.println("--- expected results ---");
			for (List<Card> expected : expectedResult){
				System.out.println("******");
				System.out.println(handToString(expected));
			}
			System.out.println("---- actual results -----");
			for (List<Card> result : results){
				System.out.println("******");
				System.out.println(handToString(result));
			}
		}
	}
	
	private static void handsWithCardHelper(Map<PokerHand, List<Card>> results, 
			Map<PokerHand, List<Card>> expected, int testNum){
		boolean equalResults = results.size() == expected.size();
		Map<PokerHand, List<List<Card>>> failedHands = new HashMap<PokerHand, List<List<Card>>>();
		if (equalResults){
			for (PokerHand pokerHand : results.keySet()){
				List<Card> result = results.get(pokerHand);
				List<Card> exp = expected.get(pokerHand);
				if (!result.equals(exp)){
					equalResults = false;
					List<List<Card>> hands = new ArrayList<List<Card>>();
					hands.add(exp);
					hands.add(result);
					failedHands.put(pokerHand, hands);
					break;
				}
			}
		}
		
		if (!equalResults){
			if (failedHands.isEmpty()){
				// not even the same size!
				
			} else {
				
			}
		}
	}
	
	private static List<Card> constructHand(Object[] cards){
		List<Card> hand = new ArrayList<Card>();
		for (int i = 0; i < cards.length; i++){
			int value = ((Integer)cards[i]).intValue();
			Card.Suit suit = (Card.Suit)cards[i+1];
			hand.add(new BigTwoCard(value, suit));
			i++; 
		}
		return hand;
	}

}
