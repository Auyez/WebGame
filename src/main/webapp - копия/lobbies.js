var nameInput = null
var nameList = null

var socket = new WebSocket("ws://" + location.host + "/WebGame/websocketendpoint");
$(document).ready(function() {
    nameInput = $("#nameInput");
    nameList = $("#nameList");
});


function onHostClick() {
    success = function(data, status, jqhxr) {
        if (data.error) {
            console.log(data.error);
        } else {
            console.log("Create lobby");
        }
    };

    jQuery.post('services/lobbylist/host', nameInput.val(), success, "json")
}

function onJoinClick() {
    success = function(data, status, jqhxr) {
        if (data.error) {
            console.log(data.error);
        } else {
            console.log("Join lobby");
        }
    };

    jQuery.post('services/lobbylist/join', nameInput.val(), success, "json")
}

function refreshNameList() {
    success = function(data, status, jqhxr) {
        lobbies = data;
        nameList.empty()
        for (var name in lobbies) {
            nameList.append("<li>" + name + "</li>");
        }
    }

    jQuery.get('services/lobbylist/names', null, success, 'json');
}

function getRandomInt(min, max) {
  min = Math.ceil(min);
  max = Math.floor(max);
  return Math.floor(Math.random() * (max - min)) + min; //The maximum is exclusive and the minimum is inclusive
}

function sendMessage() {
    var msg = parseInt(document.getElementById("msg").value);
    var buf = new ArrayBuffer(6);
    var dataView = new DataView(buf);
    dataView.setInt8(0, 0);
    dataView.setInt8(1, 1);
    dataView.setInt8(2, msg);
    socket.send(buf);
}

function sendProtocolized() {

}

socket.onmessage = function(event) {
    var blob = event.data;
    var reader = new FileReader();
    var command = null;
    reader.onload = function() {
        var arrayBuffer = reader.result;
        var dataView = new DataView(arrayBuffer);
        var command = dataView.getInt8(0);

        if(command == Protocol.Client.START_GAME) {
            game = new Game("game");
        }
        if(command == Protocol.Client.GAME_MSG) {
            var message = "<br />" + dataView.getInt8(3);
            console.log("Chat: ", message);
            document.getElementById("chat").innerHTML += message;
        }
    }
    reader.readAsArrayBuffer(blob);
}