
var socket = new WebSocket("ws://" + location.host + "/WebGame/websocketendpoint");
var game = null;
var lobbyIndex = 0;

socket.onopen = function(event) {
    console.log('onopen::' + JSON.stringify(event, null, 4));

    var playerId = 100500;
    var buf = new ArrayBuffer(6);
    var dataView = new DataView(buf);
    dataView.setInt8(0, 0);
    dataView.setInt8(1, Protocol.Server.ADD_PLAYER);
    dataView.setInt32(2, playerId);

    socket.send(buf);
}

socket.onmessage = function(event) {
    console.log("asd");
    var blob = event.data;
    var reader = new FileReader();
    var command = null;
    reader.onload = function() {
        var arrayBuffer = reader.result;
        var dataView = new DataView(arrayBuffer);
        var command = dataView.getInt8(0);

        if(command == Protocol.Client.START_GAME) {
            game = new Game("game", 0);
        }
        if(command == Protocol.Client.GAME_MSG) {
            game.onmessage(arrayBuffer);
        }
    }
    reader.readAsArrayBuffer(blob);
}

socket.onclose = function(event) {
    console.log('onclose::' + JSON.stringify(event, null, 4));
    // go back to main page
}

socket.onerror = function(event) {
    console.log('onerror::' + JSON.stringify(event, null, 4));
}


