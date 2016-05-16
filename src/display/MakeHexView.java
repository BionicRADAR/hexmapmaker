package display;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import controller.MakeHexController;

public class MakeHexView extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final int DEF_PANEL_WIDTH = 1200;
	private static final int DEF_PANEL_HEIGHT = 800;
	private static final int DEF_WIDTH = 1215;
	private static final int DEF_HEIGHT = 838;
	private JPanel hexPanel;
	private JMenuBar makeHexBar;
	private int ratioWidth;
	private int ratioHeight;
	private BufferedImage image;
	private MakeHexController controller;
	
	public MakeHexView(MakeHexController controller) {
		super();
		this.controller = controller;
	}
	
	public void init() {
		setLayout(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(DEF_WIDTH, DEF_HEIGHT);
		makeHexBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		fileMenu.setMnemonic('f');
		JMenuItem newItem = new JMenuItem("New Map");
		newItem.setMnemonic('n');
		newItem.addActionListener(new NewMapListener());
		fileMenu.add(newItem);
		JMenuItem loadMapItem = new JMenuItem("Load Map");
		loadMapItem.setMnemonic('l');
		loadMapItem.addActionListener(new LoadMapListener(this));
		fileMenu.add(loadMapItem);
		JMenuItem makeHexItem = new JMenuItem("Make New Hex");
		makeHexItem.setMnemonic('m');
		makeHexItem.addActionListener(new MakeHexListener(this));
		fileMenu.add(makeHexItem);
		JMenuItem closeItem = new JMenuItem("Close");
		closeItem.setMnemonic('c');
		closeItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.close();
			}
		});
		fileMenu.add(closeItem);
		JMenuItem exitItem = new JMenuItem("Exit");
		exitItem.setMnemonic('x');
		exitItem.addActionListener(new ExitListener());
		fileMenu.add(exitItem);
		makeHexBar.add(fileMenu);
		JMenu hexMakerMenu = new JMenu();
		hexMakerMenu.setText("Hex Maker");
		hexMakerMenu.setMnemonic('h');
		JMenuItem resetItem = new JMenuItem();
		resetItem.setText("Reset Image");
		resetItem.setMnemonic('r');
		resetItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controller.reset();
			}
		});
		hexMakerMenu.add(resetItem);
		JMenuItem hexListItem = new JMenuItem();
		hexListItem.setText("Add to Hex List");
		hexListItem.setMnemonic('a');
		hexListItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String name = JOptionPane.showInputDialog(MakeHexView.this, "Name the hex: ", "Name Hex", JOptionPane.PLAIN_MESSAGE);
				if (name == null)
					return;
				if (name == "") {
					JOptionPane.showMessageDialog(MakeHexView.this, "Error: invalid filename", "Bad File", JOptionPane.ERROR_MESSAGE);
					return;
				}
				File toSave = new File("assets/hexes/" + name + ".png");
				while(toSave.exists()) {
					int confirm = JOptionPane.showConfirmDialog(MakeHexView.this, 
							"A hex by this name already exists. Would you like to replace it?",
							"Name Taken", JOptionPane.YES_NO_CANCEL_OPTION);
					if (confirm == JOptionPane.YES_OPTION) {
						break;
					}
					if (confirm == JOptionPane.NO_OPTION) {
						toSave = new File(JOptionPane.showInputDialog(MakeHexView.this, "Name the hex: ", "Name Hex", JOptionPane.PLAIN_MESSAGE));
					}
					else if (confirm == JOptionPane.CANCEL_OPTION) {
						return;
					}
				}
				controller.addHextoList(toSave);
			}
		});
		hexMakerMenu.add(hexListItem);
		makeHexBar.add(hexMakerMenu);
		this.setJMenuBar(makeHexBar);
		hexPanel = new JPanel() {
			public void paint(Graphics g) {
				g.setColor(Color.white);
				g.fillRect(0, 0, getWidth(), getHeight());
				g.drawImage(image, 0, 0, ratioWidth, ratioHeight, null);
			}
		};
		hexPanel.addMouseListener(new MouseListener() {
			private Point center;
			
			@Override
			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				if (arg0.getButton() == 1)
					center = arg0.getPoint();
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				if (center != null && arg0.getButton() == 1) {
					controller.makeHex(center, arg0.getPoint());
				}
			}
			
		});
		hexPanel.setBounds(0, 0, DEF_PANEL_WIDTH, DEF_PANEL_HEIGHT);
		add(hexPanel);
	}
	
	public void setImage(BufferedImage image, int width, int height) {
		ratioWidth = width;
		ratioHeight = height;
		this.image = image;
		repaint();
	}
	
	public void paint(Graphics g) {
		hexPanel.repaint();
		makeHexBar.repaint();
	}
	
	public int getPanelWidth() {
		return hexPanel.getWidth();
	}
	
	public int getPanelHeight() {
		return hexPanel.getHeight();
	}
}