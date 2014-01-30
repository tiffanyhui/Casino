package casino.ui.test.poker.bigtwo;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import casino.test.poker.bigtwo.TestBigTwo;
import casino.test.poker.bigtwo.TestBigTwoGamePlay;
import casino.ui.poker.bigtwo.BigTwoTable;

public class TestBigTwoUIGamePlay {

	public static void main(String[] args) {
		Display display = new Display ();
		Shell shell = new Shell (display);
		GridLayout shellLayout = new GridLayout();
		shellLayout.makeColumnsEqualWidth = true;
		shell.setLayout(shellLayout);
		shell.setText("BIG TWO");
		BigTwoTable table = new BigTwoTable(shell, TestBigTwoGamePlay.gameContext);
		
		shell.pack();
		shell.open();
		
		table.start();
	
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) display.sleep();
		}
		display.dispose();
	}

}
