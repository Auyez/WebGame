package Game;

public class Input {
	private boolean[] 	keys;
	private Vec2		mouse;
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
	
	public Vec2 getMouse() {return mouse;}
	public void setMouse(int x, int y) {mouse = new Vec2(x, y);}
	public void clrMouse() {mouse = null;}
}
