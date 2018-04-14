package lobby;
import java.util.ArrayList;

class LobbyList {
    private static ArrayList<Lobby> lobbies;

    public static ArrayList<Lobby> getLobbies() {
        if (lobbies == null) {
            lobbies = new ArrayList<>();

            lobbies.add(new Lobby("Lobby0"));
            lobbies.add(new Lobby("Lobby1"));
            lobbies.add(new Lobby("Lobby2"));
            lobbies.add(new Lobby("Lobby3"));
        }

        return lobbies;
    }
}
