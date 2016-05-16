package display;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import controller.Saver;

public class HexSaveListener implements ActionListener {

	private Component parent;
	private Saver saver;
	private String directory;
	
	public HexSaveListener(Component parent, Saver saver, String directory) {
		this.parent = parent;
		this.saver = saver;
		this.directory = directory;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		JFileChooser chooser;
		if (directory != null)
			chooser = new JFileChooser(directory);
		else
			chooser = new JFileChooser();
		int accept = chooser.showSaveDialog(parent);
		if (accept == JFileChooser.APPROVE_OPTION) {
			while (chooser.getSelectedFile().exists()) {
				int confirm = JOptionPane.showConfirmDialog(chooser, 
						"A file by this name already exists. Would you like to replace it?",
						"File Name Taken", JOptionPane.YES_NO_CANCEL_OPTION);
				if (confirm == JOptionPane.YES_OPTION) {
					break;
				}
				if (confirm == JOptionPane.NO_OPTION) {
					accept = chooser.showSaveDialog(parent);
					if (accept != JFileChooser.APPROVE_OPTION)
						return;
				}
				else if (confirm == JOptionPane.CANCEL_OPTION) {
					return;
				}
			}
			saver.save(chooser.getSelectedFile());
		}
	}
}
