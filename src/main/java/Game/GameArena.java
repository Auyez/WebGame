package Game;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
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
			collision_map = new int[h][w];	
			
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
	
	public int getEntry(int y, int x) {return collision_map[y][x];}
	public int getWidth() {return w * tileSize;}
	public int getHeight() {return h * tileSize;}
	public int getTilesWidth() {return w;}
	public int getTilesHeight() {return h;}
	public int getTileSize() {return tileSize;}
	
	// Return sequence of target coordinates
	public ArrayList<TileNode> aStar(int x_init, int y_init, int x_target, int y_target) {
		ArrayList<TileNode> open = new ArrayList<TileNode>();
		ArrayList<TileNode> closed = new ArrayList<TileNode>();
		boolean toSkip = false;
		
		TileNode initTile = new TileNode(x_init, y_init, null);
		initTile.setG(0);
		initTile.setH(0);
		initTile.setF();
		open.add(initTile);
		
		while(!open.isEmpty()) {
			Collections.sort(open);
			TileNode q = open.remove(0);
			ArrayList<TileNode> successors = q.generateSuccessors(collision_map);
			
			for(TileNode i : successors) {
				
				if(i.getX() == x_target && i.getY() == y_target) {
					closed.add(i);
					return generateTargets(closed);
				}
				i.setG(q.getG() + 1);
				i.setH(Math.pow(Math.abs(i.getX() - x_target), 2) + Math.pow(Math.abs(i.getY() - y_target), 2));
				i.setF();
				
				toSkip = false;
				
				for (TileNode j : open) {
					if (j.isSame(i)) {
						if (j.getF() <= i.getF()) {
							toSkip = true;
							break;
						}
					}
				}
				for (TileNode j : closed) {
					if (j.isSame(i)) {
						if (j.getF() <= i.getF()) {
							
							toSkip = true;
							break;
						}
					}
				}
				if (!toSkip) {
					open.add(i);
				}
			}
			closed.add(q);
		}
		
		return closed;
	}

	private static ArrayList<TileNode> generateTargets(ArrayList<TileNode> tiles) {
		ArrayList<TileNode> turns = new ArrayList<TileNode>();
		TileNode target = tiles.get(tiles.size() - 1);
		turns.add(target);
		
		TileNode check = target.getParent().getParent();
		while (check != null) {
			// Detect turn
			if (check.getX() != target.getX() && check.getY() != target.getY()) {
				turns.add(target.getParent());
			}
			target = target.getParent();
			check = check.getParent();
		}
		
		return turns;
	}
}
