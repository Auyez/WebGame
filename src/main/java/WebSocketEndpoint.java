import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

@ServerEndpoint(value="/websocketendpoint")
public class WebSocketEndpoint {

    public WebSocketEndpoint() {

    }


    @OnMessage
    public void onMessage(byte[] message, Session session) throws IOException {
        if(message.length <= 0)
            return;
        int lobbyIndex = message[0];
        if(0 <= lobbyIndex && lobbyIndex < LobbyList.getLobbies().size()) {
            LobbyList.getLobbies().get(lobbyIndex).onMessage(message, session);
        }

    }

    @OnClose
    public void onClose(Session session) throws IOException {
        // Tell all lobbies that a session has closed in case any of them are using it.
        for (Lobby lobby : LobbyList.getLobbies()) {
            lobby.onClose(session);
        }
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        // Do error handling here
    }
}
