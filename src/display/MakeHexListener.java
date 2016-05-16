package display;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFileChooser;

import controller.MakeHexController;

public class MakeHexListener implements ActionListener {
	
	private Component parent;
	
	public MakeHexListener(Component parent) {
		this.parent = parent;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JFileChooser chooser = new JFileChooser("assets");
		int accept = chooser.showOpenDialog(parent);
		if (accept == JFileChooser.APPROVE_OPTION) {
			new MakeHexController(chooser.getSelectedFile());
		}
	}

}
