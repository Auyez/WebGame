package Game;

import java.io.File;
import java.util.Scanner;
import javafx.util.Pair;

public class GameArena {
	private int[][] collision_map;
	private int w, h, tileSize;
	
	public GameArena(String fileName) {
		//File file = new File(fileName);
		try{
			Scanner s = new Scanner(ExampleMap.getMap());
			String[] params = s.nextLine().split(" ");
			
			w = Integer.parseInt(params[0]);
			h = Integer.parseInt(params[1]);
			tileSize = Integer.parseInt(params[2]);
			collision_map = new int[w][h];	
			
			for(int i = 0; s.hasNextLine(); i++) {
				String[] line = s.nextLine().split(" ");
				for(int j = 0; j < line.length; j++) {
					collision_map[i][j] = Integer.parseInt(line[j]);
				}
			}
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public boolean collides(Actor a) {
		for(Pair<Integer, Integer> p : a.getLowerBoxPoints()) {
			if(collision_map[p.getKey()/tileSize][p.getValue()/tileSize] == 1)
				return true;
		}
		return false;
	}
	public int getWidth() {return w * tileSize;}
	public int getHeight() {return h * tileSize;}
}
