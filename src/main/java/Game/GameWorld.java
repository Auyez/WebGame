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
		Random r = new Random();
		Player p = new Player(r.nextInt(ga.getWidth()), r.nextInt(ga.getHeight()), 32, 48, 12, id, this);
		actors.add(p);
		players.add(p);
	}
	
	public GameArena getArena() {return ga;}
	public List<Actor> getActors(){return actors;}
	
	public Player getPlayer(int id) {
		for (Player p : players) {
			if (p.getId() == id) {
				return p;
			}
		}
		return null;
	}
}
