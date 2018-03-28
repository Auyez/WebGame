package Game;

public class TileNode implements Comparable<TileNode>{
	private int x;
	private int y;
	private int g;
	private int h;
	private int f;
	
	public TileNode(int x, int y, int g, int h) {
		this.x = x;
		this.y = y;
		this.g = g;
		this.h = h;
		this.f = g + h;
	}

	@Override
	public int compareTo(TileNode instance) {
		return this.f - instance.f;
	}
	
	
}
