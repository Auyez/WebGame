var lobbyIndex = parseInt(sessionStorage.getItem('lobbyIndex'));
document.title = 'Lobby' + lobbyIndex;
var lobby1 = new Lobby(lobbyIndex, 'game1');
//var lobby2 = new Lobby(lobbyIndex, 'game2')

function Lobby(lobbyIndex, parent) {
    //var lobbyIndex = parseInt(sessionStorage.getItem('lobbyIndex'));
    //document.title = 'Lobby' + lobbyIndex;

    var self = this;
    self.socket = new WebSocket("ws://" + location.host + "/WebGame/websocketendpoint");
    self.game = null;

    self.socket.onopen = function(event) {
        console.log('onopen::' + JSON.stringify(event, null, 4));
        //var lobbyIndex = 0;
        var playerId = self.getRandomInt(1, 2147483637); // almost upper limit of int32 signed

        var servermsg = new Protocol.Server.ServerMsg();
        servermsg.lobbyIndex = lobbyIndex;
        servermsg.lobbyCmd = new Protocol.Server.LobbyCmd();
        servermsg.lobbyCmd.addPlayerId = playerId;

        self.socket.send(servermsg.bytes());
    };

    self.socket.onmessage = function(event) {
        var blob = event.data;
        var reader = new FileReader();
        reader.onload = function() {
            var arrayBuffer = reader.result;
            var clientMsg = Protocol.Client.ClientMsg.parse(new ByteReader(arrayBuffer));
            self.lobbyOnMessage(clientMsg);
        }
        reader.readAsArrayBuffer(blob);
    };

    self.lobbyOnMessage = function(clientMsg) {
        if (clientMsg.startGame != null) {
            document.getElementById(parent).innerHTML = '';
            self.game = CreateGame(parent, self.socket, lobbyIndex);
        } else if (clientMsg.gameMsg != null) {
            // drop messages if game is not ready yet
            if (self.game.isready()) {
                self.game.onmessage(clientMsg.gameMsg);
            }
        }
    };

    self.socket.onclose = function(event) {
        console.log('onclose::' + JSON.stringify(event, null, 4));
        location.href = "index.html";
    };

    self.socket.onerror = function(event) {
        console.log('onerror::' + JSON.stringify(event, null, 4));
        location.href = "index.html";
    };


    self.toArrayBuf = function(int8arr) {
        var buf = new ArrayBuffer(int8arr.length);
        var dataView = new DataView(buf);
        for(var i in int8arr) {
            dataView.setInt8(i, int8arr[i]);
        }
        return buf;
    };

    self.getRandomInt = function(min, max) {
      min = Math.ceil(min);
      max = Math.floor(max);
      return Math.floor(Math.random() * (max - min)) + min; //The maximum is exclusive and the minimum is inclusive
    };
}