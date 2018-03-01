package Game;//Constants for the protocol

public class Protocol {
    public static class Server {
        public static final byte ADD_PLAYER = 0;
        public static final byte GAME_MSG = 1;

        public static class Game {
            public static final byte INPUT = 0;
        }
    }

    public static class Client {
        public static final byte START_GAME = 0;
        public static final byte GAME_MSG = 1;

        public static class Game {
            public static final byte WORLD_STATE = 0;
            public static final byte PLAYER_SETUP = 1;

            public static class Entity {
                public static final byte PLAYER = 0;
            }
        }
    }
}