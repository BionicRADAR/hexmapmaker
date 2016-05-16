package display;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import controller.HexMapController;

public class MapWindow extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BufferedImage mapImage, hexesImage;
	private int displayWidth;
	private int displayHeight;
	private int hexesWidth;
	private int hexesHeight;
	private JScrollPane mapPane, hexesPane;
	private HexMapController controller;
	
	private int defaultWidth = 1200;
	private int defaultHeight = 800;

	public void init(HexMapController controller) {
		this.controller = controller;
		setBounds(20, 20, defaultWidth, defaultHeight);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		Container p = getContentPane();
		p.setLayout(new GridBagLayout());
		addMapPanel(p);
		addHexesPanel(p);
		addMenuBar();
		addWindowListener(controller.getCloser());
		setVisible(true);
	}
	
	private void addMapPanel(Container p) {
		GridBagConstraints c = new GridBagConstraints();
		c.weightx = 0.8;
		c.weighty = 1.0;
		c.gridx = 0;
		c.gridy = 0;
		c.fill = GridBagConstraints.BOTH;
		c.gridwidth = 1;
		c.gridheight = 1;
		JPanel mapPanel = new JPanel() {
			public void paintComponent (Graphics g) {
				setPreferredSize(new Dimension(displayWidth, displayHeight));
				mapPane.revalidate();
				g.setColor(Color.white);
				g.fillRect(0, 0, getWidth(), getHeight());
				g.drawImage(mapImage, 0, 0, displayWidth, displayHeight, null);
			}
			
			
		};
		mapPanel.setPreferredSize(new Dimension(50, 30));
		mapPanel.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (arg0.getButton() == MouseEvent.BUTTON1)
					controller.handleLeftClick(arg0.getX(), arg0.getY());
				else
					controller.handleRightClick(arg0.getX(), arg0.getY());
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
				// TODO Auto-generated method stub
				
			}
			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		mapPane = new JScrollPane(mapPanel);
		//JPanel hist = new JPanel();
		//hist.add(history);
		p.add(mapPane, c);
	}
	
	private void addHexesPanel(Container p) {
		GridBagConstraints c = new GridBagConstraints();
		c.weightx = 0.2;
		c.weighty = 1.0;
		c.gridx = 1;
		c.gridy = 0;
		c.fill = GridBagConstraints.BOTH;
		c.gridwidth = 1;
		c.gridheight = 1;
		JPanel hexesPanel = new JPanel() {
			public void paintComponent(Graphics g) {
				setPreferredSize(new Dimension(getWidth(), hexesHeight));
				hexesPane.revalidate();
				g.setColor(Color.white);
				g.fillRect(0, 0, getWidth(), getHeight());
				g.drawImage(hexesImage, 0, 0, hexesWidth, hexesHeight, null);
			}
		};
		hexesPanel.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				controller.changeHex(arg0.getY());
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
				// TODO Auto-generated method stub
				
			}
			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		hexesPane = new JScrollPane(hexesPanel);
		hexesPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		p.add(hexesPane, c);
	}
	
	private void addMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		JMenu mapMaker = new JMenu("Map Maker");
		JMenuItem newMap = new JMenuItem("New Map");
		newMap.setMnemonic('n');
		newMap.addActionListener(new NewMapListener());
		mapMaker.add(newMap);
		JMenuItem load = new JMenuItem("Load Map");
		load.setMnemonic('l');
		load.addActionListener(new LoadMapListener(this));
		mapMaker.add(load);
		JMenuItem save = new JMenuItem("Save Map");
		save.setMnemonic('s');
		save.addActionListener(new HexSaveListener(this, controller.getSaver(), "assets/maps"));
		mapMaker.add(save);
		JMenuItem export = new JMenuItem("Export Map as Image");
		export.setMnemonic('e');
		export.addActionListener(new HexSaveListener(this, controller.getExporter(), null));
		mapMaker.add(export);
		JMenuItem makeHex = new JMenuItem("Make New Hex");
		makeHex.setMnemonic('m');
		makeHex.addActionListener(new MakeHexListener(this));
		mapMaker.add(makeHex);
		JMenuItem close = new JMenuItem("Close");
		close.setMnemonic('c');
		close.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.close();
			}
		});
		mapMaker.add(close);
		JMenuItem exit = new JMenuItem("Exit");
		exit.setMnemonic('x');
		exit.addActionListener(new ExitListener());
		mapMaker.add(exit);
		menuBar.add(mapMaker);
		setJMenuBar(menuBar);
	}
	
	public void setMapImage(BufferedImage mapImage, int width, int height) {
		this.mapImage = mapImage;
		displayWidth = width;
		displayHeight = height;
		repaint();
	}
	
	public void setHexesImage(BufferedImage hexesImage, int width, int height) {
		this.hexesImage = hexesImage;
		hexesWidth = width;
		hexesHeight = height;
		repaint();
	}
}
