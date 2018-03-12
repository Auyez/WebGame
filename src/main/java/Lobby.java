import Game.Game;
import Game.GameWorld;
import Game.Protocol;
import org.apache.commons.lang3.tuple.Pair;

import javax.websocket.Session;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.*;

class Lobby {
	private static final int MAX_PLAYERS = 1;
	
    private String name;
    private Map<Session, Integer> sessions = new HashMap<Session, Integer>(); // Session -> PlayerID map
    private Thread gameThread;
    private volatile Queue<Pair<Session, Protocol.Server.GameMsg>> gameMessages = new LinkedList<>();
    private int readyCount;

    Lobby(String name) {
        this.name = name;
    }


    public void onMessage(Protocol.Server.LobbyCmd message, Session session) {
        if (message.addPlayerId != null) {
            addPlayer(session, message.addPlayerId);
        } else if (message.ready != null) {
            readyCount++;
            if (readyCount >= MAX_PLAYERS) {
                try {
                    sendPlayerSetup();
                } catch (IOException ex) {
                    ex.printStackTrace(); // how should be deal with this?
                }
                readyCount = 0;
            }
        } else if (message.gameMsg != null) {
            if (isGameRunning()) {
                synchronized (gameMessages) {
                    gameMessages.add(Pair.of(session, message.gameMsg));
                }
            }
        }
    }



	private void sendPlayerSetup() throws IOException {
		for (Session s : sessions.keySet()) {
            Protocol.Client.ClientMsg message = new Protocol.Client.ClientMsg();
            message.gameMsg = new Protocol.Client.GameMsg();
            message.gameMsg.playerSetup = new Protocol.Client.PlayerSetup();
            message.gameMsg.playerSetup.items.addAll(sessions.values()); // add all ids

		    s.getBasicRemote().sendBinary(ByteBuffer.wrap(message.bytes()));
		}
	}

    public void onClose(Session session) {
        if (sessions.containsKey(session)) {
            System.out.println(name + ": #" + sessions.get(session) + " removed");
            sessions.remove(session);
            // if game is running tell it that the player has exited

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
                Protocol.Client.ClientMsg message = new Protocol.Client.ClientMsg();
                message.startGame = new Protocol.Client.StartGame();

                session.getBasicRemote().sendBinary(ByteBuffer.wrap(message.bytes()));
            }
            
            gameMessages.clear();
            
            Game game = new Game(gameMessages, sessions);
            // Perhaps we should move this loop to Game
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
