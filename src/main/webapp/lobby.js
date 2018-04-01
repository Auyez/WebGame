var lobbyIndex = 0;
var lobby1 = new Lobby(lobbyIndex, 'game1');
var lobby2 = new Lobby(lobbyIndex, 'game2');

function Lobby(lobbyIndex, gameParent) {
    var socket = new WebSocket("ws://" + location.host + "/WebGame/websocketendpoint");
    var game = null;

    socket.onopen = function(event) {
        console.log(gameParent + ' onopen::' + JSON.stringify(event, null, 4));

        var playerId = getRandomInt(1, 2147483637); // almost upper limit of int32 signed

        var servermsg = new Protocol.Server.ServerMsg();
        servermsg.lobbyIndex = lobbyIndex;
        servermsg.lobbyCmd = new Protocol.Server.LobbyCmd();
        servermsg.lobbyCmd.addPlayerId = playerId;

        socket.send(servermsg.bytes());
    }

    socket.onclose = function(event) {
        console.log(gameParent + ' onclose::' + JSON.stringify(event, null, 4));
        // go back to main page
    }

    socket.onerror = function(event) {
        console.log(gameParent + ' onerror::' + JSON.stringify(event, null, 4));
    }

    socket.onmessage = function(event) {
        var blob = event.data;
        var reader = new FileReader();
        reader.onload = function() {
            var arrayBuffer = reader.result;
            var clientMsg = Protocol.Client.ClientMsg.parse(new ByteReader(arrayBuffer));
            lobbyOnMessage(clientMsg);
        }
        reader.readAsArrayBuffer(blob);
    }

    function lobbyOnMessage(clientMsg) {
        if (clientMsg.startGame != null) {
            document.getElementById(gameParent).innerHTML = '';
            game = CreateGame(gameParent, socket, lobbyIndex);
        } else if (clientMsg.gameMsg != null) {
            game.onmessage(clientMsg.gameMsg);
        }
    }
    this.socket = socket;
    this.game = game;
}



function toArrayBuf(int8arr) {
    var buf = new ArrayBuffer(int8arr.length);
    var dataView = new DataView(buf);
    for(var i in int8arr) {
        dataView.setInt8(i, int8arr[i]);
    }
    return buf;
}

function getRandomInt(min, max) {
  min = Math.ceil(min);
  max = Math.floor(max);
  return Math.floor(Math.random() * (max - min)) + min; //The maximum is exclusive and the minimum is inclusive
}