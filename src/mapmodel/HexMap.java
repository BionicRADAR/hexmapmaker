package mapmodel;

public class HexMap {
	
	private int[][] hexes;
	private int width;
	private int height;
	
	public HexMap(int width, int height) {
		hexes = new int[height][width];
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++)
				hexes[j][i] = -1;
		}
		this.width = width;
		this.height = height;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public int getHex(int x, int y) {
		return hexes[y][x];
	}
	
	public void setHex(int newHex, int x, int y) {
		if (x >= 0 && y >= 0 && x < width && y < height)
			hexes[y][x] = newHex;
	}
}
