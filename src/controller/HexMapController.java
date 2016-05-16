package controller;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import mapmodel.HexList;
import mapmodel.HexMap;
import display.HexSaveListener;
import display.MapWindow;

public class HexMapController {

	private MapWindow view;
	private BufferedImage mapImage, hexListImage;
	private HexMap model;
	private int controllerNumber, currentHex;
	private int side, width, height, sideToCenter, hexListSide, hexListWidth, hexListHeight, hexListSideToCenter;
	private static ArrayList<HexMapController> hexMapControllers = new ArrayList<HexMapController>();
	private static final int defaultWidth = 25;
	private static final int defaultHeight = 29;
	private static final int defaultSide = 64;
	private static final int defaultHexListWidth = 1024;
	private static final int defaultHexListHeight = 384;
	private static final int defaultHexListSide = defaultHexListHeight / 3;
	private static final double SIDE_TO_CENTER_RATIO = Math.sqrt(3.0)/2.0;
	private static final double HEX_SLOPE = 1.0 / Math.sqrt(3.0);
	private int viewRatio = 4;
	private int hexListViewRatio = 4;
	private Saver saver, exporter;
	private WindowListener closer;
	private boolean closeSaveComplete = false;
	
	public static void init(){}
	
	public static Iterator<HexMapController> getHexMapControllers() {
		return hexMapControllers.iterator(); 
	}
	
	public static void exit() {
		for (HexMapController c : hexMapControllers) {
			c.closeWithoutRemove();
		}
		System.exit(0);
	}
	
	public HexMapController() {
		model = new HexMap(defaultWidth, defaultHeight);
		initController();
		view.setTitle("Hex Map Maker");
	}
	
	public HexMapController(File source) {
		load(source);
		initController();
		view.setTitle("Hex Map Maker - " + source.getName());
	}
	
	public void initController() {
		controllerNumber = hexMapControllers.size();
		hexMapControllers.add(this);
		currentHex = 0;
		view = new MapWindow();
		saver = new Saver() {
			public void save(File target) {
				HexMapController.this.save(target);
			}
		};
		exporter = new Saver() {
			public void save(File target) {
				export(target);
			}
		};
		closer = new WindowListener(){
			@Override
			public void windowActivated(WindowEvent e) {}
			@Override
			public void windowClosed(WindowEvent e) {}
			@Override
			public void windowClosing(WindowEvent e) {
				close();
			}
			@Override
			public void windowDeactivated(WindowEvent e) {}
			@Override
			public void windowDeiconified(WindowEvent e) {}
			@Override
			public void windowIconified(WindowEvent e) {}
			@Override
			public void windowOpened(WindowEvent e) {}
			
		};
		view.init(this);
		side = defaultSide;
		width = defaultWidth;
		height = defaultHeight;
		sideToCenter = (int) Math.round(((double) side) * SIDE_TO_CENTER_RATIO);
		mapImage = new BufferedImage(2 * width * sideToCenter, (int) Math.round(1.5 * ((double) (side * (1 + height)))), BufferedImage.TYPE_INT_ARGB);
		drawMap();
		hexListWidth = defaultHexListWidth;
		hexListHeight = defaultHexListHeight;
		hexListSide = defaultHexListSide;
		hexListSideToCenter = (int) Math.round(((double) hexListSide) * SIDE_TO_CENTER_RATIO);
		hexListImage = new BufferedImage(hexListWidth, HexList.getInstance().size() * hexListHeight, BufferedImage.TYPE_INT_ARGB);
		drawHexList();
	}
	
	public void changeHex(int y) {
		int newHex = (y * hexListViewRatio) / hexListHeight;
		if (newHex >= 0 && newHex < HexList.getInstance().size())
			currentHex = newHex;
		drawHexList();
	}
	
	public void handleRightClick(int x, int y) {
		handleClick(x, y, -1);
	}
	
	public void handleLeftClick(int x, int y) {
		handleClick(x, y, currentHex);
	}
	
	private void handleClick(int x, int y, int hex) {
		IntPair hexLoc = getClickedHex(x, y);
		if (hexLoc != null) { 
			model.setHex(hex, hexLoc.x(), hexLoc.y());
			refresh();
		}
	}
	
	private IntPair getClickedHex(int x, int y) {
		int yZone = (viewRatio * y) / (3 * side);
		int ySub = (viewRatio * y) % (3 * side);
		int yZoneType = ySub / (side / 2);
		if (yZoneType == 0) {
			ySub %= (side / 2);
			int xSub = ((x * viewRatio) - sideToCenter) % sideToCenter;
			if (xSub < 0) 
				return null;
			int xZone = ((x * viewRatio) - sideToCenter) / sideToCenter;
			if (xZone % 2 == 0) {
				int mapX = xZone / 2;
				if (ySub < ((double) side / 2.0) - HEX_SLOPE * xSub) {
					return new IntPair(mapX,  2 * yZone - 1);
				} else {
					return new IntPair(mapX, 2 * yZone);
				}
			} else {
				if (ySub < HEX_SLOPE * xSub) {
					return new IntPair(xZone / 2 + 1, 2 * yZone - 1);
				} else {
					return new IntPair(xZone / 2, 2 * yZone);
				}
			}
		} else if (yZoneType < 3) {
			return new IntPair(((x * viewRatio) - sideToCenter) / (2 * sideToCenter), 2 * yZone);
		} else if (yZoneType == 3) {
			ySub %= (side / 2);
			int xSub = (x * viewRatio) % sideToCenter;
			int xZone = (x * viewRatio) / sideToCenter;
			if (xZone % 2 == 0) {
				if (ySub < ((double) side / 2.0) - HEX_SLOPE * xSub) {
					return new IntPair(xZone / 2 - 1, 2 * yZone);
				} else {
					return new IntPair (xZone / 2, 2 * yZone + 1);
				}
			} else {
				int mapX = xZone / 2;
				if (ySub < HEX_SLOPE * xSub) {
					return new IntPair(mapX, 2 * yZone);
				} else {
					return new IntPair(mapX, 2 * yZone + 1);
				}
			}
		} else {
			return new IntPair((x * viewRatio) / (2 * sideToCenter), 2 * yZone + 1);
		}
	}
	
	public void close() {
		if (closeWithoutRemove() == 0)
			hexMapControllers.remove(controllerNumber);
	}
	
	private int closeWithoutRemove() {
		closeSaveComplete = false;
		int option = JOptionPane.showConfirmDialog(view, "Do you want to save the map?", "Closing Map", JOptionPane.YES_NO_CANCEL_OPTION);
		if (option == JOptionPane.YES_OPTION) {
			new HexSaveListener(view, saver, "assets/maps").actionPerformed(null);
			if (!closeSaveComplete)
				return closeWithoutRemove();
		}
		else if (option == JOptionPane.CANCEL_OPTION) {
			return 1;
		}
		view.dispose();
		return 0;
	}
	
	public void refresh() {
		drawMap();
		drawHexList();
	}
	
	private void drawMap() {
		Graphics2D g = mapImage.createGraphics();
		g.setColor(Color.white);
		g.fillRect(0, 0, mapImage.getWidth(), mapImage.getHeight());
		g.setColor(Color.black);
		g.setStroke(new BasicStroke(viewRatio));
		int vertShift = 3 * side;
		int horizShift = 2 * sideToCenter;
		int maxY = height / 2 + height % 2;
		for (int y = 0; y < maxY; y++) {
			int top = vertShift * y;
			int bot = top + 2 * side;
			for (int x = 0; x < width; x++) {
				int left = sideToCenter + horizShift * x;
				int right = left + horizShift;
				drawHex(g, HexList.getInstance().getHex(model.getHex(x, 2 * y)), top, left, right, bot, side);
			}
		}
		int topEdge = (int) Math.round(1.5 * ((double) side));
		maxY = height / 2;
		for (int y = 0; y < maxY; y++) {
			int top = topEdge + vertShift * y;
			int bot = top + 2 * side;
			for (int x = 0; x < width; x++) {
				int left = horizShift * x;
				int right = left + horizShift;
				drawHex(g, HexList.getInstance().getHex(model.getHex(x, 2 * y + 1)), top, left, right, bot, side);
			}
		}
		g.dispose();
		view.setMapImage(mapImage, mapImage.getWidth() / viewRatio, mapImage.getHeight() / viewRatio);
	}
	
	private void drawHex (Graphics g, BufferedImage hex, int top, int left, int right, int bot, int side) {
		if (hex != null)
			g.drawImage(hex, left, top, right - left, bot - top, null);
		outlineHex(g, top, left, right, bot, side);
	}
	
	private void outlineHex (Graphics g, int top, int left, int right, int bot, int side) {
		int high = top + (int) Math.round(((double) side)/2.0);
		int low = bot - (int) Math.round(((double) side)/2.0);
		int mid = left + sideToCenter;
		g.drawLine(left, high, mid, top);
		g.drawLine(mid, top, right, high);
		g.drawLine(right, high, right, low);
		g.drawLine(right, low, mid, bot);
		g.drawLine(mid, bot, left, low);
		g.drawLine(left, low, left, high);
	}
	
	private void drawHexList() {
		Graphics2D g = hexListImage.createGraphics();
		g.setColor(Color.white);
		g.fillRect(0, 0, hexListImage.getWidth(), hexListImage.getHeight());
		g.setColor(Color.black);
		g.setStroke(new BasicStroke(2 * hexListViewRatio));
		for (int i = 0; i < HexList.getInstance().size(); i++) {
			if (i == currentHex) {
				g.setColor(Color.blue);
				g.fillRect(0, i * hexListHeight, hexListImage.getWidth(), hexListHeight);
				g.setColor(Color.black);
			}
			g.drawRect(0, i * hexListHeight, hexListImage.getWidth(), hexListHeight);
			g.drawImage(HexList.getInstance().getHex(i), hexListImage.getWidth() / 2 - hexListSideToCenter, 
						i * hexListHeight + hexListHeight / 6, 2 * hexListSideToCenter, 2 * hexListSide, null);
		}
		g.dispose();
		view.setHexesImage(hexListImage, hexListImage.getWidth() / hexListViewRatio, hexListImage.getHeight() / hexListViewRatio);
	}
	
	public void updateHexList() {
		hexListImage = new BufferedImage(hexListWidth, HexList.getInstance().size() * hexListHeight, BufferedImage.TYPE_INT_ARGB);
		drawHexList();
	}
	
	public void save(File target) {
		Writer output = null;
		try {
			output = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(target), "utf-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Iterator<String> it = HexList.getInstance().getHexNames();
		try {
			output.write(HexList.getInstance().size() + "\n");
			while (it.hasNext()) {
				output.write(it.next() + "\n");
			}
			output.write(model.getWidth() + " ");
			output.write(model.getHeight() + "\n");
			for (int j = 0; j < model.getHeight(); j++) {
				for (int i = 0; i < model.getWidth(); i++) {
					output.write(model.getHex(i, j) + " ");
				}
				output.write("\n");
			}
			output.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} finally {
			try {
				output.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		view.setTitle("Hex Map Maker - " + target.getName());
		closeSaveComplete = true;
	}
	
	public void load(File source) {
		Scanner s = null;
		try {
			s = new Scanner(source);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int numHexTypes = s.nextInt();
		s.nextLine();
		int[] hexConverter = new int[numHexTypes];
		for (int i = 0; i < numHexTypes; i++) {
			String temp = s.nextLine();
			hexConverter[i] = HexList.getInstance().findNumberOfName(temp);
		}
		int width = s.nextInt();
		int height = s.nextInt();
		model = new HexMap(width, height);
		for (int j = 0; j < height; j++) {
			for (int i = 0; i < width; i++) {
				int nextHex = s.nextInt();
				if (nextHex == -1) {
					model.setHex(-1, i, j);
				}
				else {
					model.setHex(hexConverter[nextHex], i, j);
				}
			}
		}
		s.close();
	}
	
	public void export(File target) {
		try {
			ImageIO.write(mapImage, "png", target);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Saver getSaver() {return saver;}
	public Saver getExporter() {return exporter;}
	public WindowListener getCloser() {return closer;}
}
