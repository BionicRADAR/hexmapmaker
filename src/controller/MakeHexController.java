package controller;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;

import display.MakeHexView;
import imagemodel.MakeHexModel;
import imagemodel.NullHexException;

public class MakeHexController {
	
	private MakeHexModel model;
	private MakeHexView view;
	private DimPair viewDims;
	private File imgFile;
	
	public MakeHexController(File imgFile) {
		this.imgFile = imgFile;
		model = new MakeHexModel(imgFile);
		view = new MakeHexView(this);
		view.init();
		BufferedImage image = model.getImage();
		viewDims = DimPair.newDims(image.getWidth(), image.getHeight(), view.getPanelWidth(), view.getPanelHeight());
		view.setImage(image, viewDims.width(), viewDims.height());
		view.setTitle("Hex Maker - " + imgFile.getName());
		view.setVisible(true);
	}
	
	public void addHextoList(File destination) {
		try {
			HexListController.getInstance().addHex(model.getFinalHex(), destination);
		} catch (NullHexException e) {
			e.printStackTrace();
		}
	}
	
	public void makeHex(Point center, Point end) {
		double ratio = 0;
		int actualWidth = model.getImage().getWidth();
		int actualHeight = model.getImage().getHeight();
		if (actualHeight > actualWidth) {
			ratio = ((double) actualHeight) / ((double) viewDims.height());
		} else {
			ratio = ((double) actualWidth) / ((double) viewDims.width());
		}
		double xchange = end.getX() - center.getX();
		double ychange = end.getY() - center.getY();
		model.drawHex(center.getX() * ratio, center.getY() * ratio, ratio * Math.sqrt(xchange * xchange + ychange * ychange));
		view.repaint();
	}
	
	public void reset() {
		model = new MakeHexModel(imgFile);
		view.setImage(model.getImage(), viewDims.width(), viewDims.height());
	}
	
	public void close() {
		view.dispose();
	}
}
