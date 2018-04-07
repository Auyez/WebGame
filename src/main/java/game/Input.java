package game;

import java.util.ArrayList;

public class Input {
	private boolean[] 	keys;
	private ArrayList<Vec2>		mouse;
	public Input() {
		keys = new boolean[255];
		mouse = null;
	}
	
	public void press(int c) {
		keys[c] = true;
	}
	
	public void releaseAll() {
		for (int i = 0; i < 255; i++)
			keys[i] = false;
	}
	
	public boolean isKeyDown(char c) {
		return keys[(int) c];
	}
	
	public Vec2 getMouse() {
		if (mouse == null) {
			return null;
		}
		if (mouse.size() > 0) {
			return mouse.get(mouse.size() - 1);
		} else {
			return null;
		}
	}
	public Vec2 getNextTarget() {
		if (mouse.size() > 0) {
			return mouse.remove(mouse.size() - 1);
		} else {
			return null;
		}
	}
	public void setMouse(ArrayList<TileNode> sequence) {
		for (TileNode i : sequence) {
			mouse.add(new Vec2(i.getX(), i.getY()));
		}
	}
	
	public void setDestination(int x, int y) {
		mouse = new ArrayList<Vec2>();
		mouse.add(new Vec2(x, y));
	}
	
	public void clrMouse() {mouse = null;}
}
