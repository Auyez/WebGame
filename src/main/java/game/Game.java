package game;

import lobby.WebSocketEndpoint;
import org.apache.commons.lang3.tuple.Pair;
import game.actors.Actor;
import game.actors.Player;
import game.actors.TileActor;
import game.skill.Blink;
import game.skill.Skill;
import game.skill.ThrowFireball;
import lobby.Protocol;
import lobby.Protocol.Server.Input;
import javax.websocket.Session;
import java.awt.Rectangle;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Random;

public class Game implements Runnable {
	
    private final Queue<Pair<Session, Protocol.Server.GameMsg>> 		messages;
    private final Queue<Integer> 								        playerDisconnectMessages;
    private final Map<Session, Integer> 								sessions;
	private List<Actor> 												actors;
	private List<Player> 												players;
	private GameArena 													ga;
	private BufferedWriter writer;


    public Game(Queue<Pair<Session, Protocol.Server.GameMsg>> messages,
                Queue<Integer> playerDisconnectMessages,
                Map<Session, Integer> sessions,
                int number) {
        this.messages = messages;
        this.playerDisconnectMessages = playerDisconnectMessages;
        this.sessions = sessions;
		actors = new ArrayList<Actor>();
		players = new ArrayList<Player>();
		ga = new GameArena("map.txt");
		
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		Date date = new Date();
		
		String name = number + dateFormat.format(date) + ".txt";
		System.out.println(name);
		// relative path of absolute path?
		try {
			writer = new BufferedWriter(new FileWriter(name, true));
		} catch (IOException e) {
			e.printStackTrace();
		}
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
                if(frameCount % 1 == 0) { // kind of tick rate
                	sendWorldState();
                }
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
        }
    }


	private void update(long delta) {
		for (Player p : players) {
			if( p.update_dead(delta) ) {
				Random r = new Random();
				// randomly choose free spawn point
				do{
					Vec2 spawn = new Vec2(Constants.SPAWN_POINTS[r.nextInt(Constants.MAX_PLAYERS)]);	// get new spawn point
					spawn.scalar(Constants.GAME_TILE_SIZE);				// convert to pixels
					p.setPosition(spawn);
				}while(collides(p) != null);
				actors.add(p);
			}
		}
        for (int i = 0; i < actors.size(); i++) {
        	Actor actor = actors.get(i);
            actor.update(delta);
            Actor collidedWith = collides(actor);
            actor.resolve_collision(delta, collidedWith);
            if (actor.isDestroyed()) {
            	actors.remove(actor);
            	i--;
            }
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
		        
		        if (gameMsg.skillInput != null) {							// Detect if 'QWER' was pressed
		        	player.getInput().activateSkill(gameMsg.skillInput.skillType);
		        	player.getInput().setSkillTarget(new Vec2(gameMsg.skillInput.x, gameMsg.skillInput.y));
		        } else if (gameMsg.input != null) {
		        	setInput(gameMsg.input, player);						//Movement using path find
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
		int dy = Constants.PLAYER_HEIGHT - Constants.PLAYER_LOWER_HEIGHT; 
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
	
    private void sendWorldState()  {
    	if (actors.size() > 0) {
    	    Protocol.Client.ClientMsg message = new Protocol.Client.ClientMsg();
            message.gameMsg = new Protocol.Client.GameMsg();
            message.gameMsg.worldState = new Protocol.Client.WorldState();

            for (Actor a : actors) {
                message.gameMsg.worldState.items.add(a.getState());
            }
            // Write to file as replay
            recordAsReplay();
            
            synchronized (sessions) {
				for (Session s : sessions.keySet()) {
					WebSocketEndpoint.sendBinary(s, message.bytes());
				}
			}
    	}
    }


	private void recordAsReplay()  {
		try {
			String str = "1_";
			writer.append(str);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	private void addPlayer(int id) {	
		Player p = new Player(ga.getTileSize()*Constants.SPAWN_POINTS[actors.size()].getX(),
					   ga.getTileSize()*Constants.SPAWN_POINTS[actors.size()].getY(),
					   Constants.PLAYER_WIDTH, 
					   Constants.PLAYER_HEIGHT, 
					   Constants.PLAYER_LOWER_HEIGHT, 
					   id);
		Skill Q =  new ThrowFireball(p, this) ;
		Skill W =  new Blink(p, this);
		p.setSkill(Q, (byte) 0);
		p.setSkill(W, (byte) 1);
		actors.add(p);
		players.add(p);
	}
	

	public Actor collides(Actor a) {	
		//collision with tile map part
		Rectangle hitbox = (a.getLowerBox() != null ) ? a.getLowerBox() : a.getHitbox();
		int left = hitbox.x/ga.getTileSize();
		int right = (hitbox.x + hitbox.width)/ga.getTileSize();
		int up = hitbox.y/ga.getTileSize();
		int bottom = (hitbox.y + hitbox.height)/ga.getTileSize();

		for(int i = up; i <= bottom; i++)
			for(int j = left; j <= right; j++)
				if(ga.getEntry(i, j) == 1)
					return new TileActor();
		//collision with objects

		hitbox = a.getHitbox();
		for(Actor b : actors)
			if (a != b && hitbox.intersects(b.getHitbox()))
				return b;
		return null;
	}
	
	public void addActor(Actor a) { actors.add(a);}
	
	public int getFreeId() {
    	int maxId = 0;
    	for (Actor actor : actors) {
    		maxId = Math.max(maxId, actor.getId());
		}
		return maxId + 1;
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
}
