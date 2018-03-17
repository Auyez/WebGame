package Game;//Constants for the protocol

import java.util.ArrayList;

/*
Potential Pitfall: Server.GameMsg and Client.GameMsg are different classes.

Writing Examples:
// Start Game
ClientMsg clientMsg = new ClientMsg();
//clientMsg is a union. That is only one of its members should be non-null
clientMsg.startGame = new StartGame();
byte[] bytes = clientMsg.bytes();

// Player Setup
ClientMsg clientMsg = new ClientMsg();
clientMsg.gameMsg = new GameMsg();
clientMsg.gameMsg.playerSetup = new PlayerSetup();
// playerSetup is a list. Every list has a field 'items'
clientMsg.gameMsg.playerSetup.items.add(15);
clientMsg.gameMsg.playerSetup.items.add(16);
byte[] bytes = clientMsg.bytes();

// World State
ClientMsg clientMsg = new ClientMsg();
clientMsg.gameMsg = new GameMsg();
clientMsg.gameMsg.worldState = new WorldState();
Entity entity = new Entity();
entity.player = new Player();
entity.player.x = 12;
entity.player.y = 13;
entity.player.a = 14;
entity.player.id = 15;
clientMsg.gameMsg.worldState.items.add(entity);
byte[] bytes = clientMsg.bytes();



Reading Example:
//get bytes from socket
ServerMsg serverMsg = ServerMsg.parse(ByteBuffer.wrap(bytes));
*/


/*
namespace Server
    struct server_msg
        Integer lobby_index
        union lobby_cmd
            Integer add_player_id
            struct ready
            union game_msg
                struct input
                    Byte key

namespace Client
    union client_msg
        struct start_game
        union game_msg
            list player_setup
                Integer id
            Integer remove_player_id
            list world_state
                union entity
                    struct player
                        Integer x
                        Integer y
                        Integer a
                        Integer id
*/
public class Protocol {
    public static class Server {
        public static class ServerMsg { /*Struct*/
            public Integer lobbyIndex;
            public LobbyCmd lobbyCmd;
            public byte[] bytes() {
                ByteWriter writer = new ByteWriter();
                writer.writeBytes(ByteWriter.Integer2bytes(lobbyIndex));
                writer.writeBytes(lobbyCmd.bytes());
                return writer.bytes();
            }
            public static ServerMsg parse(ByteReader reader) {
                ServerMsg obj = new ServerMsg();
                obj.lobbyIndex = reader.readInteger();
                obj.lobbyCmd = LobbyCmd.parse(reader);
                return obj;
            }
        }
        public static class LobbyCmd { /*Union*/
            public Integer addPlayerId;
            public Ready ready;
            public GameMsg gameMsg;
            static final byte ADD_PLAYER_ID = 0;
            static final byte READY = 1;
            static final byte GAME_MSG = 2;
            public byte[] bytes() {
                ByteWriter writer = new ByteWriter();
                if (false) {;
                } else if (addPlayerId != null) {
                    writer.writeByte(ADD_PLAYER_ID);
                    writer.writeBytes(ByteWriter.Integer2bytes(addPlayerId));
                } else if (ready != null) {
                    writer.writeByte(READY);
                    writer.writeBytes(ready.bytes());
                } else if (gameMsg != null) {
                    writer.writeByte(GAME_MSG);
                    writer.writeBytes(gameMsg.bytes());
                }
                return writer.bytes();
            }
            public static LobbyCmd parse(ByteReader reader) {
                LobbyCmd obj = new LobbyCmd();
                byte type = reader.readByte();
                if (type == ADD_PLAYER_ID) {
                    obj.addPlayerId = reader.readInteger();
                }

                if (type == READY) {
                    obj.ready = Ready.parse(reader);
                }

                if (type == GAME_MSG) {
                    obj.gameMsg = GameMsg.parse(reader);
                }
                return obj;
            }
        }
        public static class Ready { /*Struct*/
            public byte[] bytes() {
                ByteWriter writer = new ByteWriter();
                return writer.bytes();
            }
            public static Ready parse(ByteReader reader) {
                Ready obj = new Ready();
                return obj;
            }
        }
        public static class GameMsg { /*Union*/
            public Input input;
            static final byte INPUT = 0;
            public byte[] bytes() {
                ByteWriter writer = new ByteWriter();
                if (false) {;
                } else if (input != null) {
                    writer.writeByte(INPUT);
                    writer.writeBytes(input.bytes());
                }
                return writer.bytes();
            }
            public static GameMsg parse(ByteReader reader) {
                GameMsg obj = new GameMsg();
                byte type = reader.readByte();
                if (type == INPUT) {
                    obj.input = Input.parse(reader);
                }
                return obj;
            }
        }
        public static class Input { /*Struct*/
            public Byte key;
            public byte[] bytes() {
                ByteWriter writer = new ByteWriter();
                writer.writeBytes(ByteWriter.Byte2bytes(key));
                return writer.bytes();
            }
            public static Input parse(ByteReader reader) {
                Input obj = new Input();
                obj.key = reader.readByte();
                return obj;
            }
        }
    }
    public static class Client {
        public static class ClientMsg { /*Union*/
            public StartGame startGame;
            public GameMsg gameMsg;
            static final byte START_GAME = 0;
            static final byte GAME_MSG = 1;
            public byte[] bytes() {
                ByteWriter writer = new ByteWriter();
                if (false) {;
                } else if (startGame != null) {
                    writer.writeByte(START_GAME);
                    writer.writeBytes(startGame.bytes());
                } else if (gameMsg != null) {
                    writer.writeByte(GAME_MSG);
                    writer.writeBytes(gameMsg.bytes());
                }
                return writer.bytes();
            }
            public static ClientMsg parse(ByteReader reader) {
                ClientMsg obj = new ClientMsg();
                byte type = reader.readByte();
                if (type == START_GAME) {
                    obj.startGame = StartGame.parse(reader);
                }

                if (type == GAME_MSG) {
                    obj.gameMsg = GameMsg.parse(reader);
                }
                return obj;
            }
        }
        public static class StartGame { /*Struct*/
            public byte[] bytes() {
                ByteWriter writer = new ByteWriter();
                return writer.bytes();
            }
            public static StartGame parse(ByteReader reader) {
                StartGame obj = new StartGame();
                return obj;
            }
        }
        public static class GameMsg { /*Union*/
            public PlayerSetup playerSetup;
            public Integer removePlayerId;
            public WorldState worldState;
            static final byte PLAYER_SETUP = 0;
            static final byte REMOVE_PLAYER_ID = 1;
            static final byte WORLD_STATE = 2;
            public byte[] bytes() {
                ByteWriter writer = new ByteWriter();
                if (false) {;
                } else if (playerSetup != null) {
                    writer.writeByte(PLAYER_SETUP);
                    writer.writeBytes(playerSetup.bytes());
                } else if (removePlayerId != null) {
                    writer.writeByte(REMOVE_PLAYER_ID);
                    writer.writeBytes(ByteWriter.Integer2bytes(removePlayerId));
                } else if (worldState != null) {
                    writer.writeByte(WORLD_STATE);
                    writer.writeBytes(worldState.bytes());
                }
                return writer.bytes();
            }
            public static GameMsg parse(ByteReader reader) {
                GameMsg obj = new GameMsg();
                byte type = reader.readByte();
                if (type == PLAYER_SETUP) {
                    obj.playerSetup = PlayerSetup.parse(reader);
                }

                if (type == REMOVE_PLAYER_ID) {
                    obj.removePlayerId = reader.readInteger();
                }

                if (type == WORLD_STATE) {
                    obj.worldState = WorldState.parse(reader);
                }
                return obj;
            }
        }
        public static class PlayerSetup { /*List*/
            public ArrayList < Integer > items = new ArrayList < > ();
            public byte[] bytes() {
                ByteWriter writer = new ByteWriter();
                writer.writeInt(items.size());
                for (int i = 0; i < items.size(); ++i) {
                    writer.writeBytes(ByteWriter.Integer2bytes(items.get(i)));
                }
                return writer.bytes();
            }
            public static PlayerSetup parse(ByteReader reader) {
                PlayerSetup obj = new PlayerSetup();
                int size = reader.readInteger();
                for (int i = 0; i < size; ++i) {
                    Integer item = reader.readInteger();
                    obj.items.add(item);
                }
                return obj;
            }
        }
        public static class WorldState { /*List*/
            public ArrayList < Entity > items = new ArrayList < > ();
            public byte[] bytes() {
                ByteWriter writer = new ByteWriter();
                writer.writeInt(items.size());
                for (int i = 0; i < items.size(); ++i) {
                    writer.writeBytes(items.get(i).bytes());
                }
                return writer.bytes();
            }
            public static WorldState parse(ByteReader reader) {
                WorldState obj = new WorldState();
                int size = reader.readInteger();
                for (int i = 0; i < size; ++i) {
                    Entity item = Entity.parse(reader);
                    obj.items.add(item);
                }
                return obj;
            }
        }
        public static class Entity { /*Union*/
            public Player player;
            static final byte PLAYER = 0;
            public byte[] bytes() {
                ByteWriter writer = new ByteWriter();
                if (false) {;
                } else if (player != null) {
                    writer.writeByte(PLAYER);
                    writer.writeBytes(player.bytes());
                }
                return writer.bytes();
            }
            public static Entity parse(ByteReader reader) {
                Entity obj = new Entity();
                byte type = reader.readByte();
                if (type == PLAYER) {
                    obj.player = Player.parse(reader);
                }
                return obj;
            }
        }
        public static class Player { /*Struct*/
            public Integer x;
            public Integer y;
            public Integer a;
            public Integer id;
            public byte[] bytes() {
                ByteWriter writer = new ByteWriter();
                writer.writeBytes(ByteWriter.Integer2bytes(x));
                writer.writeBytes(ByteWriter.Integer2bytes(y));
                writer.writeBytes(ByteWriter.Integer2bytes(a));
                writer.writeBytes(ByteWriter.Integer2bytes(id));
                return writer.bytes();
            }
            public static Player parse(ByteReader reader) {
                Player obj = new Player();
                obj.x = reader.readInteger();
                obj.y = reader.readInteger();
                obj.a = reader.readInteger();
                obj.id = reader.readInteger();
                return obj;
            }
        }
    }
}
