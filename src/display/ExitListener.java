package display;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import controller.HexMapController;

public class ExitListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent arg0) {
		HexMapController.exit();
	}

}
