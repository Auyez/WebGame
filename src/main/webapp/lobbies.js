var nameInput = null
var nameList = null

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