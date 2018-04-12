
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
                this.addPlayerId = null;
                this.ready = null;
                this.gameMsg = null;
            }
            LobbyCmd.prototype.bytes = function () {
                var writer = new ByteWriter();
                if (false) {
                }
                else if (this.addPlayerId != null) {
                    writer.writeByte(LobbyCmd.ADD_PLAYER_ID);
                    writer.writeBytes(ByteWriter.Integer2bytes(this.addPlayerId));
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
                if (type === LobbyCmd.ADD_PLAYER_ID) {
                    obj.addPlayerId = reader.readInteger();
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
        LobbyCmd.ADD_PLAYER_ID = 0;
        LobbyCmd.READY = 1;
        LobbyCmd.GAME_MSG = 2;
        Server.LobbyCmd = LobbyCmd;
        LobbyCmd["__class"] = "Protocol.Server.LobbyCmd";
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
                    writer.writeBytes(ByteWriter.Byte2bytes(this.skillInput));
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
                    obj.skillInput = reader.readByte();
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
                this.items = ([]);
            }
            WorldState.prototype.bytes = function () {
                var writer = new ByteWriter();
                writer.writeInt(/* size */ this.items.length);
                for (var i = 0; i < this.items.length; ++i) {
                    writer.writeBytes(/* get */ this.items[i].bytes());
                }
                ;
                return writer.bytes();
            };
            WorldState.parse = function (reader) {
                var obj = new Client.WorldState();
                var size = reader.readInteger();
                for (var i = 0; i < size; ++i) {
                    var item = Client.Actor.parse(reader);
                    /* add */ (obj.items.push(item) > 0);
                }
                ;
                return obj;
            };
            return WorldState;
        }());
        Client.WorldState = WorldState;
        WorldState["__class"] = "Protocol.Client.WorldState";
        var Actor = (function () {
            function Actor() {
                this.id = null;
                this.type = null;
                this.x = null;
                this.y = null;
                this.animation = null;
            }
            Actor.prototype.bytes = function () {
                var writer = new ByteWriter();
                writer.writeBytes(ByteWriter.Integer2bytes(this.id));
                writer.writeBytes(ByteWriter.Integer2bytes(this.type));
                writer.writeBytes(ByteWriter.Integer2bytes(this.x));
                writer.writeBytes(ByteWriter.Integer2bytes(this.y));
                writer.writeBytes(ByteWriter.Byte2bytes(this.animation));
                return writer.bytes();
            };
            Actor.parse = function (reader) {
                var obj = new Client.Actor();
                obj.id = reader.readInteger();
                obj.type = reader.readInteger();
                obj.x = reader.readInteger();
                obj.y = reader.readInteger();
                obj.animation = reader.readByte();
                return obj;
            };
            return Actor;
        }());
        Client.Actor = Actor;
        Actor["__class"] = "Protocol.Client.Actor";
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
    return ByteWriter;
}());
ByteWriter["__class"] = "ByteWriter";