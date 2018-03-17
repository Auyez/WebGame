package Game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.tuple.Pair;

public class GameWorld {
	private List<Actor> actors;
	private List<Player> players;
	private GameArena ga;
	
	public GameWorld() {
		actors = new ArrayList<Actor>();
		players = new ArrayList<Player>();
		ga = new GameArena("map.txt");
	}
	
	public void addPlayer(int id) {
		int w = 15;
		int h = 30;
		int lh = 10;
		
		Player p = null;
		Random r = new Random();
		int x,y;
		do {
			x = r.nextInt(ga.getWidth());
			y = r.nextInt(ga.getHeight());
			
			if(p != null)
				p.setPosition(x, y);
			else
				p = new Player(x, y, w, h, lh, id, this);		
		}while(!(	( (x + w) < ga.getWidth()  ) && ( (y + h) < ga.getHeight() ) && !p.collides() 	));
		actors.add(p);
		players.add(p);
	}

	public void removePlayer(int id) {
	    Player player = getPlayer(id);
	    actors.remove(player);
	    players.remove(player);
    }

	public GameArena getArena() {return ga;}
	public List<Actor> getActors(){return actors;}
	public List<Player> getPlayers(){return players;}
	
	public Player getPlayer(int id) {
		for (Player p : players) {
			if (p.getId() == id) {
				return p;
			}
		}
		return null;
	}
}
