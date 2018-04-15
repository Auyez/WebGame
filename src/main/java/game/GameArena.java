package game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class GameArena {
	private static final int INF = 999999999;
	
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
			s.close();
		}catch(Exception e) {
			System.out.println("GameArena::GameArena exception");
		}
	}
	
	public int getEntry(int y, int x) {
		if (0 <= x && x < w && 0 <= y && y < h)
			return collision_map[y][x];
		return 1;
	}
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
				i.setH(Math.sqrt(Math.pow(Math.abs(i.getX() - x_target), 2) + Math.pow(Math.abs(i.getY() - y_target), 2)));
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

	private ArrayList<TileNode> generateTargets(ArrayList<TileNode> tiles) {
		ArrayList<TileNode> turns = new ArrayList<TileNode>();
		TileNode target = tiles.get(tiles.size() - 1);
		turns.add(target.convert());
		TileNode check = target.getParent().getParent();
		while (check != null) {
			// Detect turn
			if (check.getX() != target.getX() && check.getY() != target.getY()) {
				turns.add(target.getParent().convert());
			}
			target = target.getParent();
			check = check.getParent();
		}
		turns.add(target.getParent().convert());
		/*
		for (int i = 0; i < turns.size() - 1; i++) {
			turns.get(i).setParent(turns.get(i + 1));
		}
		turns.get(turns.size() - 1).setParent(null);
		*/
		
		return turns;
	}
	
	public ArrayList<TileNode> raytrace(double x0, double y0, double x1, double y1)
	{
		ArrayList<TileNode> result = new ArrayList<TileNode>();
		
	    double dx = Math.abs(x1 - x0);
	    double dy = Math.abs(y1 - y0);

	    int x = (int) (Math.floor(x0));
	    int y = (int) (Math.floor(y0));

	    int n = 1;
	    int x_inc, y_inc;
	    double error;

	    if (dx == 0)
	    {
	        x_inc = 0;
	        error = INF;
	    }
	    else if (x1 > x0)
	    {
	        x_inc = 1;
	        n += (int)(Math.floor(x1)) - x;
	        error = (Math.floor(x0) + 1 - x0) * dy;
	    }
	    else
	    {
	        x_inc = -1;
	        n += x - (int)(Math.floor(x1));
	        error = (x0 - Math.floor(x0)) * dy;
	    }

	    if (dy == 0)
	    {
	        y_inc = 0;
	        error -= INF;
	    }
	    else if (y1 > y0)
	    {
	        y_inc = 1;
	        n += (int)(Math.floor(y1)) - y;
	        error -= (Math.floor(y0) + 1 - y0) * dx;
	    }
	    else
	    {
	        y_inc = -1;
	        n += y - (int)(Math.floor(y1));
	        error -= (y0 - Math.floor(y0)) * dx;
	    }

	    for (; n > 0; --n)
	    {
	    	result.add(new TileNode(x, y, null));

	        if (error > 0)
	        {
	            y += y_inc;
	            error -= dx;
	        }
	        else
	        {
	            x += x_inc;
	            error += dy;
	        }
	    }
	    
	    return result;
	}

	public boolean checkCollision(float x0, float y0, int x_target, int y_target) {
		boolean collides = false;
		
		float x1 = (float)x_target;
		float y1 = (float)y_target;
		// TODO make player width, height and lower height constants in Player.java ?
		float dy1 = 20;
		float dy2 = 40;
		float dx = 20;
		
		ArrayList<TileNode> lineTiles = new ArrayList<TileNode>();
		// Checks all four lines from each corner of hit-box
		lineTiles.addAll(raytrace((x0)/tileSize, (y0 + dy1)/tileSize, (x1)/tileSize, (y1)/tileSize));
		lineTiles.addAll(raytrace((x0)/tileSize, (y0 + dy2)/tileSize, (x1)/tileSize, (y1 + dy1)/tileSize));
		lineTiles.addAll(raytrace((x0 + dx)/tileSize, (y0 + dy1)/tileSize, (x1 + dx)/tileSize, (y1)/tileSize));
		lineTiles.addAll(raytrace((x0 + dx)/tileSize, (y0 + dy2)/tileSize, (x1 + dx)/tileSize, (y1 + dy1)/tileSize));
		
		for (TileNode i : lineTiles) {
			if (collision_map[i.getY()][i.getX()] == 1) {
				collides = true;
			}
		}
		
		return collides;
	}
}
