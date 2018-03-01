var Protocol = {
    Server : {
        ADD_PLAYER : 0,
        GAME_MSG : 1,

        Game : {
            INPUT : 0
        }
    },

    Client : {
        START_GAME : 0,
        GAME_MSG : 1,

        Game : {
            WORLD_STATE : 0,
            PLAYER_SETUP : 1,

            Entity : {
                PLAYER : 0
            }
        }
    }
};


Protocol.Client.getLobbyCmd = function(dataView) {
    return dataView.getInt8(0);
};

Protocol.Client.Game.getGameCmd = function(dataView) {
    return dataView.getInt8(1);
};

Protocol.Client.Game.getWorldState = function(dataView) {
    var numEntities = dataView.getInt8(2);
    var worldState = [];

    var byte = 3;
    for (var i = 0; i < numEntities; ++i) {
        var entityType = dataView.getInt8(byte);
        byte++;

        if (entityType == this.Entity.PLAYER) {
            var x = dataView.getInt32(byte);
            byte += 4;
            var y = dataView.getInt32(byte);
            byte += 4;
            var a = dataView.getInt32(byte);
            byte += 4;
            var id =  dataView.getInt32(byte);
            byte += 4;

            entity = {
                type : entityType,
                x : x,
                y : y,
                a : a,
                id : id
            };
            worldState.push(entity);
        }
    }

    return worldState;
};

Protocol.Client.Game.getPlayerSetup = function(dataView) {
    var numPlayers = dataView.getInt8(2);
    var ids = [];

    var byte = 3;
    for (var i = 0; i < numPlayers; ++i) {
        var id = dataView.getInt32(byte);
        byte += 4;

        ids.push(id);
    }
    return ids;
};