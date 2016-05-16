package imagemodel;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class MakeHexModel {

	private BufferedImage image;
	private BufferedImage toSave;
	
	public MakeHexModel(File imageSource) {
		try {
			BufferedImage temp = ImageIO.read(imageSource);
			image = new BufferedImage(temp.getWidth(), temp.getHeight(), BufferedImage.TYPE_INT_ARGB);
			Graphics2D g = image.createGraphics();
			g.drawImage(temp, 0, 0, image.getWidth(), image.getHeight(), null);
			g.dispose();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void drawHex(double centerX, double centerY, double side) {
		int[] ys = new int[6];
		int[] xs = new int[6];
		double halfSide = side/2;
		double sideToCenter = side * Math.sqrt(3)/2;
		int botY = (int) Math.round(centerY + side);
		int highY = (int) Math.round(centerY - halfSide);
		int lowY = (int) Math.round(centerY + halfSide);
		int topY = (int) Math.round(centerY - side);;
		int leftX = (int) Math.round(centerX - sideToCenter);
		int rightX = (int) Math.round(centerX + sideToCenter);
		int midX = (int) Math.round(centerX);
		ys[0] = topY;
		ys[1] = highY;
		ys[2] = lowY;
		ys[3] = botY;
		ys[4] = lowY;
		ys[5] = highY;
		xs[0] = midX;
		xs[1] = leftX;
		xs[2] = leftX;
		xs[3] = midX;
		xs[4] = rightX;
		xs[5] = rightX;
		Graphics2D imageGraphics = image.createGraphics();
		imageGraphics.setComposite(AlphaComposite.Clear);
		imageGraphics.fillRect(0, 0, image.getWidth(), ys[0]);
		imageGraphics.fillRect(0, 0, xs[1], image.getHeight());
		imageGraphics.fillRect(xs[4], 0, image.getWidth()-xs[4], image.getHeight());
		imageGraphics.fillRect(0, ys[3], image.getWidth(), image.getHeight()-ys[3]);
		double slope = 1.0/Math.sqrt(3);
		for (int i = 0; i < xs[0] - xs[1]; i++) {
			double vertchange = ((double) i) * slope;
			int height = (int) Math.round(((double) ys[1]) - vertchange);
			imageGraphics.fillRect(xs[1] + i, 0, 1, height);
			imageGraphics.fillRect(xs[1] + i, (int) (Math.floor((double) ys[4]) + vertchange), 1, height);
			imageGraphics.fillRect(xs[4] - (i + 1), 0, 1, height);
			imageGraphics.fillRect(xs[4] - (i + 1), (int) (Math.floor((double) ys[4]) + vertchange), 1, height);
		}
		imageGraphics.dispose();
		toSave = new BufferedImage(rightX - leftX, botY - topY, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = toSave.createGraphics();
		g.drawImage(image, 0, 0, toSave.getWidth(), toSave.getHeight(), leftX, topY, rightX, botY, null);
	}
	
	public BufferedImage getImage() {
		return image;
	}
	
	public BufferedImage getFinalHex() throws NullHexException {
		if (toSave != null) 
			return toSave;
		else
			throw new NullHexException("Error in MakeHexModel.getFinalHex(): no hex to add (hex is null)");
	}
	
	public int writeImage(File destination) {
		try {
			if (toSave != null) {
				ImageIO.write(toSave, "png", destination);
				return 0;
			}
			else
				return 1;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}
}
