/* Generated from Java with JSweet 2.0.0 - http://www.jsweet.org */
var Protocol = (function () {
    function Protocol() {
    }
    return Protocol;
}());
Protocol["__class"] = "Protocol";
(function (Protocol) {
    var Server = (function () {
        function Server() {
        }
        return Server;
    }());
    Protocol.Server = Server;
    Server["__class"] = "Protocol.Server";
    (function (Server) {
        var ServerMsg = (function () {
            function ServerMsg() {
                this.lobbyIndex = null;
                this.lobbyCmd = null;
            }
            ServerMsg.prototype.bytes = function () {
                var writer = new ByteWriter();
                writer.writeBytes(ByteWriter.Integer2bytes(this.lobbyIndex));
                writer.writeBytes(this.lobbyCmd.bytes());
                return writer.bytes();
            };
            ServerMsg.parse = function (reader) {
                var obj = new Server.ServerMsg();
                obj.lobbyIndex = reader.readInteger();
                obj.lobbyCmd = Server.LobbyCmd.parse(reader);
                return obj;
            };
            return ServerMsg;
        }());
        Server.ServerMsg = ServerMsg;
        ServerMsg["__class"] = "Protocol.Server.ServerMsg";
        var LobbyCmd = (function () {
            function LobbyCmd() {
                this.addPlayer = null;
                this.ready = null;
                this.gameMsg = null;
            }
            LobbyCmd.prototype.bytes = function () {
                var writer = new ByteWriter();
                if (false) {
                }
                else if (this.addPlayer != null) {
                    writer.writeByte(LobbyCmd.ADD_PLAYER);
                    writer.writeBytes(this.addPlayer.bytes());
                }
                else if (this.ready != null) {
                    writer.writeByte(LobbyCmd.READY);
                    writer.writeBytes(this.ready.bytes());
                }
                else if (this.gameMsg != null) {
                    writer.writeByte(LobbyCmd.GAME_MSG);
                    writer.writeBytes(this.gameMsg.bytes());
                }
                return writer.bytes();
            };
            LobbyCmd.parse = function (reader) {
                var obj = new Server.LobbyCmd();
                var type = reader.readByte();
                if (type === LobbyCmd.ADD_PLAYER) {
                    obj.addPlayer = Server.AddPlayer.parse(reader);
                }
                if (type === LobbyCmd.READY) {
                    obj.ready = Server.Ready.parse(reader);
                }
                if (type === LobbyCmd.GAME_MSG) {
                    obj.gameMsg = Server.GameMsg.parse(reader);
                }
                return obj;
            };
            return LobbyCmd;
        }());
        LobbyCmd.ADD_PLAYER = 0;
        LobbyCmd.READY = 1;
        LobbyCmd.GAME_MSG = 2;
        Server.LobbyCmd = LobbyCmd;
        LobbyCmd["__class"] = "Protocol.Server.LobbyCmd";
        var AddPlayer = (function () {
            function AddPlayer() {
                this.playerId = null;
                this.authToken = null;
            }
            AddPlayer.prototype.bytes = function () {
                var writer = new ByteWriter();
                writer.writeBytes(ByteWriter.Integer2bytes(this.playerId));
                writer.writeBytes(ByteWriter.String2bytes(this.authToken));
                return writer.bytes();
            };
            AddPlayer.parse = function (reader) {
                var obj = new Server.AddPlayer();
                obj.playerId = reader.readInteger();
                obj.authToken = reader.readString();
                return obj;
            };
            return AddPlayer;
        }());
        Server.AddPlayer = AddPlayer;
        AddPlayer["__class"] = "Protocol.Server.AddPlayer";
        var Ready = (function () {
            function Ready() {
            }
            Ready.prototype.bytes = function () {
                var writer = new ByteWriter();
                return writer.bytes();
            };
            Ready.parse = function (reader) {
                var obj = new Server.Ready();
                return obj;
            };
            return Ready;
        }());
        Server.Ready = Ready;
        Ready["__class"] = "Protocol.Server.Ready";
        var GameMsg = (function () {
            function GameMsg() {
                this.input = null;
                this.skillInput = null;
            }
            GameMsg.prototype.bytes = function () {
                var writer = new ByteWriter();
                if (false) {
                }
                else if (this.input != null) {
                    writer.writeByte(GameMsg.INPUT);
                    writer.writeBytes(this.input.bytes());
                }
                else if (this.skillInput != null) {
                    writer.writeByte(GameMsg.SKILL_INPUT);
                    writer.writeBytes(this.skillInput.bytes());
                }
                return writer.bytes();
            };
            GameMsg.parse = function (reader) {
                var obj = new Server.GameMsg();
                var type = reader.readByte();
                if (type === GameMsg.INPUT) {
                    obj.input = Server.Input.parse(reader);
                }
                if (type === GameMsg.SKILL_INPUT) {
                    obj.skillInput = Server.SkillInput.parse(reader);
                }
                return obj;
            };
            return GameMsg;
        }());
        GameMsg.INPUT = 0;
        GameMsg.SKILL_INPUT = 1;
        Server.GameMsg = GameMsg;
        GameMsg["__class"] = "Protocol.Server.GameMsg";
        var Input = (function () {
            function Input() {
                this.xTarget = null;
                this.yTarget = null;
            }
            Input.prototype.bytes = function () {
                var writer = new ByteWriter();
                writer.writeBytes(ByteWriter.Integer2bytes(this.xTarget));
                writer.writeBytes(ByteWriter.Integer2bytes(this.yTarget));
                return writer.bytes();
            };
            Input.parse = function (reader) {
                var obj = new Server.Input();
                obj.xTarget = reader.readInteger();
                obj.yTarget = reader.readInteger();
                return obj;
            };
            return Input;
        }());
        Server.Input = Input;
        Input["__class"] = "Protocol.Server.Input";
        var SkillInput = (function () {
            function SkillInput() {
                this.x = null;
                this.y = null;
                this.skillType = null;
            }
            SkillInput.prototype.bytes = function () {
                var writer = new ByteWriter();
                writer.writeBytes(ByteWriter.Integer2bytes(this.x));
                writer.writeBytes(ByteWriter.Integer2bytes(this.y));
                writer.writeBytes(ByteWriter.Byte2bytes(this.skillType));
                return writer.bytes();
            };
            SkillInput.parse = function (reader) {
                var obj = new Server.SkillInput();
                obj.x = reader.readInteger();
                obj.y = reader.readInteger();
                obj.skillType = reader.readByte();
                return obj;
            };
            return SkillInput;
        }());
        Server.SkillInput = SkillInput;
        SkillInput["__class"] = "Protocol.Server.SkillInput";
    })(Server = Protocol.Server || (Protocol.Server = {}));
    var Client = (function () {
        function Client() {
        }
        return Client;
    }());
    Protocol.Client = Client;
    Client["__class"] = "Protocol.Client";
    (function (Client) {
        var ClientMsg = (function () {
            function ClientMsg() {
                this.startGame = null;
                this.gameMsg = null;
            }
            ClientMsg.prototype.bytes = function () {
                var writer = new ByteWriter();
                if (false) {
                }
                else if (this.startGame != null) {
                    writer.writeByte(ClientMsg.START_GAME);
                    writer.writeBytes(this.startGame.bytes());
                }
                else if (this.gameMsg != null) {
                    writer.writeByte(ClientMsg.GAME_MSG);
                    writer.writeBytes(this.gameMsg.bytes());
                }
                return writer.bytes();
            };
            ClientMsg.parse = function (reader) {
                var obj = new Client.ClientMsg();
                var type = reader.readByte();
                if (type === ClientMsg.START_GAME) {
                    obj.startGame = Client.StartGame.parse(reader);
                }
                if (type === ClientMsg.GAME_MSG) {
                    obj.gameMsg = Client.GameMsg.parse(reader);
                }
                return obj;
            };
            return ClientMsg;
        }());
        ClientMsg.START_GAME = 0;
        ClientMsg.GAME_MSG = 1;
        Client.ClientMsg = ClientMsg;
        ClientMsg["__class"] = "Protocol.Client.ClientMsg";
        var StartGame = (function () {
            function StartGame() {
            }
            StartGame.prototype.bytes = function () {
                var writer = new ByteWriter();
                return writer.bytes();
            };
            StartGame.parse = function (reader) {
                var obj = new Client.StartGame();
                return obj;
            };
            return StartGame;
        }());
        Client.StartGame = StartGame;
        StartGame["__class"] = "Protocol.Client.StartGame";
        var GameMsg = (function () {
            function GameMsg() {
                this.worldState = null;
            }
            GameMsg.prototype.bytes = function () {
                var writer = new ByteWriter();
                if (false) {
                }
                else if (this.worldState != null) {
                    writer.writeByte(GameMsg.WORLD_STATE);
                    writer.writeBytes(this.worldState.bytes());
                }
                return writer.bytes();
            };
            GameMsg.parse = function (reader) {
                var obj = new Client.GameMsg();
                var type = reader.readByte();
                if (type === GameMsg.WORLD_STATE) {
                    obj.worldState = Client.WorldState.parse(reader);
                }
                return obj;
            };
            return GameMsg;
        }());
        GameMsg.WORLD_STATE = 0;
        Client.GameMsg = GameMsg;
        GameMsg["__class"] = "Protocol.Client.GameMsg";
        var WorldState = (function () {
            function WorldState() {
                this.actors = null;
                this.players = null;
                this.skillsCooldown = null;
            }
            WorldState.prototype.bytes = function () {
                var writer = new ByteWriter();
                writer.writeBytes(this.actors.bytes());
                writer.writeBytes(this.players.bytes());
                writer.writeBytes(this.skillsCooldown.bytes());
                return writer.bytes();
            };
            WorldState.parse = function (reader) {
                var obj = new Client.WorldState();
                obj.actors = Client.Actors.parse(reader);
                obj.players = Client.Players.parse(reader);
                obj.skillsCooldown = Client.SkillsCooldown.parse(reader);
                return obj;
            };
            return WorldState;
        }());
        Client.WorldState = WorldState;
        WorldState["__class"] = "Protocol.Client.WorldState";
        var Actors = (function () {
            function Actors() {
                this.items = ([]);
            }
            Actors.prototype.bytes = function () {
                var writer = new ByteWriter();
                writer.writeInt(/* size */ this.items.length);
                for (var i = 0; i < this.items.length; ++i) {
                    writer.writeBytes(/* get */ this.items[i].bytes());
                }
                ;
                return writer.bytes();
            };
            Actors.parse = function (reader) {
                var obj = new Client.Actors();
                var size = reader.readInteger();
                for (var i = 0; i < size; ++i) {
                    var item = Client.Actor.parse(reader);
                    /* add */ (obj.items.push(item) > 0);
                }
                ;
                return obj;
            };
            return Actors;
        }());
        Client.Actors = Actors;
        Actors["__class"] = "Protocol.Client.Actors";
        var Actor = (function () {
            function Actor() {
                this.id = null;
                this.type = null;
                this.x = null;
                this.y = null;
                this.animation = null;
                this.angle = null;
            }
            Actor.prototype.bytes = function () {
                var writer = new ByteWriter();
                writer.writeBytes(ByteWriter.Integer2bytes(this.id));
                writer.writeBytes(ByteWriter.Integer2bytes(this.type));
                writer.writeBytes(ByteWriter.Integer2bytes(this.x));
                writer.writeBytes(ByteWriter.Integer2bytes(this.y));
                writer.writeBytes(ByteWriter.Byte2bytes(this.animation));
                writer.writeBytes(ByteWriter.Integer2bytes(this.angle));
                return writer.bytes();
            };
            Actor.parse = function (reader) {
                var obj = new Client.Actor();
                obj.id = reader.readInteger();
                obj.type = reader.readInteger();
                obj.x = reader.readInteger();
                obj.y = reader.readInteger();
                obj.animation = reader.readByte();
                obj.angle = reader.readInteger();
                return obj;
            };
            return Actor;
        }());
        Client.Actor = Actor;
        Actor["__class"] = "Protocol.Client.Actor";
        var Players = (function () {
            function Players() {
                this.items = ([]);
            }
            Players.prototype.bytes = function () {
                var writer = new ByteWriter();
                writer.writeInt(/* size */ this.items.length);
                for (var i = 0; i < this.items.length; ++i) {
                    writer.writeBytes(/* get */ this.items[i].bytes());
                }
                ;
                return writer.bytes();
            };
            Players.parse = function (reader) {
                var obj = new Client.Players();
                var size = reader.readInteger();
                for (var i = 0; i < size; ++i) {
                    var item = Client.Player.parse(reader);
                    /* add */ (obj.items.push(item) > 0);
                }
                ;
                return obj;
            };
            return Players;
        }());
        Client.Players = Players;
        Players["__class"] = "Protocol.Client.Players";
        var Player = (function () {
            function Player() {
                this.id = null;
                this.hp = null;
            }
            Player.prototype.bytes = function () {
                var writer = new ByteWriter();
                writer.writeBytes(ByteWriter.Integer2bytes(this.id));
                writer.writeBytes(ByteWriter.Integer2bytes(this.hp));
                return writer.bytes();
            };
            Player.parse = function (reader) {
                var obj = new Client.Player();
                obj.id = reader.readInteger();
                obj.hp = reader.readInteger();
                return obj;
            };
            return Player;
        }());
        Client.Player = Player;
        Player["__class"] = "Protocol.Client.Player";
        var SkillsCooldown = (function () {
            function SkillsCooldown() {
                this.items = ([]);
            }
            SkillsCooldown.prototype.bytes = function () {
                var writer = new ByteWriter();
                writer.writeInt(/* size */ this.items.length);
                for (var i = 0; i < this.items.length; ++i) {
                    writer.writeBytes(/* get */ this.items[i].bytes());
                }
                ;
                return writer.bytes();
            };
            SkillsCooldown.parse = function (reader) {
                var obj = new Client.SkillsCooldown();
                var size = reader.readInteger();
                for (var i = 0; i < size; ++i) {
                    var item = Client.Skill.parse(reader);
                    /* add */ (obj.items.push(item) > 0);
                }
                ;
                return obj;
            };
            return SkillsCooldown;
        }());
        Client.SkillsCooldown = SkillsCooldown;
        SkillsCooldown["__class"] = "Protocol.Client.SkillsCooldown";
        var Skill = (function () {
            function Skill() {
                this.skillType = null;
                this.cooldown = null;
            }
            Skill.prototype.bytes = function () {
                var writer = new ByteWriter();
                writer.writeBytes(ByteWriter.Byte2bytes(this.skillType));
                writer.writeBytes(ByteWriter.Integer2bytes(this.cooldown));
                return writer.bytes();
            };
            Skill.parse = function (reader) {
                var obj = new Client.Skill();
                obj.skillType = reader.readByte();
                obj.cooldown = reader.readInteger();
                return obj;
            };
            return Skill;
        }());
        Client.Skill = Skill;
        Skill["__class"] = "Protocol.Client.Skill";
    })(Client = Protocol.Client || (Protocol.Client = {}));
})(Protocol || (Protocol = {}));



var ByteReader = (function () {
    function ByteReader(arraybuf) {
        this.dataview = new DataView(arraybuf);
        this.pos = 0;
    }
    ByteReader.prototype.readInteger = function () {
        var v = this.dataview.getInt32(this.pos);
        this.pos += 4;
        return v;
    };
    ByteReader.prototype.readByte = function () {
        var v = this.dataview.getInt8(this.pos);
        this.pos += 1;
        return v;
    };
    ByteReader.prototype.readString = function () {
        var length = this.dataview.getInt32(this.pos);
        this.pos += 4;

        var chars = [];
        for (var i = 0; i < length; ++i) {
            var char = this.dataview.getInt8(this.pos);
            this.pos += 1;
            chars.push(char);
        }

        var decoder = new TextDecoder();
        var str = decoder.decode(new Int8Array(chars));

        return str;
    };
    return ByteReader;
}());
ByteReader["__class"] = "ByteReader";



var ByteWriter = (function () {
    function ByteWriter() {
        this.arraybuf = new ArrayBuffer(0);
        this.dataview = new DataView(this.arraybuf);
        this.pos = 0;
    }
    ByteWriter.prototype.bytes = function () {
        return this.arraybuf.slice(0, this.pos);
    };
    ByteWriter.prototype.writeInt = function (x) {
        this.grow(this.pos + 4);
        this.dataview.setInt32(this.pos, x);
        this.pos += 4;
    };
    ByteWriter.prototype.writeByte = function (x) {
        this.grow(this.pos + 1);
        this.dataview.setInt8(this.pos, x);
        this.pos += 1;
    };
    ByteWriter.prototype.writeBytes = function (arraybuf) {
        this.grow(this.pos + arraybuf.byteLength);
        var bytes = new Int8Array(arraybuf);
        for (var i = 0; i < bytes.length; ++i) {
            this.dataview.setInt8(this.pos, bytes[i]);
            this.pos += 1;
        }
    };
    ByteWriter.prototype.grow = function (capacity) {
        if (this.arraybuf.byteLength < capacity) {
            var newArrayBuf = new ArrayBuffer(capacity * 2);
            var newDataView = new DataView(newArrayBuf);

            for (var i = 0; i < this.arraybuf.byteLength; ++i) {
                newDataView.setInt8(i, this.dataview.getInt8(i));
            }

            this.arraybuf = newArrayBuf;
            this.dataview = newDataView;
        }
    };
    ByteWriter.prototype.toString = function () {
        return '[' + (new Uint8Array(this.bytes())).toString() + ']';
    }
    ByteWriter.Integer2bytes = function (x) {
        var arraybuf = new ArrayBuffer(4);
        var dataview = new DataView(arraybuf);
        dataview.setInt32(0, x);
        return arraybuf;
    };
    ByteWriter.Byte2bytes = function (x) {
        var arraybuf = new ArrayBuffer(1);
        var dataview = new DataView(arraybuf);
        dataview.setInt8(0, x);
        return arraybuf;
    };
    ByteWriter.String2bytes = function (x) {
        var arraybuf = new ArrayBuffer(4 + x.length);
        var dataview = new DataView(arraybuf);
        var pos = 0;

        dataview.setInt32(pos, x.length);
        pos += 4;

        var encoder = new TextEncoder();
        var chars = encoder.encode(x);
        for (var i = 0; i < x.length; ++i) {
            dataview.setInt8(pos, chars[i]);
            pos += 1;
        }
        return arraybuf;
    };
    return ByteWriter;
}());
ByteWriter["__class"] = "ByteWriter";