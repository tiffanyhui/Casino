package casino.ui.poker.bigtwo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import casino.deck.Card;
import casino.player.Player;
import casino.poker.bigtwo.BigTwoCard;
import casino.poker.bigtwo.player.BigTwoPlayer;
import casino.poker.bigtwo.player.ComputerBigTwoPlayer;
import casino.poker.bigtwo.player.HumanBigTwoPlayer;
import casino.poker.bigtwo.score.EndGameUtils;
import casino.poker.bigtwo.utils.BigTwoGameContext;
import casino.ui.poker.bigtwo.player.BigTwoPlayerComposite;
import casino.ui.score.EndGameDialog;
import casino.utils.CasinoUtils;

public class BigTwoTable implements BigTwoHumanListener{

	public BigTwoGameContext gameContext;
	BigTwoCardsComposite playedHandComp;
	Map<Player, BigTwoPlayerComposite> playerToCardComposite;
	
	ScrolledComposite screen;
	Composite screenContent;
	
	public BigTwoTable(Shell shell, BigTwoGameContext gameContext){
		this.gameContext = gameContext;
		init(shell);
	}
	
	private void init(Shell shell){
		screen = new ScrolledComposite(shell, SWT.H_SCROLL|SWT.V_SCROLL|SWT.BORDER);
		screen.setExpandHorizontal(true);
		screen.setExpandVertical(true);
		screen.setLayout(new GridLayout(1, true));
		screen.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		screenContent = new Composite(screen, SWT.NONE);
		screenContent.setLayout(new GridLayout(1, true));
		screenContent.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		playerToCardComposite = new HashMap<Player, BigTwoPlayerComposite>();
		
		List<BigTwoPlayer> players = gameContext.getPlayers();
		
		for (BigTwoPlayer player : players){
			BigTwoPlayerComposite playerComposite = new BigTwoPlayerComposite(screenContent, player);
			playerComposite.addHumanListener(this);
			playerComposite.activate(false);
			playerToCardComposite.put(player, playerComposite);
		}
		
		// bold the player with action
		BigTwoPlayer actionPlayer = (BigTwoPlayer) gameContext.callNext();
		updatePlayerControl(null, actionPlayer);
		
		// create cards played composite
		Composite playedArea = new Composite(screenContent, SWT.BORDER);
		playedArea.setLayout(new GridLayout(1, true));
		GridData playedAreaData = new GridData(SWT.FILL, SWT.FILL, true, true);
		playedArea.setLayoutData(playedAreaData);
		Label playedLabel = new Label (playedArea, SWT.NONE);
		GridData playedLabelData = new GridData(SWT.CENTER, SWT.TOP, true, false);
		playedLabelData.horizontalSpan = 1;
		playedLabel.setLayoutData(playedLabelData);
		playedLabel.setText("Played Hand:");
		Composite playedHandParentComp = new Composite(playedArea, SWT.NONE);
		playedHandParentComp.setLayout(new GridLayout(5, true));
		GridData playedHandData = new GridData(SWT.FILL, SWT.FILL, true, true);
		playedHandData.minimumHeight = 200;
		playedHandParentComp.setLayoutData(playedHandData);
		playedHandComp = new BigTwoCardsComposite(playedHandParentComp, true, false, new ArrayList<BigTwoCard>());
		
		screen.setContent(screenContent);
		screen.setMinSize(screenContent.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		
	}
	
	public void refreshCardsPlayed(){
		Collections.sort(gameContext.getLastPlayedCards(), BigTwoCard.getRankSorter());
		playedHandComp.layout(gameContext.getLastPlayedCards());
	}
	
	public void updatePlayerControl(BigTwoPlayer previousPlayer, BigTwoPlayer nextPlayer){
		if (previousPlayer != null){
			BigTwoPlayerComposite actionPlayerComposite = playerToCardComposite.get(previousPlayer);
			actionPlayerComposite.refreshCardsComp();
			actionPlayerComposite.activate(false);
		}
		if (nextPlayer != null){
			BigTwoPlayerComposite actionPlayerComposite = playerToCardComposite.get(nextPlayer);
			actionPlayerComposite.activate(true);
		}
	}
	
	/**
	 * Actually starts the event chaining for the game
	 */
	public void start(){
		if (CasinoUtils.isComputer(gameContext.getCurrentPlayer())){
			ComputerBigTwoPlayer computerPlayer = (ComputerBigTwoPlayer)gameContext.getCurrentPlayer();
			computerPlay(computerPlayer);
		}
	}
	
	@Override
	public void humanPlayed(HumanBigTwoPlayer humanPlayer, List<BigTwoCard> cardsPlayed) {
		refreshCardsPlayed();
		updateNextPlayer(humanPlayer);
	}

	@Override
	public void humanPassed(HumanBigTwoPlayer humanPlayer) {
		updateNextPlayer(humanPlayer);
		refreshCardsPlayed(); 
		// call after updateNextPlayer! might be the event that 
		// all players passed and need to reset 'last hand' composite
	}
	
	private void updateNextPlayer(BigTwoPlayer previousPlayer){
		// first check if game is over!
		if (gameContext.isGameOver()){
			// tally up the scores and do some animation!
			updatePlayerControl(previousPlayer, null);
			EndGameUtils.tallyDeduction(gameContext);
			EndGameDialog dialog = new EndGameDialog(screen.getShell(), gameContext);
			dialog.open();
			// TODO restart game with same players!
		} else {
			BigTwoPlayer nextPlayer = gameContext.callNext();
			updatePlayerControl(previousPlayer, nextPlayer);
			screen.layout();
			if (CasinoUtils.isComputer(nextPlayer)){
				ComputerBigTwoPlayer computerPlayer = (ComputerBigTwoPlayer)nextPlayer;
				computerPlay(computerPlayer);
			}
		}
	}
	
	private void computerPlay(ComputerBigTwoPlayer computerPlayer){
		List<List<Card>> possibleHands = new ArrayList<List<Card>>();
		if (computerPlayer.canPlay(gameContext, possibleHands)){
			List<Card> playedCards = computerPlayer.play(gameContext, possibleHands);
			List<BigTwoCard> bigTwoCards = new ArrayList<BigTwoCard>();
			for (Card card : playedCards){
				bigTwoCards.add((BigTwoCard)card);
			}
			if (gameContext.continuePlay(computerPlayer, bigTwoCards)){
				refreshCardsPlayed();
				updateNextPlayer(computerPlayer);
			}
		} else {
			updateNextPlayer(computerPlayer);
			refreshCardsPlayed(); // call after updateNextPlayer
		}
	}
}
