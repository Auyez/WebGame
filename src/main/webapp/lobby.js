
var socket = new WebSocket("ws://" + location.host + "/WebGame/websocketendpoint");
var game = null;

socket.onopen = function(event) {
    console.log('onopen::' + JSON.stringify(event, null, 4));
    var lobbyIndex = 0;
    var playerId = getRandomInt(1, 2,147,483,637); // almost upper limit of int32 signed
    var buf = new ArrayBuffer(6);
    var dataView = new DataView(buf);
    dataView.setInt8(0, lobbyIndex);
    dataView.setInt8(1, Protocol.Server.ADD_PLAYER);
    dataView.setInt32(2, playerId);
    console.log(buf);
    socket.send(buf);
}

socket.onmessage = function(event) {
    var blob = event.data;
    var reader = new FileReader();
    var command = null;
    reader.onload = function() {
        var arrayBuffer = reader.result;
        onmessage(arrayBuffer);
    }
    reader.readAsArrayBuffer(blob);
}

function onmessage(arrayBuffer) {
    var dataView = new DataView(arrayBuffer);
    var command = Protocol.Client.getLobbyCmd(dataView);

    if(command == Protocol.Client.START_GAME) {
        game = new Game("game");
    }
    if(game && command == Protocol.Client.GAME_MSG) {
        game.onmessage(arrayBuffer);
    }
}

socket.onclose = function(event) {
    console.log('onclose::' + JSON.stringify(event, null, 4));
    // go back to main page
}

socket.onerror = function(event) {
    console.log('onerror::' + JSON.stringify(event, null, 4));
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