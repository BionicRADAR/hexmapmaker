package controller;

public class DimPair {

	private int width, height;
	
	private DimPair(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	public int width() {return width;}
	public int height() {return height;}
	
	public static DimPair newDims(int sourceWidth, int sourceHeight, int destWidth, int destHeight) {
		double heightRatio = ((double) destHeight) / ((double) sourceHeight);
		double fitHeightWidth = ((double) sourceWidth) * heightRatio;
		if (fitHeightWidth <= destWidth)
			return new DimPair((int) Math.round(fitHeightWidth), destHeight);
		double widthRatio = ((double) destWidth) / ((double) sourceWidth);
		double fitWidthHeight = ((double) sourceHeight) * widthRatio;
		if (fitWidthHeight <= destHeight)
			return new DimPair(destWidth, (int) Math.round(fitWidthHeight));
		System.err.println("Error in DimPair.newDims(): neither dimension fit worked, which shouldn't be possible");
		return new DimPair(sourceWidth, sourceHeight);
	}
	
}
