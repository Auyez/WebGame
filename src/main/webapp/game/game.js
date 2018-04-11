function CreateGame(parent, socket, lobbyIndex) {
	var game = new Phaser.Game(
			            1200, 900, Phaser.AUTO, parent,
			            {
			            	preload: preload, 
			            	create: create, 
			            	update: update
			            }
		        	);
	
	var players = {}; // id -> player
    var cursors = null;
	var q = null;
	var inputMessage = null;
    
    function preload() {
        Player.preload(game);
        game.load.image('tile', 'game/assets/map.png');
    }
    
    function create() {
        var servermsg = new Protocol.Server.ServerMsg();
		servermsg.lobbyIndex = lobbyIndex;
		servermsg.lobbyCmd = new Protocol.Server.LobbyCmd();
		servermsg.lobbyCmd.ready = new Protocol.Server.Ready();
		// Sends to server that game is loaded and ready to receive playerSetup
		socket.send(servermsg.bytes());

        inputMessage = new Protocol.Server.ServerMsg();
        inputMessage.lobbyIndex = lobbyIndex;
        inputMessage.lobbyCmd = new Protocol.Server.LobbyCmd();
        inputMessage.lobbyCmd.gameMsg = new Protocol.Server.GameMsg();
        inputMessage.lobbyCmd.gameMsg.input = new Protocol.Server.Input();
        

        cursors = game.input.keyboard.createCursorKeys();
		q = game.input.keyboard.addKey(Phaser.Keyboard.Q); //.onDown .onPress
		//q.onDown.add(spellOne, )
		
		
		///////////////////////
    	var map = [
			[1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1],
			[1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1],
			[1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1],
			[1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,1], 
			[1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,1], 
			[1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,1], 
			[1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,1], 
			[1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,1], 
			[1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,1], 
			[1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,1], 
			[1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,1], 
			[1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,1], 
			[1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,1], 
			[1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,1], 
			[1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,1], 
			[1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,1], 
			[1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,1], 
			[1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,1], 
			[1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,1], 
			[1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,1],
			[1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1],
			[1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1],
			[1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1],
			[1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1],
			[1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1],
			[1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1],
			[1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1],
			[1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1],
			[1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1],
			[1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1]
    	];
    	
    	for (var i = 0; i < 30; i++){
    		for(var j = 0; j < 40; j++){
    			if(map[i][j] == 1){
    				var test = game.add.sprite( (j % 40) * 30, (i % 30) * 30, 'tile' );
    			}
    		}
    	}
    	//////////////////////////////////////////////////
    }

    function update() {  	
    	game.input.onDown.add(move, this);
    }

    function move() {
    	//console.log(game.input.x);
    	//console.log(game.input.y);
    	inputMessage.lobbyCmd.gameMsg.input.xTarget = game.input.x;
    	inputMessage.lobbyCmd.gameMsg.input.yTarget = game.input.y;
    	socket.send(inputMessage.bytes());
    }
    
    game.onmessage = function(gameMsg) {
        if (gameMsg.playerSetup != null) {
            var ids = gameMsg.playerSetup.items;
            console.log('playersetup', gameMsg.playerSetup.items);
            addPlayers(ids);
        } else if (gameMsg.worldState != null) {
            var entities = gameMsg.worldState.items;
            updateEntities(entities);
        }
    }

    function addPlayers(ids) {
        for (var i in ids) {
            var id = ids[i];
            player = new Player(game);
            players[id] = player;
        }
    }

    function removePlayer(id) {
        if (id in players) {
            var player = players[id];
            player.destroy();
            delete players[id];
        }
    }

    function updateEntities(entities) {
        var playersToRemove = new Set(Object.keys(players));

        for (var i in entities) {
            var entity = entities[i];
            if (entity.player != null) {
                var playerMsg = entity.player;
                if (playerMsg.id in players) {
                    // the player is in the list -> do not remove it
                    playersToRemove.delete(String(playerMsg.id));

                    var player = players[playerMsg.id];
                    player.update(playerMsg);
                }
            }
        }

        for (var id of playersToRemove) {
            removePlayer(id);
        }
    }	
	
	return game;
}


function Player(game) {
    this.sprite = game.add.sprite(60, game.world.height - 150, 'cultist');
    this.sprite.animations.add('idle', [7], 10, true);
    this.sprite.animations.add('up', [0, 1, 2], 10, true);
    this.sprite.animations.add('right', [3, 4, 5], 10, true);
    this.sprite.animations.add('down', [6, 7, 8], 10, true);
    this.sprite.animations.add('left', [9, 10, 11], 10, true);


    this.update = function(playerMsg) {
        this.sprite.x = playerMsg.x;
        this.sprite.y = playerMsg.y;

        if (playerMsg.ismoving == 0) {
            this.sprite.animations.play('idle');
        } else {
            var dir = Math.floor(playerMsg.a / 45);
            var animations = ['right', 'down', 'down', 'left', 'left', 'up', 'up', 'right'];
            this.sprite.animations.play(animations[dir]);
        }
    }

    this.destroy = function() {
        this.sprite.destroy();
    }
}

Player.preload = function(game) {
    game.load.spritesheet('cultist', 'game/assets/cultist.png', 32, 36);
}