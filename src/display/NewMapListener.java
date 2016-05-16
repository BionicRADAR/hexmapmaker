package display;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import controller.HexMapController;

public class NewMapListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		new HexMapController();
	}

}
