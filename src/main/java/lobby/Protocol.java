package lobby;//Constants for the protocol

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
					Integer x_target
					Integer y_target
				struct skill_input
					Integer x
					Integer y
					Byte skill_type

namespace Client
	union client_msg
		struct start_game
		union game_msg
			list world_state
				struct actor
					Integer id
					Integer type
					Integer x
					Integer y
					Byte animation
					Integer angle
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
            public SkillInput skillInput;
            static final byte INPUT = 0;
            static final byte SKILL_INPUT = 1;
            public byte[] bytes() {
                ByteWriter writer = new ByteWriter();
                if (false) {;
                } else if (input != null) {
                    writer.writeByte(INPUT);
                    writer.writeBytes(input.bytes());
                } else if (skillInput != null) {
                    writer.writeByte(SKILL_INPUT);
                    writer.writeBytes(skillInput.bytes());
                }
                return writer.bytes();
            }
            public static GameMsg parse(ByteReader reader) {
                GameMsg obj = new GameMsg();
                byte type = reader.readByte();
                if (type == INPUT) {
                    obj.input = Input.parse(reader);
                }

                if (type == SKILL_INPUT) {
                    obj.skillInput = SkillInput.parse(reader);
                }
                return obj;
            }
        }
        public static class Input { /*Struct*/
            public Integer xTarget;
            public Integer yTarget;
            public byte[] bytes() {
                ByteWriter writer = new ByteWriter();
                writer.writeBytes(ByteWriter.Integer2bytes(xTarget));
                writer.writeBytes(ByteWriter.Integer2bytes(yTarget));
                return writer.bytes();
            }
            public static Input parse(ByteReader reader) {
                Input obj = new Input();
                obj.xTarget = reader.readInteger();
                obj.yTarget = reader.readInteger();
                return obj;
            }
        }
        public static class SkillInput { /*Struct*/
            public Integer x;
            public Integer y;
            public Byte skillType;
            public byte[] bytes() {
                ByteWriter writer = new ByteWriter();
                writer.writeBytes(ByteWriter.Integer2bytes(x));
                writer.writeBytes(ByteWriter.Integer2bytes(y));
                writer.writeBytes(ByteWriter.Byte2bytes(skillType));
                return writer.bytes();
            }
            public static SkillInput parse(ByteReader reader) {
                SkillInput obj = new SkillInput();
                obj.x = reader.readInteger();
                obj.y = reader.readInteger();
                obj.skillType = reader.readByte();
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
            public WorldState worldState;
            static final byte WORLD_STATE = 0;
            public byte[] bytes() {
                ByteWriter writer = new ByteWriter();
                if (false) {;
                } else if (worldState != null) {
                    writer.writeByte(WORLD_STATE);
                    writer.writeBytes(worldState.bytes());
                }
                return writer.bytes();
            }
            public static GameMsg parse(ByteReader reader) {
                GameMsg obj = new GameMsg();
                byte type = reader.readByte();
                if (type == WORLD_STATE) {
                    obj.worldState = WorldState.parse(reader);
                }
                return obj;
            }
        }
        public static class WorldState { /*List*/
            public ArrayList < Actor > items = new ArrayList < > ();
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
                    Actor item = Actor.parse(reader);
                    obj.items.add(item);
                }
                return obj;
            }
        }
        public static class Actor { /*Struct*/
            public Integer id;
            public Integer type;
            public Integer x;
            public Integer y;
            public Byte animation;
            public Integer angle;
            public byte[] bytes() {
                ByteWriter writer = new ByteWriter();
                writer.writeBytes(ByteWriter.Integer2bytes(id));
                writer.writeBytes(ByteWriter.Integer2bytes(type));
                writer.writeBytes(ByteWriter.Integer2bytes(x));
                writer.writeBytes(ByteWriter.Integer2bytes(y));
                writer.writeBytes(ByteWriter.Byte2bytes(animation));
                writer.writeBytes(ByteWriter.Integer2bytes(angle));
                return writer.bytes();
            }
            public static Actor parse(ByteReader reader) {
                Actor obj = new Actor();
                obj.id = reader.readInteger();
                obj.type = reader.readInteger();
                obj.x = reader.readInteger();
                obj.y = reader.readInteger();
                obj.animation = reader.readByte();
                obj.angle = reader.readInteger();
                return obj;
            }
        }
    }
}
