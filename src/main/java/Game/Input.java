package Game;

public class Input {
	private boolean[] keys;
	
	public Input() {
		keys = new boolean[255];
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
}
