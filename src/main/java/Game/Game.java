package Game;

import org.apache.commons.lang3.tuple.Pair;

import javax.websocket.Session;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Random;

public class Game implements Runnable {
    private volatile Queue<Pair<Session, Protocol.Server.GameMsg>> 		messages;
    private volatile Queue<Integer> 									playerDisconnectMessages;
    private Map<Session, Integer> 										sessions;
    
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
            
            for (int id : sessions.values()) {
            	addPlayer(id);
            }
            
            while (running) {
            	delta = frameStartTime;
                frameStartTime = System.currentTimeMillis(); // TODO check whether Java optimizes this or not
                delta = frameStartTime - delta;
                processMessages();
                update(delta);
                if(frameCount % 1 == 0)
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
            ex.printStackTrace();
        }
    }

    
	private void update(long delta) {
        for (Actor actor : actors) {
            actor.update(delta);
        }
    }
	
	
	private void processMessages() {
		synchronized (messages) {	
			while (!messages.isEmpty()) {
				Pair<Session, Protocol.Server.GameMsg> message = messages.remove();
				Protocol.Server.GameMsg gameMsg = message.getRight();
		        Session session = message.getLeft();

		        int id = sessions.get(session);
		        Player player = getPlayer(id);

		        if (gameMsg.input != null) {
		        	//byte key = gameMsg.input.key;
		        	//player.getInput().press(key);
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

		
    private void sendWorldState() {
    	if (actors.size() > 0) {
    	    Protocol.Client.ClientMsg message = new Protocol.Client.ClientMsg();
            message.gameMsg = new Protocol.Client.GameMsg();
            message.gameMsg.worldState = new Protocol.Client.WorldState();

            for (Actor a : actors) {
                message.gameMsg.worldState.items.add(a.getState());
            }

	    	for(Session s : sessions.keySet()) {
	    		try{
	    			s.getBasicRemote().sendBinary(ByteBuffer.wrap(message.bytes()));
	    		}catch(Exception e) {
	    			e.printStackTrace();
	    		}
	    	}
    	}
    }
    
    
	private void addPlayer(int id) {
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
		}while(!(	( (x + w) < ga.getWidth()  ) && ( (y + h) < ga.getHeight() ) && (p.collides() < -1)	));
		actors.add(p);
		players.add(p);
	}
	
	
	private void removePlayer(int id) {
	    Player player = getPlayer(id);
	    actors.remove(player);
	    players.remove(player);
    }
	
	//need to sort player list after each addition of player
	//then need to reimplement this method and use faster search
	private Player getPlayer(int id) {
		for (Player p : players)
			if (p.getId() == id)
				return p;
		return null;
	}
	private Actor getActor(int id) {
		for (Actor a : actors)
			if (a.getId() == id)
				return a;
		return null;
	}
	public GameArena getArena() {return ga;}
	public List<Actor> getActors(){return actors;}
	public List<Player> getPlayers(){return players;}
}
