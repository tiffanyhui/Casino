package casino.ui.utils;

import java.io.InputStream;
import java.util.Collections;
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
import org.eclipse.swt.widgets.Display;

import casino.deck.Card;
import casino.player.HumanPlayer;
import casino.player.Player;
import casino.utils.CasinoUtils;

public class CasinoUIUtils {

	private static String IMAGE_FORMAT = ".jpg";
	public static Color COLOR_RED = Display.getCurrent().getSystemColor(SWT.COLOR_RED);

	public static Image getCardImage(Card card){
		int rank = card.getRank();
		Card.Suit suit = card.getSuit();
		String imgName = getCardImageName(rank, suit);
		InputStream inputStream = CasinoUIUtils.class.getClassLoader().getResourceAsStream("casino/ui/img/"+imgName);
		if (inputStream != null){
			Image cardImage = new Image(Display.getCurrent(), inputStream);
			return cardImage;
		}
		return null;
	}
	
	public static Image getCardBack(){
		InputStream inputStream = CasinoUIUtils.class.getClassLoader().getResourceAsStream("casino/ui/img/back.jpg");
		return new Image(Display.getCurrent(), inputStream);
	}
	
	private static String getCardImageName(int rank, Card.Suit suit){
		int imgRank = rank+1; // no off by 1 in card name
		String imgSuit = suit.toString().toLowerCase();
		return imgRank + imgSuit + IMAGE_FORMAT;//
	}
	public static void addPlayerCards(Composite screen, Player player){
		addCards(screen, player.getCards(), player);
	}
	
	public static void addCards(Composite screen, List<? extends Card> cards, final Player player){
		for (Control card : screen.getChildren()){
			card.dispose(); // clears all current cards/buttons
		}
		boolean isComputer = player != null && CasinoUtils.isComputer(player);
		for (Card card : cards){
			final Button cardButton = new Button(screen, SWT.PUSH);
			cardButton.setData(card);
			Image cardImage;
			if (!isComputer){
				cardImage = CasinoUIUtils.getCardImage(card);
			} else {
				cardImage = CasinoUIUtils.getCardBack();
			}
			cardButton.setImage(cardImage);
			cardButton.setBackground(null);
			cardButton.setLayoutData(new GridData());
			
			// add selection listener to label for human players
			if (!isComputer && player != null){
				cardButton.addSelectionListener(new SelectionAdapter(){
					@SuppressWarnings("unchecked")
					@Override
					public void widgetSelected(SelectionEvent e) {
						HumanPlayer<Card> humanPlayer = (HumanPlayer<Card>)player;
						Button clickedButton = (Button)e.getSource();
						Color newColor;
						if (COLOR_RED.equals(clickedButton.getBackground())){
							// unselected card
							newColor = null;
							humanPlayer.removeSelectedCards(Collections.singletonList((Card)cardButton.getData()));
						} else {
							// selecting card
							newColor = COLOR_RED;
							humanPlayer.addSelectedCards(Collections.singletonList((Card)cardButton.getData()));
						}
						clickedButton.setBackground(newColor);
					}
				});
			}
		}
		screen.layout();
	}
}
