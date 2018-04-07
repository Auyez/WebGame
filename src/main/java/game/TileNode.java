package game;

import java.util.ArrayList;

public class TileNode implements Comparable<TileNode>{
	private int x;
	private int y;
	private int g;
	private double h;
	private double f;
	private TileNode parent;
	
	public TileNode(int x, int y, TileNode parent) {
		this.x = x;
		this.y = y;
		this.parent = parent;
	}

	@Override
	public int compareTo(TileNode instance) {
		return (int) Math.ceil(this.f - instance.f);
	}
	
	public TileNode getParent() {return parent;}
	public double getF() {return f;}
	public int getG() {return g;}
	public double getH() {return h;}
	public int getX() {return x;}
	public int getY() {return y;}
	public Vec2 getCoordinates() {return new Vec2(x, y);}
	public void setG(int g) {this.g = g;}
	public void setH(double h) {this.h = h;}
	public void setF() {this.f = this.g + this.h;}
	public void setParent(TileNode parent) {this.parent = parent;}
	public boolean isSame(TileNode inst) {
		if (this.x == inst.getX() && this.y == inst.getY()) {
			return true;
		} else {
			return false;
		}
	}
	
	public ArrayList<TileNode> generateSuccessors(int[][] map) {
		ArrayList<TileNode> result = new ArrayList<TileNode>();
		int map_x = this.x;
		int map_y = this.y;
		if (map_x - 1 >= 0 && map[map_y][map_x - 1] == 0) {
			TileNode left = new TileNode(map_x - 1, map_y, this);
			result.add(left);
		}
		if (map_x + 1 < 40 && map[map_y][map_x + 1] == 0) {
			TileNode right = new TileNode(map_x + 1, map_y, this);
			result.add(right);
		}
		if (map_y + 1 < 30 && map[map_y + 1][map_x] == 0) {
			TileNode top = new TileNode(map_x, map_y + 1, this);
			result.add(top);
		}
		if (map_y - 1 >= 0 && map[map_y - 1][map_x] == 0) {
			TileNode bottom = new TileNode(map_x, map_y - 1, this);
			result.add(bottom);
		}
		
		return result;
	}

	public TileNode convert() {
		TileNode converted = new TileNode((this.x * 30) + 5, (this.y * 30) - 15, this.parent);

		return converted;
	}

}
