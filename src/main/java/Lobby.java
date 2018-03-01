import Game.Game;
import Game.GameWorld;
import Game.Protocol;
import org.apache.commons.lang3.tuple.Pair;

import javax.websocket.Session;
import java.nio.ByteBuffer;
import java.util.*;

class Lobby {
    private String name;
    private Map<Session, Integer> sessions = new HashMap<Session, Integer>(); // Session -> PlayerID map
    private Thread gameThread;
    private Queue<Pair<Session, ByteBuffer>> gameMessages = new LinkedList<>();


    Lobby(String name) {
        this.name = name;
    }

    
    // TODO: find a better way of parsing messages
    public void onMessage(byte[] message, Session session) {
        try {
            ByteBuffer buffer = ByteBuffer.wrap(message);
            byte lobbyIndex = buffer.get();
            byte command = buffer.get();
            switch (command) {
                case Protocol.Server.ADD_PLAYER:
                    int playerId = buffer.getInt();
                    addPlayer(session, playerId);
                    break;
                case Protocol.Server.GAME_MSG:
                    if (isGameRunning()) {
                        buffer.clear(); // reset the ByteBuffer position. Position was modified by get() methods.
                        gameMessages.add(Pair.of(session, buffer));
                    }
                    break;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void onClose(Session session) {
        if (sessions.containsKey(session)) {
            System.out.println(name + ": #" + sessions.get(session) + " removed");
            sessions.remove(session);

            if (sessions.isEmpty()) {
                gameThread.interrupt(); // TODO: stop game properly
            }
        }
    }

    private void addPlayer(Session session, int playerId) {
        if (sessions.containsKey(session) || sessions.containsValue(playerId) || isGameRunning())
            return;

        sessions.put(session, playerId);
        System.out.println(name + ": #" + playerId + " added");

        if (sessions.size() >= 1) {
            startGame(); // change start game only if all players checked "ready"
        }
    }
    
    
    private void startGame() {
        try {
        	// Starts game on Clients' side AND sets up all players
        	byte numPlayers = (byte) sessions.size();
            for (Session session : sessions.keySet()) {
                ByteBuffer buffer = ByteBuffer.allocate(3 + (4 * numPlayers));
                buffer.put(Protocol.Client.START_GAME);
                buffer.put(Protocol.Client.Game.PLAYER_SETUP); // Rewrite protocol so that player setup comes with start game
                buffer.put(numPlayers);
                for (int id : sessions.values()) {
                	buffer.putInt(id);
                }
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
