package lobby;
import org.apache.commons.lang3.tuple.Pair;

import game.Constants;
import game.Game;
import javax.websocket.Session;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class Lobby {
    private int number;
    private final Map<Session, Integer> sessions = new HashMap<Session, Integer>(); // Session -> PlayerID map
    private Thread gameThread;
    private final Queue<Pair<Session, Protocol.Server.GameMsg>> gameMessages = new LinkedList<>();
    private final Queue<Integer> gamePlayerDisconnectMessages = new LinkedList<>();
    private int readyCount;
    private String mapJson;

    Lobby(int number, String mapJson) {
        this.number = number;
        this.mapJson = mapJson;
    }


    public synchronized void onMessage(Protocol.Server.LobbyCmd message, Session session) {
        if (message.addPlayer != null && !isGameRunning()) {
            addPlayer(session, message.addPlayer.playerId);
        } else if (message.ready != null && isGameRunning()) {
            readyCount++;
            if (readyCount >= Constants.MAX_PLAYERS) {
                // not used anymore :D
                readyCount = 0;
            }
        } else if (message.gameMsg != null && isGameRunning()) {
            synchronized (gameMessages) {
                gameMessages.add(Pair.of(session, message.gameMsg));
            }
        }
    }

    public synchronized void onClose(Session session) {
        synchronized (sessions) {
            if (sessions.containsKey(session)) {
                int id = sessions.get(session);
                sessions.remove(session);
                System.out.println("Lobby" + number + ": #" + id + " removed");

                //System.out.println(id + " onClose1");
                if (isGameRunning()) {
                    //System.out.println(id + " onClose2");
                    synchronized (gamePlayerDisconnectMessages) {
                        gamePlayerDisconnectMessages.add(id);
                    }
                }
            }
        }
    }

    private void addPlayer(Session session, int playerId) {
        synchronized (sessions) {
            if (sessions.containsKey(session) || sessions.containsValue(playerId) || isGameRunning())
                return;

            sessions.put(session, playerId);
            System.out.println("Lobby" + number + ": #" + playerId + " added");


            if (sessions.size() >= Constants.MAX_PLAYERS) {
                startGame(); // TODO change start game only if all players checked "ready"
            }
        }
    }
    
    
    private void startGame() {
        synchronized (sessions) {
            for (Session session : sessions.keySet()) {
                Protocol.Client.ClientMsg message = new Protocol.Client.ClientMsg();
                message.startGame = new Protocol.Client.StartGame();

                WebSocketEndpoint.sendBinary(session, message.bytes());
            }

            gameMessages.clear();
            gamePlayerDisconnectMessages.clear();

            Game game = new Game(gameMessages, gamePlayerDisconnectMessages, sessions, number, mapJson);
            gameThread = new Thread(game);
            gameThread.start();
        }
    }

    public boolean isGameRunning() {
        if (gameThread == null)
            return false;
        return gameThread.isAlive();
    }

    public String getMapJson() {
        return mapJson;
    }
}
