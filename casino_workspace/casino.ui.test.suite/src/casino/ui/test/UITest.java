package casino.ui.test;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import casino.deck.Card;
import casino.deck.Deck;
import casino.ui.utils.CasinoUIUtils;


public class UITest {
	
	public static void main(String[] args){
		Display display = new Display ();
		Shell shell = new Shell (display);
		shell.setLayout(new GridLayout());
		shell.setText("All cards");

		Composite parentComp = new Composite(shell, SWT.NONE);
		parentComp.setLayout(new GridLayout(13, true));
		parentComp.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		Deck deck = new Deck();
		List<Card> cards = deck.getCards();
		
		CasinoUIUtils.addCards(parentComp, cards, null);
		
		shell.pack();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) display.sleep();
		}
		display.dispose();
	}
}
