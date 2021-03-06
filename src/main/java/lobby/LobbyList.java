package lobby;
import org.apache.commons.io.IOUtils;

import game.Constants;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class LobbyList {
    private static ArrayList<Lobby> lobbies;

    public static ArrayList<Lobby> getLobbies() {
        if (lobbies == null) {
            lobbies = new ArrayList<>();

            try {
                // TODO: find another way to store maps
                /*InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream("maps/map.json");
                String mapJson = IOUtils.toString(input, "UTF-8");
                input.close();


                lobbies.add(new Lobby(0, mapJson));
                lobbies.add(new Lobby(1, mapJson));
                lobbies.add(new Lobby(2, mapJson));
                lobbies.add(new Lobby(3, mapJson));*/
                for (int i = 0; i < Constants.MAX_LOBBIES; i++) {
                    InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream("maps/map" + i + ".json");
                    String mapJson = IOUtils.toString(input, "UTF-8");
                    lobbies.add(new Lobby(i, mapJson));
                    input.close();                	
                }
            } catch (IOException ex) {
                System.out.println("Couldn't load map");
            }
        }

        return lobbies;
    }
}
