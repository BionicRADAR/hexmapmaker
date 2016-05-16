package controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

import javax.imageio.ImageIO;

import mapmodel.HexList;

public class HexListController {

	private static HexListController instance = new HexListController();
	private String hexDirectory = "assets/hexes";
	private String fileExtension = ".png";
	
	private HexListController() {
		for (File file : new File(hexDirectory).listFiles(new FilenameFilter(){
			@Override
			public boolean accept(File arg0, String arg1) {
				if (arg1.substring(arg1.length() - 4).equals(fileExtension))
					return true;
				return false;
			}
		})) {
			try {
				HexList.getInstance().addHex(ImageIO.read(file), file.getName());
			} catch (IOException e) {
				System.err.println("Failed to load hex: " + file.getName() + "in HexListController");
				e.printStackTrace();
			}
		}
	}
	
	public static void init() {}
	
	public static HexListController getInstance() {
		return instance;
	}
	
	public void addHex(BufferedImage hex, File hexFile) {
		try {
			ImageIO.write(hex, "png", hexFile);
		} catch (IOException e) {
			System.err.println("Failed to write hex " + hexFile.getName() + " image to file in HexListController");
			e.printStackTrace();
		}
		HexList.getInstance().addHex(hex, hexFile.getName());
	}
	
}
