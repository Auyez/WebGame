import Game.Game;
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
            System.out.println(buffer);
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
            for (Session session : sessions.keySet()) {
                ByteBuffer buffer = ByteBuffer.allocate(6);
                buffer.put(Protocol.Client.START_GAME);
                buffer.flip();
                session.getBasicRemote().sendBinary(buffer);
            }

            gameMessages.clear();
            gameThread = new Thread(new Game(gameMessages, sessions));
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
