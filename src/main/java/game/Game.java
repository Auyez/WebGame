package game;

import lobby.WebSocketEndpoint;

import org.apache.commons.lang3.tuple.Pair;

import game.actors.Actor;
import game.actors.Player;
import game.actors.Projectile;
import game.actors.TileActor;
import lobby.Protocol;
import lobby.Protocol.Server.Input;
import lobby.Protocol.Server.SkillInput;

import javax.websocket.Session;

import java.awt.Rectangle;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Random;

public class Game implements Runnable {
	// Temporary constants, should be moved into separate class in the future
	public static final int PLAYER_WIDTH = 20;
	public static final int PLAYER_HEIGHT = 40;
	public static final int PLAYER_LOWER_HEIGHT = 20;
	
    private final Queue<Pair<Session, Protocol.Server.GameMsg>> 		messages;
    private final Queue<Integer> 								        playerDisconnectMessages;
    private final Map<Session, Integer> 								sessions;
	private List<Actor> 												actors;
	private List<Player> 												players;
	private GameArena 													ga;


    public Game(Queue<Pair<Session, Protocol.Server.GameMsg>> messages,
                Queue<Integer> playerDisconnectMessages,
                Map<Session, Integer> sessions) {
        this.messages = messages;
        this.playerDisconnectMessages = playerDisconnectMessages;
        this.sessions = sessions;
		actors = new ArrayList<Actor>();
		players = new ArrayList<Player>();
		ga = new GameArena("map.txt");
    }


    @Override
    public void run() {
        try {
            final int 	FPS = 60;
            int 		frameCount = 0;
            boolean 	running = true;
            long 		frameStartTime = 0;
            long		delta;

            synchronized (sessions) {
				for (int id : sessions.values()) {
					addPlayer(id);
				}
			}

            while (running) {
            	delta = frameStartTime;
                frameStartTime = System.currentTimeMillis(); // TODO check whether Java optimizes this or not
                delta = frameStartTime - delta;
                processMessages();
                update(delta);
                if(frameCount % 1 == 0) // kind of tick rate
                	sendWorldState();
                long frameElapsedTime = System.currentTimeMillis() - frameStartTime;
                long frameRemainingTime = 1000/FPS - frameElapsedTime;
                if (frameRemainingTime > 0)
                    Thread.sleep(frameRemainingTime);
                frameCount++;

                if (actors.size() <= 0) {
                    running = false;
                    System.out.println("Game loop over");
                }
            }
        } catch (InterruptedException ex) {
            System.out.println("Game::run exception");
            //ex.printStackTrace();
        }
    }


	private void update(long delta) {
        for (Actor actor : actors) {
            actor.update(delta);
            Actor collidedWith = collides(actor);
            actor.resolve_collision(delta, collidedWith);
        }
    }


	private void processMessages() {
		synchronized (messages) {
			while (!messages.isEmpty()) {
				Pair<Session, Protocol.Server.GameMsg> message = messages.remove();
				Protocol.Server.GameMsg gameMsg = message.getRight();
		        Session session = message.getLeft();
		        int id;
		        synchronized (sessions) {
					id = sessions.get(session);
				}
		        Player player = getPlayer(id);
		        
		        // Movement input
		        if (gameMsg.input != null)
		        	setInput(gameMsg.input, player);
		        if (gameMsg.skillInput != null) {
		        	System.out.println("Skill input detected!");
		        	spellQ(gameMsg.skillInput, player);
		        }
			}
		}
		synchronized (playerDisconnectMessages) {
			while (!playerDisconnectMessages.isEmpty()) {
				Integer id = playerDisconnectMessages.remove();
				removePlayer(id);
			}
		}
	}

	private void setInput(Input input, Player player) {
		int size = ga.getTileSize();
			// This bias is needed for path-finding algorithm, because
			// collision occurs with lower part of player, but the movement is calculated using upper part of player.
		int dy = PLAYER_HEIGHT - PLAYER_LOWER_HEIGHT; 
		if (ga.getEntry((input.yTarget + dy) / size, input.xTarget / size) == 0) {

			int x_init = (int) player.getPosition().getX() / size;
			int y_init = (int) (player.getPosition().getY() + dy) / size;
			int x_target = input.xTarget / size;
			int y_target = (input.yTarget + dy) / size;
			
			player.getInput().setDestination(input.xTarget, input.yTarget - dy);
			
			// Initial check if there are no obstacles between initial and target destinations
			if (ga.checkCollision(player.getPosition().getX(),
								  player.getPosition().getY(), 
								  input.xTarget,
								  input.yTarget)) {
				// Call A* search here, setMouse should take a sequence of destination coordinates
				ArrayList<TileNode> sequence =  ga.aStar(x_init, y_init, x_target, y_target);
				player.getInput().setMouse(sequence);
			}
			
			
		}
	}
	
    private void sendWorldState() {
    	if (actors.size() > 0) {
    	    Protocol.Client.ClientMsg message = new Protocol.Client.ClientMsg();
            message.gameMsg = new Protocol.Client.GameMsg();
            message.gameMsg.worldState = new Protocol.Client.WorldState();

            for (Actor a : actors) {
                message.gameMsg.worldState.items.add(a.getState());
            }

            synchronized (sessions) {
				for (Session s : sessions.keySet()) {
					WebSocketEndpoint.sendBinary(s, message.bytes());
				}
			}
    	}
    }


	private void addPlayer(int id) {
		
		Player p = null;
		Random r = new Random();
		int x,y;
		do {
			x = r.nextInt(ga.getWidth());
			y = r.nextInt(ga.getHeight());

			if(p != null)
				p.setPosition(x, y);
			else
				p = new Player(x, y, PLAYER_WIDTH, PLAYER_HEIGHT, PLAYER_LOWER_HEIGHT, id);
		}while(!(	( (x + PLAYER_WIDTH) < ga.getWidth()  ) && ( (y + PLAYER_HEIGHT) < ga.getHeight() ) && (collides(p) == null)	));
		actors.add(p);
		players.add(p);
	}


	//returns -2 if no collision happens, or -1 if actor collides with arena, otherwise returns id
	public Actor collides(Actor a) {	
		//collision with tile map part
		Rectangle hitbox = a.getHitbox();
		Rectangle lowerBox = a.getLowerBox();
		int left = a.lowerBox.x/ga.getTileSize();
		int right = (a.lowerBox.x + a.lowerBox.width)/ga.getTileSize();
		int up = lowerBox.y/ga.getTileSize();
		int bottom = (lowerBox.y + lowerBox.height)/ga.getTileSize();

		for(int i = up; i <= bottom; i++)
			for(int j = left; j <= right; j++)
				if(ga.getEntry(i, j) == 1)
					return new TileActor();
		//collision with objects
		for(Actor b : actors)
			if (a != b && hitbox.intersects(b.hitbox))
				return b;
		return null;
	}
	
	
	private void removePlayer(int id) {
	    Player player = getPlayer(id);
	    actors.remove(player);
	    players.remove(player);
    }

	
	private Player getPlayer(int id) {
		for (Player p : players)
			if (p.getId() == id)
				return p;
		return null;
	}
	public GameArena getArena() {return ga;}
	public List<Actor> getActors(){return actors;}
	public List<Player> getPlayers(){return players;}
	
	
	
	// ************************************************************
	// ************************************************************
	// ************************************************************
	// ************************************************************
	// Temporary function that implements skill mechanics
	private void spellQ(SkillInput skillInput, Player player) {
		float x_init = player.getPosition().getX();
		float y_init = player.getPosition().getY();
		int x_direction = skillInput.x;
		int y_direction = skillInput.y;
		int size = 5;
		int speed = 300;
		int id = player.getId();
		Projectile s = new Projectile(x_init, y_init, x_direction, y_direction, size, id, speed);
		
	}
}
