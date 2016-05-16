package display;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

public class CloseListener implements ActionListener {
	
	private JFrame parent = null;

	public CloseListener(JFrame parent) {
		super();
		this.parent = parent;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		parent.dispatchEvent(new WindowEvent(parent, WindowEvent.WINDOW_CLOSING));
	}

}
