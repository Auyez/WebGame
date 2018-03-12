package Game;

import org.apache.commons.lang3.tuple.Pair;

import javax.websocket.Session;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

public class Game implements Runnable {
    private volatile Queue<Pair<Session, Protocol.Server.GameMsg>> messages;
    private Map<Session, Integer> sessions;
    private GameWorld gw;
    
    public Game(Queue<Pair<Session, Protocol.Server.GameMsg>> messages, Map<Session, Integer> sessions) {
        this.messages = messages;
        this.sessions = sessions;
        gw = new GameWorld();
    }

    public GameWorld getWorld() {return gw;}
    
    @Override
    public void run() {
        try {
            final int FPS = 60;
            int frameCount = 0;
            boolean running = true;
            
            while (running) {
                long frameStartTime = System.currentTimeMillis(); // TODO check whether Java optimizes this or not
                
                processMessages();
                
                if(frameCount % 1 == 0)
                	sendWorldState();
                long frameElapsedTime = System.currentTimeMillis() - frameStartTime;
                long frameRemainingTime = 1000/FPS - frameElapsedTime;
                if (frameRemainingTime > 0)
                    Thread.sleep(frameRemainingTime);
                frameCount++;
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

	private void processMessages() {
		synchronized (messages) {	
			while (!messages.isEmpty()) {
				Pair<Session, Protocol.Server.GameMsg> message = messages.remove();
				Protocol.Server.GameMsg gameMsg = message.getRight();
		        Session session = message.getLeft();

		        int id = sessions.get(session);
		        Player player = gw.getPlayer(id);

		        if (gameMsg.input != null) {
		        	byte key = gameMsg.input.key;
		        	System.out.println((char)key);
		        	player.getInput().press(key);
		        	player.update();
		        	//System.out.println(player.getPosition());
		        }
			}
		}
	}
    
    private void sendWorldState() {
    	if (gw.getActors().size() > 0) {
    	    Protocol.Client.ClientMsg message = new Protocol.Client.ClientMsg();
            message.gameMsg = new Protocol.Client.GameMsg();
            message.gameMsg.worldState = new Protocol.Client.WorldState();

            for (Actor a : gw.getActors()) {
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
    
    private void parseMessage(ByteBuffer msg) {
    	
    }
}
