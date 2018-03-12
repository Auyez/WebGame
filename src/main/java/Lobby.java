import Game.Game;
import Game.GameWorld;
import Game.Protocol;
import org.apache.commons.lang3.tuple.Pair;

import javax.websocket.Session;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.*;

class Lobby {
	private static final int MAX_PLAYERS = 3;
	
    private String name;
    private Map<Session, Integer> sessions = new HashMap<Session, Integer>(); // Session -> PlayerID map
    private Thread gameThread;
    private volatile Queue<Pair<Session, ByteBuffer>> gameMessages = new LinkedList<>();
    private int readyCount;

    Lobby(String name) {
        this.name = name;
    }

    
    // TODO: find a better way of parsing messages
    public void onMessage(byte[] message, Session session) {
        try {
            ByteBuffer buffer = ByteBuffer.wrap(message);
            byte lobbyIndex = buffer.get(); // TODO get rid of this tumor (check .clear() in WebSocket class)
            byte command = buffer.get();
            switch (command) {
                case Protocol.Server.ADD_PLAYER:
                    int playerId = buffer.getInt();
                    addPlayer(session, playerId);
                    break;
                case Protocol.Server.GAME_MSG:
                	handleGameMessage(session, buffer);
                    break;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

	private void handleGameMessage(Session session, ByteBuffer buffer) throws IOException {
		byte cmdType = buffer.get(2);
		if (cmdType == Protocol.Server.Game.READY) {
			readyCount++;
			if (readyCount >= MAX_PLAYERS) {
				sendPlayerSetup();
			}
		} else if (isGameRunning() ) {
		    buffer.clear(); // reset the ByteBuffer position. Position was modified by get() methods.
		    synchronized(gameMessages) {
		    	gameMessages.add(Pair.of(session, buffer));
		    }
		}
	}


	private void sendPlayerSetup() throws IOException {
		byte numPlayers = (byte) sessions.size();
		
		for (Session s : sessions.keySet()) {
		    ByteBuffer buf = ByteBuffer.allocate(3 + (4 * numPlayers));
		    buf.put(Protocol.Client.GAME_MSG);
		    buf.put(Protocol.Client.Game.PLAYER_SETUP);
		    buf.put(numPlayers);
		    for (int id : sessions.values()) {
		    	buf.putInt(id);
		    }
		    buf.flip();
		    System.out.println(buf);
		    s.getBasicRemote().sendBinary(buf);
		}
		readyCount = 0;
	}

    public void onClose(Session session) {
        if (sessions.containsKey(session)) {
            System.out.println(name + ": #" + sessions.get(session) + " removed");
            sessions.remove(session);

            if (sessions.isEmpty() && gameThread != null) {
                gameThread.interrupt(); // TODO: stop game properly
            }
        }
    }

    private void addPlayer(Session session, int playerId) {
        if (sessions.containsKey(session) || sessions.containsValue(playerId) || isGameRunning())
            return;

        sessions.put(session, playerId);
        System.out.println(name + ": #" + playerId + " added");


        if (sessions.size() >= MAX_PLAYERS) {
            startGame(); // change start game only if all players checked "ready"
        }
    }
    
    
    private void startGame() {
        try {
        	// Starts game on Clients' side
            for (Session session : sessions.keySet()) {
                ByteBuffer buffer = ByteBuffer.allocate(7);
                buffer.put(Protocol.Client.START_GAME);
                buffer.flip();
                session.getBasicRemote().sendBinary(buffer);
            }
            
            gameMessages.clear();
            
            Game game = new Game(gameMessages, sessions);
            GameWorld gw = game.getWorld();
            for (int id : sessions.values()) {
            	gw.addPlayer(id);
            }
            
            gameThread = new Thread(game);
            gameThread.start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public boolean isGameRunning() {
        if (gameThread == null)
            return false;
        return gameThread.isAlive(); // Maybe we should find a way to notify Lobby when Game thread ends
    }
}
