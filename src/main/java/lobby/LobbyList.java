package lobby;
import java.util.ArrayList;

class LobbyList {
    private static ArrayList<Lobby> lobbies;

    public static ArrayList<Lobby> getLobbies() {
        if (lobbies == null) {
            lobbies = new ArrayList<>();

            lobbies.add(new Lobby(0));
            lobbies.add(new Lobby(1));
            lobbies.add(new Lobby(2));
            lobbies.add(new Lobby(3));
        }

        return lobbies;
    }

    private LobbyList() {

    }
}
