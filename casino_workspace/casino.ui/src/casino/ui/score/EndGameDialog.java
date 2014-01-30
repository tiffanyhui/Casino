package casino.ui.score;

import java.util.List;

import org.eclipse.jface.dialogs.TrayDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import casino.deck.Card;
import casino.game.GameContext;
import casino.player.Player;

public class EndGameDialog extends TrayDialog {

	GameContext<Card> gameContext;
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public EndGameDialog(Shell shell, GameContext gameContext) {
		super(shell);
		this.gameContext = gameContext;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite parentComp = (Composite)super.createDialogArea(parent);
		FillLayout fillLayout = new FillLayout();
		parentComp.setLayout(fillLayout);
		Table table = new Table(parentComp, SWT.BORDER);
		table.setLinesVisible(true);
		table.setHeaderVisible (true);
		TableColumn playerColumn = new TableColumn(table, SWT.NONE);
		playerColumn.setText("Player");
		TableColumn scoreColumn = new TableColumn(table, SWT.NONE);
		scoreColumn.setText("Score");
		TableColumn totalColumn = new TableColumn(table, SWT.NONE);
		totalColumn.setText("Total");

		List<? extends Player> players = gameContext.getPlayers();
		int[] scoreChanges = gameContext.getEndGameScoreChanges();
		for (int i = 0; i < players.size()-1; i++){
			Player player = players.get(i);
			TableItem item = new TableItem(table, SWT.NONE);
			item.setText(new String[]{player.getName(), ""+scoreChanges[i], ""+player.getScore()});
		}
		
		playerColumn.pack();
		scoreColumn.pack();
		totalColumn.pack();
		
		return parentComp;
	}
	
	public int openDialog(){
		return open();
	}
}
