package mapmodel;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;

import controller.HexMapController;

public class HexList {

	private static HexList instance = new HexList();
	private ArrayList<BufferedImage> hexes;
	private ArrayList<String> hexNames;
	
	public static HexList getInstance() {
		return instance;
	}
	
	private HexList() {
		hexes = new ArrayList<BufferedImage>();
		hexNames = new ArrayList<String>();
	}
	
	public void addHex(BufferedImage hex, String hexName) {
		hexes.add(hex);
		hexNames.add(hexName);
		Iterator<HexMapController> it = HexMapController.getHexMapControllers();
		if (!it.hasNext())
			return;
		HexMapController c;
		for (c = it.next(); it.hasNext(); c = it.next())
			c.updateHexList();
		c.updateHexList();
	}
	
	public Iterator<BufferedImage> getHexes() {
		return hexes.iterator();
	}
	
	public Iterator<String> getHexNames() {
		return hexNames.iterator();
	}
	
	public BufferedImage getHex(int index) {
		if (index < 0 || index >= hexes.size())
			return null;
		return hexes.get(index);
	}
	
	public int findNumberOfName(String name) {
		return hexNames.indexOf(name);
	}
	
	public int size() {
		return hexes.size();
	}
}
