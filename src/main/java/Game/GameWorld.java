package Game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.tuple.Pair;

public class GameWorld {
	private int width;
	private int height;
	private List<Actor> actors;
	private List<Player> players;
	private GameArena ga;
	
	public GameWorld(int w, int h) {
		width = w; height = h;
		actors = new ArrayList<Actor>();
		players = new ArrayList<Player>();
		ga = new GameArena("map.txt");
	}
	
	public void addPlayer(int id) {
		Random r = new Random();
		Player p = new Player(r.nextInt(width), r.nextInt(height), 48, 48, 12, id, this);
		actors.add(p);
		players.add(p);
	}
	
	public List<Actor> getActors(){return actors;}
}
