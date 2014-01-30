package casino.ui.poker.bigtwo;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import casino.deck.Card;
import casino.ui.utils.CasinoUIUtils;

public class BigTwoCardsComposite {

	boolean showCards;
	boolean registerSelections;
	Composite cardsComposite;
	List<CardSelectionListener> cardListeners;
	
	/**
	 * 
	 * @param parent
	 * @param showCards
	 * @param registerSelections
	 * @param cards
	 */
	public BigTwoCardsComposite(Composite parent, 
			boolean showCards, boolean registerSelections, 
			List<? extends Card> cards){
		this.cardsComposite = parent;
		this.showCards = showCards;
		this.registerSelections = registerSelections;
		cardListeners = new ArrayList<CardSelectionListener>();
		layout(cards);
	}
	
	public void layout(List<? extends Card> cards){
		for (Control card : cardsComposite.getChildren()){
			card.dispose(); // clears all current cards/buttons
		}
		for (Card card : cards){
			final Button cardButton = new Button(cardsComposite, SWT.PUSH);
			cardButton.setData(card);
			Image cardImage;
			if (showCards){
				cardImage = CasinoUIUtils.getCardImage(card);
			} else {
				cardImage = CasinoUIUtils.getCardImage(card);
			}
			cardButton.setImage(cardImage);
			cardButton.setBackground(null);
			cardButton.setLayoutData(new GridData());
			
			// add selection listener to label for human players
			if (registerSelections){
				cardButton.addSelectionListener(new SelectionAdapter(){
					@Override
					public void widgetSelected(SelectionEvent e) {
						Button clickedButton = (Button)e.getSource();
						Color newColor;
						boolean selected = !CasinoUIUtils.COLOR_RED.equals(clickedButton.getBackground());
						if (!selected){
							// unselected card
							newColor = null;
						} else {
							// selecting card
							newColor = CasinoUIUtils.COLOR_RED;
						}
						clickedButton.setBackground(newColor);
						for (CardSelectionListener listener : cardListeners){
							listener.cardSelected((Card)cardButton.getData(), selected);
						}
					}
				});
			}
		}
		cardsComposite.layout();
	}
	
	public void addCardSelectionListener(CardSelectionListener listener){
		cardListeners.add(listener);
	}
}
