package casino.ui.poker.bigtwo.player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;

import casino.deck.Card;
import casino.poker.bigtwo.BigTwoCard;
import casino.poker.bigtwo.player.BigTwoPlayer;
import casino.poker.bigtwo.player.HumanBigTwoPlayer;
import casino.poker.bigtwo.utils.BigTwoGameContext.BigTwoSortOrder;
import casino.poker.bigtwo.utils.BigTwoUtils;
import casino.poker.utils.PokerHand;
import casino.ui.poker.bigtwo.BigTwoCardsComposite;
import casino.ui.poker.bigtwo.BigTwoHumanListener;
import casino.ui.poker.bigtwo.CardSelectionListener;
import casino.utils.CasinoUtils;

public class BigTwoPlayerComposite implements CardSelectionListener{
	
	BigTwoPlayer player;
	
	List<BigTwoHumanListener> bigTwoHumanListeners;
	
	// Card related variables
	BigTwoCardsComposite cardsComp;
	
	// Button related variables
	GridData buttonCompData;
	Composite buttonComp;
	Button playButton;
	Button passButton;
	
	Composite playerComp;
	
	public BigTwoPlayerComposite(Composite parent, BigTwoPlayer player){
		this.player = player;
		bigTwoHumanListeners = new ArrayList<BigTwoHumanListener>();
		init(parent);
	}
	
	private void init(Composite screen){
		playerComp = new Composite(screen, SWT.BORDER);
		playerComp.setLayout(new GridLayout(1, false));
		playerComp.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		Label playerName = new Label(playerComp, SWT.NONE);
		playerName.setText(player.getName());
		GridData labelData = new GridData(SWT.LEFT, SWT.TOP, true, false);
		labelData.horizontalSpan = 13;
		playerName.setLayoutData(labelData);

		Composite cardsParentComp = new Composite(playerComp, SWT.NONE);
		cardsParentComp.setLayout(new GridLayout(13, true));
		cardsParentComp.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		cardsComp = new BigTwoCardsComposite(cardsParentComp, 
				!CasinoUtils.isComputer(player), 
				!CasinoUtils.isComputer(player), player.getCards());
		if (!CasinoUtils.isComputer(player)){
			cardsComp.addCardSelectionListener(this);
		}
		
		// create composite for buttons (Play, Pass, Resort) if not computer
		if (!CasinoUtils.isComputer(player)){
			final HumanBigTwoPlayer humanPlayer = (HumanBigTwoPlayer) player;
			buttonComp = new Composite(playerComp, SWT.NONE);
			buttonComp.setLayout(new GridLayout(3, false));
			buttonCompData = new GridData(SWT.CENTER, SWT.BOTTOM, true, false);
			buttonCompData.horizontalSpan = 13;
			buttonComp.setLayoutData(buttonCompData);
			playButton = new Button (buttonComp, SWT.PUSH);
			playButton.setText("PLAY");
			playButton.addSelectionListener(new SelectionAdapter(){
				@Override
				public void widgetSelected(SelectionEvent e) {
					// assume selected cards can be played
					// remove selected cards from player cards
					humanPlayer.playSelectedCards();
					if (BigTwoPlayer.context.continuePlay(player, humanPlayer.getSelectedCards())){
						for (BigTwoHumanListener listener : bigTwoHumanListeners){
							listener.humanPlayed(humanPlayer, humanPlayer.getSelectedCards());
						}
					}
					humanPlayer.clearSelectedCards();
				}

			});
			passButton = new Button(buttonComp, SWT.PUSH);
			passButton.setText("PASS");
			passButton.addSelectionListener(new SelectionAdapter(){
				@Override
				public void widgetSelected(SelectionEvent e) {
					for (BigTwoHumanListener listener : bigTwoHumanListeners){
						listener.humanPassed(humanPlayer);
					}
					// clear the selected cards (if any)
					humanPlayer.clearSelectedCards();
				}

			});
			Button resortButton = new Button(buttonComp, SWT.PUSH);
			resortButton.setText("RE-SORT");
			resortButton.addSelectionListener(new SelectionAdapter(){

				@Override
				public void widgetSelected(SelectionEvent e) {
					humanPlayer.clearSelectedCards();
					if (humanPlayer.sortOrder == BigTwoSortOrder.SUIT){
						Collections.sort(humanPlayer.getCards(), BigTwoCard.getRankSorter());
						humanPlayer.sortOrder = BigTwoSortOrder.RANK;
					} else {
						Collections.sort(humanPlayer.getCards(), BigTwoCard.getSuitSorter());
						humanPlayer.sortOrder = BigTwoSortOrder.SUIT;
					}
					cardsComp.layout(humanPlayer.getCards());
				}

			});
		}
	}

	public void refreshCardsComp(){
		// check if selected cards can be played
		cardsComp.layout(player.getCards());
	}
	
	public void activate(boolean activate){
		if (playerComp != null){
			if (activate){
				playerComp.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_GREEN));
				if (buttonComp != null){
					buttonCompData.exclude = false;
					buttonComp.setVisible(true);
					
				}
				if (!CasinoUtils.isComputer(player)){
					updateButtons((HumanBigTwoPlayer)player);
				}
			} else {
				playerComp.setBackground(null);
				if (buttonComp != null){
					buttonCompData.exclude = true;
					buttonComp.setVisible(false);
				}
			}
			playerComp.layout();
		}
	}
	
	public void addHumanListener(BigTwoHumanListener listener){
		bigTwoHumanListeners.add(listener);
	}
	
	public void removeHumanListener(BigTwoHumanListener listener){
		bigTwoHumanListeners.remove(listener);
	}

	@Override
	public void cardSelected(Card card, boolean selected) {
		if (!CasinoUtils.isComputer(player)){
			HumanBigTwoPlayer humanPlayer = (HumanBigTwoPlayer)player;
			List<BigTwoCard> cards = new ArrayList<BigTwoCard>();
			cards.add((BigTwoCard)card);
			if (selected){
				humanPlayer.addSelectedCards(cards);
			} else {
				humanPlayer.removeSelectedCards(cards);
			}
			// update buttons based on selection change
			updateButtons(humanPlayer);
		}
	}

	private void updateButtons(HumanBigTwoPlayer humanPlayer){
		// enable play button based on whether or not human player can play currently selected hand
		List<BigTwoCard> lastPlayedCards = BigTwoPlayer.context.getLastPlayedCards();
		List<BigTwoCard> selectedCards = humanPlayer.getSelectedCards();
		if (lastPlayedCards != null){
			if (!lastPlayedCards.isEmpty() && !selectedCards.isEmpty()){
				playButton.setEnabled(BigTwoUtils.compareHands(lastPlayedCards, selectedCards) < 0);
				passButton.setEnabled(true);
			} else {
				// human has not selected any cards, disable play
				playButton.setEnabled(!selectedCards.isEmpty());
				// no previous cards selected (player has control), disable pass
				passButton.setEnabled(!lastPlayedCards.isEmpty());
			}
		} else {
			// human has control of the board, but make sure the selected cards are valid before player can 'play'
			PokerHand selectedHand = BigTwoUtils.identifyHand(selectedCards);
			playButton.setEnabled(selectedHand != null);
		}
	}
	
}
