package display;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFileChooser;

import controller.HexMapController;

public class LoadMapListener implements ActionListener {

	private Component parent;
	
	public LoadMapListener(Component parent) {
		this.parent = parent;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		JFileChooser chooser = new JFileChooser("assets/maps");
		int accept = chooser.showOpenDialog(parent);
		if (accept == JFileChooser.APPROVE_OPTION) {
			new HexMapController(chooser.getSelectedFile());
		}
	}

}
